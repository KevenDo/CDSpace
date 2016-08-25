package com.u51.CDSpace.utils.httpServer;

import com.sun.net.httpserver.HttpServer;   
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import java.io.FileInputStream;
import java.io.IOException;   
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;   

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
@SuppressWarnings("restriction")
public class HttpMockServer {
	private final static String pathOfkeyFile = "testkey.jks";
    private HttpServer server;
    
    public void initServer(int port) {
    	InetSocketAddress addr = new InetSocketAddress(port);//监听端口
    	try {
			server = HttpServer.create(addr, 0);//创建HttpServer
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void initServer(int port, String protocolType) {
    	InetSocketAddress addr = new InetSocketAddress(port);//监听端口
    	try {
			server = HttpsServer.create(addr, 0);//创建HttpServer
			SSLContext sslContext = SSLContext.getInstance(protocolType);

            // initialise the keystore
            char[] password = "password".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(pathOfkeyFile);
            ks.load(fis, password);

            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            // setup the HTTPS context and parameters
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            ((HttpsServer) server).setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {
                        // initialise the SSL context
                        SSLContext c = SSLContext.getDefault();
                        SSLEngine engine = c.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());

                        // get the default parameters
                        SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                        params.setSSLParameters(defaultSSLParameters);

                    } catch (Exception ex) {
                        System.out.println("Failed to create HTTPS port");
                    }
                }
            });
		} catch (Exception exception) {
			System.out.println("Failed to create HTTPS server on port " + port + " of localhost");
            exception.printStackTrace();
		}
    }
    
    public void createContext(HttpContext httpContext) {
    	HttpHandle httpHandle = new HttpHandle(httpContext);
    	server.createContext(httpContext.getUrl(), httpHandle);//创建URL上下文
    }
    
    public void startServer() {
    	server.setExecutor(Executors.newCachedThreadPool());//初始化连接池
        server.start(); //启动HttpServer
    }
    
    public void stop() {
    	if (null != server) {
    		server.stop(0);//停止HttpServer
    	}
    }
} 
