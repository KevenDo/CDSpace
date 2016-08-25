package com.u51.CDSpace.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import com.u51.CDSpace.model.Header;
import com.u51.CDSpace.model.NodeItem;
import com.u51.CDSpace.utils.httpClient.HttpClientResponse;
import com.u51.CDSpace.utils.httpClient.HttpClientUtil;
import com.u51.CDSpace.model.HttpClientRequest;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class HttpClientEditorView {
	private ObservableList<Header> data = FXCollections.observableArrayList();
	
	private TextArea taOfURL;
	
	private ComboBox<String> methodComboBox;
	
	private TextArea taOfRequestBody;
	
	private TableView<Header> table;
	
	private TextArea taOfResponseBody;
	
	private TextArea taOfResponseHeader;
	
	private TreeItem<NodeItem> treeItem;
	
	public HttpClientEditorView() {
		
	}
	
	public VBox initCenterBorder() {
		VBox vbox = new VBox(10);
		vbox.setPrefHeight(800);
		vbox.setPrefWidth(1000);
        
        TabPane tpOfRequest = new TabPane();
        Tab tabOfURL = new Tab();
        tabOfURL.setClosable(false);
        tabOfURL.setText("URL");
        tabOfURL.setContent(initURLPane());
        Tab tabOfRequestHeader = new Tab();
        tabOfRequestHeader.setClosable(false);
        tabOfRequestHeader.setText("Request Header");
        tabOfRequestHeader.setContent(initHeaderPane());
        Tab tabOfRequestBody = new Tab();
        tabOfRequestBody.setClosable(false);
        tabOfRequestBody.setText("Request Body");
        tabOfRequestBody.setContent(initBodyPane());
        tpOfRequest.getTabs().addAll(tabOfURL, tabOfRequestBody, tabOfRequestHeader);
        
        TabPane tpOfResponse = new TabPane();
        Tab tabOfResponseBody = new Tab();
        tabOfResponseBody.setClosable(false);
        tabOfResponseBody.setText("Response Body");
        tabOfResponseBody.setContent(initResponseBodyPane());
        Tab tabOfResponseHeader = new Tab();
        tabOfResponseHeader.setClosable(false);
        tabOfResponseHeader.setText("Response Header");
        tabOfResponseHeader.setContent(initResponseHeaderPane());
        tpOfResponse.getTabs().addAll(tabOfResponseBody, tabOfResponseHeader);
        
        vbox.getChildren().addAll(initRequestGrid(), tpOfRequest, tpOfResponse);
        
		return vbox;
	}
	
	private Pane initRequestGrid() {//Http Request Pane
		GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 0, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        //Http Method ComboBox
		methodComboBox = new ComboBox<String>();
		methodComboBox.getItems().addAll(
            "GET",
            "POST",
            "PUT",
            "DELETE"
        );
		methodComboBox.setValue("GET");
		methodComboBox.setMinWidth(180);
        grid.add(methodComboBox, 0, 0);
        
        //send button
        Button sendButton = new Button("Send");
        sendButton.setOnAction((ActionEvent e) -> {
        	sendButton();
        });
        sendButton.setMinWidth(190);
        grid.add(sendButton, 1, 0);
        //save button
        Button saveButton = new Button("Save");
        saveButton.setOnAction((ActionEvent e) -> {
        	saveButton();
        });
        saveButton.setMinWidth(190);
        grid.add(saveButton, 2, 0);
        
        return grid;
	}
	
	@SuppressWarnings("unchecked")
	private Pane initHeaderPane() {
		VBox vbox = new VBox(10);
		
		HBox headersTableBox = new HBox();
        headersTableBox.setPadding(new Insets(10, 10, 0, 10));
        table = new TableView<Header>();
        table.setEditable(true);
        table.setPrefWidth(580);
        table.setPrefHeight(200);
        headersTableBox.getChildren().add(table);
        
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
		
        table.setItems(data);
        table.getColumns().addAll(headerNameCol, headerValueCol);
        
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
            data.remove(table.getFocusModel().getFocusedIndex());
        });
        addHeadergrid.add(deleteButton, 3, 0);
        
        vbox.getChildren().addAll(headersTableBox, addHeadergrid);
        return vbox;
	}
	
	private Pane initBodyPane() {
        HBox hb = new HBox();
        hb.setPadding(new Insets(10, 10, 0, 10));
        taOfRequestBody = new TextArea();
        taOfRequestBody.setWrapText(true);//自动换行
        taOfRequestBody.setPromptText("Enter Http Request Body");
        taOfRequestBody.setPrefWidth(580);
        hb.getChildren().add(taOfRequestBody);
        
        return hb;
	}
	
	private Pane initURLPane() {
        HBox hb = new HBox();
        hb.setPadding(new Insets(10, 10, 0, 10));
        taOfURL = new TextArea();
        taOfURL.setWrapText(true);//自动换行
        taOfURL.setPromptText("Enter Http Request URL");
        taOfURL.setPrefWidth(580);
        hb.getChildren().add(taOfURL);
        
        return hb;
	}
	
	private Pane initResponseBodyPane() {
        HBox consoleHb = new HBox();
        consoleHb.setPadding(new Insets(10, 10, 10, 10));
        taOfResponseBody = new TextArea();
        taOfResponseBody.setEditable(false);
        taOfResponseBody.setWrapText(true);
        taOfResponseBody.setPrefWidth(580);
        taOfResponseBody.setPrefHeight(280);
        consoleHb.getChildren().add(taOfResponseBody);
        
        return consoleHb;
	}
	
	private Pane initResponseHeaderPane() {
        HBox consoleHb = new HBox();
        consoleHb.setPadding(new Insets(10, 10, 10, 10));
        taOfResponseHeader = new TextArea();
        taOfResponseHeader.setEditable(false);
        taOfResponseHeader.setWrapText(true);
        taOfResponseHeader.setPrefWidth(580);
        taOfResponseHeader.setPrefHeight(280);
        consoleHb.getChildren().add(taOfResponseHeader);
        
        return consoleHb;
	}
	
	private HashMap<String, String> exchangeHeaders(ObservableList<Header> data){
		HashMap<String, String> headers = new HashMap<String, String>();
		for (int i = 0; i < data.size(); i++) {
			Header header = data.get(i);
			headers.put(header.getName(), header.getValue());
		}
		return headers;
	}
	
	private ObservableList<Header> exchangeMapToList(HashMap<String, String> headers){
		ObservableList<Header> data = FXCollections.observableArrayList();
		if (null != headers && !headers.isEmpty()) {
        	Iterator<Entry<String, String>> iter = headers.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Entry<String, String>) iter.next();
				Header header = new Header(entry.getKey(), entry.getValue());
				data.add(header);
			}
        }
		return data;
	}
	
	public void setContent(HttpClientRequest request){
		methodComboBox.setValue(request.getMethod());
		taOfURL.setText(request.getUrl());
		taOfRequestBody.setText(request.getBody());
		ObservableList<Header> dataInfile = exchangeMapToList(request.getHeaders());
		data.clear();
		for (Header header : dataInfile) {
			data.add(header);
		}
		table.setItems(data);
		taOfResponseBody.setText("");
		taOfResponseHeader.setText("");
	}
	
	public void setEmptyContent(){
		methodComboBox.setValue("GET");
		taOfURL.setText("");
		taOfRequestBody.setText("");
		data.clear();
		taOfResponseBody.setText("");
		taOfResponseHeader.setText("");
	}
	
	public void saveButton() {
		HttpClientRequest request = new HttpClientRequest();
    	request.setMethod(methodComboBox.getSelectionModel().getSelectedItem().toString());
    	request.setUrl(taOfURL.getText());
    	request.setBody(taOfRequestBody.getText());
    	request.setHeaders(exchangeHeaders(data));
    	HttpClientRequest.requestToFile(request, this.treeItem.getValue().getFile().getPath());
	}
	
	public void sendButton() {
		HttpClientResponse response = new HttpClientResponse();
    	String methodOfHTTP = methodComboBox.getSelectionModel().getSelectedItem().toString();
    	if (methodOfHTTP.equals("GET")){
    		response = HttpClientUtil.doGet(taOfURL.getText(), exchangeHeaders(data));
    	} else if (methodOfHTTP.equals("POST")) {
    		response = HttpClientUtil.doPost(taOfURL.getText(), exchangeHeaders(data), taOfRequestBody.getText());
    	} else if (methodOfHTTP.equals("DELETE")) {
    		response = HttpClientUtil.doDelete(taOfURL.getText(), exchangeHeaders(data));
    	} else if (methodOfHTTP.equals("PUT")) {
    		response = HttpClientUtil.doPut(taOfURL.getText(), exchangeHeaders(data), taOfRequestBody.getText());
    	}
    	if (response.getStateCode() != null) {
    		taOfResponseBody.setText(response.getStateCode() + "\n" + response.getResponse());
    		taOfResponseHeader.setText(response.getHeaders());
    	} else {
    		taOfResponseBody.setText("Send request fail. Please check the content of request or the state of http server!");
    		taOfResponseHeader.setText("");
    	}
	}
	
	public ObservableList<Header> getData() {
		return data;
	}

	public void setData(ObservableList<Header> data) {
		this.data = data;
	}

	public TextArea getTaOfRequestBody() {
		return taOfRequestBody;
	}

	public void setTaOfRequestBody(TextArea taOfRequestBody) {
		this.taOfRequestBody = taOfRequestBody;
	}

	public TextArea getTaOfResponseBody() {
		return taOfResponseBody;
	}

	public void setTaOfResponseBody(TextArea taOfResponseBody) {
		this.taOfResponseBody = taOfResponseBody;
	}

	public TextArea getTaOfResponseHeader() {
		return taOfResponseHeader;
	}

	public void setTaOfResponseHeader(TextArea taOfResponseHeader) {
		this.taOfResponseHeader = taOfResponseHeader;
	}

	public TreeItem<NodeItem> getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem<NodeItem> treeItem) {
		this.treeItem = treeItem;
	}

	public TableView<Header> getTable() {
		return table;
	}

	public void setTable(TableView<Header> table) {
		this.table = table;
	}
}
