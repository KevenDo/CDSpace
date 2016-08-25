package com.u51.CDSpace.view;

import java.io.File;

import com.u51.CDSpace.model.NodeItem;
import com.u51.CDSpace.utils.FileUtil;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class DirPreviewView {
	private final Image dirIcon = new Image(getClass().getResourceAsStream("/image/iconfont-dir.png"));
	private final Image clientIcon = new Image(getClass().getResourceAsStream("/image/iconfont-client.png"));
	private final Image serverIcon = new Image(getClass().getResourceAsStream("/image/iconfont-server.png"));
	
	private TreeItem<NodeItem> treeItem;
	
	private TextField tfDirName;
	
	public DirPreviewView() {
		
	}
	
	public VBox initView() {
		VBox vbox = new VBox(10);
		vbox.setPrefHeight(600);
		vbox.setPrefWidth(600);
		
		GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 0, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        vbox.getChildren().addAll(grid);
        
        tfDirName = new TextField();//directory name
        tfDirName.setMinWidth(360);
        grid.add(tfDirName, 0, 0);
        
        Button saveButton = new Button("save");
        saveButton.setOnAction((ActionEvent e) -> {
        	saveButton();
        });
        grid.add(saveButton, 1, 0);
        
		return vbox;
	}
	
	public void setContent(TreeItem<NodeItem> treeItem) {
		tfDirName.setText(treeItem.getValue().getFileName());
	}
	
	public void saveButton() {
		String oldName = treeItem.getValue().getFileName();
    	String newName = tfDirName.getText();
    	String fileParentDirectoryPath = treeItem.getParent().getValue().getFile().getPath();
    	boolean isNewNameExist = false;
    	if (!oldName.equals(newName)) {
    		for (File file : new File(fileParentDirectoryPath).listFiles()) {
    			if (file.isDirectory() && file.getName().equals(newName)) {
    				isNewNameExist = true;
    				Alert alert = new Alert(AlertType.ERROR);
    				alert.setTitle("Error Dialog");
    				alert.setHeaderText("An Error Dialog.");
    				alert.setContentText("Ooops, this name is repeated!");
    				alert.showAndWait();
    				break;
    			}
    		}
			if (!isNewNameExist) {
				File oldFile = new File(fileParentDirectoryPath + "/" + oldName);
    			File newFile = new File(fileParentDirectoryPath + "/" + newName);
    			treeItem.setValue(new NodeItem(newFile, newName));
    			oldFile.renameTo(newFile);
    			
    			treeItem.getChildren().clear();//清空节点
        		refreshTreeItem(treeItem);
			}
    	}
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

	public TreeItem<NodeItem> getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem<NodeItem> treeItem) {
		this.treeItem = treeItem;
	}

	public TextField getTfOfurl() {
		return tfDirName;
	}

	public void setTfOfurl(TextField tfDirName) {
		this.tfDirName = tfDirName;
	}
}
