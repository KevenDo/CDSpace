package com.u51.CDSpace.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class FileUtil {
	private static Scanner scanner;
	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static void createFileWithString(String content, String filePath){
		try {
			PrintWriter writer = new PrintWriter(new File(filePath), "UTF-8");
			writer.println(content);
			writer.close();
		} catch (FileNotFoundException e) {
			logger.error("File is not found. File path is \"" + filePath + "\".");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 创建空文件
	 * type为1，文件名为path
	 * type为2，文件名为(path+random(10000, 100000))
	 */
	public static void createEmptyFile(String path, int type) {
		try {
			String filePath = "";
			if (type == 1) {
				filePath = path;
			} else if (type == 2) {
				filePath = path + StringUtil.randInt(10000, 100000);
				while (new File(filePath).exists()) {
					filePath = path + StringUtil.randInt(10000, 100000);
				}
			}
			PrintWriter writer = new PrintWriter(new File(filePath));
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not Find!");
			e.printStackTrace();
		}
	}
	
	public static void createEmptyDir(String path, int type) {
		String filePath = "";
		if (type == 1) {
			filePath = path;
		} else if (type == 2) {
			filePath = path + StringUtil.randInt(10000, 100000);
			while (new File(filePath).exists()) {
				filePath = path + StringUtil.randInt(10000, 100000);
			}
		}
		new File(filePath).mkdir();
	}
	
	public static String readFile(String filePath) {
	    String content = null;
	    File file = new File(filePath);
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader !=null){try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
	    }
	    return content;
	}
	
	public static String readFirstLineOfFile(String filePath) {
		String responseCode = "";
		try {
			scanner = new Scanner(new File(filePath));
			while(scanner.hasNextLine()){
				responseCode = scanner.nextLine();
		        break;
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    return responseCode;
	}
	
	public static String readFileWithoutFirstLine(String filePath) {
		ArrayList<String> content = FileUtil.readConfigFile(filePath);
		String responseBody = "";
		for (int i = 1; i < content.size(); i++) {
			responseBody += content.get(i);
			if (i != (content.size()-1)) {
				responseBody += "\n";
			}
		}
		return responseBody;
	}
	
	public static ArrayList<String> readConfigFile(String filePath) {
		ArrayList<String> content = new ArrayList<String>();
		try {
			scanner = new Scanner(new File(filePath));
			while(scanner.hasNextLine()){
		        String line = scanner.nextLine();
		        content.add(line);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    return content;
	}
	
	/*
	 * 根据文件的后缀名判断文件类型
	 * .server返回2
	 * .client返回1
	 * 其他返回0
	 */
	public static int getFileType(String fileName) {
		int fileType = 0;
		String[] names = fileName.split("\\.");
		String fileTypeName = names[names.length - 1];
		if (fileTypeName.equals("client")) {
			fileType = 1;
		} else if (fileTypeName.equals("server")) {
			fileType = 2;
		}
		return fileType;
	}
	
	public static String getFilePreName(String fileName) {
		String filePreName = "";
		String[] names = fileName.split("\\.");
		for (int i = 0; i < names.length-1; i++) {
			if (i == names.length-2 ) {
				filePreName += names[i];
				break;
			}
			filePreName += names[i] + ".";
		}
		
		return filePreName;
	}
	
	public static void main(String[] args) {
//		System.out.println(FileUtils.readFile("/Users/duliangliang/Desktop/project/99 temp/test.txt"));
//		ArrayList<String> content = FileUtils.readConfigFile("/Users/duliangliang/Downloads/HttpMockServerTool.config");
//		for (int i = 0; i < content.size(); i++) {
//			System.out.println(content.get(i));
//			String[] urlAndPath = content.get(i).split(",");
//			System.out.println(urlAndPath[0]);
//			System.out.println(urlAndPath[1]);
//		}
//		System.out.println(FileUtils.readFirstLineOfFile("/Users/duliangliang/Downloads/HttpMockServerTool.config"));
//		System.out.println(FileUtil.readFileWithoutFirstLine("/Users/duliangliang/Downloads/1.txt"));
		
//		File file = new File("/Users/duliangliang/Downloads/1.txt");
//		FileUtil.createEmptyFile("/Users/duliangliang/Downloads/2.txt", 2);
//		FileUtil.createEmptyDir("/Users/duliangliang/Downloads/2");
//		FileUtil.createFileWithString("11111", "/Users/duliangliang/Downloads/1.txt");
		System.out.println(FileUtil.getFilePreName("test.server.1.client"));
	}
}
