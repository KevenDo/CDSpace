package com.u51.CDSpace.view;

import com.u51.CDSpace.model.Header;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class HttpServerContextTabView extends Tab{
	private ObservableList<Header> data = FXCollections.observableArrayList();
	private TextField tfResponseCode;
	private TextField tfUrl;
	private TableView<Header> tvHeaders;
	private TextArea taOfResponseBody;
	
	public HttpServerContextTabView() {
		this.setClosable(true);
		this.setContent(initPaneContex());
	}
	
	@SuppressWarnings("unchecked")
	private Pane initPaneContex() {
		VBox vbox = new VBox(10);
		
		GridPane gpUrlAndResponseCode = new GridPane();
		gpUrlAndResponseCode.setPadding(new Insets(10, 10, 0, 10));
		gpUrlAndResponseCode.setHgap(10);
		tfUrl = new TextField();
		tfUrl.setPromptText("Url");
		tfUrl.setPrefWidth(405);
		gpUrlAndResponseCode.add(tfUrl, 0, 0);
        tfResponseCode = new TextField();
        tfResponseCode.setPromptText("Response Code");
        tfResponseCode.setMinWidth(100);
        gpUrlAndResponseCode.add(tfResponseCode, 1, 0);
		
		HBox headersTableBox = new HBox();
        headersTableBox.setPadding(new Insets(0, 10, 0, 10));
        tvHeaders = new TableView<Header>();
        tvHeaders.setEditable(true);
        tvHeaders.setPrefWidth(580);
        tvHeaders.setPrefHeight(200);
        headersTableBox.getChildren().add(tvHeaders);
        
        TableColumn<Header, String> headerNameCol = new TableColumn<Header, String>("Header Name");
        headerNameCol.setPrefWidth(290);
        headerNameCol.setCellValueFactory(new PropertyValueFactory<>("name")); 
        headerNameCol.setCellFactory(TextFieldTableCell.<Header>forTableColumn());
        headerNameCol.setOnEditCommit(
            (CellEditEvent<Header, String> t) -> {
                ((Header) t.getTableView().getItems().get(
                         t.getTablePosition().getRow())
                         ).setName(t.getNewValue());
        });
        
        TableColumn<Header, String> headerValueCol = new TableColumn<Header, String>("Header Value");
        headerValueCol.setPrefWidth(290);
        headerValueCol.setCellValueFactory(new PropertyValueFactory<>("value")); 
        headerValueCol.setCellFactory(TextFieldTableCell.<Header>forTableColumn());
        headerValueCol.setOnEditCommit(
            (CellEditEvent<Header, String> t) -> {
                ((Header) t.getTableView().getItems().get(
                         t.getTablePosition().getRow())
                         ).setValue(t.getNewValue());
        });
		
        tvHeaders.setItems(data);
        tvHeaders.getColumns().addAll(headerNameCol, headerValueCol);
        
        //Http Add Header Pane
        GridPane addHeadergrid = new GridPane();
        addHeadergrid.setPadding(new Insets(0, 10, 0, 10));
        addHeadergrid.setHgap(10);
        final TextField addHeaderName = new TextField();
        addHeaderName.setPromptText("Header Name");
        addHeaderName.setPrefWidth(175);
        addHeadergrid.add(addHeaderName, 0, 0);
        final TextField addHeaderValue = new TextField();
        addHeaderValue.setPromptText("Header Value");
        addHeaderValue.setPrefWidth(175);
        addHeadergrid.add(addHeaderValue, 1, 0);
        final Button addButton = new Button("add");
        addButton.setMinWidth(100);
        addButton.setOnAction((ActionEvent e) -> {
            data.add(new Header(
            		addHeaderName.getText(),
                    addHeaderValue.getText()));
            addHeaderName.clear();
            addHeaderValue.clear();
        });
        addHeadergrid.add(addButton, 2, 0);
        final Button deleteButton = new Button("delete");
        deleteButton.setMinWidth(100);
        deleteButton.setOnAction((ActionEvent e) -> {
            data.remove(tvHeaders.getFocusModel().getFocusedIndex());
        });
        addHeadergrid.add(deleteButton, 3, 0);
        
        HBox consoleHb = new HBox();
        consoleHb.setPadding(new Insets(0, 10, 10, 10));
        taOfResponseBody = new TextArea();
        taOfResponseBody.setWrapText(true);
        taOfResponseBody.setPrefWidth(580);
        taOfResponseBody.setPrefHeight(280);
        consoleHb.getChildren().add(taOfResponseBody);
        
        vbox.getChildren().addAll(gpUrlAndResponseCode, headersTableBox, addHeadergrid, consoleHb);
        return vbox;
	}

	public ObservableList<Header> getData() {
		return data;
	}

	public void setData(ObservableList<Header> data) {
		this.data = data;
	}

	public TextField getTfResponseCode() {
		return tfResponseCode;
	}

	public void setTfResponseCode(TextField tfResponseCode) {
		this.tfResponseCode = tfResponseCode;
	}

	public TextField getTfUrl() {
		return tfUrl;
	}

	public void setTfUrl(TextField tfUrl) {
		this.tfUrl = tfUrl;
	}

	public TableView<Header> getTvHeaders() {
		return tvHeaders;
	}

	public void setTvHeaders(TableView<Header> tvHeaders) {
		this.tvHeaders = tvHeaders;
	}

	public TextArea getTaOfResponseBody() {
		return taOfResponseBody;
	}

	public void setTaOfResponseBody(TextArea taOfResponseBody) {
		this.taOfResponseBody = taOfResponseBody;
	}
}
