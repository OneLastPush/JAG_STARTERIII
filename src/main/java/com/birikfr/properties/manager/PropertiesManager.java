package com.birikfr.properties.manager;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;

import java.io.*;


import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.birikfr.properties.MailConfigBean;

/**
 * Example of a class to manager properties
 *
 * @author Frank Birikundavyi
 *
 */
public class PropertiesManager {

    /**
     * Returns a MailConfigBean object with the contents of the properties file
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final MailConfigBean loadTextProperties(final String path, final String propFileName) throws IOException {

        Properties prop = new Properties();

        Path txtFile = get(path, propFileName + ".properties");

        MailConfigBean mailConfig = new MailConfigBean();
        File directory = new File(path);
        if(!directory.exists()){
        	directory.mkdirs();
        	
        	}
        
        // File must exist
        if (Files.exists(txtFile)) {
            try (InputStream propFileStream = newInputStream(txtFile);) {
                prop.load(propFileStream);
            }
            mailConfig.setHost(prop.getProperty("host"));
            mailConfig.setUserEmailAddress(prop.getProperty("userName"));
            mailConfig.setPassword(prop.getProperty("password"));
            mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
            mailConfig.setUserEmailAddress(prop.getProperty("imap"));
            mailConfig.setUserEmailAddress(prop.getProperty("smtp"));
            mailConfig.setUserEmailAddress(prop.getProperty("firstName"));
            mailConfig.setUserEmailAddress(prop.getProperty("lastName"));
            mailConfig.setLocale(Locale.forLanguageTag(prop.getProperty("Language")));
        }
        return mailConfig;
    }

    /**
     * Creates a plain text properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file
     * @param mailConfig The bean to store into the properties
     * @throws IOException
     */
    public final void writeTextProperties(final String path, final String propFileName, final MailConfigBean mailConfig) throws IOException {

        Properties prop = new Properties();
        prop.setProperty("host", mailConfig.getHost());
        prop.setProperty("userName", mailConfig.getUserEmailAddress());
        prop.setProperty("password", mailConfig.getPassword());
        prop.setProperty("userEmailAddress", mailConfig.getUserEmailAddress());
        prop.setProperty("imap", mailConfig.getImap());
        prop.setProperty("smtp", mailConfig.getSmtp());
        prop.setProperty("lastName", mailConfig.getLastName());
        prop.setProperty("firstName", mailConfig.getFirstName());
        prop.setProperty("Language", mailConfig.getLocale().toLanguageTag());
        Path txtFile = get(path, propFileName + ".properties");
        File directory = new File(path);
        if(!directory.exists()){
        	directory.mkdirs();
        	}
        System.out.println(directory.getAbsolutePath());
        File file = new File(path + propFileName + ".properties");
        
        if(!file.exists()){
        	Writer writer = null;

        	try {
        	    writer = new BufferedWriter(new OutputStreamWriter(
        	          new FileOutputStream(path + propFileName + ".properties"), "utf-8"));
        	    prop.store(writer, "SMTP Properties");
        	    
        	}finally {
        	   {writer.close();} 
        	}
        }
        
        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(txtFile)) {
            prop.store(propFileStream, "SMTP Properties");
        }
    }

    /**
     * Returns a MailConfigBean object with the contents of the properties file
     * that is in an XML format
     *
     * @param path Must exist, will not be created. Empty string means root of
     * program folder
     * @param propFileName Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final MailConfigBean loadXmlProperties(final String path, final String propFileName) throws IOException {

        Properties prop = new Properties();

        // The path of the XML file
        Path xmlFile = get(path, propFileName + ".xml");

        MailConfigBean mailConfig = new MailConfigBean();

        // File must exist
        if (Files.exists(xmlFile)) {
            try (InputStream propFileStream = newInputStream(xmlFile);) {
                prop.loadFromXML(propFileStream);
            }
            mailConfig.setUserEmailAddress(prop.getProperty("userName"));
            mailConfig.setPassword(prop.getProperty("password"));
            mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
        }
        return mailConfig;
    }

    /**
     * Creates an XML properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file. Empty string means root
     * of program folder
     * @param mailConfig The bean to store into the properties
     * @throws IOException
     * @throws ParserConfigurationException 
     */
    public final void writeXmlProperties(final String path, final String propFileName, final MailConfigBean mailConfig) throws IOException, ParserConfigurationException {

        Properties prop = new Properties();

        prop.setProperty("userName", mailConfig.getUserEmailAddress());
        prop.setProperty("password", mailConfig.getPassword());
        prop.setProperty("userEmailAddress", mailConfig.getUserEmailAddress());

        Path xmlFile = get(path, propFileName + ".xml");
        File file = new File(path + propFileName);
        if(!file.exists()){
        	file.createNewFile();
        }
        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(xmlFile)) {
            prop.storeToXML(propFileStream, "XML SMTP Properties");
        }
    }

    /**
     * Retrieve the properties file. This syntax rather than normal File I/O is
     * employed because the properties file is inside the jar. The technique
     * shown here will work in both Java SE and Java EE environments. A similar
     * technique is used for loading images.
     *
     * In a Maven project all configuration files, images and other files go
     * into src/main/resources. The files and folders placed there are moved to
     * the root of the project when it is built.
     *
     * @param propertiesFileName : Name of the properties file
     * @throws IOException : Error while reading the file
     * @throws NullPointerException : File not found
     */
    public final MailConfigBean loadJarTextProperties(final String propertiesFileName) throws IOException, NullPointerException {

        Properties prop = new Properties();
        MailConfigBean mailConfig = new MailConfigBean();

        // There is no exists method for files in a jar so we try to get the
        // resource and if its not there it returns a null
        if (this.getClass().getResource("/" + propertiesFileName) != null) {
            // Assumes that the properties file is in the root of the
            // project/jar.
            // Include a path from the root if required.
            try (InputStream stream = this.getClass().getResourceAsStream("/" + propertiesFileName);) {
                prop.load(stream);
            }
            mailConfig.setUserEmailAddress(prop.getProperty("userName"));
            mailConfig.setPassword(prop.getProperty("password"));
            mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
        }
        return mailConfig;
    }
}
