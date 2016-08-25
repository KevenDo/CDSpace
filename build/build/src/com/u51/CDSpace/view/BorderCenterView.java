package com.u51.CDSpace.view;

import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.u51.CDSpace.model.TreeItemAndTab;
import com.u51.CDSpace.utils.FileUtil;
import com.u51.CDSpace.model.HttpClientRequest;
import com.u51.CDSpace.model.HttpServerResponse;
import com.u51.CDSpace.model.NodeItem;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class BorderCenterView {
	private Logger logger = Logger.getLogger(BorderCenterView.class);
	private TabPane tabPane;
	private ArrayList<TreeItemAndTab> tabs;
	
	public VBox initView() {
		VBox vbox = new VBox(10);
		vbox.setPrefHeight(650);
		vbox.setPrefWidth(1000);
		
		tabs = new ArrayList<TreeItemAndTab>();
		tabPane = new TabPane();
		vbox.getChildren().add(tabPane);
		
		return vbox;
	}
	
	/*
	 * openType取值为1和2
	 * 1：在当前tab页打开页面
	 * 2：open in a new tab
	 */
	public void loadTab(TreeItem<NodeItem> treeItem, int openType) {
		TreeItemAndTab treeItemAndTab = new TreeItemAndTab();
		if (tabPane.getTabs().size() == 0) {
			treeItemAndTab = addNewTab(treeItem);
			setContent(treeItem, treeItemAndTab);
			treeItemAndTab.setTreeItem(treeItem);//将tab和当前要显示的treeItem绑定
		} else {
			TreeItemAndTab selectedTab = getTreeItemAndTabByTab(tabPane.getSelectionModel().getSelectedItem());
			if (openType == 1) {
				if (FileUtil.getFileType(selectedTab.getTreeItem().getValue().getFileName()) == 2 && selectedTab.getHttpServerEditorView().isServerStart()) {
					treeItemAndTab = addNewTab(treeItem);//当前tab有Httpserver启动时，在新的tab页打开file
				} else {
					treeItemAndTab = selectedTab;
				}
			} else if (openType == 2) {
				treeItemAndTab = addNewTab(treeItem);
			}
			Tab tab = treeItemAndTab.getTab();
			tab.setText(treeItem.getValue().setTabName());
			setContent(treeItem, treeItemAndTab);
			treeItemAndTab.setTreeItem(treeItem);//将tab和当前要显示的treeItem绑定
		}
		logger.info("tab's size is" + tabPane.getTabs().size());
	}
	
	private void setContent(TreeItem<NodeItem> treeItem, TreeItemAndTab treeItemAndTab) {
		Tab tab = treeItemAndTab.getTab();
		if (treeItem.getValue().getFile().isFile()) {
    		logger.debug("selected treeItem file path is "+treeItem.getValue().getFile().getPath());
    		if (FileUtil.getFileType(treeItem.getValue().getFileName()) == 1) {
    			HttpClientEditorView view = treeItemAndTab.getHttpClientEditorView();
    			view.setTreeItem(treeItem);
    			tab.setContent(view.initCenterBorder());
        		if (treeItem.getValue().getFile().length() != 0) {
        			view.setContent(new HttpClientRequest(treeItem.getValue().getFile().getPath()));
        		} else {
        			view.setEmptyContent();
        		}
    		} else if (FileUtil.getFileType(treeItem.getValue().getFileName()) == 2) {
    			HttpServerEditorView view = treeItemAndTab.getHttpServerEditorView();
    			view.setTreeItem(treeItem);
    			tab.setContent(view.initView());
        		if (treeItem.getValue().getFile().length() != 0) {
        			view.setContent(new HttpServerResponse(treeItem.getValue().getFile().getPath()));
        		} else {
        			view.setEmptyContent();
        		}
    		}
    	} else if (treeItem.getValue().getFile().isDirectory()) {
			logger.debug("selected treeItem file path is "+treeItem.getValue().getFile().getPath());
			DirPreviewView dirPreviewView = treeItemAndTab.getDirPreviewView();
			dirPreviewView.setTreeItem(treeItem);
			tab.setContent(dirPreviewView.initView());
			dirPreviewView.setContent(treeItem);
		}
	}
	
	public TreeItemAndTab getTreeItemAndTabByTab(Tab tab) {
		TreeItemAndTab selectedTab = new TreeItemAndTab();
		for (TreeItemAndTab treeItemAndTab : tabs) {
			if (tab.equals(treeItemAndTab.getTab())) {
				selectedTab = treeItemAndTab;
			}
		}
		return selectedTab;
	}
	
	private TreeItemAndTab addNewTab(TreeItem<NodeItem> treeItem) {
		Tab tab = new Tab();
		HttpClientEditorView httpClientEditorView = new HttpClientEditorView();
		HttpServerEditorView httpServerEditorView = new HttpServerEditorView();
		DirPreviewView dirPreviewView = new DirPreviewView();
		TreeItemAndTab treeItemAndTab = new TreeItemAndTab(treeItem, tab, httpClientEditorView, httpServerEditorView, dirPreviewView);
		tab.setText(treeItem.getValue().setTabName());
		tab.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if (FileUtil.getFileType(treeItem.getValue().getFileName()) == 2 && httpServerEditorView.isServerStart()) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
	            	alert.setTitle("Confirmation Dialog");
	            	alert.setHeaderText("Confirmation Dialog\nYou have a httpserver working!");
	            	alert.setContentText("Are you sure close this tab? If you close this tab, the httpserver will also be stoped.");
	            	alert.showAndWait().ifPresent(response -> {
	            		if (response == ButtonType.OK) {
	            			tabs.remove(treeItemAndTab);
	            			tabPane.getTabs().remove(tab);
	            			httpServerEditorView.getHttpMockServer().stop();
	            		} else {
	            			event.consume();
	            		}
	            	});
				} else {
					tabs.remove(treeItemAndTab);
        			tabPane.getTabs().remove(tab);
				}
				logger.info("tab's size is" + tabPane.getTabs().size());
				logger.info("arraylist tab's size is" + tabs.size());
			}	
        });
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);
		tabs.add(treeItemAndTab);
		
		return treeItemAndTab;
	}

	public TabPane getTabPane() {
		return tabPane;
	}

	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	public ArrayList<TreeItemAndTab> getTabs() {
		return tabs;
	}

	public void setTabs(ArrayList<TreeItemAndTab> tabs) {
		this.tabs = tabs;
	}
}
