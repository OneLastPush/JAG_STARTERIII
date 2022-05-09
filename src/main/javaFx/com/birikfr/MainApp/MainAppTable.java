package com.birikfr.MainApp;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainAppTable extends Application {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private Stage primaryStage;
	private AnchorPane emailFxTableLayout;
	private Locale currentLocale;
	public MainAppTable() {
		super();
	}
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Email Table");
		initRootLayout();
        primaryStage.show();
	
	}
	
	public void initRootLayout() {
		Locale locale = Locale.getDefault();
		log.debug("Locale = " + locale);
	currentLocale = new Locale("en","CA");
//			currentLocale = new Locale("fr","CA");
	    try {
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainAppTable.class
	                .getResource("/fxml/TableLayout.fxml"));
	        loader.setResources(ResourceBundle.getBundle("bundles/TextBundle", currentLocale));
	        emailFxTableLayout = (AnchorPane) loader.load();
	        Scene scene = new Scene(emailFxTableLayout);
	        primaryStage.setScene(scene);
	        
	    } catch (Exception e) {
	        log.error("Error display table", e);
	    }
	    
	    
	}

}
