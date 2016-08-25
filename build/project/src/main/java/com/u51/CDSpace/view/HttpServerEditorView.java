package com.u51.CDSpace.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.u51.CDSpace.model.Header;
import com.u51.CDSpace.model.HttpServerResponse;
import com.u51.CDSpace.model.HttpServerResponseContext;
import com.u51.CDSpace.model.NodeItem;
import com.u51.CDSpace.utils.httpServer.HttpContext;
import com.u51.CDSpace.utils.httpServer.HttpMockServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class HttpServerEditorView{
	private TreeItem<NodeItem> treeItem;
	private HttpMockServer httpMockServer;
	private TextField tfPort;
	private ComboBox<String> cbServerType;
	private ComboBox<String> cbProtocolType;
	private TabPane tabPane;
	private TextArea taConsole;
	private HttpServerResponse response;
	private boolean isServerStart = false;
	private Button buttonStart;
	private Button buttonStop;
	
	public HttpServerEditorView() {
		
	}
	
	/*
	 * HttpServerEditorView由两部分组成
	 * 1、gridGlobalProperties，包括以下元素：tfPort,cbServerType,cbProtocolType,buttonAddContextTab
	 * 2、tabPane，包括两个Tab：contextTab,consoleTab
	 * 		contextTab，这是一个TabPane list，每个TabPane相当于一个server context，包括以下元素：tfUrl,tfReponseCode,tfReponseBody,tvHeaders,tfHeaderName,tfHeaderValue
	 * 		consoleTab
	 */
	public VBox initView() {
		VBox vbox = new VBox(10);
		vbox.setPrefHeight(800);
		vbox.setPrefWidth(1000);
		vbox.getChildren().addAll(initGridGlobalProperties(), initTabPane());
		
		return vbox;
	}
	
	public void setContent(HttpServerResponse response) {
		setButtonDisabled();
		tfPort.setText(response.getPort());
		cbServerType.setValue(response.getServerType());
		cbProtocolType.setValue(response.getProtocolType());
		taConsole.setText("");
		HttpServerResponseContext[] contexts = response.getContexts();
		if (tabPane.getTabs().size() > 1) {
			for (int i = 1; i < tabPane.getTabs().size(); i++) {
				tabPane.getTabs().remove(i);
			}
		}
		if (contexts.length > 0) {
			for (int i = 0; i < contexts.length; i++) {
				HttpServerContextTabView tab = new HttpServerContextTabView();
				HttpServerResponseContext context = contexts[i];
				tab.setText(context.getContextName());
				tab.getTfUrl().setText(context.getUrl());
				tab.getTfResponseCode().setText(context.getResponseCode());
				ObservableList<Header> dataInfile = exchangeMapToList(context.getHeaders());
				tab.getData().clear();
				for (Header header : dataInfile) {
					tab.getData().add(header);
				}
				tab.getTaOfResponseBody().setText(context.getResponseBody());
				tabPane.getTabs().add(tab);
			}
		}
	}
	
	public void setEmptyContent() {
		setButtonDisabled();
		tfPort.setText("");
		cbServerType.setValue("HTTP");
		cbProtocolType.setValue("SSL");
		taConsole.setText("");
		if (tabPane.getTabs().size() > 1) {
			for (int i = 1; i < tabPane.getTabs().size(); i++) {
				tabPane.getTabs().remove(i);
			}
		}
	}
	
	private GridPane initGridGlobalProperties() {
		GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 0, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        
        tfPort = new TextField();
        tfPort.setMinWidth(130);
        tfPort.setPromptText("Listen Port");
        
        cbServerType = new ComboBox<String>();
        cbServerType.getItems().addAll(
            "HTTP", "HTTPS"
        );
        cbServerType.setValue("HTTP");
        cbServerType.setMinWidth(130);
        
        cbProtocolType = new ComboBox<String>();
        cbProtocolType.getItems().addAll(
        	"SSL", "SSLv2", "SSLv3", "TLS", "TLSv1", "TLSv1.1", "TLSv1.2"
        );
        cbProtocolType.setValue("SSL");
        cbProtocolType.setMinWidth(130);
        
        Button buttonSave = new Button("Save");
        buttonSave.setMinWidth(125);
        buttonSave.setOnAction((ActionEvent e) -> {
        	saveDataToFile();
        });
        
        grid.add(tfPort, 0, 0);
        grid.add(cbServerType, 1, 0);
        grid.add(cbProtocolType, 2, 0);
        grid.add(buttonSave, 3, 0);
        
        Button buttonAddContext = new Button("Add Context");
        buttonAddContext.setMinWidth(125);
        buttonAddContext.setOnAction((ActionEvent e) -> {
        	TextInputDialog dialog = new TextInputDialog("New Server Context");
        	dialog.setHeaderText("Input Server Context Name.");
        	dialog.setTitle("New Server Context");
        	dialog.setContentText("Name:");
        	dialog.showAndWait().ifPresent(response -> {
        		HttpServerContextTabView httpServerContextTab = new HttpServerContextTabView();
        		httpServerContextTab.setText(dialog.getEditor().getText());
        		tabPane.getTabs().add(httpServerContextTab);
        	});
        });
        buttonStart = new Button("Start Server");
        buttonStart.setMinWidth(160);
        buttonStart.setOnAction((ActionEvent e) -> {
    		startServer();
        });
        buttonStop = new Button("Stop Server");
        buttonStop.setMinWidth(130);
        buttonStop.setOnAction((ActionEvent e) -> {
    		stopServer();
        });
        Button buttonReboot = new Button("Clear Console");
        buttonReboot.setMinWidth(130);
        buttonReboot.setOnAction((ActionEvent e) -> {
        	taConsole.setText("");
        });
        
        grid.add(buttonStart, 0, 1);
        grid.add(buttonStop, 1, 1);
        grid.add(buttonReboot, 2, 1);
        grid.add(buttonAddContext, 3, 1);
        
        return grid;
	}
	
	private TabPane initTabPane() {
		tabPane = new TabPane();
		Tab tpConsole = new Tab();
		tpConsole.setClosable(false);
		tpConsole.setText("Console");
		tpConsole.setContent(initTabConsole());
		tabPane.getTabs().add(tpConsole);
		
		return tabPane;
	}
	
	private Pane initTabConsole() {
		HBox consoleHb = new HBox();
        consoleHb.setPadding(new Insets(10, 10, 10, 10));
        taConsole = new TextArea();
        taConsole.setEditable(false);
        taConsole.setWrapText(true);
        taConsole.setPrefWidth(580);
        taConsole.setPrefHeight(800);
        consoleHb.getChildren().add(taConsole);
        
        return consoleHb;
	}
	
	public void saveDataToFile() {
		ObservableList<Tab> contextTabs = tabPane.getTabs();
		String port = tfPort.getText();
		if (null != port && !port.equals("") && contextTabs.size() > 1) {
			String serverType = cbServerType.getSelectionModel().getSelectedItem().toString();
			String protocolType = cbProtocolType.getSelectionModel().getSelectedItem().toString();
			HttpServerResponseContext[] contexts = new HttpServerResponseContext[contextTabs.size()-1];
			for (int i = 1; i < contextTabs.size(); i++) {
				HttpServerContextTabView tab = (HttpServerContextTabView) contextTabs.get(i);
				String url = tab.getTfUrl().getText();
				if(null != url && !url.equals("")){
					String contextName = tab.getText();
					String responseCode = tab.getTfResponseCode().getText();
					String responseBody = tab.getTaOfResponseBody().getText();
					HashMap<String, String> headers = exchangeHeaders(tab.getData());
					HttpServerResponseContext context = new HttpServerResponseContext(contextName, url, responseCode, responseBody, headers);
					contexts[i-1] = context;
				}
			}
			HttpServerResponse response = new HttpServerResponse(port, serverType, protocolType, contexts);
			HttpServerResponse.requestToFile(response, this.treeItem.getValue().getFile().getPath());
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("An Error Dialog.");
			alert.setContentText("Ooops, make sure port is not empty and have one server context at least!");
			alert.showAndWait();
		}
	}
	
	public void startServer() {
		if (!isServerStart) {
			httpMockServer = new HttpMockServer();
			String port = tfPort.getText();
			ObservableList<Tab> contextTabs = tabPane.getTabs();
			if (null != port && !port.equals("") && contextTabs.size() > 1) {
				String serverType = cbServerType.getSelectionModel().getSelectedItem().toString();
				String protocolType = cbProtocolType.getSelectionModel().getSelectedItem().toString();
				if (serverType.equals("HTTP")) {
					httpMockServer.initServer(Integer.valueOf(port));
				} else {
					httpMockServer.initServer(Integer.valueOf(port), protocolType);
				}
				for (int i = 1; i < contextTabs.size(); i++) {
					HttpServerContextTabView tab = (HttpServerContextTabView) contextTabs.get(i);
					String url = tab.getTfUrl().getText();
					String responseCode = tab.getTfResponseCode().getText();
					if (Integer.valueOf(responseCode) >= 200) {//错误码小于200为无效错误码，100，101无法返回给客户端
						if(null != url && !url.equals("")){
							String responseBody = tab.getTaOfResponseBody().getText();
							HashMap<String, String> headers = exchangeHeaders(tab.getData());
							HttpContext httpContext = new HttpContext();
							httpContext.setResultCode(Integer.valueOf(responseCode));
							httpContext.setResponseBody(responseBody);
							httpContext.setUrl(url);
							httpContext.setHeaders(headers);
							httpContext.setRequestParams("");
							httpContext.setAreaOfConsole(taConsole);
							httpMockServer.createContext(httpContext);
						}
					}
				}
				httpMockServer.startServer();
				setServerStart(true);
				setButtonDisabled();
				if (serverType.equals("HTTP")) {
					taConsole.appendText("Start HTTP server successfully. Listen port is " + port + ".\n\n");
				} else {
					taConsole.appendText("Start HTTP server successfully. Listen port is " + port + "."
							+ "Encrypt protocol is" + protocolType + ".\n\n");
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("An Error Dialog.");
				alert.setContentText("Ooops, make sure port is not empty and have one server context at least!");
				alert.showAndWait();
			}
		}
	}
	
	public void stopServer() {
		if (null != httpMockServer && isServerStart) {
			httpMockServer.stop();
			setServerStart(false);
			setButtonDisabled();
			taConsole.appendText("HTTP Server Stop.\n\n");
		}
	}
	
	private void setButtonDisabled() {
		if (isServerStart) {
			buttonStart.setDisable(true);
			buttonStop.setDisable(false);
		} else {
			buttonStart.setDisable(false);
			buttonStop.setDisable(true);
		}
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

	public TreeItem<NodeItem> getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem<NodeItem> treeItem) {
		this.treeItem = treeItem;
	}

	public HttpMockServer getHttpMockServer() {
		return httpMockServer;
	}

	public void setHttpMockServer(HttpMockServer httpMockServer) {
		this.httpMockServer = httpMockServer;
	}

	public TextField getTfPort() {
		return tfPort;
	}

	public void setTfPort(TextField tfPort) {
		this.tfPort = tfPort;
	}

	public ComboBox<String> getCbServerType() {
		return cbServerType;
	}

	public void setCbServerType(ComboBox<String> cbServerType) {
		this.cbServerType = cbServerType;
	}

	public ComboBox<String> getCbProtocolType() {
		return cbProtocolType;
	}

	public void setCbProtocolType(ComboBox<String> cbProtocolType) {
		this.cbProtocolType = cbProtocolType;
	}

	public TabPane getTabPane() {
		return tabPane;
	}

	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	public TextArea getTaConsole() {
		return taConsole;
	}

	public void setTaConsole(TextArea taConsole) {
		this.taConsole = taConsole;
	}

	public HttpServerResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServerResponse response) {
		this.response = response;
	}

	public boolean isServerStart() {
		return isServerStart;
	}

	public void setServerStart(boolean isServerStart) {
		this.isServerStart = isServerStart;
	}
}
