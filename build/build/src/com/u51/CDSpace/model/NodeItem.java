package com.u51.CDSpace.model;

import java.io.File;

import org.apache.log4j.Logger;

import com.u51.CDSpace.utils.FileUtil;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class NodeItem {
	private static Logger logger = Logger.getLogger(NodeItem.class);
	private File file;
	
	private String fileName;
	
	public NodeItem(File file, String fileName) {
		this.file = file;
		this.fileName = fileName;
	}
	
	public String toString() {
		if (getFile().isFile()) {
			String[] names = getFileName().split("\\.");
			String fileName = "";
			for (int i = 0; i < names.length - 1; i++) {
				fileName += names[i];
			}
			return fileName;
		} else {
			logger.debug("set tree name " + getFile().getPath().toString());
			return getFileName();
		}
	}
	
	public String setTabName() {
		String tabName = "";
		if (FileUtil.getFileType(fileName) == 1) {
			tabName = "C-" + toString();
		} else if (FileUtil.getFileType(fileName) == 2) {
			tabName = "S-" + toString();
		} else {
			tabName = toString();
		}
		
		return tabName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
