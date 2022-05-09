package com.birikfr.controllers;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.birikfr.mailbean.MailBean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class TableController {
	@FXML
	private ResourceBundle resources;
    @FXML
    private TableColumn< MailBean, String> fromColumn;

    @FXML
    private TableView<MailBean> TableLayout;

    @FXML
    private TableColumn< MailBean,String> subjectColumn;

    @FXML
    private AnchorPane TablePane;
   
    public TableController() {
		super();
	}
    public void initialize() {
    	fromColumn.setCellValueFactory(cellData -> cellData.getValue().getFromFieldProperty());
    	subjectColumn.setCellValueFactory(cellData -> cellData.getValue().getSubjectFieldProperty());
    	adjustColumnWidths();
    	populate();
    }
    public void populate(){
	ArrayList<MailBean> list = new ArrayList<>();
	MailBean m1 = new MailBean();
	MailBean m2 = new MailBean();
	MailBean m3 = new MailBean();
	m1.setSubjectField("Change your haircut Today!");
	m1.setFromField("barbershop@hairless.ca");
	list.add(m1);
	m2.setSubjectField("New Years Deals!");
	m2.setFromField("oldEgg@promotion.eu");
	list.add(m2);
	m3.setFromField("mommy2345_23@cookie.com");
	m3.setSubjectField("You forgot your lunch pack...");
	list.add(m3);
	ObservableList<MailBean> olist = FXCollections.observableArrayList(list);
	TableLayout.setItems(olist);
	}
    private void adjustColumnWidths() {
    	double width = TableLayout.getPrefWidth();
    	fromColumn.setPrefWidth(width * .05);
    	subjectColumn.setPrefWidth(width * .15);
    	TableLayout.getSelectionModel()
		.selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> showEmailDetail(newValue));
    }
	private void showEmailDetail(MailBean newValue) {
		System.out.println("Eamil: "+newValue.getId()+"\n" + newValue);
	}
}
