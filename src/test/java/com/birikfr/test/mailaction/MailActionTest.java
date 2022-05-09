package com.birikfr.test.mailaction;

import javafx.beans.property.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.birikfr.mailaction.BasicSendAndReceive;
import com.birikfr.mailbean.MailBean;
import com.birikfr.properties.MailConfigBean;
import com.birikfr.test.MethodLogger;

import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * A basic test method that determines if a simple message sent is the same
 * message received
 * 
 * @author Frank Birikundavyi
 *
 */
@SuppressWarnings("restriction")
public class MailActionTest {
	
	
	// A Rule is implemented as a class with methods that are associated
	// with the lifecycle of a unit test. These methods run when required.
	// Avoids the need to cut and paste code into every test method.
	@Rule
	public MethodLogger methodLogger = new MethodLogger();

	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	/**
	 * Test method for
	 * {@link com.kenfogel.mailaction.BasicSendAndReceive#sendEmail(com.birikfr.mailbean.MailBean, com.kenfogel.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared.
	 * 
	 * @throws IOException
	 */
	private void sendEmail(MailBean mailBeanSend, String info) throws IOException {
		MailConfigBean sendConfigBean = new MailConfigBean("smtp.gmail.com", "birikfr.send@gmail.com", "gallerie","","","","",null);
		MailConfigBean receiveConfigBean = new MailConfigBean("imap.gmail.com", "birikfr.receive@gmail.com",
				"gallerie","","","","",null);
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();
		String messageId;
		try {
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
			log.info(info);
			log.info("MessageId is " + messageId);

			// Add a five second pause to allow the Gmail server to receive what
			// has
			// been sent
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.error("Threaded sleep failed", e);
			}

			ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean, log);

			boolean test = false;

			log.info("     To: " + mailBeanSend.getToField().get(0) + " : "
					+ mailBeansReceive.get(0).getToField().get(0));
			log.info("   From: " + mailBeanSend.getFromField() + " : " + mailBeansReceive.get(0).getFromField());
			log.info("Subject: " + mailBeanSend.getSubjectField() + " : " + mailBeansReceive.get(0).getSubjectField());
			log.info(
					"   Text: " + mailBeanSend.getTextMessageField() + "=" + mailBeanSend.getTextMessageField().length()
							+ " : " + mailBeansReceive.get(0).getTextMessageField() + "="
							+ mailBeansReceive.get(0).getTextMessageField().length());

			test = mailBeanSend.equals(mailBeansReceive.get(0), log);
			assertTrue("Messages are not the same", test);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	@Test
	public void testSendBasicEmail() throws IOException {
		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 01");
		mailBeanSend.setTextMessageField("This is the text of the message - 01.");
		String info = "Test 1: ONE text message, ONE subject, ONE recipient ";
		sendEmail(mailBeanSend, info);
	}

	@Test
	public void testEmbededEmail() throws IOException {

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 02");
		mailBeanSend.setTextMessageField("This is the text of the message - 02." + "This email has a attach file");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		String info = "Test 2: ONE text message, ONE subject, ONE recipient, ONE attachment image ";
		sendEmail(mailBeanSend, info);
	}

	@Test
	public void testHtmlEmail() throws IOException {
		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 03" + "Embed image");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 03 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		String info = "Test 3: ONE html message, ONE subject, ONE recipient, ONE embed image ";
		sendEmail(mailBeanSend, info);
	}
	@Test
	public void testAttachAndEmbedEmail() throws IOException {
		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 05");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 04 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		String info = "Test 4: ONE html message, ONE subject, ONE recipient, ONE embed image, ONE attachment ";
		sendEmail(mailBeanSend, info);
	}

	@Test
	public void testMultipleRecipientEmail() throws IOException {

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.send@gmail.com"));
		mailBeanSend.getToField().add(new SimpleStringProperty("zejingle@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 08");
		mailBeanSend.setTextMessageField(" A Test Message - 08");
		String info = "Test 5 has been initialized - Basic html email Test: 3 recipient, 0 bcc, 0 cc, an subject, 0 textField, 0 html ";
		sendEmail(mailBeanSend, info);

	}

	@Test
	public void testCCEmail() throws IOException {

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.getCcField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.getCcField().add(new SimpleStringProperty("birikfr.send@gmail.com"));
		mailBeanSend.getCcField().add(new SimpleStringProperty("zejingle@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 06");
		mailBeanSend.setHtmlMessageField("<h1> A Test Message - 06<//h1>");

		String info = "Test 6 has been initialized - Basic html email Test: 1 recipient, 0 bcc, 3 cc, an subject, 1 html ";
		sendEmail(mailBeanSend, info);
	}
	

	@Test
	public void testBCCEmail() throws IOException {

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.getBccField().add(new SimpleStringProperty("zejingle@gmail.com"));
		mailBeanSend.getCcField().add(new SimpleStringProperty("birikfr.send@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 07");
		mailBeanSend.setHtmlMessageField("<h1> A Test Message in bcc - 07");

		String info = "Test 7 has been initialized - Basic html email Test: 1 recipient, 1 bcc, 0 cc, 1 attachment, 1 embeded, an subject, 0 textField, 1 html ";

		sendEmail(mailBeanSend, info);

	}

}
