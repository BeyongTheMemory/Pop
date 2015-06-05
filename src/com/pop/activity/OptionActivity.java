package com.pop.activity;


import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 设置界面
 * @author xg
 *6.7
 */
public class OptionActivity extends Activity{
	private  TextView backText = null;
//	private  TextView aboutText = null;
//	private  TextView accountText = null;
	private RelativeLayout accountManagerLayout = null;
	private RelativeLayout aboutPopLayout = null;
	private RelativeLayout  noticeAndRemindLayout=null;
	
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
        // 去标题栏
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.option_activity);
		//返回
		backText =(TextView)findViewById(R.id.back_text);
		backText.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				OptionActivity.this.finish();
				
			}
			
		});
		
//	aboutText = (TextView)findViewById(R.id.aboutPopLayout_textView);
//	aboutText.setOnClickListener(new OnClickListener(){
//
//		
//		public void onClick(View v) {
//			  Intent intent = new Intent();
//   			     intent.setClass(OptionActivity.this,AboutActivity.class);
//   			     OptionActivity.this.startActivity(intent);
//		}
//		
//	});
	
	accountManagerLayout = (RelativeLayout)findViewById(R.id.accountManagerLayout);
	accountManagerLayout.setOnClickListener(new OnClickListener(){
	
		public void onClick(View v) {
			  Intent intent = new Intent();
   			     intent.setClass(OptionActivity.this,AccoutActivity.class);
   			     OptionActivity.this.startActivity(intent);
		}
		
	});
	
	aboutPopLayout = (RelativeLayout)findViewById(R.id.aboutPopLayout);
	aboutPopLayout.setOnClickListener(new OnClickListener(){

		
		public void onClick(View v) {
			  Intent intent = new Intent();
   			     intent.setClass(OptionActivity.this,AboutActivity.class);
   			     OptionActivity.this.startActivity(intent);
		}
		
	});
	
	
	 noticeAndRemindLayout = (RelativeLayout)findViewById(R.id. noticeAndRemindLayout);
	 noticeAndRemindLayout.setOnClickListener(new OnClickListener(){

		
		public void onClick(View v) {
			  Intent intent = new Intent();
   			     intent.setClass(OptionActivity.this,MessageNoticeActivity.class);
   			     OptionActivity.this.startActivity(intent);
		}
		
	});
//	accountText = (TextView)findViewById(R.id.accountManager_textView);
//	accountText.setOnClickListener(new OnClickListener(){
//	
//		public void onClick(View v) {
//			  Intent intent = new Intent();
//   			     intent.setClass(OptionActivity.this,AccoutActivity.class);
//   			     OptionActivity.this.startActivity(intent);
//		}
//		
//	});
	}

}
