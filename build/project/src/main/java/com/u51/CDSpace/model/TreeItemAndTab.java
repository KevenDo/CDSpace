package com.u51.CDSpace.model;

import com.u51.CDSpace.view.DirPreviewView;
import com.u51.CDSpace.view.HttpClientEditorView;
import com.u51.CDSpace.view.HttpServerEditorView;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class TreeItemAndTab {
	private TreeItem<NodeItem> treeItem;
	
	private Tab tab;
	
	private HttpClientEditorView httpClientEditorView;
	
	private HttpServerEditorView httpServerEditorView;
	
	private DirPreviewView dirPreviewView;
	
	public TreeItemAndTab() {
		
	}
	
	public TreeItemAndTab(TreeItem<NodeItem> treeItem, Tab tab, HttpClientEditorView httpClientEditorView, HttpServerEditorView httpServerEditorView
			, DirPreviewView dirPreviewView) {
		this.treeItem = treeItem;
		this.tab = tab;
		this.httpClientEditorView = httpClientEditorView;
		this.httpServerEditorView = httpServerEditorView;
		this.dirPreviewView = dirPreviewView;
	}

	public TreeItem<NodeItem> getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem<NodeItem> treeItem) {
		this.treeItem = treeItem;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public HttpClientEditorView getHttpClientEditorView() {
		return httpClientEditorView;
	}

	public void setHttpClientEditorView(HttpClientEditorView httpClientEditorView) {
		this.httpClientEditorView = httpClientEditorView;
	}

	public HttpServerEditorView getHttpServerEditorView() {
		return httpServerEditorView;
	}

	public void setHttpServerEditorView(HttpServerEditorView httpServerEditorView) {
		this.httpServerEditorView = httpServerEditorView;
	}

	public DirPreviewView getDirPreviewView() {
		return dirPreviewView;
	}

	public void setDirPreviewView(DirPreviewView dirPreviewView) {
		this.dirPreviewView = dirPreviewView;
	}
}
