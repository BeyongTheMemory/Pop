package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class AboutActivity extends Activity{
	ImageView back = null;
	Button phone = null;
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
        // »•±ÍÃ‚¿∏
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.aboutpop_activity);
		back = (ImageView)findViewById(R.id.imageview_aboutpopback);
		back.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				AboutActivity.this .finish();
				
			}
			
		});
		phone = (Button)findViewById(R.id.button_popphone);
        phone.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:15386422422"));
				startActivity(intent);
			}
			
		});
		
	}

}
