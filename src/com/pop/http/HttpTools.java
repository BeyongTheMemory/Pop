package com.pop.http;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings;
import android.util.Log;

public class HttpTools {

	private HttpGet httpGet = null;
	private HttpPost httpPost = null;
	private HttpResponse response = null;
	private HttpEntity httpEntity = null;
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟

	public HttpTools() {

	}

	public String sendGetResult(String url) {
		httpGet = new HttpGet(url);
		String result = null;
		try {
			// request.setEntity(new UrlEncodedFormEntity(null,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// 执行请求返回相应
			HttpResponse response = client.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == 200) {
				// 获得响应信息
				result = EntityUtils.toString(response.getEntity());
				Log.v("SUC", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String sendPostResult(String url, List<NameValuePair> params) {
		httpPost = new HttpPost(url);
		String result = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient httpClient = getHttpClient();
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("httperror");
		}
		return result;

	}

	// 初始化HttpClient，并设置超时
	private HttpClient getHttpClient() {
		// TODO Auto-generated method stub
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

}
