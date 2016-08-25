package com.u51.CDSpace.view;

import java.io.File;

import com.u51.CDSpace.controller.FMContextMenuController;
import com.u51.CDSpace.model.NodeItem;
import com.u51.CDSpace.utils.FileUtil;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.apache.log4j.Logger;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class FileManagerView{
	private static Logger logger = Logger.getLogger(FileManagerView.class);
	private final Image projectIcon = new Image(getClass().getResourceAsStream("/image/iconfont-project.png"));
	private final Image dirIcon = new Image(getClass().getResourceAsStream("/image/iconfont-dir.png"));
	private final Image clientIcon = new Image(getClass().getResourceAsStream("/image/iconfont-client.png"));
	private final Image serverIcon = new Image(getClass().getResourceAsStream("/image/iconfont-server.png"));

	private String filePath;
	
	private MenuBarAndToolBarView toolBarView;
	
	private BorderCenterView borderCenterTabPaneView;
	
	private TreeView<NodeItem> treeView;
	
	private VBox vbox;
	
	private BorderPane border;
    
    public FileManagerView(String filePath, BorderPane border, BorderCenterView borderCenterTabPaneView){
    	TreeView<NodeItem> treeView = buildFileSystemBrowser(filePath, this);
    	this.border = border;
    	this.treeView = treeView;
    	this.borderCenterTabPaneView = borderCenterTabPaneView;
    	this.vbox = new VBox();
    	
		vbox.setPadding(new Insets(10, 5, 10, 10));
		vbox.setPrefWidth(300);
		VBox.setVgrow(treeView, Priority.ALWAYS);
	    vbox.getChildren().addAll(treeView);
    }
	
	private TreeView<NodeItem> buildFileSystemBrowser(String filePath, FileManagerView fileManagerView) {
		if (!new File(filePath).exists()) {
			FileUtil.createEmptyDir(filePath, 1);
		}
	    TreeItem<NodeItem> root = new TreeItem<NodeItem>(new NodeItem(new File(filePath), "project"));
	    initProjectItems(root);
	    ObservableList<TreeItem<NodeItem>> children = root.getChildren();
		for (int i = 0; i < children.size(); i++) {
			TreeItem<NodeItem> projectItem = children.get(i);
			initTreeItems(projectItem);
		}
	    TreeView<NodeItem> treeView = new TreeView<>(root);
	    treeView.setShowRoot(false);//隐藏root节点，以第二节点作为project
	    FMContextMenuController contextMenu = new FMContextMenuController(treeView);
	    contextMenu.setFileManagerView(fileManagerView);
	    treeView.setContextMenu(contextMenu.getContextMenu());
	    treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	@Override
            public void handle(MouseEvent mouseEvent){            
                if (mouseEvent.getClickCount() == 1){
                	setCenterBorderAfterSelectTreeItem();
                }
            }
        });
	    return treeView;
	}
	
	private void initProjectItems(TreeItem<NodeItem> root) {
		for (File file : root.getValue().getFile().listFiles()) {
			if (file.isDirectory()) {
				TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(projectIcon));
				root.getChildren().add(newTreeItem);
			}
		}
	}
	
	private void initTreeItems(TreeItem<NodeItem> item) {
		File[] files = item.getValue().getFile().listFiles();
		for (File file : files) {
			if(file.isDirectory()) {
				TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(dirIcon));
				item.getChildren().add(newTreeItem);
				logger.info("Init tree file is" + newTreeItem.getValue().getFile().getPath().toString());
				initTreeItems(newTreeItem);
			} else {
				if ( FileUtil.getFileType(file.getName()) == 1) {
					TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(clientIcon));
					item.getChildren().add(newTreeItem);
				} else if ( FileUtil.getFileType(file.getName()) == 2) {
					TreeItem<NodeItem> newTreeItem = new TreeItem<NodeItem>(new NodeItem(file, file.getName()), new ImageView(serverIcon));
					item.getChildren().add(newTreeItem);
				}
			}
		}
	}
	
	public void setCenterBorderAfterSelectTreeItem() {
		TreeItem<NodeItem> selectedTreeItem = getTreeView().getSelectionModel().getSelectedItem();//获取被选定的节点
		if (selectedTreeItem != null) {
			borderCenterTabPaneView.loadTab(selectedTreeItem, 1);
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public TreeView<NodeItem> getTreeView() {
		return treeView;
	}

	public void setTreeView(TreeView<NodeItem> treeView) {
		this.treeView = treeView;
	}

	public VBox getVbox() {
		return vbox;
	}

	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}

	public BorderPane getBorder() {
		return border;
	}

	public void setBorder(BorderPane border) {
		this.border = border;
	}

	public BorderCenterView getBorderCenterTabPaneView() {
		return borderCenterTabPaneView;
	}

	public void setBorderCenterTabPaneView(BorderCenterView borderCenterTabPaneView) {
		this.borderCenterTabPaneView = borderCenterTabPaneView;
	}

	public MenuBarAndToolBarView getToolBarView() {
		return toolBarView;
	}

	public void setToolBarView(MenuBarAndToolBarView toolBarView) {
		this.toolBarView = toolBarView;
	}
}
