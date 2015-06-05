package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class TopPopActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {  
		Window window = this.getWindow();
        // »•±ÍÃ‚¿∏
       window.requestFeature(window.FEATURE_NO_TITLE);
	      super.onCreate(savedInstanceState);  
	      setContentView(R.layout.option_activity);
	}
}
