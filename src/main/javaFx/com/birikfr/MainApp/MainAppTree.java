package com.birikfr.MainApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.birikfr.controllers.TreeController;
import com.birikfr.persistence.MailDAO;
import com.birikfr.persistence.MailDAOImpl;


public class MainAppTree extends Application {

	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private Stage primaryStage;
	private AnchorPane treeLayout;
	private MailDAO dao;
	
	public MainAppTree() {
		super();
		dao = new MailDAOImpl();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Folder Tree");

		// Set the application icon using getResourceAsStream.
		/*this.primaryStage.getIcons().add(
				new Image(MainAppTree.class
						.getResourceAsStream("/images/bluefish_icon.png")));
		 */
		initRootLayout();
        primaryStage.show();
	}

	/**
	 * Load the layout and controller. 
	 */
	public void initRootLayout() {
	    try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainAppTree.class
	                .getResource("/fxml/TreeLayout.fxml"));
	        
	        treeLayout = (AnchorPane) loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(treeLayout);
	        primaryStage.setScene(scene);

	        TreeController controller = loader.getController();
	        controller.setMailDao(dao);
	        controller.displayTree();
	    } catch (Exception e) {
	        log.error("Error display table", e);
	    }
	}
	
	/**
	 * Where it all begins
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
		System.exit(0);
	}
}