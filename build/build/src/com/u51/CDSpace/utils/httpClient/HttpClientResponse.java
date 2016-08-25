package com.u51.CDSpace.utils.httpClient;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class HttpClientResponse {
	private String stateCode;
	
	private String response;
	
	private String headers;

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}
}
