package com.u51.CDSpace.view;

import com.u51.CDSpace.controller.FMContextMenuController;
import com.u51.CDSpace.model.NodeItem;
import com.u51.CDSpace.model.TreeItemAndTab;
import com.u51.CDSpace.utils.FileUtil;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class MenuBarAndToolBarView {
	private final Image createIcon = new Image(getClass().getResourceAsStream("/image/iconfont-create.png"));
	private final Image addDirIcon = new Image(getClass().getResourceAsStream("/image/iconfont-dir.png"));
	private final Image addProjectIcon = new Image(getClass().getResourceAsStream("/image/iconfont-project.png"));
	private final Image clientIcon = new Image(getClass().getResourceAsStream("/image/iconfont-client.png"));
	private final Image serverIcon = new Image(getClass().getResourceAsStream("/image/iconfont-server.png"));
	private final Image copyIcon = new Image(getClass().getResourceAsStream("/image/iconfont-copy.png"));
	private final Image deleteIcon = new Image(getClass().getResourceAsStream("/image/iconfont-delete.png"));
	private final Image pasteIcon = new Image(getClass().getResourceAsStream("/image/iconfont-paste.png"));
	private final Image refreshIcon = new Image(getClass().getResourceAsStream("/image/iconfont-refresh.png"));
	private final Image renameIcon = new Image(getClass().getResourceAsStream("/image/iconfont-rename01.png"));
	private final Image tabIcon = new Image(getClass().getResourceAsStream("/image/iconfont-tab.png"));
	private final Image saveIcon = new Image(getClass().getResourceAsStream("/image/iconfont-save.png"));
	private final Image sendIcon = new Image(getClass().getResourceAsStream("/image/iconfont-send.png"));
	private final Image startIcon = new Image(getClass().getResourceAsStream("/image/iconfont-start.png"));
	private final Image stopIcon = new Image(getClass().getResourceAsStream("/image/iconfont-stop.png"));
	
	private VBox vbox = new VBox();
	
	private BorderCenterView borderCenterTabPaneView;
	
	private TreeItem<NodeItem> treeItem;
	
	private TreeView<NodeItem> treeView;
	
	private TreeItem<NodeItem> prepareForCopyTreeItem;
	
	private FileManagerView fileManagerView;
	
	private FMContextMenuController menuController = new FMContextMenuController();
	
	public MenuBarAndToolBarView(FileManagerView fileManagerView) {
		this.fileManagerView = fileManagerView;
		this.borderCenterTabPaneView = fileManagerView.getBorderCenterTabPaneView();
		menuController.setTreeView(fileManagerView.getTreeView());
		menuController.setFileManagerView(fileManagerView);
		initView();
	}
	
	public void initView() {
		vbox.getChildren().addAll(initMenuBar(), initToolBar());
	}
	
	public ToolBar initToolBar() {
		ToolBar toolBar = new ToolBar();
		Button btnNewProject = new Button("", new ImageView(addProjectIcon));
		Button btnNewDir = new Button("", new ImageView(addDirIcon));
		Button btnNewClient = new Button("", new ImageView(clientIcon));
		Button btnNewServer = new Button("", new ImageView(serverIcon));
		Button btnSave = new Button("", new ImageView(saveIcon));
		Button btnRefresh = new Button("", new ImageView(refreshIcon));
		Button btnCreateNewTab = new Button("", new ImageView(tabIcon));
		Button btnSend = new Button("", new ImageView(sendIcon));
		Button btnStart = new Button("", new ImageView(startIcon));
		Button btnStop = new Button("", new ImageView(stopIcon));
		
		toolBar.getItems().addAll(btnNewProject, btnNewDir, btnNewClient, btnNewServer,
				new Separator(), btnSave, btnRefresh, 
				new Separator(), btnCreateNewTab, 
				new Separator(), btnSend, 
				new Separator(), btnStart, btnStop);
		
		btnNewProject.setOnAction((ActionEvent t) -> {menuController.addProject();});
		btnNewDir.setOnAction((ActionEvent t) -> {menuController.addDir();});
		btnNewClient.setOnAction((ActionEvent t) -> {menuController.addClient();});
		btnNewServer.setOnAction((ActionEvent t) -> {menuController.addServer();});
		btnRefresh.setOnAction((ActionEvent t) -> {menuController.refresh();});
		btnSave.setOnAction((ActionEvent t) -> {btnAction(1);});
		btnSend.setOnAction((ActionEvent t) -> {btnAction(2);});
		btnStart.setOnAction((ActionEvent t) -> {btnAction(3);});
		btnStop.setOnAction((ActionEvent t) -> {btnAction(4);});
		btnCreateNewTab.setOnAction((ActionEvent t) -> {btnCreateNewTab();});
		
		return toolBar;
	}
	
	public MenuBar initMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		Menu menuNew = new Menu("New     ", new ImageView(createIcon));
		MenuItem menuItemOfAddClient = new MenuItem("HttpClient", new ImageView(clientIcon));
		MenuItem menuItemOfAddServer = new MenuItem("HttpServer", new ImageView(serverIcon));
		MenuItem menuItemOfAddDir = new MenuItem("Directory", new ImageView(addDirIcon));
		MenuItem menuItemOfAddProject = new MenuItem("Project", new ImageView(addProjectIcon));
		MenuItem menuItemOfSave = new MenuItem("Save", new ImageView(saveIcon));
		MenuItem menuItemOfExist = new MenuItem("Exist");
		Menu menuEdit = new Menu("Edit");
	    MenuItem menuItemOfCopy = new MenuItem("Copy ", new ImageView(copyIcon));
	    MenuItem menuItemOfDelete = new MenuItem("Delete", new ImageView(deleteIcon));
	    MenuItem menuItemOfPaste = new MenuItem("Paste", new ImageView(pasteIcon));
	    MenuItem menuItemOfRefresh = new MenuItem("Refresh", new ImageView(refreshIcon));
	    MenuItem menuItemOfRename = new MenuItem("Rename", new ImageView(renameIcon));
	    Menu menuRun = new Menu("Run");
	    MenuItem menuItemOfTab = new MenuItem("Open in a new tab", new ImageView(tabIcon));
	    MenuItem menuItemOfSend = new MenuItem("Send", new ImageView(sendIcon));
	    MenuItem menuItemOfStart = new MenuItem("Start", new ImageView(startIcon));
	    MenuItem menuItemOfStop = new MenuItem("Stop", new ImageView(stopIcon));
	    Menu menuHelp = new Menu("Help");
	    MenuItem miAboutCDSpace = new MenuItem("About CDSpace");
	    MenuItem miAboutMe = new MenuItem("About Author");
	    menuItemOfSave.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
	    
	    menuNew.getItems().addAll(menuItemOfAddProject, menuItemOfAddDir, menuItemOfAddClient, menuItemOfAddServer);
		menuFile.getItems().addAll(menuNew, menuItemOfSave, new SeparatorMenuItem(), menuItemOfExist);
		menuEdit.getItems().addAll(menuItemOfCopy, menuItemOfPaste, menuItemOfRename, menuItemOfRefresh, menuItemOfDelete);
		menuRun.getItems().addAll(menuItemOfTab, menuItemOfSend, menuItemOfStart, menuItemOfStop);
		menuHelp.getItems().addAll(miAboutCDSpace, miAboutMe);
		menuBar.getMenus().addAll(menuFile, menuEdit, menuRun, menuHelp);
		//File Menu
		menuItemOfAddClient.setOnAction((ActionEvent t) -> {menuController.addClient();});
		menuItemOfAddServer.setOnAction((ActionEvent t) -> {menuController.addServer();});
		menuItemOfAddDir.setOnAction((ActionEvent t) -> {menuController.addDir();});
		menuItemOfAddProject.setOnAction((ActionEvent t) -> {menuController.addProject();});
		menuItemOfSave.setOnAction((ActionEvent t) -> {btnAction(1);});
		menuItemOfExist.setOnAction((ActionEvent t) -> {System.exit(0);});
		//Edit Menu
		menuItemOfCopy.setOnAction((ActionEvent t) -> {menuController.copyItem();});
		menuItemOfDelete.setOnAction((ActionEvent t) -> {menuController.deleteItem();});
		menuItemOfPaste.setOnAction((ActionEvent t) -> {menuController.pasteItem();});
		menuItemOfRefresh.setOnAction((ActionEvent t) -> {menuController.refresh();});
		menuItemOfRename.setOnAction((ActionEvent t) -> {menuController.rename();});
		//Run Menu
		menuItemOfTab.setOnAction((ActionEvent t) -> {btnCreateNewTab();});
		menuItemOfSend.setOnAction((ActionEvent t) -> {btnAction(2);});
		menuItemOfStart.setOnAction((ActionEvent t) -> {btnAction(3);});
		menuItemOfStop.setOnAction((ActionEvent t) -> {btnAction(4);});
		//Help Menu
		miAboutCDSpace.setOnAction((ActionEvent t) -> {aboutCDSpace();});
		miAboutMe.setOnAction((ActionEvent t) -> {aboutMe();});
		
		return menuBar;
	}
	
	/*
	 * type 
	 * 1: save
	 * 2: send
	 * 3: start
	 * 4: stop
	 */
	private void btnAction(int type) {
		if (borderCenterTabPaneView.getTabPane().getTabs().size() > 0 && null != getFileManagerView().getTreeView().getSelectionModel().getSelectedItem()) {
			setTreeItem(getFileManagerView().getTreeView().getSelectionModel().getSelectedItem());
			TreeItemAndTab treeItemAndTab = borderCenterTabPaneView.getTreeItemAndTabByTab(borderCenterTabPaneView.getTabPane().getSelectionModel().getSelectedItem());
			if (treeItem.getValue().getFile().isFile()) {
				if (FileUtil.getFileType(treeItemAndTab.getTreeItem().getValue().getFileName()) == 1) {
					HttpClientEditorView view = treeItemAndTab.getHttpClientEditorView();
					switch (type) {
		            case 1:  view.saveButton();
		                     break;
		            case 2:  view.sendButton();
                    		 break;
					}
				} else if (FileUtil.getFileType(treeItemAndTab.getTreeItem().getValue().getFileName()) == 2) {
					HttpServerEditorView view = treeItemAndTab.getHttpServerEditorView();
					switch (type) {
		            case 1:  view.saveDataToFile();
		                     break;
		            case 3:  view.startServer();;
                    		 break;
		            case 4:  view.stopServer();;
                    		 break;
					}
				}
			} else {
				DirPreviewView dirPreviewView = treeItemAndTab.getDirPreviewView();
				dirPreviewView.saveButton();
			}
		}
	}
	
	private void btnCreateNewTab() {
		if (null != getFileManagerView().getTreeView().getSelectionModel().getSelectedItem()) {
			setTreeItem(getFileManagerView().getTreeView().getSelectionModel().getSelectedItem());
			getBorderCenterTabPaneView().loadTab(treeItem, 2);
		}
	}
	
	private void aboutCDSpace() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("About CDSpace");
		alert.setContentText("CDSpace SDK\n\n"
				+ "Version: Green (1.0)\n"
				+ "Build id: 20160318\n\n"
				+ "CDSpace can send HTTP request and mock HTTP server. Hope it can help you.");
		alert.showAndWait();
	}
	
	private void aboutMe() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("About me");
		alert.setContentText("Author: Kevin Do\nEmail: dldsryx@126.com\n微博: adoaiwen1314");
		alert.showAndWait();
	}

	public VBox getVbox() {
		return vbox;
	}

	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}

	public BorderCenterView getBorderCenterTabPaneView() {
		return borderCenterTabPaneView;
	}

	public void setBorderCenterTabPaneView(BorderCenterView borderCenterTabPaneView) {
		this.borderCenterTabPaneView = borderCenterTabPaneView;
	}

	public TreeItem<NodeItem> getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem<NodeItem> treeItem) {
		this.treeItem = treeItem;
	}

	public TreeItem<NodeItem> getPrepareForCopyTreeItem() {
		return prepareForCopyTreeItem;
	}

	public void setPrepareForCopyTreeItem(TreeItem<NodeItem> prepareForCopyTreeItem) {
		this.prepareForCopyTreeItem = prepareForCopyTreeItem;
	}

	public FileManagerView getFileManagerView() {
		return fileManagerView;
	}

	public void setFileManagerView(FileManagerView fileManagerView) {
		this.fileManagerView = fileManagerView;
	}

	public FMContextMenuController getMenuController() {
		return menuController;
	}

	public void setMenuController(FMContextMenuController menuController) {
		this.menuController = menuController;
	}

	public TreeView<NodeItem> getTreeView() {
		return treeView;
	}

	public void setTreeView(TreeView<NodeItem> treeView) {
		this.treeView = treeView;
	}
}
