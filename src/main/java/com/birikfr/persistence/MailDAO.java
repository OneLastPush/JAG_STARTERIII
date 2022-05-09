package com.birikfr.persistence;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.birikfr.mailbean.FolderBean;
import com.birikfr.mailbean.MailBean;

import javafx.collections.ObservableList;

public interface MailDAO {
//This is the two create method needed for the application
public int create(MailBean mailBean) throws SQLException;
public int create(FolderBean folderName) throws SQLException;
//This is 7 read method, each of them are to be use to retrieve data from
//the database. All the search variable has to be identical to retrieve
//any data. Contains will not work
public ArrayList<MailBean> findMailByToField(String email) throws SQLException;
public ArrayList<MailBean> findMailBySubject(String subject) throws SQLException;
public ArrayList<MailBean> findMailByFromField(String email)throws SQLException;
public ArrayList<MailBean> findMailByCCField(String email)throws SQLException;
public ArrayList<MailBean> findMailByBCCField(String email)throws SQLException;
public ArrayList<MailBean> findMailByDateSent(LocalDateTime date)throws SQLException;
public ArrayList<MailBean> findMailByDateReceive(LocalDateTime date)throws SQLException;
public ArrayList<MailBean> findMailByFolder(String folderName) throws SQLException;
public ObservableList<FolderBean> findAllFolder() throws SQLException;
//public ArrayList<MailBean> findAllMail(String email) throws SQLException;
//This 2 classes update the database table, the first one modified a
//folder's name. The other one move a mail to another folder
public int update(String folderOldName, String folderNewName) throws SQLException;
public int update(MailBean mailBean, String folderName) throws SQLException;

//Delete
public int deleteFolder(String folderName) throws SQLException;
public int deleteMail(int mailId) throws SQLException;
}
