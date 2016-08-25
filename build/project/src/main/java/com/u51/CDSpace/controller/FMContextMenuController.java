package com.u51.CDSpace.controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.u51.CDSpace.model.NodeItem;
import com.u51.CDSpace.utils.FileUtil;
import com.u51.CDSpace.utils.StringUtil;
import com.u51.CDSpace.view.FileManagerView;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class FMContextMenuController {
	private static Logger logger = Logger.getLogger(FMContextMenuController.class);
	private final Image projectIcon = new Image(getClass().getResourceAsStream("/image/iconfont-project.png"));
	private final Image createIcon = new Image(getClass().getResourceAsStream("/image/iconfont-create.png"));
	private final Image dirIcon = new Image(getClass().getResourceAsStream("/image/iconfont-dir.png"));
	private final Image addDirIcon = new Image(getClass().getResourceAsStream("/image/iconfont-addDir.png"));
	private final Image addProjectIcon = new Image(getClass().getResourceAsStream("/image/iconfont-project.png"));
	private final Image clientIcon = new Image(getClass().getResourceAsStream("/image/iconfont-client.png"));
	private final Image serverIcon = new Image(getClass().getResourceAsStream("/image/iconfont-server.png"));
	private final Image copyIcon = new Image(getClass().getResourceAsStream("/image/iconfont-copy.png"));
	private final Image deleteIcon = new Image(getClass().getResourceAsStream("/image/iconfont-delete.png"));
	private final Image pasteIcon = new Image(getClass().getResourceAsStream("/image/iconfont-paste.png"));
	private final Image refreshIcon = new Image(getClass().getResourceAsStream("/image/iconfont-refresh.png"));
	private final Image renameIcon = new Image(getClass().getResourceAsStream("/image/iconfont-rename01.png"));
	private final Image tabIcon = new Image(getClass().getResourceAsStream("/image/iconfont-tab.png"));
	
	private ContextMenu contextMenu = new ContextMenu();
	private TreeView<NodeItem> treeView;
	private TreeItem<NodeItem> prepareForCopyTreeItem;
	private FileManagerView fileManagerView;
	
	public FMContextMenuController() {
		
	}
	
	public FMContextMenuController(TreeView<NodeItem> treeView){
		this.treeView = treeView;
		
		Menu menu = new Menu("New             ", new ImageView(createIcon));
		MenuItem menuItemOfAddClient = new MenuItem("HttpClient", new ImageView(clientIcon));
		MenuItem menuItemOfAddServer = new MenuItem("HttpServer", new ImageView(serverIcon));
		MenuItem menuItemOfAddDir = new MenuItem("Directory", new ImageView(addDirIcon));
		MenuItem menuItemOfAddProject = new MenuItem("Project", new ImageView(addProjectIcon));
		menu.getItems().add(menuItemOfAddClient);
		menu.getItems().add(menuItemOfAddServer);
        menu.getItems().add(menuItemOfAddDir);
        menu.getItems().add(menuItemOfAddProject);
        MenuItem menuItemOfCopy = new MenuItem("Copy ", new ImageView(copyIcon));
        MenuItem menuItemOfDelete = new MenuItem("Delete", new ImageView(deleteIcon));
        MenuItem menuItemOfPaste = new MenuItem("Paste", new ImageView(pasteIcon));
        MenuItem menuItemOfRefresh = new MenuItem("Refresh", new ImageView(refreshIcon));
        MenuItem menuItemOfRename = new MenuItem("Rename", new ImageView(renameIcon));
        MenuItem menuItemOfTab = new MenuItem("Open in a new tab", new ImageView(tabIcon));
        menuItemOfCopy.setAccelerator(KeyCombination.keyCombination("Shortcut+C"));
        menuItemOfPaste.setAccelerator(KeyCombination.keyCombination("Shortcut+V"));
        menuItemOfRefresh.setAccelerator(KeyCombination.keyCombination("Shortcut+R"));
        menuItemOfRename.setAccelerator(KeyCombination.keyCombination("Shortcut+E"));
        
        getContextMenu().getItems().add(menu);
        getContextMenu().getItems().add(new SeparatorMenuItem());
        getContextMenu().getItems().add(menuItemOfTab);
        getContextMenu().getItems().add(menuItemOfRename);
        getContextMenu().getItems().add(menuItemOfCopy);
        getContextMenu().getItems().add(menuItemOfPaste);
        getContextMenu().getItems().add(menuItemOfRefresh);
        getContextMenu().getItems().add(new SeparatorMenuItem());
        getContextMenu().getItems().add(menuItemOfDelete);
        
        
        menuItemOfAddClient.setOnAction((ActionEvent t) -> {addClient();});
        menuItemOfAddServer.setOnAction((ActionEvent t) -> {addServer();});
        menuItemOfAddDir.setOnAction((ActionEvent t) -> {addDir();});
        menuItemOfAddProject.setOnAction((ActionEvent t) -> {addProject();});
        menuItemOfTab.setOnAction((ActionEvent t) -> {addTab();});
        menuItemOfRename.setOnAction((ActionEvent t) -> {rename();});
        menuItemOfCopy.setOnAction((ActionEvent t) -> {copyItem();});
        menuItemOfPaste.setOnAction((ActionEvent t) -> {pasteItem();});
        menuItemOfRefresh.setOnAction((ActionEvent t) -> {refresh();});
        menuItemOfDelete.setOnAction((ActionEvent t) -> {deleteItem();});
	}
	
	public void addProject() {
		TextInputDialog dialog = new TextInputDialog("New Project");
    	dialog.setHeaderText("Input project name.\nPlease don't input these characters:\\ .");
    	dialog.setTitle("New Project");
    	dialog.setContentText("Name:");
    	dialog.showAndWait().ifPresent(response -> {
    		String fileName = dialog.getEditor().getText();
    		String fileParentDirectoryPath = getTreeView().getRoot().getValue().getFile().getPath();
    		TreeItem<NodeItem> parentTreeItem = getTreeView().getRoot();
    		for (File file : new File(fileParentDirectoryPath).listFiles()) {
    			if (file.getName().equals(fileName)) {
    				fileName += StringUtil.randInt(10000, 100000);
    				break;
    			}
    		}
    		String newFilePath = fileParentDirectoryPath + "/" + fileName;
    		TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(new File(newFilePath), fileName), new ImageView(projectIcon));
    		parentTreeItem.getChildren().add(newTreeItem);
    		getTreeView().getSelectionModel().select(newTreeItem);
        	FileUtil.createEmptyDir(newFilePath, 1);
    	});
	}
	
	public void addClient() {
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
    	if (selectedTreeItem != null) {
    		TextInputDialog dialog = new TextInputDialog("New HttpClient");
        	dialog.setHeaderText("Input file name.\nPlease don't input these characters:\\ .");
        	dialog.setTitle("New HttpClient");
        	dialog.setContentText("Name:");
        	dialog.showAndWait().ifPresent(response -> {
        		String fileName = dialog.getEditor().getText();
        		String fullName = fileName + ".client";
        		boolean isDirectory = selectedTreeItem.getValue().getFile().isDirectory();//判断该节点是否是文件夹
        		String fileParentDirectoryPath = isDirectory ? selectedTreeItem.getValue().getFile().getPath() :
        			selectedTreeItem.getParent().getValue().getFile().getPath();//如果是文件夹，目录不变，如果是文件，目录取父节点的path
        		TreeItem<NodeItem> parentTreeItem = isDirectory ? selectedTreeItem : selectedTreeItem.getParent();//同文件夹的处理
        		for (File file : new File(fileParentDirectoryPath).listFiles()) {//对比输入的文件名是否重名，重名则在文件名末尾添加随机数
        			if (file.getName().equals(fullName)){
        				fileName += StringUtil.randInt(10000, 100000);
        				fullName = fileName + ".client";
        				break;
        			}
        		}
        		String newFilePath = fileParentDirectoryPath + "/" + fullName;//新文件的path
        		TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(new File(newFilePath), fullName), new ImageView(clientIcon));//创建新节点
        		parentTreeItem.getChildren().add(newTreeItem);//将新节点添加到父节点
        		getTreeView().getSelectionModel().select(newTreeItem);
        		FileUtil.createEmptyFile(newFilePath, 1);//创建空文件
        		fileManagerView.setCenterBorderAfterSelectTreeItem();
        	 });
    	} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Information Dialog");
			alert.setContentText("Before you add a http client, please keep a project treeItem selected.");
			alert.showAndWait();
		}
	}
	
	public void addServer() {
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
    	if (selectedTreeItem != null) {
    		TextInputDialog dialog = new TextInputDialog("New HttpServer");
        	dialog.setHeaderText("Input file name.\nPlease don't input these characters:\\ .");
        	dialog.setTitle("New HttpServer");
        	dialog.setContentText("Name:");
        	dialog.showAndWait().ifPresent(response -> {
        		String fileName = dialog.getEditor().getText();
        		String fullName = fileName + ".server";
        		boolean isDirectory = selectedTreeItem.getValue().getFile().isDirectory();//判断该节点是否是文件夹
        		String fileParentDirectoryPath = isDirectory ? selectedTreeItem.getValue().getFile().getPath() :
        			selectedTreeItem.getParent().getValue().getFile().getPath();//如果是文件夹，目录不变，如果是文件，目录取父节点的path
        		TreeItem<NodeItem> parentTreeItem = isDirectory ? selectedTreeItem : selectedTreeItem.getParent();//同文件夹的处理
        		for (File file : new File(fileParentDirectoryPath).listFiles()) {//对比输入的文件名是否重名，重名则在文件名末尾添加随机数
        			if (file.getName().equals(fullName)){
        				fileName += StringUtil.randInt(10000, 100000);
        				fullName = fileName + ".server";
        				break;
        			}
        		}
        		String newFilePath = fileParentDirectoryPath + "/" + fullName;//新文件的path
        		TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(new File(newFilePath), fullName), new ImageView(serverIcon));//创建新节点
        		parentTreeItem.getChildren().add(newTreeItem);//将新节点添加到父节点
        		getTreeView().getSelectionModel().select(newTreeItem);
        		FileUtil.createEmptyFile(newFilePath, 1);//创建空文件
        		fileManagerView.setCenterBorderAfterSelectTreeItem();
        	 });
    	} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Information Dialog");
			alert.setContentText("Before you add a http client, please keep a project treeItem selected.");
			alert.showAndWait();
		}
	}
	
	public void addDir() {
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();
    	if (null != selectedTreeItem) {
    		TextInputDialog dialog = new TextInputDialog("New Directory");
        	dialog.setHeaderText("Input directory name.\nPlease don't input these characters:\\ .");
        	dialog.setTitle("New Directory");
        	dialog.setContentText("Name:");
        	dialog.showAndWait().ifPresent(response -> {
        		String fileName = dialog.getEditor().getText();
        		boolean isDirectory = selectedTreeItem.getValue().getFile().isDirectory();
        		String fileParentDirectoryPath = isDirectory ? selectedTreeItem.getValue().getFile().getPath() :
        			selectedTreeItem.getParent().getValue().getFile().getPath();
        		TreeItem<NodeItem> parentTreeItem = isDirectory ? selectedTreeItem : selectedTreeItem.getParent();
        		for (File file : new File(fileParentDirectoryPath).listFiles()) {
        			if (file.getName().equals(fileName)) {
        				fileName += StringUtil.randInt(10000, 100000);
        				break;
        			}
        		}
        		String newFilePath = fileParentDirectoryPath + "/" + fileName;
        		TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(new File(newFilePath), fileName), new ImageView(dirIcon));
        		parentTreeItem.getChildren().add(newTreeItem);
        		getTreeView().getSelectionModel().select(newTreeItem);
            	FileUtil.createEmptyDir(newFilePath, 1);
            	fileManagerView.setCenterBorderAfterSelectTreeItem();
        	});
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Information Dialog");
			alert.setContentText("Before you add directory, please keep a project treeItem selected.");
			alert.showAndWait();
		}
	}
	
	public void refresh() {
//		TreeItem<NodeItem> parentTreeItem = getTreeView().getRoot();
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
    	if (null != selectedTreeItem) {
    		if (isProjectItem(selectedTreeItem)) {//判断prepareForCopyTreeItem是否为project节点
    			TreeItem<NodeItem> parentTreeItem = getTreeView().getRoot();
    			parentTreeItem.getChildren().clear();//清空节点
    			for (File file : parentTreeItem.getValue().getFile().listFiles()) {
    				if (file.isDirectory()) {
    					TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(projectIcon));
    					parentTreeItem.getChildren().add(newTreeItem);
    					refreshTreeItem(newTreeItem);
    				}
    			}
    		} else {
    			TreeItem<NodeItem> parentTreeItem;
        		if (selectedTreeItem.getValue().getFile().isDirectory()) {
        			parentTreeItem = selectedTreeItem;
        		} else {
        			parentTreeItem = selectedTreeItem.getParent();
        		}
        		parentTreeItem.getChildren().clear();//清空节点
        		refreshTreeItem(parentTreeItem);
    		}
    	}
	}
	
	public void rename() {
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
    	if (null != selectedTreeItem) {
    		String oldName = selectedTreeItem.getValue().toString();
    		String oldFullName = selectedTreeItem.getValue().getFileName();
    		File fileSelected = selectedTreeItem.getValue().getFile();
        	String fileParentDirectoryPath = selectedTreeItem.getParent().getValue().getFile().getPath();
        	TextInputDialog dialog = new TextInputDialog(oldName);
        	dialog.setHeaderText("Input new name.\nPlease don't input these characters:\\ .");
        	dialog.setTitle("Rename Dialog");
        	dialog.setContentText("Name:");
        	dialog.showAndWait().ifPresent(response -> {
        		String newName = dialog.getEditor().getText();
        		String newFullName = "";
        		boolean isExist = false;
        		if (!oldName.equals(newName)) {
        			for (File file : new File(fileParentDirectoryPath).listFiles()) {
        				if (fileSelected.isFile() && FileUtil.getFileType(fileSelected.getName()) == 1) {
        					newFullName = newName + ".client";
        				} else if (fileSelected.isFile() && FileUtil.getFileType(fileSelected.getName()) == 2) {
        					newFullName = newName + ".server";
        				} else {
        					newFullName = newName;
        				}
            			if (file.getName().equals(newFullName)) {
            				isExist = true;
            				Alert alert = new Alert(AlertType.ERROR);
            				alert.setTitle("Error Dialog");
            				alert.setHeaderText("An Error Dialog.");
            				alert.setContentText("Ooops, this name is repeated!");
            				alert.showAndWait();
            				break;
            			}
            		}
        			if (!isExist) {
        				File oldFile = new File(fileParentDirectoryPath + "/" + oldFullName);
            			File newFile = new File(fileParentDirectoryPath + "/" + newFullName);
            			selectedTreeItem.setValue(new NodeItem(newFile, newFullName));
            			oldFile.renameTo(newFile);
            			refresh();
        			}
        		}
        	});
		}
	}
	
	public void addTab() {
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
    	if (null != selectedTreeItem) {
    		fileManagerView.getBorderCenterTabPaneView().loadTab(selectedTreeItem, 2);
    	}
	}
	
	public void copyItem() {
		prepareForCopyTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
	}
	
	public void pasteItem() {
		if ( prepareForCopyTreeItem != null ) {
			if (isProjectItem(getPrepareForCopyTreeItem())) {//判断prepareForCopyTreeItem是否为project节点
	    		String fileName = prepareForCopyTreeItem.getValue().getFileName();
	    		TreeItem<NodeItem> parentTreeItem = getTreeView().getRoot();
	    		for (File file : parentTreeItem.getValue().getFile().listFiles()) {
	    			if (file.getName().equals(fileName)) {
	    				fileName += StringUtil.randInt(10000, 100000);
	    				break;
	    			}
	    		}
	    		String newFilePath = parentTreeItem.getValue().getFile().getPath() + "/" + fileName;
	    		try {
					TreeItem<NodeItem> newTreeItem = deepCopyTreeItem(prepareForCopyTreeItem);
					newTreeItem.setValue(new NodeItem(new File(newFilePath), fileName));
					newTreeItem.setGraphic(new ImageView(dirIcon));
	        		parentTreeItem.getChildren().add(newTreeItem);
					FileUtils.copyDirectory(prepareForCopyTreeItem.getValue().getFile(), new File(newFilePath));
					newTreeItem.getChildren().clear();
					refreshTreeItem(newTreeItem);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	} else {
	    		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
	    		String fileName = prepareForCopyTreeItem.getValue().getFileName();
	    		String filePreName = FileUtil.getFilePreName(fileName);
	    		boolean isDirectory = selectedTreeItem.getValue().getFile().isDirectory();
	    		String fileParentDirectoryPath = isDirectory ? selectedTreeItem.getValue().getFile().getPath() :
	    			selectedTreeItem.getParent().getValue().getFile().getPath();
	    		TreeItem<NodeItem> parentTreeItem = isDirectory ? selectedTreeItem : selectedTreeItem.getParent();
	    		for (File file : new File(fileParentDirectoryPath).listFiles()) {
	    			if (file.getName().equals(fileName)) {
	    				filePreName += StringUtil.randInt(10000, 100000);
	    				if (FileUtil.getFileType(prepareForCopyTreeItem.getValue().getFileName()) == 1) {
	    					fileName = filePreName + ".client";
	    				} else if (FileUtil.getFileType(prepareForCopyTreeItem.getValue().getFileName()) == 2) {
	    					fileName = filePreName + ".server";
	    				} else if (prepareForCopyTreeItem.getValue().getFile().isDirectory()) {
	    					fileName = fileName + filePreName;
	    				}
	    				break;
	    			}
	    		}
	    		String newFilePath = fileParentDirectoryPath + "/" + fileName;
	    		if (prepareForCopyTreeItem.getValue().getFile().isFile()) {//判断prepareForCopyTreeItem是否为file
	    			try {
	    				if (FileUtil.getFileType(prepareForCopyTreeItem.getValue().getFileName()) == 1) {
	    					TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(new File(newFilePath), fileName), new ImageView(clientIcon));
	                		parentTreeItem.getChildren().add(newTreeItem);
							FileUtils.copyFile(prepareForCopyTreeItem.getValue().getFile(), new File(newFilePath));
	    				} else if (FileUtil.getFileType(prepareForCopyTreeItem.getValue().getFileName()) == 2) {
	    					TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(new File(newFilePath), fileName), new ImageView(serverIcon));
	                		parentTreeItem.getChildren().add(newTreeItem);
							FileUtils.copyFile(prepareForCopyTreeItem.getValue().getFile(), new File(newFilePath));
	    				}
	    				
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		} else {//prepareForCopyTreeItem为directory
	    			try {
	    				if (prepareForCopyTreeItem.equals(selectedTreeItem)) {
	    					Alert alert = new Alert(AlertType.ERROR);
	        				alert.setTitle("Error Dialog");
	        				alert.setHeaderText("An Error Dialog.");
	        				alert.setContentText("Ooops, you can't copy directory into itself!");
	        				alert.showAndWait();
	    				} else {
	    					TreeItem<NodeItem> newTreeItem = deepCopyTreeItem(prepareForCopyTreeItem);
	        				newTreeItem.setValue(new NodeItem(new File(newFilePath), fileName));
	        				newTreeItem.setGraphic(new ImageView(dirIcon));
	                		parentTreeItem.getChildren().add(newTreeItem);
							FileUtils.copyDirectory(prepareForCopyTreeItem.getValue().getFile(), new File(newFilePath));
							newTreeItem.getChildren().clear();
							refreshTreeItem(newTreeItem);
	    				}
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    	}
		}
	}
	
	public void deleteItem() {
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
    	if (null != selectedTreeItem) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Confirmation Dialog");
        	alert.setHeaderText("Confirmation Dialog");
        	alert.setContentText("Are you sure with delete this file or derectory?");
        	alert.showAndWait().ifPresent(response -> {
        		if (response == ButtonType.OK) {
        			selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
        			logger.info("Delete file and file path is" + selectedTreeItem.getValue().getFile().getPath());
            		try {
            			if (selectedTreeItem.getValue().getFile().isDirectory()) {
            				FileUtils.deleteDirectory(selectedTreeItem.getValue().getFile());
            				fileManagerView.setCenterBorderAfterSelectTreeItem();
            			} else {
            				selectedTreeItem.getValue().getFile().delete();
            				fileManagerView.setCenterBorderAfterSelectTreeItem();
            			}
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
        		}
        	});
    	}
	}
	
	private TreeItem<NodeItem> deepCopyTreeItem(TreeItem<NodeItem> item) {
	    TreeItem<NodeItem> copy = new TreeItem<NodeItem>(item.getValue());
	    for (TreeItem<NodeItem> child : item.getChildren()) {
	    	copy.getChildren().add(deepCopyTreeItem(child));
	    }
	    return copy;
	}
	
	private void refreshTreeItem(TreeItem<NodeItem> item) {
		File[] files = item.getValue().getFile().listFiles();
		for (File file : files) {
			if(file.isDirectory()) {
				TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(dirIcon));
				item.getChildren().add(newTreeItem);
				refreshTreeItem(newTreeItem);
			} else {
				if (FileUtil.getFileType(file.getName()) == 1) {
					TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(clientIcon));
					item.getChildren().add(newTreeItem);
				} else if (FileUtil.getFileType(file.getName()) == 2) {
					TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(serverIcon));
					item.getChildren().add(newTreeItem);
				}
			}
		}
	}
	
	private boolean isProjectItem(TreeItem<NodeItem> item) {
		boolean isProjectItem = false;
		if (item.getParent().equals(getTreeView().getRoot())) {
			isProjectItem = true;
		}	
		return isProjectItem;
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(ContextMenu contextMenu) {
		this.contextMenu = contextMenu;
	}

	public TreeView<NodeItem> getTreeView() {
		return treeView;
	}

	public void setTreeView(TreeView<NodeItem> treeView) {
		this.treeView = treeView;
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
}
