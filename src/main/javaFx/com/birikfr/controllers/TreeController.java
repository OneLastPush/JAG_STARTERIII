package com.birikfr.controllers;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.birikfr.mailbean.FolderBean;
import com.birikfr.mailbean.MailBean;
import com.birikfr.persistence.MailDAO;
import com.birikfr.persistence.MailDAOImpl;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * This controller began its life as part of a standalone display of a container
 * with a menu, tool bar and HTML editor. It is now part of another container.
 * 
 * A method was added to allow the RootLayoutController to pass in a reference
 * to the FishFXTableController
 * 
 * i18n added
 * 
 * @author Ken Fogel
 * @version 1.1
 *
 */
public class TreeController {
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());
	private MailDAO dao;
	private TableController tableController;
	private TreeItem<FolderBean> root;
	@FXML
	private TreeView<FolderBean> treeView;
    @FXML
    private AnchorPane treeLayout;
	private Locale currentLocale;
	// Resource bundle is injected when controller is loaded
	@FXML
	private ResourceBundle resources;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
		Locale locale = Locale.getDefault();
		log.debug("Locale = " + locale);
		currentLocale = new Locale("en","CA");
		// We need a root node for the tree and it must be the same type as all
		// nodes
		FolderBean folder = new FolderBean();
		// The tree will display common name so we set this for the root
		// Because we are using i18n the root name comes from the resource
		// bundle
		
		resources = ResourceBundle.getBundle("bundles/TextBundle", currentLocale);
		
		folder.setName(resources.getString("Folder"));
		
		root = new TreeItem<>(folder);
		
		
		root.setExpanded(true);
		treeView.setRoot(root);
		
		
		// This cell factory is used to choose which field in the FihDta object
		// is used for the node name
		treeView.setCellFactory((e) -> new TreeCell<FolderBean>() {
			@Override
			protected void updateItem(FolderBean item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getName());
					setGraphic(getTreeItem().getGraphic());
				} else {
					setText("");
					setGraphic(null);
				}
			}
		});
		
	}

	/**
	 * The RootLayoutController calls this method to provide a reference to the
	 * FishDAO object.
	 * 
	 * @param fishDAO
	 * @throws SQLException
	 */
	public void setMailDao(MailDAO dao) {
		this.dao = dao;
	}

	/**
	 * The RootLayoutController calls this method to provide a reference to the
	 * FishFXTableController from which it can request a reference to the
	 * TreeView. With theTreeView reference it can change the selection in the
	 * TableView.
	 * 
	 * @param fishFXTableController
	 */
	public void setTableController(TableController tableController) {
		this.tableController = tableController;
	}

	/**
	 * Build the tree from the database
	 * 
	 * @throws SQLException
	 */
	public void displayTree() throws SQLException {
		
		// Retrieve the list of fish
		ObservableList<FolderBean> folders = dao.findAllFolder();

		// Build an item for each fish and add it to the root
		if (folders != null) {
			for (FolderBean fb : folders) {
				TreeItem<FolderBean> item = new TreeItem<>(fb);
				//item.setGraphic(new ImageView(getClass().getResource(
				//		"/images/fish.png").toExternalForm()));
				treeView.getRoot().getChildren().add(item);
			}
		}

		// Open the tree
		treeView.getRoot().setExpanded(true);

		// Listen for selection changes and show the fishData details when
		// changed.
		/*treeView
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showFishDetailsTree(newValue));
	*/}

	/**
	 * Using the reference to the FishFXTableController it can change the
	 * selected row in the TableView It also displays the FishData object that
	 * corresponds to the selected node.
	 * 
	 * @param fishData
	 */
	/*private void showFishDetailsTree(TreeItem<FolderBean> folder) {

		// Select the row that contains the FishData object from the Tree
		tableController.getfishDataTable().getSelectionModel()
				.select(fishData.getValue());
		// Get the row number
		int x = fishFXTableController.getfishDataTable().getSelectionModel()
				.getSelectedIndex();
		// Scroll the table so that the row is at the top of the displayed table
		fishFXTableController.getfishDataTable().scrollTo(x);

		System.out.println("showFishDetailsTree\n" + fishData.getValue());
	}*/
}
