package com.birikfr.MainApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.birikfr.MainApp.MainAppWebView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainAppWebView extends Application {
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private Stage primaryStage;
	private AnchorPane WebViewLayout;
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Email View");

		// Set the application icon using getResourceAsStream.


		initRootLayout();
        primaryStage.show();
	}
	public void initRootLayout() {
	    try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainAppWebView.class
	        		.getResource("/fxml/WebViewLayout.fxml"));
	        WebViewLayout = (AnchorPane) loader.load();
	        // Show the scene containing the root layout.
	        Scene scene = new Scene(WebViewLayout);
	        primaryStage.setScene(scene);

            // If you need to send messages to the controller you can retrieve the reference to it.
	        //FishFXWebViewController controller = loader.getController();

	    } catch (Exception e) {
	        log.error("Error displaying html editor", e);
	    }
	}
	public static void main(String[] args) {
		launch(args);
	}

}
