package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AccoutActivity  extends Activity{
	ImageView back = null;
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
        // »•±ÍÃ‚¿∏
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.account_activity);
		back = (ImageView)findViewById(R.id.imageview_back);
		 back.setOnClickListener(new OnClickListener(){

				
				public void onClick(View v) {
				AccoutActivity.this.finish();
				}
				
			});
		
	}
}
