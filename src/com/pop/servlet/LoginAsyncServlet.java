/*
 * @auther:xg
 * 5.31.
 * ��½��֤
 */

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

import com.pop.R;
import com.pop.activity.LoginActivity;
import com.pop.activity.MainActivity;
import com.pop.activity.RegistActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class LoginAsyncServlet extends AsyncTask<String, Void,String>{
	private String username;
	private String password;
	private LoginActivity loginActivity;
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;
	JSONArray jArray;
	TextView informationText;
	  private ProgressDialog progressDialog = null; //������
	public LoginAsyncServlet(){
		super();
	}
	public LoginAsyncServlet(String username,String password,TextView informationText,LoginActivity loginActivity){
		super();
		this.username = username;
		this.password = password;
		this.informationText = informationText;
		this.loginActivity = loginActivity;
	}
	
	protected void onPreExecute(){
		super.onPreExecute();
		progressDialog = ProgressDialog.show(loginActivity, "���Ե�...", "��½��...", true);
	}
	
	protected String doInBackground(String...params){
		ArrayList nameValuePairs = new ArrayList();
		//���ݲ���
		
		//Log.v("username", username);
		//Log.v("password", password);
		
	    nameValuePairs.add(new BasicNameValuePair("username", username));
	    nameValuePairs.add(new BasicNameValuePair("password", password));

	    // http post
	    try {
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(
	        		params[0]);//params[0]�Ǵ�����url;
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	        } catch (Exception e) {
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
	
	/*���������doInBackground����
	 * ������ui�߳��У����Ը���ui����
	 * ����result��doInBackgroud���ص�ֵ
	 */
	protected void onPostExecute(String result){
		String ct_username;
	    String ct_password;
	    try {
	    	jArray = new JSONArray(result);
	    	JSONObject json_data = null;
	        for (int i = 0; i < jArray.length(); i++) {
	    	    json_data = jArray.getJSONObject(i);
	    		ct_username = json_data.getString("username");
	    		ct_password = json_data.getString("password");
	    	}
	        //��½�ɹ�ҳ����ת
	        informationText.setText("�ɹ���");
	        progressDialog.dismiss();
	        Intent intent = new Intent();
		     intent.setClass(loginActivity,MainActivity.class);
		    loginActivity.startActivity(intent);
		    loginActivity.finish();
	        
	    } catch (JSONException e1) {
               //tv.append("No result" + "\n");
	    	informationText.setText("�û������������");
	    	 progressDialog.dismiss();
	    } catch (ParseException e1) {
	    	   e1.printStackTrace();
	    }
	}
}