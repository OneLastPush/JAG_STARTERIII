package com.birikfr.MainApp;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainAppFX extends Application{
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Locale currentLocale;
	public static void main(String[] args) {
		launch(args);
		System.exit(0);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		//set icon
		initRootLayout();
		this.primaryStage.setTitle("Email View");
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	public void initRootLayout() {
		Locale locale = Locale.getDefault();
		log.debug("Locale = " + locale);
	currentLocale = new Locale("en","CA");
		try {
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("bundles/TextBundle", currentLocale));
		
		loader.setLocation(MainAppFX.class
				.getResource("/fxml/Root.fxml"));
		rootLayout = (BorderPane) loader.load();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close(){
		primaryStage.close();
	}


}
