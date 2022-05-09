package com.birikfr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.birikfr.persistence.MailDAO;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class WebViewController {

	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());
    @FXML
    private WebView WebView;
    @FXML
	private void initialize() {
    	  final String html = "example.html";
	        final java.net.URI uri = java.nio.file.Paths.get(html).toAbsolutePath().toUri();
	        log.info("uri= " + uri.toString());
	 
	        // create WebView with specified local content
	        WebView.getEngine().load(uri.toString());

	}
}
