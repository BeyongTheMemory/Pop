package com.pop.activity;

import java.util.HashMap;
import java.util.Map;

import com.pop.R;
import com.pop.http.Communication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 新文本泡泡界面
 * @author xg
 *6.7
 */
public class NewPop_words_Activity extends Activity{
	private TextView cancelText = null;
	//private ImageView addImageView;
	private static int RESULT_LOAD_IMAGE = 1;
	//private String imageURL;
	
	private EditText editText;
	private TextView writePrivacy;
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
        // 去标题栏
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.newpop_words_activity);
		//返回
				cancelText =(TextView)findViewById(R.id.cancel_text);
				cancelText.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						NewPop_words_Activity.this.finish();
						
					}
					
				});
				editText = (EditText) findViewById(R.id.words_edit);
				writePrivacy = (TextView) findViewById(R.id.writePrivacy_text);
				//selectBtn = (Button) findViewById(R.id.select);
				//uploadBtn = (Button) findViewById(R.id.upload);
				//addImageView = (ImageView) findViewById(R.id.imgView);
				//addImageView = (ImageView) findViewById(R.id.appPhpto_imageView);
				//addImageView.setOnClickListener(new SelectListener());
				writePrivacy.setOnClickListener(new UploadListener());
				//selectBtn.setOnClickListener(new SelectListener());
				//uploadBtn.setOnClickListener(new UploadListener());
			}
			
			private class SelectListener implements OnClickListener{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
				}
				
			}
			
			protected void onActivityResult(int requestCode, int resultCode, Intent data){
				super.onActivityResult(requestCode, resultCode, data);
				if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
		            Uri selectedImage = data.getData();
		            String[] filePathColumn = { MediaStore.Images.Media.DATA };
		  
		            Cursor cursor = getContentResolver().query(selectedImage,
		                    filePathColumn, null, null, null);
		            cursor.moveToFirst();
		  
		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String picturePath = cursor.getString(columnIndex);
		           // imageURL = picturePath;
		            //System.out.println(imageURL);
		            cursor.close();
		  
		            //addImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
				}
			}
			
			private class UploadListener implements OnClickListener{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final Communication com = new Communication();
					final Map<String, Object> map = new HashMap<String, Object>();
					SharedPreferences sharedPreferences = getSharedPreferences("loacation", 
							Activity.MODE_PRIVATE); 
							double latitude = Double.parseDouble(sharedPreferences.getString("latitude","")); 
							double longitude =  Double.parseDouble(sharedPreferences.getString("longitude","")); 
							double altitude =  Double.parseDouble(sharedPreferences.getString("altitude","")); 
							Log.v("zuihi","zuihi:" + latitude + "," + longitude + "," +  altitude + ",");
						
					map.put("username", "a");
					map.put("latitude", latitude);
					map.put("logitude", longitude);
					map.put("height", altitude); 
					map.put("info", editText.getText().toString());
					final String url = "http://121.40.120.82:8080/PopService/UploadPictureServlet";
					//final String imageName = "tupian";
					new Thread(){
						public void run() {
							String result = com.communication02(url, map, null, null);
							System.out.println(result);
							
							//Message msg = handler.obtainMessage();
						}
					}.start();		
					NewPop_words_Activity.this.finish();
				}
				
			}

//			@Override
//			public boolean onCreateOptionsMenu(Menu menu) {
//				// Inflate the menu; this adds items to the action bar if it is present.
//				getMenuInflater().inflate(R.menu.main, menu);
//				return true;
//			}

		

	}


