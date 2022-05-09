package com.birikfr.mailaction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Flags;

import org.slf4j.Logger;

import com.birikfr.mailbean.MailBean;
import com.birikfr.properties.MailConfigBean;

import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailFilter;
import jodd.mail.EmailMessage;
import jodd.mail.ImapServer;
import jodd.mail.ImapSslServer;
import jodd.mail.MailAddress;
import jodd.mail.ReceiveMailSession;
import jodd.mail.ReceivedEmail;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;
import jodd.util.MimeTypes;
import javafx.beans.property.*;
/**
 * This class sends and receives email with smtp and Imap It implements several
 * fields such as to, cc, bcc, attachment and embed file
 * 
 * 
 * @author Frank Birikundavyi
 *
 */
@SuppressWarnings("restriction")
public class BasicSendAndReceive {

	/**
	 * Basic send routine for this sample. It does not handle CC, BCC,
	 * attachments or other information.
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public final String sendEmail(final MailBean mailBean, final MailConfigBean sendConfigBean)
			throws IOException, SQLException {

		// Create am SMTP server object
		SmtpServer<?> smtpServer = SmtpSslServer.create(sendConfigBean.getHost())
				.authenticateWith(sendConfigBean.getUserEmailAddress(), sendConfigBean.getPassword());

		// Do not use the fluent type because ArrayLists need to be processed
		// from the bean
		Email email = Email.create();

		email.from(sendConfigBean.getUserEmailAddress());

		for ( StringProperty emailAddress : mailBean.getToField()) {
			email.to(emailAddress.getValue());
		}
		for (StringProperty emailAddress : mailBean.getBccField()) {
			email.bcc(emailAddress.getValue());
		}

		if (mailBean.getHtmlMessageField().isEmpty()) {
			email.subject(mailBean.getSubjectField()).addText(mailBean.getTextMessageField());
		} else {
			email.subject(mailBean.getSubjectField()).addText(mailBean.getTextMessageField())
					.addHtml(mailBean.getHtmlMessageField());
		}

		for (StringProperty emailAddress : mailBean.getCcField()) {
			email.cc(emailAddress.getValue());
		}
		for (EmailAttachment ea : mailBean.getAttachAttachments()) {
			email.attach(ea);
		}
		for (EmailAttachment ea : mailBean.getEmbedAttachments()) {
			email.embed(ea);
		}

		mailBean.getFolder().setName("SentMessage");
		// A session is the object responsible for communicating with the server
		SendMailSession session = smtpServer.createSession();

		// Like a file we open the session, send the message and close the
		// session
		String messageId = "";
		if (session == null) {
		} else {
			session.open();

			messageId = session.sendMail(email);
			mailBean.setSendTime(LocalDateTime.now());
			session.close();
		}
		return messageId;

	}

	/**
	 * Basic receive that only takes the values that match the basic mail bean.
	 * Returns an array list because there could be more than one message. This
	 * could be a problem during testing so use addresses that do not receive
	 * any other messages and you can assume that subscript 0 in the array is
	 * the message you just sent.
	 */
	public final ArrayList<MailBean> receiveEmail(final MailConfigBean receiveConfigBean, Logger log) {

		ArrayList<MailBean> mailBeans = null;

		// Create an IMAP server that does not display debug info
		ImapServer imapServer = new ImapSslServer(receiveConfigBean.getHost(), receiveConfigBean.getUserEmailAddress(),
				receiveConfigBean.getPassword());

		// A session is the object responsible for communicating with the server
		ReceiveMailSession session = imapServer.createSession();
		session.open();

		// We only want messages that have not been read yet.
		// Messages that are delivered are then marked as read on the server
		ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(EmailFilter.filter().flag(Flags.Flag.SEEN, false));

		// If there is any email then loop through them adding their contents to
		// a new MailBean that is then added to the array list.
		if (emails != null) {

			// Instantiate the array list of messages
			mailBeans = new ArrayList<MailBean>();

			for (ReceivedEmail email : emails) {

				MailBean mailBean = new MailBean();
				mailBean.setReceiveTime(LocalDateTime.now());
				mailBean.setFromField(email.getFrom().getEmail());
				mailBean.setSubjectField(email.getSubject());
				for (MailAddress mailAddress : email.getTo()) {
					mailBean.getToField().add(new SimpleStringProperty(mailAddress.getEmail()));
				}
				for (MailAddress mailAddress : email.getCc()) {
					mailBean.getCcField().add(new SimpleStringProperty(mailAddress.getEmail()));
				}
				mailBean.setReceiveTime(LocalDateTime.now());

				// Messages may be multi-part so they are stored in an array
				// In this demo we only want the first part
				List<EmailMessage> messages = email.getAllMessages();
				for (EmailMessage message : messages) {
					if (message.getMimeType() == MimeTypes.MIME_TEXT_HTML) {
						mailBean.setHtmlMessageField(message.getContent());
					} else {
						mailBean.setTextMessageField(message.getContent());
					}
				}

				mailBean.getFolder().setName("Inbox");
				mailBean.setMailStatus(1);
				if (email.getAttachments() != null) {
					for (EmailAttachment ea : email.getAttachments()) {

						if (ea.getContentId() == null) {
							log.debug("there is one attachment file");
							mailBean.getAttachAttachments().add(ea);
						} else {
							log.debug("there is a one embed");
							mailBean.getEmbedAttachments().add(ea);
						}
					}
				}
				for (EmailMessage em : email.getAllMessages()) {
					String mimeType = em.getMimeType();
					log.debug(mimeType);
					log.debug("Second mime type:  " + MimeTypes.MIME_TEXT_HTML.toString());
					if (mimeType.equalsIgnoreCase(MimeTypes.MIME_TEXT_HTML.toString())) {
						mailBean.setHtmlMessageField(em.getContent().trim());
						log.debug("html is set");
					} else {
						mailBean.setTextMessageField(em.getContent().trim());
					}
				}
				// Add the mailBean to the array list
				mailBeans.add(mailBean);

			}
		}
		session.close();

		return mailBeans;
	}
}
