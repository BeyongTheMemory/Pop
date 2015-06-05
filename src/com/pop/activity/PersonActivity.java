package com.pop.activity;


import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 个人信息界面
 * @author xg
 *6.7
 */
public class PersonActivity extends Activity{
	private TextView backText = null;
	private TextView optionText = null;
	private RelativeLayout personLayout = null;
	private RelativeLayout myPhotoLayout = null;
	private RelativeLayout myCollectLayout = null;
	private RelativeLayout goodPopLayout = null;
	private RelativeLayout individuationLayout = null;
	
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
        // 去标题栏
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.person_activity);
		//返回
				backText =(TextView)findViewById(R.id.back_text);
				backText.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(PersonActivity.this,MainActivity.class);					
						PersonActivity.this.startActivity(intent);
						PersonActivity.this.finish();
						
					}
					
				});
		//设置
		      optionText = (TextView)findViewById(R.id.option_text);
		      optionText.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(PersonActivity.this,OptionActivity.class);
						PersonActivity.this.startActivity(intent);
						
					}
					
				});
		      personLayout = (RelativeLayout)findViewById(R.id.personLayout);
		      personLayout.setOnClickListener(new OnClickListener(){
		  	
		  		public void onClick(View v) {
		  			  Intent intent = new Intent();
		     			     intent.setClass(PersonActivity.this,AccoutActivity.class);
		     			     PersonActivity.this.startActivity(intent);
		  		}
		  		
		  	});
		      
		      myPhotoLayout = (RelativeLayout)findViewById(R.id.myPhotoLayout);
		      myPhotoLayout.setOnClickListener(new OnClickListener(){
		  	
		  		public void onClick(View v) {
		  			  Intent intent = new Intent();
		     			     intent.setClass(PersonActivity.this,MyPhotoActivity.class);
		     			     PersonActivity.this.startActivity(intent);
		  		}
		  		
		  	});
		      
		      myCollectLayout = (RelativeLayout)findViewById(R.id.myCollectLayout);
		      myCollectLayout.setOnClickListener(new OnClickListener(){
		  	
		  		public void onClick(View v) {
		  			  Intent intent = new Intent();
		     			     intent.setClass(PersonActivity.this,MyCollectActivity.class);
		     			     PersonActivity.this.startActivity(intent);
		  		}
		  		
		  	});
		     
		      goodPopLayout = (RelativeLayout)findViewById(R.id.goodPopLayout);
		      goodPopLayout.setOnClickListener(new OnClickListener(){
		  	
		  		public void onClick(View v) {
		  			  Intent intent = new Intent();
		     			     intent.setClass(PersonActivity.this,TopPopActivity.class);
		     			     PersonActivity.this.startActivity(intent);
		  		}
		  		
		  	});
		      
		      
		      individuationLayout = (RelativeLayout)findViewById(R.id.individuationLayout);
		      individuationLayout.setOnClickListener(new OnClickListener(){
		  	
		  		public void onClick(View v) {
		  			  Intent intent = new Intent();
		     			     intent.setClass(PersonActivity.this,PersonnalActivity.class);
		     			     PersonActivity.this.startActivity(intent);
		  		}
		  		
		  	});
		      
		      
	}

}
