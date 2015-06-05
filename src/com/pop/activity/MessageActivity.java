package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
/**
 * 消息界面
 * @author xg
 *6.7
 */
public class MessageActivity extends Activity{
	private TextView backText = null;
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
        // 去标题栏
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.message_activity);
		//返回
				backText =(TextView)findViewById(R.id.back_text);
				backText.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MessageActivity.this,MainActivity.class);					
						MessageActivity.this.startActivity(intent);
						MessageActivity.this.finish();
						
						
					}
					
				});
	}

}
