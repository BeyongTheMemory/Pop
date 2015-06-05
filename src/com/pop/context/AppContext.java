package com.pop.context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;





import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.location.Location;

public class AppContext extends Application {

	
	private SharedPreferences preferences;
	private Location curloction;
//	private int screenWidth;
//	private int screenHeight;

	public AppContext(){
		super();
	}
	
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public Location getCurloction() {
		return curloction;
	}

	public void setCurloction(Location curloction) {
		this.curloction = curloction;
	}
	
	
	
}

	



