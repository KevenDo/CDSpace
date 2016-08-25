package com.u51.CDSpace.utils.httpServer;

import java.util.HashMap;

import javafx.scene.control.TextArea;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class HttpContext {
	private String actualRequest;
	
	private String expectResponse;
	
	private String requestParams;
	
	private String url;

	private int resultCode;
	
	private HashMap<String, String> headers;
	
	private String responseBody;
	
	private TextArea areaOfConsole;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url.substring(0, 1).equals("/")){
			this.url = url;
		} else {
			this.url = "/" + url;
		}
	}

	public TextArea getAreaOfConsole() {
		return areaOfConsole;
	}

	public void setAreaOfConsole(TextArea areaOfConsole) {
		this.areaOfConsole = areaOfConsole;
	}

	public String getActualRequest() {
		return actualRequest;
	}

	public void setActualRequest(String actualRequest) {
		this.actualRequest = actualRequest;
	}

	public String getExpectResponse() {
		return expectResponse;
	}

	public void setExpectResponse(String expectResponse) {
		this.expectResponse = expectResponse;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}
}
