package com.birikfr.persistence;

import java.sql.Connection;
import javafx.beans.property.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.birikfr.mailbean.FolderBean;
import com.birikfr.mailbean.MailBean;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.log.Log;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.mail.EmailAttachment;
import jodd.mail.att.InputStreamAttachment;
@SuppressWarnings("restriction")
public class MailDAOImpl implements MailDAO {
	private final String url = "jdbc:mysql://localhost:3306/emailDB";
	private final String user = "root";
	private final String password = "";

	@Override
	public int create(MailBean mailBean) throws SQLException {
		int mailId;
		String createQuery = "INSERT INTO MAIL "
				+ "(fromField,subject,textMessage,htmlMessage, dateSent, dateReceived, folderID, mailStatus) "
				+ "VALUES (?,?,?,?,?,?,?,?)";

		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, mailBean.getFromField());
			mailBean.setId(this.getFolderId(mailBean.getFolder().getName()));
			ps.setInt(7, create(mailBean.getFolder()));
			ps.setString(2, mailBean.getSubjectField());
			ps.setString(3, mailBean.getTextMessageField());
			ps.setString(4, mailBean.getHtmlMessageField());
			if (mailBean.getSendTime() == null) {
				ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
			} else {
				ps.setTimestamp(5, Timestamp.valueOf(mailBean.getSendTime()));
			}
			if (mailBean.getReceiveTime() == null) {
				ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
			} else {
				ps.setTimestamp(6, Timestamp.valueOf(mailBean.getReceiveTime()));
			}
			ps.setInt(8, mailBean.getMailStatus());
			ps.executeUpdate();
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next())
					mailId = generatedKeys.getInt(1);
				else
					throw new SQLException("ID NOT FOUND:MAIL Generated Keys");
			}
			createAddress(mailId, conn, mailBean.getToField(), "TO");
			createAddress(mailId, conn, mailBean.getBccField(), "BCC");
			createAddress(mailId, conn, mailBean.getCcField(), "CC");
			createAttachment(mailId, conn, mailBean);
			mailBean.setId(mailId);
			return mailId;
		}

	}

	private void createAttachment(int mailId, Connection conn, MailBean mb) throws SQLException {

		String createQuery = "INSERT INTO ATTACHMENT " + "(emailID,contentID, name, size, content, typeField) "
				+ "VALUES (?,?,?,?,?,?)";

		PreparedStatement ps = conn.prepareStatement(createQuery);
		String type = "ATT";

		for (EmailAttachment attachment : mb.getAttachAttachments()) {
			ps.setInt(1, mailId);
			ps.setString(2, "");
			ps.setString(3, attachment.getName());
			ps.setInt(4, attachment.getSize());
			ps.setBytes(5, attachment.toByteArray());
			ps.setString(6, type);
			ps.executeUpdate();
		}
		type = "EMB";
		for (EmailAttachment attachment : mb.getEmbedAttachments()) {
			ps.setInt(1, mailId);
			ps.setString(2, attachment.getContentId());
			ps.setString(3, attachment.getName());
			ps.setInt(4, attachment.getSize());
			ps.setBytes(5, attachment.toByteArray());
			ps.setString(6, type);

			ps.executeUpdate();
		}

	}

	private void createAddress(int mailId, Connection conn,  ArrayList<StringProperty> addresses, String type)
			throws SQLException {
		String createQuery = "INSERT INTO ADDRESS(emailID,email,typeField) VALUES (?,?,?)";
		PreparedStatement ps = conn.prepareStatement(createQuery);
		ps.setInt(1, mailId);
		ps.setString(3, type);
		for (StringProperty email : addresses) {
			ps.setString(2, email.getValue());
			ps.executeUpdate();
		}
	}

	@Override
	public int create(FolderBean folder) throws SQLException {
	
		if (folder.getId() == -1) {
			try (Connection conn = DriverManager.getConnection(url, user, password)) {
				String createFolder = "INSERT INTO FOLDER(name) VALUES (?)";
				PreparedStatement ps = conn.prepareStatement(createFolder, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, folder.getName());
				ps.executeUpdate();
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next())
						folder.setId(generatedKeys.getInt(1));
					else
						throw new SQLException("ID NOT FOUND:Folder Generated Keys");
				}
			}

		} 
		return folder.getId();
	}

	private int getFolderId(String folderName) throws SQLException {
		String selectQuery = "SELECT ID FROM FOLDER WHERE name = ?";

		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, folderName);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt("ID");
				else
					return -1;
			}
		}
	}

	private MailBean createMailBean(ResultSet rs) throws SQLException {
		MailBean mb = new MailBean();
		// ArrayList<EmailAttachment> rows = new ArrayList<>();
		mb.setFromField(rs.getString("fromField"));
		mb.setHtmlMessageField(rs.getString("htmlMessage"));
		mb.setTextMessageField(rs.getString("textMessage"));
		int mailId = rs.getInt("ID");
		mb.setId(mailId);
		mb.setSubjectField(rs.getString("subject"));
		mb.setSendTime(rs.getTimestamp("dateSent").toLocalDateTime());
		mb.setReceiveTime(rs.getTimestamp("dateReceived").toLocalDateTime());
		mb.setMailStatus(rs.getInt("mailStatus"));
		String selectQuery = "SELECT * FROM ATTACHMENT WHERE emailID = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setInt(1, mailId);
			try (ResultSet resultAtt = ps.executeQuery()) {
				while (resultAtt.next()) {
					EmailAttachment ea = new InputStreamAttachment(resultAtt.getBlob("content").getBinaryStream(),
							resultAtt.getString("typeField"), resultAtt.getString("name"),
							resultAtt.getString("contentID"));
					if (ea.getContentId() == "") {
						mb.getAttachAttachments().add(ea);
					} else {
						mb.getEmbedAttachments().add(ea);
					}

				}
			}
			mb.getFolder().setName(rs.getString("name"));
			mb.getFolder().setId(rs.getInt("folderId"));
			selectQuery = "SELECT * FROM ADDRESS WHERE emailID = ?";
			ps = conn.prepareStatement(selectQuery);
			ps.setInt(1, mailId);
			try (ResultSet resultEmail = ps.executeQuery()) {
				String email;
				while (resultEmail.next()) {
					email = resultEmail.getString("email");
					if (resultEmail.getString("typeField").equalsIgnoreCase("TO")) {
						mb.getToField().add(new SimpleStringProperty(email));
					} else if (resultEmail.getString("typeField").equalsIgnoreCase("CC")) {
						mb.getCcField().add(new SimpleStringProperty(email));
					} else {
						mb.getBccField().add(new SimpleStringProperty(email));
					}

				}
			}
			

		}

		return mb;
	}

	@SuppressWarnings("unused")
	private String getFolderName(Connection conn, int mailId) throws SQLException {

		String selectQuery = "SELECT FOLDER.name"
				+ "FROM FOLDER "
				+ "INNER JOIN MAIL "
				+ "ON MAIL.FOLDERID = FOLDER.ID "
				+ "WHERE MAIL.ID = ?";
		PreparedStatement ps = conn.prepareStatement(selectQuery);
		ps.setInt(1, mailId);
		try (ResultSet resultFolder = ps.executeQuery()) {
			return resultFolder.getString("name");
		}
	}

	@Override
	public ArrayList<MailBean> findMailByToField(String email) throws SQLException {

		return findAddress("TO", email);
	}

	/*@Override
	public ArrayList<MailBean> findAllMail(String email) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();
		String selectQuery = "SELECT * FROM MAIL INNER JOIN ADDRESS ON ADDRESS.EMAILID = MAIL.ID "
				+ "WHERE ADDRESS.EMAIL = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rows.add(createMailBean(rs));
				}
			}
		}
		return rows;
	}
*/
	@Override
	public ArrayList<MailBean> findMailBySubject(String subject) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();
		String selectQuery = "SELECT * FROM MAIL " + "WHERE subject = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, subject);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rows.add(createMailBean(rs));
				}
			}
		}
		return rows;
	}

	@Override
	public ArrayList<MailBean> findMailByFromField(String email) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();
		String selectQuery = "SELECT * FROM MAIL " + "WHERE fromField = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rows.add(createMailBean(rs));
				}
			}
		}
		return rows;
	}

	private ArrayList<MailBean> findAddress(String type, String email) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();
		ArrayList<Integer> emailsId = new ArrayList<>();
		String selectQuery = "SELECT emailId"
				+ " FROM ADDRESS " 
				+ " WHERE typeField = ? "
				+ " AND email = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, type);
			ps.setString(2, email);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					emailsId.add(rs.getInt("emailId"));
					
				}
			}
			}
			if(emailsId.size() > 0){
				selectQuery= "Select MAIL.ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, mailStatus,folderId"
						+ " FROM MAIL"
						+ " INNER JOIN FOLDER"
						+ " ON MAIL.FOLDERID = FOLDER.ID"
						+ " WHERE MAIL.ID = ?";
			try (Connection conn = DriverManager.getConnection(url, user, password)) {
				PreparedStatement ps = conn.prepareStatement(selectQuery);
			
				ps = conn.prepareStatement(selectQuery);
				for(int mailId: emailsId){
					ps.setInt(1,mailId);
					ResultSet rs = ps.executeQuery();
					while(rs.next()){
						rows.add(createMailBean(rs));
					}
				}
			}
			return rows;
			}
			return new ArrayList<>();

		
	}
	@Override
	public ArrayList<MailBean> findMailByFolder(String folderName) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();
		String selectQuery = "SELECT * FROM MAIL"
				+ "INNER JOIN FOLDER ON MAIL.folderId = FOLDER.Id "
		+"WHERE folderName = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, folderName);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rows.add(createMailBean(rs));
				}
			}
		}
		return rows;
		
	}

	@Override
	public ArrayList<MailBean> findMailByCCField(String email) throws SQLException {

		return findAddress("CC", email);

	}

	@Override
	public ArrayList<MailBean> findMailByBCCField(String email) throws SQLException {
		return findAddress("BCC", email);
	}

	@Override
	public ArrayList<MailBean> findMailByDateSent(LocalDateTime date) throws SQLException {
		return findMailByDate(date, true);
	}

	private ArrayList<MailBean> findMailByDate(LocalDateTime date, boolean sendTime) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();
		String selectQuery;
		if (sendTime) {
			selectQuery = "SELECT * FROM MAIL" + "WHERE dateSent = ?";
		} else {
			selectQuery = "SELECT * FROM MAIL" + "WHERE dateReceive = ?";
		}
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, date.toString());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rows.add(createMailBean(rs));
				}
			}
		}
		return rows;

	}

	@Override
	public ArrayList<MailBean> findMailByDateReceive(LocalDateTime date) throws SQLException {
		return findMailByDate(date, false);

	}

	@Override
	public int update(String folderOldName, String folderNewName) throws SQLException {

		String updateQuery = "UPDATE FOLDER SET name = ? where name = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(updateQuery);
			ps.setString(1, folderOldName);
			ps.setString(2, folderNewName);

			return ps.executeUpdate();
		}

	}

	@Override
	public int update(MailBean mailBean, String folderName) throws SQLException {
		int folderId = getFolderId(folderName);

		String updateQuery = "UPDATE MAIL SET folderID = ? where ID = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(updateQuery);
			ps.setInt(1, folderId);
			ps.setInt(2, mailBean.getId());

			return ps.executeUpdate();
		}
	}

	@Override
	public int deleteFolder(String folderName) throws SQLException {
		int result;
		int id = getFolderId(folderName);
		String deleteQuery = "DELETE FROM MAIL WHERE folderID = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(deleteQuery);
			ps.setInt(1, id);
			result = ps.executeUpdate();
			deleteQuery = "DELETE FROM FOLDER WHERE ID = ?";
			ps = conn.prepareStatement(deleteQuery);
			ps.setInt(1, id);
			ps.executeUpdate();
		}
		return result;
	}

	@Override
	public int deleteMail(int mailId) throws SQLException {
		int result = 0;
		String deleteQuery = "DELETE FROM MAIL WHERE ID = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement ps = conn.prepareStatement(deleteQuery);
			ps.setInt(1, mailId);
			result = ps.executeUpdate();
		}
		return result;
	}

	@Override
	public ObservableList<FolderBean> findAllFolder() throws SQLException {
		ObservableList<FolderBean> folderName = FXCollections.observableArrayList();
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
		String selectQuery = "SELECT id, name"
				+ " FROM FOLDER"; 
		PreparedStatement ps = conn.prepareStatement(selectQuery);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			folderName.add(new FolderBean(rs.getInt(1),rs.getString(2)));
		}
		return folderName;
	}

	}

}
