package com.birikfr.propretiesTest;

import static org.junit.Assert.*;


import com.birikfr.properties.manager.PropertiesManager;

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

public class PropretiesManagerTest {
	String host;
	String userEmailAddress;
	String password;
	String smtp;
	String imap;
	String fName;
	String lName;
	
	@Rule
	public MethodLogger methodLogger = new MethodLogger();

	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	@Test
	public void equalsPropertiesFile(){
	host = "smtp.gmail.com";
	userEmailAddress = "birikfr.send@gmail.com";
	password = "gallerie";
	imap = "imap.gmail.com";
	smtp = "smtp.gmail.com";
	fName = "Frnak";
	lName = "Birik";
	
		MailConfigBean mcb = new MailConfigBean(host,userEmailAddress,password,imap,smtp,fName,lName,null);
		PropertiesManager pm = new PropertiesManager();
		try {
			pm.writeTextProperties("congfigFolder","configFile",mcb);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.warn("File not created");
			e1.printStackTrace();
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		;
		try {
			MailConfigBean mcbCopy;
			mcbCopy = pm.loadTextProperties(".\\congfigFolder", "configFile");
			System.out.println( "HOST: " + mcbCopy.getHost());
			System.out.println( "User: " + mcbCopy.getUserEmailAddress());
			System.out.println( "Password: " + mcbCopy.getPassword());
			assertEquals("This config file doesnot save the proprety correctly", mcb, mcbCopy);

		} catch (IOException e) {
			log.warn("File is not loaded");
			e.printStackTrace();
		}
	}
}
