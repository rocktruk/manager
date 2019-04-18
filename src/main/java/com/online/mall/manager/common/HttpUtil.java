package com.online.mall.manager.common;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


public class HttpUtil {

	
    private static CloseableHttpClient httpClient;
    private static SSLContext       ctx             = null;
    private static SSLConnectionSocketFactory sslFactory   = null;
    
//  连接超时时间，默认5秒
    private static int connectTimeout = 2000;

    //传输超时时间，默认15秒
    private static int socketTimeout = 30000;
    
    private static int rquestConnctTimeout = 2000;
	
    private static RequestConfig requestConfig;
    private static class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }
    
    static{
    	try {
    		ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() },
                new SecureRandom());
            sslFactory = new SSLConnectionSocketFactory(
            		ctx,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			init();
		} catch (Exception e) {
//			log.error(e.getMessage(), e);
		}
    }
    
    
	private static void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
    	Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslFactory).build();
    	PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connManager.setDefaultSocketConfig(socketConfig);
		
		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(100).setMaxLineLength(2000).build();
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(100);
//		HttpHost proxy = new HttpHost(ConfigUtils.getProperty("proxy.host"),Integer.parseInt(ConfigUtils.getProperty("proxy.port")), "http");    
        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom()
        .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(rquestConnctTimeout).build();
        httpClient = HttpClients.custom().setConnectionManager(connManager)
                .build();
    }
	
	
	
	 public static String post(String url, String content) throws IOException
	 {
		 String result = "";
		 HttpPost httpPost = new HttpPost(url);
		 httpPost.addHeader("Accept", "application/x-www-form-urlencoded,application/json");
		 httpPost.addHeader("User-Agent", "pccc-pcs");
		 httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		 httpPost.setEntity(new StringEntity(content,"utf-8"));
		 httpPost.setConfig(requestConfig);
		 HttpEntity entity = null;
		 try {
			 HttpResponse response = httpClient.execute(httpPost);
			 entity = response.getEntity();
			 if(200!=response.getStatusLine().getStatusCode())
			 {
//				 log.error("response status error|"+response.getStatusLine().getStatusCode());
				 return result;
			 }
			 result = EntityUtils.toString(entity, "UTF-8");
		 } catch (Exception e) {
			 e.printStackTrace();
		 } finally {
			 httpPost.abort();
			 if(entity!=null)
			 {
				 EntityUtils.consume(entity);
			 }
		 }
		 return result;
	 }
	 
	 
}
