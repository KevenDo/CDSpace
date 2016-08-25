package com.dull.CDSpace.utils.httpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
@SuppressWarnings("restriction")
public class HttpHandle implements HttpHandler{
	private HttpContext httpContext;
	
	public HttpContext gethttpContext() {
		return httpContext;
	}

	public void sethttpContext(HttpContext httpContext) {
		this.httpContext = httpContext;
	}
	
	public HttpHandle(HttpContext httpContext) {
		this.sethttpContext(httpContext);
	}

	public void handle(HttpExchange exchange) throws IOException {
		httpContext.getAreaOfConsole().appendText(exchange.getRequestMethod() + " " + URLDecoder.decode(exchange.getRequestURI().toString(), "UTF-8") + "\n");//控制台打印HTTP方法和URL
		Headers requestHeaders = exchange.getRequestHeaders();
		Set<String> keySet = requestHeaders.keySet();
	    Iterator<String> iterOfRequestHeaders = keySet.iterator();
	    while (iterOfRequestHeaders.hasNext()) {
	        String key = iterOfRequestHeaders.next();
	        List<String> values = requestHeaders.get(key);
	        String s = key + " = " + values.toString() + "\n";
	        httpContext.getAreaOfConsole().appendText(s);
	    }
	    
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equals("POST") || requestMethod.equals("PUT")){
			httpContext.setActualRequest(fromStream(exchange.getRequestBody()).trim());
			httpContext.getAreaOfConsole().appendText("\n" + httpContext.getActualRequest());//控制台打印POST请求消息体
//		} else {
//			httpContext.setActualRequest(exchange.getRequestURI().toString().split("[?]")[1]);
//			httpContext.setExpectResponse(httpContext.getRequestParams());
		}
		httpContext.getAreaOfConsole().appendText("\n"+"\n");
//		System.out.printf("actualParams is:%s\r\nexpectParams is:%s\r\n", httpContext.getActualRequest(), httpContext.getExpectResponse());
		
		
		Headers responseHeaders = exchange.getResponseHeaders();
		if (null != this.httpContext.getHeaders() && !this.httpContext.getHeaders().isEmpty()) {
			Iterator<Entry<String, String>> iter = this.httpContext.getHeaders().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Entry<String, String>) iter.next();
				responseHeaders.set(entry.getKey(), entry.getValue());
			}
		}
		
        exchange.sendResponseHeaders(httpContext.getResultCode(), 0);//设置响应消息的返回码
        OutputStream responseBody = exchange.getResponseBody();//初始化响应消息的byte流
        String responseString = httpContext.getResponseBody();
        responseBody.write(responseString.getBytes());//写入响应消息的byte流
         
        responseBody.close();//关闭流
    }
	
	private String fromStream(InputStream in) throws IOException
	{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    StringBuilder out = new StringBuilder();
	    String newLine = System.getProperty("line.separator");
	    String line;
	    while ((line = reader.readLine()) != null) {
	        out.append(line);
	        out.append(newLine);
	    }
	    return out.toString();
	}
}
