/**
 * 
 */
package com.birikfr.mailbean;

import javafx.beans.property.*;
import javafx.beans.value.WritableObjectValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Arrays;
import org.slf4j.Logger;
import jodd.mail.EmailAttachment;
import jodd.util.StringTemplateParser;

/**
 * Here is a basic email data bean. It will have to have more fields for the
 * project
 * 
 * @author Frank Birikundavyi
 *
 */
@SuppressWarnings("restriction")
public class MailBean {

	private ArrayList<StringProperty> bccField;
	private ArrayList<StringProperty> ccField;

	private LocalDateTime sendTime;
	private LocalDateTime receiveTime;
	private ArrayList<EmailAttachment> embedAttachments;
	private ArrayList<EmailAttachment> attachAttachments;

	// The address or addresses that this email is being sent to
	private ArrayList<StringProperty> toField;
	// The sender of the email
	private StringProperty fromField;
	// The subject line of the email
	private StringProperty subjectField;
	// Plain text & html part of the email
	private StringProperty textMessageField;
	private StringProperty htmlMessageField;
	// Name of the folder
	private FolderBean folder;

	// Status 0 = New Email for Sending
	// Status 1 = Received Email
	private int mailStatus;
	private int emailId;

	/**
	 * Default constructor for a new mail message waiting to be sent
	 */
	
	public MailBean() {
		this(new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now().minusMinutes(1),
				new ArrayList<>(), "", "", "", new FolderBean(), 0, "", new ArrayList<>(), new ArrayList<>(), -1);

	}

	
	public MailBean(final ArrayList<StringProperty> bccField, final ArrayList<StringProperty> ccField,
			final LocalDateTime sendTime, final LocalDateTime receiveTime, final ArrayList<StringProperty> toField,
			final String fromField, final String subjectField, final String textMessageField, final FolderBean folder,
			final int mailStatus, final String htmlMessageField, final ArrayList<EmailAttachment> embedAttachments,
			final ArrayList<EmailAttachment> attachAttachments, final int emailId) {
		this.bccField = bccField;
		this.attachAttachments = attachAttachments;
		this.ccField = ccField;
		this.emailId = emailId;
		this.embedAttachments = embedAttachments;
		this.folder = folder;
		this.fromField = new SimpleStringProperty(fromField);
		this.htmlMessageField = new SimpleStringProperty(htmlMessageField);
		this.mailStatus = mailStatus;
		this.receiveTime = receiveTime;
		this.sendTime = sendTime;
		this.subjectField = new SimpleStringProperty(subjectField);
		this.textMessageField = new SimpleStringProperty(textMessageField);
		this.toField = toField;

	}

	/**
	 * @return the fromField
	 */
	public final String getFromField() {
		return fromField.getValue();
	}
	public final StringProperty getFromFieldProperty() {
		return fromField;
	}

	/**
	 * When passing a reference to a setter it is best practice to declare it
	 * final so that the setter cannot change the reference
	 * 
	 * @param fromField
	 *            the fromField to set
	 */
	public final void setFromField(final String fromField) {
		this.fromField.set(fromField);
	}

	/**
	 * @return the subjectField
	 */
	public final String getSubjectField() {
		return subjectField.getValue();
	}
	public final StringProperty getSubjectFieldProperty() {
		return subjectField;
	}

	/**
	 * @param subjectField
	 *            the subjectField to set
	 */
	public final void setSubjectField(final String subjectField) {
		this.subjectField.set(subjectField);
	}

	/**
	 * @return the textMessageField
	 */
	public final String getTextMessageField() {
		return textMessageField.getValue();
	}
	
	public final StringProperty getTextMessageFieldProperty() {
		return textMessageField;
	}
	/**
	 * @param textMessageField
	 *            the textMessageField to set
	 */
	public final void setTextMessageField(final String textMessageField) {
		this.textMessageField.set(textMessageField);
	}

	/**
	 * @return the folderBeab
	 */
	public final FolderBean getFolder() {
		return folder;
	}

	/**
	 * @return the mailStatus
	 */
	public final int getMailStatus() {
		return mailStatus;
	}

	/**
	 * @param mailStatus
	 *            the mailStatus to set
	 */
	public final void setMailStatus(final int mailStatus) {
		this.mailStatus = mailStatus;
	}

	/**
	 * There is no set when working with collections. When you get the ArrayList
	 * you can add elements to it. A set method implies changing the current
	 * ArrayList for another ArrayList and this is something we rarely do with
	 * collections.
	 * 
	 * @return the toField
	 */
	public final ArrayList<StringProperty> getToField() {
		return toField;
	}

	public String getHtmlMessageField() {
		return htmlMessageField.getValue();
	}
	public StringProperty getHtmlMessageFieldProperty() {
		return htmlMessageField;
	}
	
	public void setHtmlMessageField(String htmlMessageField) {
		this.htmlMessageField.setValue(htmlMessageField);
	}

	public ArrayList<StringProperty> getBccField() {
		return bccField;
	}

	public ArrayList<StringProperty> getCcField() {
		return ccField;
	}

	public LocalDateTime getSendTime() {
		return sendTime;
	}

	public LocalDateTime getReceiveTime() {
		return receiveTime;
	}

	public ArrayList<EmailAttachment> getAttachAttachments() {
		return this.attachAttachments;
	}

	public void setReceiveTime(LocalDateTime receiveTime) {
		this.receiveTime = receiveTime;
	}

	public void setSendTime(LocalDateTime sendTime) {
		this.sendTime = sendTime;
	}

	public ArrayList<EmailAttachment> getEmbedAttachments() {
		return this.embedAttachments;
	}

	public void setId(int mailId) {
		this.emailId = mailId;

	}

	public int getId() {
		return this.emailId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachAttachments == null) ? 0 : attachAttachments.hashCode());
		result = prime * result + ((ccField == null) ? 0 : ccField.hashCode());
		result = prime * result + ((embedAttachments == null) ? 0 : embedAttachments.hashCode());
		result = prime * result + ((fromField == null) ? 0 : fromField.hashCode());
		result = prime * result + ((htmlMessageField == null) ? 0 : htmlMessageField.hashCode());
		result = prime * result + ((subjectField == null) ? 0 : subjectField.hashCode());
		result = prime * result + ((textMessageField == null) ? 0 : textMessageField.hashCode());
		result = prime * result + ((toField == null) ? 0 : toField.hashCode());
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
		MailBean other = (MailBean) obj;
		String trimStr;
		StringProperty temp = new SimpleStringProperty();
		if (attachAttachments == null) {
			if (other.attachAttachments != null)
				return false;
		} else {

			if (attachAttachments.size() != other.attachAttachments.size()) {
				return false;
			} else {
				for (int ctr = 0; ctr < attachAttachments.size(); ctr++) {
					if (!Arrays.equals(attachAttachments.get(ctr).toByteArray(),
							other.attachAttachments.get(ctr).toByteArray())) {
						return false;
					}
				}
			}
		}
		if (ccField == null) {
			if (other.ccField != null)
				return false;
		} else {
			if (other.ccField != null) {
				if (ccField.size() < 0)
					for (int index = 0, len = ccField.size(); index < len; index++) {
						trimStr = ccField.get(index).getValue().trim();
						temp.set(trimStr);
						ccField.set(index, temp);
					}

				if (other.ccField.size() < 0)
					for (int index = 0, len = other.ccField.size(); index < len; index++) {
						trimStr = other.ccField.get(index).getValue().trim();
						temp.set(trimStr);
						other.ccField.set(index, temp);
					}
			}
			if (!ccField.equals(other.ccField))
				return false;
		}
		if (embedAttachments == null) {
			if (other.embedAttachments != null)
				return false;
		} else {

			if (embedAttachments.size() != other.embedAttachments.size()) {
				return false;
			} else {
				for (int ctr = 0; ctr < embedAttachments.size(); ctr++) {
					if (!Arrays.equals(embedAttachments.get(ctr).toByteArray(),
							other.embedAttachments.get(ctr).toByteArray())) {
						return false;
					}
				}
			}
		}

		if (htmlMessageField == null) {
			if (other.htmlMessageField != null) {
				return false;
			}

		} else if (!htmlMessageField.getValue().trim().equals(other.htmlMessageField.getValue().trim())) {
			return false;
		}
		if (subjectField == null) {
			if (other.subjectField != null)
				return false;

		} else if (!subjectField.getValue().trim().equals(other.subjectField.getValue().trim()))
			return false;
		if (textMessageField == null) {
			if (other.textMessageField != null)
				return false;
		} else if (!textMessageField.getValue().trim().equals(other.textMessageField.getValue().trim()))
			return false;
		if (toField == null) {
			if (other.toField != null)
				return false;
		} else if (other.toField != null) {
			if (other.toField.size() != this.toField.size()) {
				return false;
			}
			for (int index = 0, len = other.toField.size(); index < len; index++) {
				if (!(toField.get(index).getValue().trim().equals(other.toField.get(index).getValue().trim()))) {
					return false;
				}
			}

		}

		return true;
	}

	public boolean equals(Object obj, Logger log) {
		log.debug("In equal");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MailBean other = (MailBean) obj;

		if (attachAttachments == null) {
			if (other.attachAttachments != null)
				return false;
		} else {

			if (attachAttachments.size() != other.attachAttachments.size()) {
				log.debug("Attachment Size are not the same: Equal return false" + attachAttachments.size() + " : "
						+ other.attachAttachments.size());
				return false;
			} else {
				for (int ctr = 0; ctr < attachAttachments.size(); ctr++) {
					if (!Arrays.equals(attachAttachments.get(ctr).toByteArray(),
							other.attachAttachments.get(ctr).toByteArray())) {
						log.debug("Attachment byte array are not the same: Equal return false");
						return false;
					}
				}
			}
		}
		log.debug("Attachment are equals");
		ArrayList<String> thisCCField = new ArrayList<>();
		ArrayList<String> otherCCField = new ArrayList<>();
		if (ccField == null) {
			if (other.ccField != null){
				log.debug("ccField: one null, the other on not");
				return false;
			}	
		} else {
			if (other.ccField != null) {
				String trimStr;
				StringProperty temp = new SimpleStringProperty();
				if (ccField.size() < 0)
					for (int index = 0, len = ccField.size(); index < len; index++) {
						trimStr = ccField.get(index).getValue().trim();
						temp.set(trimStr);
						ccField.set(index, temp);
						log.debug(ccField.get(index).getValue());
						thisCCField.add(ccField.get(index).getValue());
					}
				if (other.ccField.size() < 0)
					for (int index = 0, len = other.ccField.size(); index < len; index++) {
						trimStr = other.ccField.get(index).getValue().trim();
						temp.set(trimStr);
						other.ccField.set(index, temp);
						log.debug(other.ccField.get(index).getValue());
						otherCCField.add(other.ccField.get(index).getValue());
					}
			}
			for(StringProperty f: this.ccField){
				log.debug(f.getValue());
				log.debug(f.getValue().length()+"");
			}
			for(StringProperty f: other.ccField){
				log.debug(f.getValue());
				log.debug(f.getValue().length()+"");
			}
			if (!otherCCField.equals(ccField)){
				log.debug("ccFIeld: last equals");
				return false;
				}
		}
		log.debug("ccField are equals");
		if (embedAttachments == null) {
			if (other.embedAttachments != null)
				return false;
		} else {

			if (embedAttachments.size() != other.embedAttachments.size()) {
				log.debug("Embed attachment Size are not the same: Equal return false");
				return false;
			} else {
				for (int ctr = 0; ctr < embedAttachments.size(); ctr++) {
					if (!Arrays.equals(embedAttachments.get(ctr).toByteArray(),
							other.embedAttachments.get(ctr).toByteArray())) {
						log.debug("Embed attachment byte array are not the same: Equal return false");
						return false;
					}
				}
			}
		}
		log.debug("embed are equals");

		if (htmlMessageField == null) {
			if (other.htmlMessageField != null) {
				log.debug("html receive has not been initialized");
				return false;
			}

		} else if (!htmlMessageField.getValue().trim().equals(other.htmlMessageField.getValue().trim())) {
			log.debug("html field are not equal");
			log.debug("HTML ONE :       " + htmlMessageField + "      HTML TWO :     " + other.htmlMessageField);
			return false;
		}
		log.debug("html are equals");
		if (subjectField == null) {
			if (other.subjectField != null)
				return false;

		} else if (!subjectField.getValue().trim().equals(other.subjectField.getValue().trim()))
			return false;
		if (textMessageField == null) {
			if (other.textMessageField != null)
				return false;
		} else if (!textMessageField.getValue().trim().equals(other.textMessageField.getValue().trim()))
			return false;
		log.debug("textMsg are equals");
		if (toField == null) {
			if (other.toField != null)
				return false;
		} else if (other.toField != null) {
			if (other.toField.size() != this.toField.size()) {
				return false;
			}
			for (int index = 0, len = other.toField.size(); index < len; index++) {
				if (!(toField.get(index).getValue().trim().equals(other.toField.get(index).getValue().trim()))) {
					log.debug("field :" + toField.get(index).getValue().trim() + "; field2:"
							+ other.toField.get(index).getValue().trim() + ";");
					return false;
				}
			}

		}
		log.debug("toField are equals");
		log.debug("Everything is equal");
		return true;
	}

}
