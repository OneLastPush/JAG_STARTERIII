package com.birikfr.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import com.birikfr.MainApp.MainAppFX;
import com.birikfr.MainApp.MainAppTree;
import com.birikfr.persistence.MailDAOImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RootController {

	 @FXML
	    private ToolBar toolbar;

	    @FXML
	    private MenuBar menubar;

	    @FXML
	    private AnchorPane webview;

	    @FXML
	    private AnchorPane treeAnchorPane;

	    @FXML
	    private AnchorPane tableView;

	    @FXML
	    private Button createBtn;
    @FXML 
    private ResourceBundle resources;
    private TreeController treeController;
	private TableController TableController;
	
	
	//private FishFXWebViewController fishFXWebViewController;
	private EmailFxHTMLController HTMLController;
    
   @FXML
	private void initialize() {
    initWebView();
    initTableView();
    initTreeView();
    setTableController();
    
    }
    private void setTableController() {
		// TODO Auto-generated method stub
		
	}
   
	private void initTableView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class
					.getResource("/fxml/TableLayout.fxml"));
			AnchorPane tableView = (AnchorPane) loader.load();

			// Give the controller the data object.
			TableController = loader.getController();
			TableController.populate();

			this.tableView.getChildren().add(tableView);
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}
	public void setResources(ResourceBundle resources){
		this.resources = resources;
	}
	private void initWebView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class
					.getResource("/fxml/WebViewLayout.fxml"));
			AnchorPane webview = (AnchorPane) loader.load();
			
			// Retrieve the controller if you must send it messages
			//WebViewController = loader.getController();

			this.webview.getChildren().add(webview);
			webview.prefWidthProperty().bind(this.webview.widthProperty());
			webview.prefHeightProperty().bind(this.webview.heightProperty());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void initTreeView(){
		try {

			  FXMLLoader loader = new FXMLLoader();
			  loader.setResources(resources);
		        loader.setLocation(MainAppTree.class
		                .getResource("/fxml/TreeLayout.fxml"));
		        
		        AnchorPane tree = (AnchorPane) loader.load();
		        this.treeAnchorPane.getChildren().add(tree);
		        // Show the scene containing the root layout.
		        

		        
			// Give the controller the data object.
			treeController = loader.getController();
			treeController.setMailDao(new MailDAOImpl());
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 @FXML
	    void openHtmlEditor(ActionEvent event) {
		 try {
	        	FXMLLoader loader = new FXMLLoader();
	        	loader.setResources(resources);
	        	loader.setLocation(MainAppFX.class
	        		.getResource("/fxml/EmailHtmlLayout.fxml"));
	        
	    		AnchorPane html = (AnchorPane) loader.load();
	    		Stage stage = new Stage();
	    		stage.setTitle(resources.getString("HtmlViewTitle"));
	    		stage.setScene(new Scene(html));
	    		stage.show();
	    	} catch (IOException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}
	    }
	
	@FXML
    void aboutAction(ActionEvent event) {
    	
    }

    @FXML
    void closeWindow(ActionEvent event) {
    	System.exit(0);
    }    

    @FXML
    void openSetting(ActionEvent event) {
    	try {
        	FXMLLoader loader = new FXMLLoader();
        	loader.setResources(resources);
        	loader.setLocation(MainAppFX.class
        		.getResource("/fxml/config.fxml"));
        
        	BorderPane setting = (BorderPane) loader.load();
    		Stage stage = new Stage();
    		stage.setTitle(resources.getString("Setting"));
    		stage.setScene(new Scene(setting));
    		stage.show();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		//e.printStackTrace();
    	}
    }
    


    @FXML
    void createMail(ActionEvent event) {
    	openHtmlEditor(event);
    
    }
    

}
