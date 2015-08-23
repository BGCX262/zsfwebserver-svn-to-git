package com.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;


public class HttpUtil {
	private static Log log = LogFactory.getLog(HttpUtil.class);
	private static final ExecutorService THREADPOOL = Executors.newFixedThreadPool(10);
	
	public static void request(final URI uri) throws ClientProtocolException, IOException {
		THREADPOOL.execute(new Runnable(){
			@Override
			public void run() {
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(uri);
				setParams(httpclient);
				HttpResponse response;
				try {
					response = httpclient.execute(httpget);
					response(uri.toString(), response);
				}  catch (Exception e) {
					log.error(e, e);
				}
			}
		});
	}

	private static void setParams(HttpClient httpclient) {
		HttpParams params = httpclient.getParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
	}

	public static String request(String url) throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		setParams(httpclient);
		HttpResponse response = httpclient.execute(httpget);
		return response(url, response);
	}

	private static String response(String uri, HttpResponse response) throws IOException,
			UnsupportedEncodingException {
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			StringBuffer contextBuffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),
					"utf-8"));
			String temp;
			while ((temp = reader.readLine()) != null) {
				contextBuffer.append(temp);
			}
			log.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -" + "\n" + uri
					+ "\n" + contextBuffer.toString());
			log.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
//			String content = URLDecoder.decode(contextBuffer.toString(), ENCODE);
			return contextBuffer.toString();
		}
		return null;
	}
}
