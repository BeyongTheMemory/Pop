package com.example.craise.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.example.craise.common.NetRequestConstant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	
	    //鐢╣et鏂瑰紡璇锋眰缃戠粶锛岃繑鍥炲搷搴旂殑缁撴灉
		public static String httpGet(NetRequestConstant nrc) {
			String result=null;
			try {
				//寤虹珛HttpGet瀵硅薄
				HttpGet httpRequest=new HttpGet(NetRequestConstant.requestUrl);
				
				//鍒涘缓HttpParams瀵硅薄锛岀敤鏉ヨ缃瓾TTP鍙傛暟
				HttpParams httpParams=new BasicHttpParams();
				
				//鍒涘缓涓�釜HttpClient瀹炰緥
				HttpClient httpClient=new DefaultHttpClient(httpParams);
				
				//鍙戦�璇锋眰骞剁瓑寰�
				HttpResponse httpResponse=httpClient.execute(httpRequest);
				
				//濡傛灉鐘舵�鐮佷负200灏監K浜�
				if(httpResponse.getStatusLine().getStatusCode()==200){
					//璇诲搷搴旀暟鎹�
					 result=EntityUtils.toString(httpResponse.getEntity());
				}else{
					 result="璇锋眰閿欒"+httpResponse.getStatusLine().toString();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		//post璇锋眰鏂瑰紡
		public static String httpPost(NetRequestConstant nrc){
			String result=null;
			try {
				//鍒涘缓HttpParams瀵硅薄锛岀敤鏉ヨ缃瓾TTP鍙傛暟
				HttpParams httpParams=new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				
				//鍒涘缓涓�釜HttpClient瀹炰緥
				HttpClient httpClient=new DefaultHttpClient(httpParams);
				
				//寤虹珛HttpPost瀵硅薄
				HttpPost httpRequest=new HttpPost(NetRequestConstant.requestUrl);
				
				//鍙戦�璇锋眰鐨勫弬鏁�
	            Map<String, Object> map = NetRequestConstant.map;
				
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				
				for(Map.Entry<String, Object> entry : map.entrySet()){
					NameValuePair pair = new BasicNameValuePair(entry.getKey(), (String) entry.getValue());
					parameters.add(pair);
				}
				
				//娣诲姞璇锋眰鍙傛暟鍒拌姹傚璞�
				httpRequest.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
				
				//鍙戦�璇锋眰骞剁瓑寰呭搷搴�
				HttpResponse httpResponse=httpClient.execute(httpRequest);
				
				//Logger.e( "NetUtil Code 锛� + NetRequestConstant.requestUrl);
				//鐘舵�鐮佷负200灏辫姹傛垚鍔熶簡
				if(httpResponse.getStatusLine().getStatusCode()==200){
					 //璇诲搷搴旀暟鎹�
					 result=EntityUtils.toString(httpResponse.getEntity());
				}else{
					 result="璇锋眰閿欒"+httpResponse.getStatusLine().toString();
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		
		public static boolean isCheckNet(Context context){
			 ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			 NetworkInfo info=cm.getActiveNetworkInfo();
			 if(info==null){
				 //娌℃湁鑱旂綉
				 return false;	
			 }else{
				 //鑱旂綉绫诲瀷
				//String type=info.getTypeName();
			    return true;	
			 } 
		 }
		
}
