package com.pop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;



import android.view.WindowManager;
import android.widget.ImageView;

import com.pop.R;

public class NewPopActivity extends Activity{
	private ImageView closeImage = null;
	private ImageView imagepop_imageView =null;
	private ImageView wordpop_image =null;
	private ImageView voicepop_image =null;
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
        // 去标题栏
       window.requestFeature(window.FEATURE_NO_TITLE);
       //后方模糊
      window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,   
    		  WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  

       //设置透明度
       WindowManager.LayoutParams lp = window.getAttributes(); 
       lp.alpha = 0.9f; 
       window.setAttributes(lp);  


		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.newpop_activity);
		//返回
		closeImage =(ImageView)findViewById(R.id.close_imageView);
		closeImage.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(NewPopActivity.this,MainActivity.class);					
						NewPopActivity.this.startActivity(intent);
						NewPopActivity.this.finish();
						
					}
					
				});
		//图文泡泡
		imagepop_imageView = (ImageView)findViewById(R.id.imagepop_imageView);
		imagepop_imageView.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NewPopActivity.this,NewPop_photo_Activity.class);
				NewPopActivity.this.startActivity(intent);
				
				
			}
			
		});
		
		wordpop_image = (ImageView)findViewById(R.id.wordpop_image);
		wordpop_image.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NewPopActivity.this,NewPop_words_Activity.class);
				NewPopActivity.this.startActivity(intent);
				
				
			}
			
		});
		
		voicepop_image = (ImageView)findViewById(R.id.voicepop_image);
		voicepop_image.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NewPopActivity.this,NewPop_yuyin_Activity.class);
				NewPopActivity.this.startActivity(intent);
				
				
			}
			
		});
	}
}
