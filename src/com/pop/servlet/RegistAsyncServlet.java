package com.pop.servlet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class RegistAsyncServlet extends AsyncTask<String, Void,String>{
	private String username;
	private String password;
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;
	JSONArray jArray;
	TextView informationText;
	
	public RegistAsyncServlet(){
		super();
	}
	public RegistAsyncServlet(String username,String password,TextView informationText){
		super();
		this.username = username;
		this.password = password;
		this.informationText = informationText;
		Log.v("x","x");
	}
	
	protected void onPreExecute(){
		super.onPreExecute();
		Log.v("xx","xx");
	}
	@Override
	protected String doInBackground(String... params) {
		Log.v("xxx","xxx");
		 
		//传递参数
		ArrayList nameValuePairs = new ArrayList();
		 nameValuePairs.add(new BasicNameValuePair("username", username));
		 nameValuePairs.add(new BasicNameValuePair("password", password));
		 // http post
		try {
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(
	        		params[0]);//params[0]是传进的url;
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	        } 
		    catch (Exception e) {
	        	Log.e("log_tag", "Error in http connection" + e.toString());
	        	}
		  // convert response to string
	    try {
	    	BufferedReader reader = new BufferedReader(
	    			new InputStreamReader(is, "iso-8859-1"), 8);
	    	sb = new StringBuilder();
	    	sb.append(reader.readLine() + "\n");
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	        	sb.append(line + "\n");
	        	}
	        is.close();
	        result = sb.toString();
	    } catch (Exception e) {
	        Log.e("log_tag", "Error converting result " + e.toString());
	    }
	    return result;
	}
	
	/*这个函数在doInBackground后发生
	 * 运行在ui线程中，可以更改ui界面
	 * 参数result是doInBackgroud返回的值
	 */
	protected void onPostExecute(String result){
		/*Log.v("xxxz","xxxx");
		String ct_username = "";
	    String ct_password = "";
	    try {
	    	Log.v("result",result);
	    	jArray = new JSONArray(result);
	    	Log.v("length",jArray.length()+"");
	    	JSONObject json_data = null;
	        for (int i = 0; i < jArray.length(); i++) {
	    	    json_data = jArray.getJSONObject(i);
	    		ct_username = json_data.getString("username");
	    		Log.v("c_ct_username",ct_username);
	    		ct_password = json_data.getString("password");
	    	}*/
		//Log.v("result",result);
	          //注册失败用户名存在
	        if(Integer.parseInt(result.trim()) == 0){
	        	informationText.setText("注册失败，用户名已存在！");
	        }
	        //注册成功
	        else{
	        	informationText.setText("注册成功！");
	        }
	    }
	/*catch (JSONException e1) {
            //tv.append("No result" + "\n");
	    	//informationText.setText("");
	    } catch (ParseException e1) {
	    	   e1.printStackTrace();
	    }*/
	}
	


