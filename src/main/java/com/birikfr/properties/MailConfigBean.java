package com.birikfr.properties;
import java.util.Locale;

import javafx.beans.property.*;
/**
 * Class to contain the information for an email account. This is sufficient for
 * this project but will need more fields if you wish the program to work with
 * mail systems other than GMail. This should be stored in properties file. If
 * you are feeling adventurous you can look into how you might encrypt the
 * password as it will be in a simple text file.
 * 
 * @author Frank Birikundavyi
 * 
 */
@SuppressWarnings("restriction")
public class MailConfigBean {

	private StringProperty host;
	private StringProperty userEmailAddress;
	private StringProperty password;
	private StringProperty smtp;
	private StringProperty imap;
	private StringProperty firstName;
	private StringProperty lastName;
	private Locale locale;

	/**
	 * Default Constructor
	 */
	public MailConfigBean() {
		super();
		this.host = new SimpleStringProperty();
		this.userEmailAddress = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.smtp = new SimpleStringProperty();
		this.imap = new SimpleStringProperty();
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.locale = null;
	}

	/**
	 * @param host
	 * @param userEmailAddress
	 * @param password
	 * @param stmp
	 * @param imap
	 * @param firstName
	 * @param lastName
	 * @param locale
	 * 
	 */
	public MailConfigBean(final String host, final String userEmailAddress, final String password,
			final String stmp,final String imap, final String firstName, final String lastName,final Locale locale) {
		super();
		this.host = new SimpleStringProperty(host);
		this.userEmailAddress = new SimpleStringProperty(userEmailAddress);
		this.password = new SimpleStringProperty(password);
		this.smtp = new SimpleStringProperty(stmp);
		this.imap = new SimpleStringProperty(imap);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.locale = locale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((imap == null) ? 0 : imap.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((smtp == null) ? 0 : smtp.hashCode());
		result = prime * result + ((userEmailAddress == null) ? 0 : userEmailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MailConfigBean other = (MailConfigBean) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.get().equals(other.firstName.get()))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.get().equals(other.host.get()))
			return false;
		if (imap == null) {
			if (other.imap != null)
				return false;
		} else if (!imap.get().equals(other.imap.get()))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.get().equals(other.lastName.get()))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.get().equals(other.password.get()))
			return false;
		if (smtp == null) {
			if (other.smtp != null)
				return false;
		} else if (!smtp.get().equals(other.smtp.get()))
			return false;
		if (userEmailAddress == null) {
			if (other.userEmailAddress != null)
				return false;
		} else if (!userEmailAddress.get().equals(other.userEmailAddress.get()))
			return false;
		return true;
	}
	public Locale getLocale(){
		return locale;
	}
	public void setLocale(Locale locale){
		this.locale = locale;
	}
	/**
	 * @return the stmp
	 */
	public final String getSmtp() {
		return smtp.get();
	}
	public final StringProperty getSmtpProperty() {
		return smtp;
	}
	/**
	 * @param smtp
	 *            the smtp to set
	 */
	
	public final void setImap(final String smtp) {
		this.smtp = new SimpleStringProperty(smtp);
	}
	
	/**
	 * @return the Imap
	 */
	public final String getImap() {
		return imap.get();
	}
	public final StringProperty getImapProperty() {
		return imap;
	}

	/**
	 * @return the firstName
	 */
	public final String getFirstName() {
		return firstName.get();
	}
	public final StringProperty getFirstNameProperty() {
		return firstName;
	}
	/**
	 * @param firstName
	 *            the firstName to set
	 */
	
	public final void setFirstName(final String firstName) {
		this.firstName = new SimpleStringProperty(firstName);
	}
	/**
	 * @return the lastName
	 */
	public final String getLastName() {
		return lastName.get();
	}
	public final StringProperty getLastNameProperty() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the firstName to set
	 */
	
	public final void setLastName(final String lastName) {
		this.lastName = new SimpleStringProperty(lastName);
	}
	/**
	 * @return the host
	 */
	public final String getHost() {
		return host.getValue();
	}
	public final StringProperty getHostProperty() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	
	public final void setHost(final String host) {
		this.host = new SimpleStringProperty(host);
	}

	/**
	 * @return the userName
	 */
	public final String getUserEmailAddress() {
		return userEmailAddress.getValue();
	}
	public final StringProperty getUserEmailAddressProperty() {
		return userEmailAddress;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public final void setUserEmailAddress(final String userEmailAddress) {
		this.userEmailAddress = new SimpleStringProperty(userEmailAddress);
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password.getValue();
	}
	public final StringProperty getPasswordProperty() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public final void setPassword(final String password) {
		this.password = new SimpleStringProperty(password);
	}
}