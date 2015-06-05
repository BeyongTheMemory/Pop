package com.pop.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pop.R;
import com.pop.downloadtest.AlertImageDialog;
import com.pop.downloadtest.HttpTools;
import com.pop.listview.MyAdapter;
import com.pop.listview.MyListView;
import com.pop.listview.MyListView.OnLoadMoreListener;
import com.pop.listview.MyListView.OnRefreshListener;






import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class getPopActivity extends Activity {

	//private Button downloadBtn;
	private ImageView imageView;
	private HttpTools httpTools;
	private TextView textView;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private MyListView myListView;
	private View headView;
	private LayoutInflater inflater;
	private MyAdapter myAdapter = null;
	private ImageView ivIcon;
	private TextView username;
	private TextView date;
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getpop_activity);
		SharedPreferences sharedPreferences = getSharedPreferences("pop", 
				Activity.MODE_PRIVATE); 
				id =sharedPreferences.getInt("id", 2);
				
		myListView = (MyListView) findViewById(R.id.myListView);
		inflater = LayoutInflater.from(getPopActivity.this);
		headView = inflater.inflate(R.layout.listhead, null);
		myListView.addHeaderView(headView);
		httpTools = new HttpTools();
		imageView = (ImageView) findViewById(R.id.image);
		textView = (TextView) findViewById(R.id.text);
		ivIcon = (ImageView) findViewById(R.id.ivicon);
		username = (TextView) findViewById(R.id.username);
		date = (TextView) findViewById(R.id.date);
		//downloadBtn = (Button) findViewById(R.id.download);
		loadInfo();
		loadMessage(0);
		myListView.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadMessage(0);
			}
			
		});
		myListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				loadMessage(1);
			}
			
		});
		//downloadBtn.setOnClickListener(new DownloadListener());
		imageView.setOnClickListener(new AlertImageListerner());
	}

	
	
//	private class DownloadListener implements OnClickListener{
//
//		@Override
//		public void onClick(View v) {
			// TODO Auto-generated method stub
//			new Thread(){
//				public void run(){
////					String url = "http://10.0.2.2:8080/PopService/GetThumbnailServlet";
////					List<NameValuePair> params = new ArrayList<NameValuePair>();
////					params.add(new BasicNameValuePair("type", "2"));
////					params.add(new BasicNameValuePair("id", "2"));
////					Bitmap bitmap = httpTools.downLoadPic(url, params);
////					System.out.println(bitmap);
////					if (bitmap != null){
////						Message msg = downloadHanlder.obtainMessage(1, bitmap);
////						downloadHanlder.sendMessage(msg);
////					} else{
////						Message msg = downloadHanlder.obtainMessage(0);
////						downloadHanlder.sendMessage(msg);
////					}
//					
//				}
//			}.start();
//			new GetContentTask().execute("2", "2");
//		}
//		
//	}
	
//	/**
//	 * 从Servlet中得到Bitmap
//	 * @param url
//	 * @return
//	 * @throws UnsupportedEncodingException 
//	 */
//	public Bitmap getImageFromUrl(String url, List<NameValuePair> params){
//		Bitmap bitmap = null;
//		//HttpGet httpRequest = new HttpGet(url);   
//		HttpPost httpRequest = new HttpPost(url);
//        //取得HttpClient 对象    
//		try {  
//		    httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//            HttpClient httpclient = new DefaultHttpClient();      
//            //请求httpClient ，取得HttpRestponse
//            HttpResponse httpResponse = httpclient.execute(httpRequest);    
//            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){    
//                //取得相关信息 取得HttpEntiy    
//                HttpEntity httpEntity = httpResponse.getEntity();    
//                InputStream is = httpEntity.getContent();                   
//                bitmap = BitmapFactory.decodeStream(is);    
//                is.close();     
//            }else{    
//                 Toast.makeText(MainActivity.this, "连接失败!", Toast.LENGTH_SHORT).show();        
//            }     
//                
//        } catch (ClientProtocolException e) {    
//            e.printStackTrace();    
//        } catch (IOException e) {     
//            e.printStackTrace();    
//        }      
//        return bitmap;
//	}
	
//	Handler downloadHanlder = new Handler(){
//		public void handleMessage(Message msg) {
//			System.out.println(msg.what);
//			switch (msg.what){
//			case 0:
//				Toast.makeText(MainActivity.this, "下载失败!", Toast.LENGTH_SHORT).show();
//				break;
//			case 1:
//				System.out.println("su");	
//				imageView.setImageBitmap((Bitmap) msg.obj);
//			}
//		}
//	};
	
	public void loadInfo(){
		//new GetContentTask().execute("2", "2");
		new GetContentTask().execute("2", id+"");
	}
	
	private  void loadMessage(final int type){
		switch (type) {
		case 0:
			//new RefreshTask().execute("0","10","2","2");
			new RefreshTask().execute("0","10","2",id+"");
			break;
			
		case 1:
			//new LoadMoreTask().execute(String.valueOf(list.size()),"10","2","2");
			new LoadMoreTask().execute(String.valueOf(list.size()),"10","2",id+"");
		default:
			break;
		}
		
	}
	private class AlertImageListerner implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub 
			imageView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			imageView.layout(0, 0, imageView.getMeasuredWidth(),imageView.getMeasuredHeight());
			imageView.buildDrawingCache();
			imageView.setDrawingCacheEnabled(true);
			System.out.print(imageView.getDrawingCache());
			Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
			imageView.setDrawingCacheEnabled(false);
			AlertImageDialog dialog = new AlertImageDialog(getPopActivity.this, bitmap);
			//dialog.show("2", "2");
			dialog.show("2", id+"");
		}
		
	}
	
	private class GetContentTask extends AsyncTask<String, Void, Map<String, Object>>{

		protected void onPreExecute(){
			super.onPreExecute();
		}
		
		String getInfoURL = "http://121.40.120.82:8080/PopService/GetInfoServlet";
		String getThumbnailURL = "http://121.40.120.82:8080/PopService/GetThumbnailServlet";
		
		@Override
		protected Map<String, Object> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type", arg0[0]));
			params.add(new BasicNameValuePair("id", arg0[1]));
			String info = httpTools.sendPostResult(getInfoURL, params);
			Map<String, Object> map = jsonToMap(info);
			Bitmap bitmap = null;
			if (map.get("picture_url") != null){
				bitmap = httpTools.downLoadPic(getThumbnailURL, params);
				
			}
			map.put("bitmap", bitmap);
			return map;
		}
		
		/*这个函数在doInBackground后发生
		 * 运行在ui线程中，可以更改ui界面
		 * 参数result是doInBackgroud返回的值
		 */
		protected void onPostExecute(Map<String, Object> map){
			System.out.println(map.get("info"));
			if (map.get("info") != null){
				textView.setText((String) map.get("info"));			
			}
			if (map.get("bitmap") != null){
				imageView.setImageBitmap((Bitmap) map.get("bitmap"));
			}
			username.setText((String)map.get("username"));
			date.setText((String)map.get("date"));
		}
		
		
	}
	
	public Map<String, Object> jsonToMap(String result){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JSONArray jsonArray = new JSONArray(result);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			map.put("date", jsonObject.getString("date"));
			map.put("info", jsonObject.getString("info"));
			map.put("username", jsonObject.getString("username"));
			map.put("picture_url", jsonObject.getString("picture_url"));						
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	public List<Map<String, String>> jsonToList(String result) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray messageArray = (JSONArray) jsonObject.get("message");
			JSONArray replyArray = (JSONArray) jsonObject.get("reply");
			// System.out.println(messageArray);
			for (int i = 0; i < messageArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject jb = messageArray.getJSONObject(i);
				// map.put("messageLid", jb.getString("LID"));
				map.put("message", jb.getString("words"));
				map.put("date", jb.getString("date"));
				map.put("username", jb.getString("username"));
				// System.out.println(map);
				for (int k = 0; k < replyArray.length(); k++) {
					JSONObject object = replyArray.getJSONObject(k);
					if (jb.getString("m_id").equals(object.getString("m_id"))) {
						// map.put("RID", object.getString("RID"));
						map.put("r_words", object.getString("r_words"));
						//map.put("reDate", object.getString("date"));
						// System.out.println(map);
					} else if (map.get("r_words") == null) {
						// map.put("RID", "");
						// map.put("r_words", "");
						// System.out.println(map);
					}
					// System.out.println(k);
				}
				// System.out.println(map);
				resultList.add(map);
				// System.out.println(dataList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	

	private class RefreshTask extends AsyncTask<String, Void, String>{

		String url = "http://121.40.120.82:8080/PopService/UpdateServlet";
		List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		String responseMsg = "";
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpParams.add(new BasicNameValuePair("m", params[0]));
			httpParams.add(new BasicNameValuePair("n", params[1]));
			httpParams.add(new BasicNameValuePair("type", params[2]));
			httpParams.add(new BasicNameValuePair("id", params[3]));
			responseMsg = httpTools.sendPostResult(url, httpParams);
			return responseMsg;
		}
		
		/*这个函数在doInBackground后发生
		 * 运行在ui线程中，可以更改ui界面
		 * 参数result是doInBackgroud返回的值
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(myAdapter == null && result != null){
				list = jsonToList(result);////////////////
				myAdapter = new MyAdapter(list, getPopActivity.this);
				//myListView.addHeaderView(headView);
				myListView.setAdapter(myAdapter);
			}else if(myAdapter != null && result != null){
				list = jsonToList(result);
				myAdapter.notifyDataSetChanged();
			}
			myListView.onRefreshComplete();
		}
		
		
	}
	
	private class LoadMoreTask extends AsyncTask<String, Void, String>{

		String url = "http://121.40.120.82:8080/PopService/UpdateServlet";
		List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		String responseMsg = "";
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpParams.add(new BasicNameValuePair("m", params[0]));
			httpParams.add(new BasicNameValuePair("n", params[1]));
			httpParams.add(new BasicNameValuePair("type", params[2]));
			httpParams.add(new BasicNameValuePair("id", params[3]));
			responseMsg = httpTools.sendPostResult(url, httpParams);
			return responseMsg;
		}
		
		/*这个函数在doInBackground后发生
		 * 运行在ui线程中，可以更改ui界面
		 * 参数result是doInBackgroud返回的值
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(myAdapter == null && result != null){
				list.addAll(jsonToList(result));////////////////
				myAdapter = new MyAdapter(list, getPopActivity.this);
				//myListView.addHeaderView(headView);
				myListView.setAdapter(myAdapter);
			}else if(myAdapter != null && result != null){
				list.addAll(jsonToList(result));
				myAdapter.notifyDataSetChanged();
			}
			myListView.onLoadMoreComplete();
		}
		
	}
}
