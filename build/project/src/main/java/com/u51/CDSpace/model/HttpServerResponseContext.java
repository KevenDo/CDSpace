package com.u51.CDSpace.model;

import java.util.HashMap;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class HttpServerResponseContext {
	private String contextName = "";
	
	private String url = "";
	
	private String responseCode = "";
	
	private String responseBody = "";
	
	private HashMap<String, String> headers;
	
	public HttpServerResponseContext(String contextName, String url, String responseCode, String responseBody, HashMap<String, String> headers) {
		this.contextName = contextName;
		this.url = url;
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		this.headers = headers;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}
}
