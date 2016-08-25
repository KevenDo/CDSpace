package com.u51.CDSpace.controller;

import com.u51.CDSpace.config.GlobalVariables;
import com.u51.CDSpace.view.BorderCenterView;
import com.u51.CDSpace.view.FileManagerView;
import com.u51.CDSpace.view.MenuBarAndToolBarView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class MainController extends Application{
	public static void main(String[] args) {
		launch(MainController.class, args);
	}
	
	@Override
	public void start(Stage stage) {
		BorderPane border = new BorderPane();
		String workspace = "./project";
		BorderCenterView borderCenterTabPaneView = new BorderCenterView();
		FileManagerView fileManagerView = new FileManagerView(workspace, border, borderCenterTabPaneView);
		MenuBarAndToolBarView toolBarView = new MenuBarAndToolBarView(fileManagerView);
		
		border.setTop(toolBarView.getVbox());
		border.setLeft(fileManagerView.getVbox());
		border.setCenter(borderCenterTabPaneView.initView());
		
		Scene scene = new Scene(border, 900, 650);
		stage.setScene(scene);
		stage.setTitle(GlobalVariables.CD_NAME + " " + GlobalVariables.CD_VERSION);
		stage.show();
	}
}
