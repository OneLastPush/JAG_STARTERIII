package com.birifr.test.persistence;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.junit.Before;

import com.birikfr.mailbean.FolderBean;
import com.birikfr.mailbean.MailBean;
import com.birikfr.persistence.MailDAO;
import com.birikfr.persistence.MailDAOImpl;

import javafx.beans.property.SimpleStringProperty;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import org.slf4j.LoggerFactory;
import org.junit.Ignore;
import org.junit.Rule;
import com.birikfr.mailaction.BasicSendAndReceive;

import com.birikfr.properties.MailConfigBean;
import com.birikfr.test.MethodLogger;

import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;*/
@SuppressWarnings("restriction")
public class mailDAOTest {
	private final String url = "jdbc:mysql://localhost:3306/";
	private final String user = "root";
	private final String password = "";
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	@Test
	public void findAllTest() throws SQLException{
		
		MailDAO dao = new MailDAOImpl();
		ArrayList<MailBean> emails = new ArrayList<>();
		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 01");
		mailBeanSend.setTextMessageField("This is the text of the message - 01.");
		mailBeanSend.setSendTime(LocalDateTime.now());
		mailBeanSend.getFolder().setName("School");
		mailBeanSend.setFromField("birikfr.receive@gmail.com");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 03 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		eab = EmailAttachment.attachment().bytes(new File("unicat.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		
		dao.create(mailBeanSend);
		log.info("TEST #1");
		log.info("Find all method : Expected result 1");
		
		
		emails = dao.findMailByToField("birikfr.receive@gmail.com");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		boolean test = false;
		if(emails.size() == 1){
			test=true;
		}
		
		log.info("Number of Result found is " + emails.size());
		assertTrue("not the same number of result" , test);
	}
	 
	@Test
	public void insertMultipleEntry() throws SQLException{
		MailDAO dao = new MailDAOImpl();
		ArrayList<MailBean> emails = new ArrayList<>();
		MailBean mailBeanSend = new MailBean();
		//Email #1
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 01");
		mailBeanSend.setTextMessageField("This is the text of the message - 01.");
		mailBeanSend.setSendTime(LocalDateTime.now());
		mailBeanSend.setFromField("birikfr.send@gmail.com");
		emails.add(mailBeanSend);
		mailBeanSend = new MailBean();
		//Email #2
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 02");
		mailBeanSend.setTextMessageField("This is the text of the message - 02." + "This email has a attach file");
		mailBeanSend.setSendTime(LocalDateTime.now());

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		mailBeanSend.setFromField("birikfr.send@gmail.com");
		emails.add(mailBeanSend);
		mailBeanSend = new MailBean();
		//Email #3
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 03" + "Embed image");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 03 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");

		eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		mailBeanSend.setSendTime(LocalDateTime.now());
		mailBeanSend.setFromField("birikfr.send@gmail.com");
		emails.add(mailBeanSend);
		for(MailBean mb: emails){
		mb.setId(dao.create(mb));	
		}
		ArrayList<MailBean> emailsReceived = new ArrayList<>();
		emailsReceived = dao.findMailByToField("birikfr.receive@gmail.com");
		log.info("TEST #2");
		log.info("Create method : Expected result 3 results found");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
	
		
		log.info("Number of Result found is " + emailsReceived.size());
		
		
		assertEquals("not the same number of result" , 3, emailsReceived.size());
		
		
	}
	
	@Test
	public void createFolderTest() throws SQLException{
		MailDAO dao = new MailDAOImpl();
		ArrayList<MailBean> emails = new ArrayList<>();
		FolderBean folder = new FolderBean();
		folder.setName("Work");
		dao.create(folder);
		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.getCcField().add(new SimpleStringProperty("zejingle@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 01");
		mailBeanSend.setTextMessageField("This is the text of the message - 01.");
		mailBeanSend.setSendTime(LocalDateTime.now());
		mailBeanSend.getFolder().setName("Work");
		mailBeanSend.setFromField("birikfr.send@gmail.com");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 03 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		eab = EmailAttachment.attachment().bytes(new File("unicat.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		
		emails = dao.findMailByFolder("Work");
		assertEquals("find Mail By folder did not return the correct email", emails.get(0), mailBeanSend);
	}
	@Test
	public void findBccField() throws SQLException{
		MailDAO dao = new MailDAOImpl();
		ArrayList<MailBean> emails = new ArrayList<>();
		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.getBccField().add(new SimpleStringProperty("zejingle@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 01");
		mailBeanSend.setTextMessageField("This is the text of the message - 01.");
		mailBeanSend.setSendTime(LocalDateTime.now());
		mailBeanSend.getFolder().setName("School");
		mailBeanSend.setFromField("birikfr.send@gmail.com");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 03 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		eab = EmailAttachment.attachment().bytes(new File("unicat.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		
		dao.create(mailBeanSend);	

		//This email is the same  exact email except for the Bcc field
		//It is not suppose to return this field
		mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 01");
		mailBeanSend.setTextMessageField("This is the text of the message - 01.");
		mailBeanSend.setSendTime(LocalDateTime.now());
		mailBeanSend.getFolder().setName("School");
		mailBeanSend.setFromField("birikfr.send@gmail.com");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 03 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");

		eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		eab = EmailAttachment.attachment().bytes(new File("unicat.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		dao.create(mailBeanSend);
		//add the missing field after adding the not equal mailbean in the database
		//now there are suppose to be equal
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		mailBeanSend.getBccField().add(new SimpleStringProperty("zejingle@gmail.com"));
		emails = dao.findMailByBCCField("zejingle@gmail.com");
		if(emails.size() != 1){
			fail("Return " + emails.size() + " result instead of 1");
		}
		assertEquals("The two message are not equals", emails.get(0),mailBeanSend);
		
		
		
		
	}
	@Test
	public void deleteEmail() throws IOException, SQLException {

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 06");
		mailBeanSend.setHtmlMessageField("<h1> A Test Message - 06<//h1>");
		
		String info = "Test 7 has been initialized - Basic email Test: 1 recipient, 0 bcc, 0 cc, an subject, 1 html ";
		MailDAO dao = new MailDAOImpl();
		
		dao.create(mailBeanSend);
		dao.deleteMail(mailBeanSend.getId());
		dao.findMailByFromField(mailBeanSend.getFromField());

	}
	@Test
	public void createNewFolder() throws SQLException{
		MailDAO dao = new MailDAOImpl();
		ArrayList<MailBean> emails = new ArrayList<>();
		MailBean mailBeanSend = new MailBean();
	}
	@Test
	public void entrySendReceivedEquals() throws SQLException{
		MailDAO dao = new MailDAOImpl();
		ArrayList<MailBean> emails = new ArrayList<>();
		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add(new SimpleStringProperty("birikfr.receive@gmail.com"));
		mailBeanSend.setSubjectField("A Test Message - 01");
		mailBeanSend.setTextMessageField("This is the text of the message - 01.");
		mailBeanSend.setSendTime(LocalDateTime.now());
		mailBeanSend.getFolder().setName("School");
		mailBeanSend.setFromField("birikfr.receive@gmail.com");
		mailBeanSend.setHtmlMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1> A Test Message - 03 Here is a embedded picture of my cat in this email.</h1><img src='cid:catgon.jpg'></body></html>");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("catgon.jpg"));
		EmailAttachment ea = eab.setInline("catgon.jpg").create();
		mailBeanSend.getEmbedAttachments().add(ea);
		eab = EmailAttachment.attachment().bytes(new File("unicat.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachAttachments().add(ea);
		
		dao.create(mailBeanSend);	
		
		ArrayList<MailBean> mailBeansReceive = new ArrayList<>();
		mailBeansReceive = dao.findMailByToField("birikfr.receive@gmail.com");
		log.info("TEST #3");
		log.info("Create method : retrieve email identical to the original email");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		boolean test = false;
		

			if(mailBeanSend.equals(mailBeansReceive.get(0),log)){
				test = true;
			}
		
		
		log.info("Number of Result found is " + mailBeansReceive.size());

		log.info("     To: " + mailBeanSend.getToField().get(0) + " : "
				+ mailBeansReceive.get(0).getToField().get(0));
		log.info("   From: " + mailBeanSend.getFromField() + " : " + mailBeansReceive.get(0).getFromField());
		log.info("Subject: " + mailBeanSend.getSubjectField() + " : " + mailBeansReceive.get(0).getSubjectField());
		log.info(
				"   Text: " + mailBeanSend.getTextMessageField() + "=" + mailBeanSend.getTextMessageField().length()
						+ " : " + mailBeansReceive.get(0).getTextMessageField() + "="
						+ mailBeansReceive.get(0).getTextMessageField().length());

		
		assertTrue("The entries are not equal" , test);
		
		
	}
	
	@Before 
	public void seedDatabase() {
		final String seedDataScript = loadAsString("createMailMySql.sql");
		try (Connection connection = DriverManager.getConnection(url, user, password);) {
			for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
				connection.prepareStatement(statement).execute();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed seeding database", e);
		}
	}
	private String loadAsString(final String path) {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
				Scanner scanner = new Scanner(inputStream)) {
			return scanner.useDelimiter("\\A").next();
		} catch (IOException e) {
			throw new RuntimeException("Unable to close input stream.", e);
		}
	}

	private List<String> splitStatements(Reader reader, String statementDelimiter) {
		final BufferedReader bufferedReader = new BufferedReader(reader);
		final StringBuilder sqlStatement = new StringBuilder();
		final List<String> statements = new LinkedList<String>();
		try {
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || isComment(line)) {
					continue;
				}
				sqlStatement.append(line);
				if (line.endsWith(statementDelimiter)) {
					statements.add(sqlStatement.toString());
					sqlStatement.setLength(0);
				}
			}
			return statements;
		} catch (IOException e) {
			throw new RuntimeException("Failed parsing sql", e);
		}
		
	}
	private boolean isComment(final String line) {
		return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
	}


}
