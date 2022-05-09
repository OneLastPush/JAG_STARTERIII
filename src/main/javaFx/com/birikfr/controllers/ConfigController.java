package com.birikfr.controllers;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import com.birikfr.properties.MailConfigBean;
import com.birikfr.properties.manager.PropertiesManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ConfigController {

    @FXML
    private TextField lNameField;

    @FXML
    private TextField fNameField;

    @FXML
    private Button SaveButn;

    @FXML
    private Button exitBtn;

    @FXML
    private TextField hostField;

    @FXML
    private RadioButton frenchRadioBtn;

    @FXML
    private TextField emailAddressField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField imapField;

    @FXML
    private TextField smtpField;

    @FXML
    private RadioButton EnglishRadioBtn;

    RootController root;
    @FXML
	private ResourceBundle resources;
    private MailConfigBean config;
    private PropertiesManager pm;
    private final String path= ".\\configInfo\\assets";;
    private final String fileName= "configFile";
    
    @FXML
    private void initialize(){
    	loadProperty();
    	setFieldInfo();
    	
    	
    }

	private void loadProperty() {
		try {
			config = pm.loadTextProperties(path, fileName);
			
		} catch (IOException e) {
			config = new MailConfigBean("", fileName, fileName, fileName, fileName, fileName, fileName, null);
			
		}
		
	}

	private void setFieldInfo() {
		lNameField.setText(config.getLastName());
		fNameField.setText(config.getFirstName());
		hostField.setText(config.getHost());
		emailAddressField.setText(config.getUserEmailAddress());
		passwordField.setText(config.getPassword());
		imapField.setText(config.getImap());
		smtpField.setText(config.getSmtp());
		if(config.getLocale().equals(new Locale("fr","CA"))){
			frenchRadioBtn.selectedProperty();
			resources = ResourceBundle.getBundle("bundles/TextBundle", config.getLocale());
			
		}else{
			resources = ResourceBundle.getBundle("bundles/TextBundle",new Locale("en","CA"));
			EnglishRadioBtn.selectedProperty();
		}
		root.setResources(resources);
		
	}
}