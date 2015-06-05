package com.pop.activity;




import static android.hardware.SensorManager.SENSOR_DELAY_GAME;

import java.util.ArrayList;
import java.util.List;






import com.pop.show.Compatibility;
import com.pop.show.MixContext;



import com.pop.context.AppContext;
import com.pop.data.DataHandler;
import com.pop.data.DataSourceList;
import com.pop.data.DataSourceStorage;
import com.pop.lib.marker.Marker;
import com.pop.lib.render.Matrix;
import com.pop.show.DataView;
import com.pop.lib.gui.PaintScreen;
import com.pop.menu.SatelliteMenu;
import com.pop.menu.SatelliteMenuItem;
import com.pop.menu.SatelliteMenu.SateliteClickedListener;
import com.pop.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主界面
 * @author xg
 *6.2
 */
public class MainActivity extends Activity implements SensorEventListener, OnTouchListener{
	private SurfaceView cameraSurfaceView; //界面中SurfaceView组件
	private SurfaceHolder cameraSurfaceHolder; //SurfaceView的SurfaceHolder
	private AugmentedView augScreen;
	private SatelliteMenu menu;
	
	private boolean isInited;
	private static PaintScreen dWindow;
	private static DataView dataView;
	private boolean fError;

	//----------
    private MixViewDataHolder mixViewData  ;
	
	// TAG for logging
	public static final String TAG = "Mixare";

	// why use Memory to save a state? MixContext? activity lifecycle? 貌似是吐槽
	//private static MixView CONTEXT;  

	/* string to name & access the preference file in the internal storage */
	public static final String PREFS_NAME = "MyPrefsFileForMenuItems";//存储在本地的文件名称

	//屏幕宽，高，自加
	public static int screenWidth;
	public static int screenHeight;
	
	private	boolean isPreview = false;//相机 是否在预览中
	
	private AppContext appcontext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.v("ss","creat");
		//MixView.CONTEXT = this;
		
		//自加
		// 获取窗口管理器
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
        // 获取屏幕的宽和高
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		//完
		try {
						
			//handleIntent(getIntent());//初始化搜索框？

			final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			getMixViewData().setmWakeLock(pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag"));//屏幕衡量

			killOnError();
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			maintainCamera();
			maintainAugmentR();
			creatMenu();
			
			if (!isInited) {
				//getMixViewData().setMixContext(new MixContext(this));
				//getMixViewData().getMixContext().setDownloadManager(new DownloadManager(mixViewData.getMixContext()));
				setdWindow(new PaintScreen());
				setDataView(new DataView(getMixViewData().getMixContext()));

				/* set the radius in data view to the last selected by the user */
				//设置数据视图半径为用户最后选定的值
				//setZoomLevel();
				isInited = true;
			}

			/*Get the preference file PREFS_NAME stored in the internal memory of the phone*/
			//获取存储的文件
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			
			/*check if the application is launched for the first time*/
			//检查是否首次运行
			if(settings.getBoolean("firstAccess",false)==false){
				//firstAccess(settings);

			}

		} catch (Exception ex) {
			doError(ex);
		}
		
		this.appcontext = new AppContext();
	}

	public MixViewDataHolder getMixViewData() {
		if (mixViewData==null){
			// TODO: VERY important, only one!
			mixViewData = new MixViewDataHolder(new MixContext(this)); //监测
		}
		return mixViewData;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isPreview = false;//相机停止预览
		try {
			this.getMixViewData().getmWakeLock().release();

			try {
				getMixViewData().getSensorMgr().unregisterListener(this,
						getMixViewData().getSensorGrav());
				getMixViewData().getSensorMgr().unregisterListener(this,
						getMixViewData().getSensorMag());
				getMixViewData().setSensorMgr(null);
				
				getMixViewData().getMixContext().getLocationFinder().switchOff();
				getMixViewData().getMixContext().getDownloadManager().switchOff();

				if (getDataView() != null) {
					getDataView().cancelRefreshTimer();
				}
			} catch (Exception ignore) {
			}

			if (fError) {
				finish();
			}
		} catch (Exception ex) {
			doError(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 * Mixare - Receives results from other launched activities
	 * Base on the result returned, it either refreshes screen or not.
	 * Default value for refreshing is false
	 * 接受其他activity的结果，根据结果判断页面是否刷新,默认不刷新
	 */
	protected void onActivityResult(final int requestCode,
			final int resultCode, Intent data) {
		Log.d(TAG + " WorkFlow", "MixView - onActivityResult Called");
		// check if the returned is request to refresh screen (setting might be
		// changed)
		//检查返回值是否需要屏幕刷新
		try {
			if (data.getBooleanExtra("RefreshScreen", false)) {
				Log.d(TAG + " WorkFlow",
						"MixView - Received Refresh Screen Request .. about to refresh");
				repaint();
				refreshDownload();
			}

		} catch (Exception ex) {
			// do nothing do to mix of return results.
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("ss","resuml");
//处理平移和旋转的代码好像在此
		try {
			//Log.v("ss","resuml226");
			this.getMixViewData().getmWakeLock().acquire();//空指针  申请锁保证系统不进入休眠
			//Log.v("ss","resuml228"); 
			killOnError();
			//Log.v("ss","resuml229");
			getMixViewData().getMixContext().doResume(this);
			//Log.v("ss","resuml230");
			repaint();
			getDataView().doStart();
			getDataView().clearEvents();
			//Log.v("ss","resuml236");
			//getMixViewData().getMixContext().getDataSourceManager().refreshDataSources();

			float angleX, angleY;

			int marker_orientation = -90;

			int rotation = Compatibility.getRotation(this);

			// display text from left to right and keep it horizontal
			//从左到右显示文本和保持水平
			angleX = (float) Math.toRadians(marker_orientation);
			getMixViewData().getM1().set(1f, 0f, 0f, 0f,
					(float) FloatMath.cos(angleX),
					(float) -FloatMath.sin(angleX), 0f,
					(float) FloatMath.sin(angleX),
					(float) FloatMath.cos(angleX));//以x轴为轴旋转
			angleX = (float) Math.toRadians(marker_orientation);
			angleY = (float) Math.toRadians(marker_orientation);
			if (rotation == 1) {//不同旋转方式得到不同矩阵
				getMixViewData().getM2().set(1f, 0f, 0f, 0f,
						(float) FloatMath.cos(angleX),
						(float) -FloatMath.sin(angleX), 0f,
						(float) FloatMath.sin(angleX),
						(float) FloatMath.cos(angleX));//以x为轴
				getMixViewData().getM3().set((float) FloatMath.cos(angleY), 0f,
						(float) FloatMath.sin(angleY), 0f, 1f, 0f,
						(float) -FloatMath.sin(angleY), 0f,
						(float) FloatMath.cos(angleY));//以Y为轴
			} else {
				getMixViewData().getM2().set((float) FloatMath.cos(angleX), 0f,
						(float) FloatMath.sin(angleX), 0f, 1f, 0f,
						(float) -FloatMath.sin(angleX), 0f,
						(float) FloatMath.cos(angleX));
				getMixViewData().getM3().set(1f, 0f, 0f, 0f,
						(float) FloatMath.cos(angleY),
						(float) -FloatMath.sin(angleY), 0f,
						(float) FloatMath.sin(angleY),
						(float) FloatMath.cos(angleY));

			}

			getMixViewData().getM4().toIdentity();//标准坐标基

			for (int i = 0; i < getMixViewData().getHistR().length; i++) {
				getMixViewData().getHistR()[i] = new Matrix();
			}

			getMixViewData()
					.setSensorMgr((SensorManager) getSystemService(SENSOR_SERVICE));

			getMixViewData().setSensors(getMixViewData().getSensorMgr().getSensorList(
					Sensor.TYPE_ACCELEROMETER));
			if (getMixViewData().getSensors().size() > 0) {
				getMixViewData().setSensorGrav(getMixViewData().getSensors().get(0));
			}

			getMixViewData().setSensors(getMixViewData().getSensorMgr().getSensorList(
					Sensor.TYPE_MAGNETIC_FIELD));
			if (getMixViewData().getSensors().size() > 0) {
				getMixViewData().setSensorMag(getMixViewData().getSensors().get(0));
			}

			getMixViewData().getSensorMgr().registerListener(this,
					getMixViewData().getSensorGrav(), SENSOR_DELAY_GAME);
			getMixViewData().getSensorMgr().registerListener(this,
					getMixViewData().getSensorMag(), SENSOR_DELAY_GAME);

			try {
				GeomagneticField gmf = getMixViewData().getMixContext().getLocationFinder().getGeomagneticField(); 
				angleY = (float) Math.toRadians(-gmf.getDeclination());
				getMixViewData().getM4().set((float) FloatMath.cos(angleY), 0f,
						(float) FloatMath.sin(angleY), 0f, 1f, 0f,
						(float) -FloatMath.sin(angleY), 0f,
						(float) FloatMath.cos(angleY));
			} catch (Exception ex) {
				Log.d("mixare", "GPS Initialize Error", ex);
			}

			//getMixViewData().getMixContext().getDownloadManager().switchOn();
			//getMixViewData().getMixContext().getLocationFinder().switchOn();
		} catch (Exception ex) {
			doError(ex);
			try {
				if (getMixViewData().getSensorMgr() != null) {
					getMixViewData().getSensorMgr().unregisterListener(this,
							getMixViewData().getSensorGrav());
					getMixViewData().getSensorMgr().unregisterListener(this,
							getMixViewData().getSensorMag());
					getMixViewData().setSensorMgr(null);
				}

				if (getMixViewData().getMixContext() != null) {
					getMixViewData().getMixContext().getLocationFinder().switchOff();
					getMixViewData().getMixContext().getDownloadManager().switchOff();
				}
			} catch (Exception ignore) {
			}
		}

		Log.d("-------------------------------------------", "resume");
		if (getDataView().isFrozen() && getMixViewData().getSearchNotificationTxt() == null) {
			getMixViewData().setSearchNotificationTxt(new TextView(this));
			getMixViewData().getSearchNotificationTxt().setWidth(
					getdWindow().getWidth());
			getMixViewData().getSearchNotificationTxt().setPadding(10, 2, 0, 0);
			getMixViewData().getSearchNotificationTxt().setText(
					"数据源搜索:" + " "
							+ DataSourceList.getDataSourcesStringList()
							+ "点击继续");
			;
			getMixViewData().getSearchNotificationTxt().setBackgroundColor(
					Color.DKGRAY);
			getMixViewData().getSearchNotificationTxt().setTextColor(Color.WHITE);

			getMixViewData().getSearchNotificationTxt().setOnTouchListener(this);
			addContentView(getMixViewData().getSearchNotificationTxt(),
					new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT));
		} else if (!getDataView().isFrozen()
				&& getMixViewData().getSearchNotificationTxt() != null) {
			getMixViewData().getSearchNotificationTxt().setVisibility(View.GONE);
			getMixViewData().setSearchNotificationTxt(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Customize Activity after switching back to it.
	 * Currently it maintain and ensures view creation.
	 * 重获焦点后
	 */
	protected void onRestart (){
		super.onRestart();
		maintainCamera();
		maintainAugmentR();
		creatMenu();
		
	}
	
	/* ********* Operators ***********/ 

	public void repaint() {
		//clear stored data
		//清楚存储数据
		getDataView().clearEvents();
		setDataView(null); //It's smelly code, but enforce garbage collector 
							//to release data.
		setDataView(new DataView(mixViewData.getMixContext()));
		setdWindow(new PaintScreen());
		//setZoomLevel(); //@TODO Caller has to set the zoom. This function repaints only.执行缩放，只在重画时需要
	}
	
	/**
	 *  Checks camScreen, if it does not exist, it creates one.
	 *  检查是否需要创建cameraScereen
	 */
	private void maintainCamera() {
		if (cameraSurfaceView == null){
			cameraSurfaceView = new CameraSurfaceView(this);
		}
		setContentView(cameraSurfaceView);
	}
	
	/**
	 * Checks augScreen, if it does not exist, it creates one.
	 */
	private void maintainAugmentR() {
		if (augScreen == null ){
		augScreen = new AugmentedView(this);
		}
		addContentView(augScreen, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * Creates a zoom bar and adds it to view.
	 * 创建小地图
	 */

	/**
	 * Refreshes Download 
	 * TODO refresh downloads
	 */
	private void refreshDownload(){
//		try {
//			if (getMixViewData().getDownloadThread() != null){
//				if (!getMixViewData().getDownloadThread().isInterrupted()){
//					getMixViewData().getDownloadThread().interrupt();
//					getMixViewData().getMixContext().getDownloadManager().restart();
//				}
//			}else { //if no download thread found
//				getMixViewData().setDownloadThread(new Thread(getMixViewData()
//						.getMixContext().getDownloadManager()));
//				//@TODO Syncronize DownloadManager, call Start instead of run.
//				mixViewData.getMixContext().getDownloadManager().run();
//			}
//		}catch (Exception ex){
//		}
	}
	
	public void refresh(){
		dataView.refresh();
		//刷新
	}

//	public void setErrorDialog(){
//		//错误提示对话框，舍弃
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setMessage(getString(R.string.connection_error_dialog));
//		builder.setCancelable(false);
//
//		/*Retry*/
//		builder.setPositiveButton(R.string.connection_error_dialog_button1, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				fError=false;
//				//TODO improve
//				try {
//					maintainCamera();
//					maintainAugmentR();
//					repaint();
//					//setZoomLevel();
//				}
//				catch(Exception ex){
//					//Don't call doError, it will be a recursive call.
//					//doError(ex);
//				}
//			}
//		});
//		/*Open settings*/
//		builder.setNeutralButton(R.string.connection_error_dialog_button2, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				Intent intent1 = new Intent(Settings.ACTION_WIRELESS_SETTINGS); 
//				startActivityForResult(intent1, 42);
//			}
//		});
//		/*Close application*/
//		builder.setNegativeButton(R.string.connection_error_dialog_button3, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				System.exit(0); //wouldn't be better to use finish (to stop the app normally?)
//			}
//		});
//		AlertDialog alert = builder.create();
//		alert.show();
//	}

	
//	public float calcZoomLevel(){
//         //裁剪缩放
//		int myZoomLevel = getMixViewData().getMyZoomBar().getProgress();
//		float myout = 5;
//
//		if (myZoomLevel <= 26) {
//			myout = myZoomLevel / 25f;
//		} else if (25 < myZoomLevel && myZoomLevel < 50) {
//			myout = (1 + (myZoomLevel - 25)) * 0.38f;
//		} else if (25 == myZoomLevel) {
//			myout = 1;
//		} else if (50 == myZoomLevel) {
//			myout = 10;
//		} else if (50 < myZoomLevel && myZoomLevel < 75) {
//			myout = (10 + (myZoomLevel - 50)) * 0.83f;
//		} else {
//			myout = (30 + (myZoomLevel - 75) * 2f);
//		}
//		return myout;
//	}

	/**
	 * Handle First time users. It display license agreement and store user's
	 * acceptance.
	 * 用户第一次进入时出现。现实许可协议，并且创建存储文件夹
	 * @param settings
	 */
//	private void firstAccess(SharedPreferences settings) {
//		SharedPreferences.Editor editor = settings.edit();
//		//AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//		//builder1.setMessage("用户协议");
//		//builder1.setNegativeButton("同意",
////				new DialogInterface.OnClickListener() {
////					public void onClick(DialogInterface dialog, int id) {
////						dialog.dismiss();
////					}
////				});
////		AlertDialog alert1 = builder1.create();
////		alert1.setTitle("用户协议");
////		alert1.show();
//		editor.putBoolean("firstAccess", true);
//
//		// value for maximum POI for each selected OSM URL to be active by
//		// default is 5
//		//每次接受POI的最大值，默认为5
//		editor.putInt("osmMaxObject", 5);
//		editor.commit();
//
//		// add the default datasources to the preferences file
//		//添加默认数据源参数文件
//		DataSourceStorage.getInstance().fillDefaultDataSources();
//	}

	/**创建并且返回小地图
	 * Create zoom bar and returns FrameLayout. FrameLayout is created to be
	 * hidden and not added to view, Caller needs to add the frameLayout to
	 * view, and enable visibility when needed.
	 * 
	 * @param SharedOreference settings where setting is stored
	 * @return FrameLayout Hidden Zoom Bar
	 */
//	private FrameLayout createZoomBar(SharedPreferences settings) {
//		getMixViewData().setMyZoomBar(new SeekBar(this));
//		getMixViewData().getMyZoomBar().setMax(100);
//		getMixViewData().getMyZoomBar()
//				.setProgress(settings.getInt("zoomLevel", 65));
//		//getMixViewData().getMyZoomBar().setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);
//		getMixViewData().getMyZoomBar().setVisibility(View.INVISIBLE);
//
//		FrameLayout frameLayout = new FrameLayout(this);
//
//		frameLayout.setMinimumWidth(3000);
//		frameLayout.addView(getMixViewData().getMyZoomBar());
//		frameLayout.setPadding(10, 0, 10, 10);
//		return frameLayout;
//	}
	
	/* ********* Operator - Menu ******/
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {//现实菜单
//		int base = Menu.FIRST;
//		/* define the first */
//		MenuItem item1 = menu.add(base, base, base,
//				getString(R.string.menu_item_1));
//		MenuItem item2 = menu.add(base, base + 1, base + 1,
//				getString(R.string.menu_item_2));
//		MenuItem item3 = menu.add(base, base + 2, base + 2,
//				getString(R.string.menu_item_3));
//		MenuItem item4 = menu.add(base, base + 3, base + 3,
//				getString(R.string.menu_item_4));
//		MenuItem item5 = menu.add(base, base + 4, base + 4,
//				getString(R.string.menu_item_5));
//		MenuItem item6 = menu.add(base, base + 5, base + 5,
//				getString(R.string.menu_item_6));
//		MenuItem item7 = menu.add(base, base + 6, base + 6,
//				getString(R.string.menu_item_7));
//
//		/* assign icons to the menu items */
//		item1.setIcon(drawable.icon_datasource);
//		item2.setIcon(android.R.drawable.ic_menu_view);
//		item3.setIcon(android.R.drawable.ic_menu_mapmode);
//		item4.setIcon(android.R.drawable.ic_menu_zoom);
//		item5.setIcon(android.R.drawable.ic_menu_search);
//		item6.setIcon(android.R.drawable.ic_menu_info_details);
//		item7.setIcon(android.R.drawable.ic_menu_share);
//
//		return true;
//	}

	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {//菜单项响应函数
//		switch (item.getItemId()) {
//		/* Data sources */
//		case 1:
//			if (!getDataView().isLauncherStarted()) {
//				Intent intent = new Intent(MixView.this, DataSourceList.class);
//				startActivityForResult(intent, 40);
//			} else {
//				Toast.makeText(this, getString(R.string.no_website_available),
//						Toast.LENGTH_LONG).show();
//			}
//			break;
//		/* List view */
//		case 2:
//			/*
//			 * if the list of titles to show in alternative list view is not
//			 * empty
//			 */
//			if (getDataView().getDataHandler().getMarkerCount() > 0) {
//				//Intent intent1 = new Intent(MixView.this, MixListView.class); 
//				//startActivityForResult(intent1, 42);
//			}
//			/* if the list is empty */
//			else {
//				Toast.makeText(this, R.string.empty_list, Toast.LENGTH_LONG)
//						.show();
//			}
//			break;
//		/* Map View */
//		case 3:
//			//Intent intent2 = new Intent(MixView.this, MixMap.class);
//			//startActivityForResult(intent2, 20);
//			break;
//		/* zoom level */
//		case 4:
//			getMixViewData().getMyZoomBar().setVisibility(View.VISIBLE);
//			getMixViewData().setZoomProgress(getMixViewData().getMyZoomBar()
//					.getProgress());
//			break;
//		/* Search */
//		case 5:
//			onSearchRequested();
//			break;
//		/* GPS Information */
//		case 6:
//			Location currentGPSInfo = getMixViewData().getMixContext().getLocationFinder().getCurrentLocation();
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage(getString(R.string.general_info_text) + "\n\n"
//					+ getString(R.string.longitude)
//					+ currentGPSInfo.getLongitude() + "\n"
//					+ getString(R.string.latitude)
//					+ currentGPSInfo.getLatitude() + "\n"
//					+ getString(R.string.altitude)
//					+ currentGPSInfo.getAltitude() + "m\n"
//					+ getString(R.string.speed) + currentGPSInfo.getSpeed()
//					+ "km/h\n" + getString(R.string.accuracy)
//					+ currentGPSInfo.getAccuracy() + "m\n"
//					+ getString(R.string.gps_last_fix)
//					+ new Date(currentGPSInfo.getTime()).toString() + "\n");
//			builder.setNegativeButton(getString(R.string.close_button),
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int id) {
//							dialog.dismiss();
//						}
//					});
//			AlertDialog alert = builder.create();
//			alert.setTitle(getString(R.string.general_info_title));
//			alert.show();
//			break;
//		/* Case 6: license agreements */
//		case 7:
//			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//			builder1.setMessage(getString(R.string.license));
//			/* Retry */
//			builder1.setNegativeButton(getString(R.string.close_button),
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int id) {
//							dialog.dismiss();
//						}
//					});
//			AlertDialog alert1 = builder1.create();
//			alert1.setTitle(getString(R.string.license_title));
//			alert1.show();
//			break;
//
//		}
//		return true;
//	}

	/* ******** Operators - Sensors ****** */

//	private SeekBar.OnSeekBarChangeListener myZoomBarOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {//缩放
//		Toast t;
//
//		public void onProgressChanged(SeekBar seekBar, int progress,
//				boolean fromUser) {
//			float myout = calcZoomLevel();
//
//			getMixViewData().setZoomLevel(String.valueOf(myout));
//			getMixViewData().setZoomProgress(getMixViewData().getMyZoomBar()
//					.getProgress());
//
//			t.setText("Radius: " + String.valueOf(myout));
//			t.show();
//		}
//
//		public void onStartTrackingTouch(SeekBar seekBar) {//缩放拖动条响应函数
//			Context ctx = seekBar.getContext();
//			t = Toast.makeText(ctx, "Radius: ", Toast.LENGTH_LONG);
//			// zoomChanging= true;
//		}
//
//		public void onStopTrackingTouch(SeekBar seekBar) {//缩放拖动条响应函数
//			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//			SharedPreferences.Editor editor = settings.edit();
//			/* store the zoom range of the zoom bar selected by the user */
//			editor.putInt("zoomLevel", getMixViewData().getMyZoomBar().getProgress());
//			editor.commit();
//			getMixViewData().getMyZoomBar().setVisibility(View.INVISIBLE);
//			// zoomChanging= false;
//
//			getMixViewData().getMyZoomBar().getProgress();
//
//			t.cancel();
//			//repaint after zoom level changed.
//			repaint();
//			setZoomLevel();
//		}
//
//	};

    /*传感器数据监听*/
	public void onSensorChanged(SensorEvent evt) {//传感器数据监听
		try {

			if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				getMixViewData().getGrav()[0] = evt.values[0];
				getMixViewData().getGrav()[1] = evt.values[1];
				getMixViewData().getGrav()[2] = evt.values[2];

				augScreen.postInvalidate();
			} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				getMixViewData().getMag()[0] = evt.values[0];
				getMixViewData().getMag()[1] = evt.values[1];
				getMixViewData().getMag()[2] = evt.values[2];

				augScreen.postInvalidate();
			}

			SensorManager.getRotationMatrix(getMixViewData().getRTmp(),
					getMixViewData().getI(), getMixViewData().getGrav(),
					getMixViewData().getMag());

			int rotation = Compatibility.getRotation(this);

			if (rotation == 1) {
				SensorManager.remapCoordinateSystem(getMixViewData().getRTmp(),
						SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z,
						getMixViewData().getRot());
			} else {
				SensorManager.remapCoordinateSystem(getMixViewData().getRTmp(),
						SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_Z,
						getMixViewData().getRot());
			}
			getMixViewData().getTempR().set(getMixViewData().getRot()[0],
					getMixViewData().getRot()[1], getMixViewData().getRot()[2],
					getMixViewData().getRot()[3], getMixViewData().getRot()[4],
					getMixViewData().getRot()[5], getMixViewData().getRot()[6],
					getMixViewData().getRot()[7], getMixViewData().getRot()[8]);

			getMixViewData().getFinalR().toIdentity();
			getMixViewData().getFinalR().prod(getMixViewData().getM4());
			getMixViewData().getFinalR().prod(getMixViewData().getM1());
			getMixViewData().getFinalR().prod(getMixViewData().getTempR());
			getMixViewData().getFinalR().prod(getMixViewData().getM3());
			getMixViewData().getFinalR().prod(getMixViewData().getM2());
			getMixViewData().getFinalR().invert();

			getMixViewData().getHistR()[getMixViewData().getrHistIdx()].set(getMixViewData()
					.getFinalR());
			getMixViewData().setrHistIdx(getMixViewData().getrHistIdx() + 1);
			if (getMixViewData().getrHistIdx() >= getMixViewData().getHistR().length)
				getMixViewData().setrHistIdx(0);

			getMixViewData().getSmoothR().set(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
			for (int i = 0; i < getMixViewData().getHistR().length; i++) {
				getMixViewData().getSmoothR().add(getMixViewData().getHistR()[i]);
			}
			getMixViewData().getSmoothR().mult(
					1 / (float) getMixViewData().getHistR().length);

			getMixViewData().getMixContext().updateSmoothRotation(getMixViewData().getSmoothR());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	/*触摸响应函数*/
	public boolean onTouchEvent(MotionEvent me) {
		try {
			killOnError();
			
			float xPress = me.getX();
			float yPress = me.getY();
			
			Log.v("touch","xgtouch: x:"+xPress+",y:"+yPress);
			
			if (me.getAction() == MotionEvent.ACTION_UP) {
				//委托给dataView，在dataView中将在链表里添加此次点击事件，稍后再进行处理
				//很赞的设计
				getDataView().clickEvent(xPress, yPress);
			}//TODO add gesture events (low)

			return true;
		} catch (Exception ex) {
			// doError(ex);
			ex.printStackTrace();
			return super.onTouchEvent(me);
		}
	}

	@Override
	/*按钮监听*/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			killOnError();

			if (keyCode == KeyEvent.KEYCODE_BACK) {//返回键按下时
				if (getDataView().isDetailsView()) {
					getDataView().keyEvent(keyCode);
					getDataView().setDetailsView(false);
					return true;
				} else {
					//TODO handle keyback to finish app correctly
					return super.onKeyDown(keyCode, event);
				}
			} else if (keyCode == KeyEvent.KEYCODE_MENU) {//菜单键按下时
				return super.onKeyDown(keyCode, event);
			} else {
				getDataView().keyEvent(keyCode);
				return false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return super.onKeyDown(keyCode, event);
		}
	}
    
	/*精度改变时*/
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE
				&& getMixViewData().getCompassErrorDisplayed() == 0) {
			for (int i = 0; i < 2; i++) {
				Toast.makeText(getMixViewData().getMixContext(),
						"Compass data unreliable. Please recalibrate compass.",
						Toast.LENGTH_LONG).show();
			}
			getMixViewData().setCompassErrorDisplayed(getMixViewData()
					.getCompassErrorDisplayed() + 1);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {//重载的触摸函数，貌似没用
		getDataView().setFrozen(false);
		Log.v("touch","xgtouch");
		if (getMixViewData().getSearchNotificationTxt() != null) {
			getMixViewData().getSearchNotificationTxt().setVisibility(View.GONE);
			getMixViewData().setSearchNotificationTxt(null);
		}
		return false;
	}


	/* ************ Handlers *************/

	public void doError(Exception ex1) {
		if (!fError) {
			fError = true;

			//setErrorDialog();

			ex1.printStackTrace();
			try {
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}

		try {
			augScreen.invalidate();
		} catch (Exception ignore) {
		}
	}

	public void killOnError() throws Exception {
		if (fError)
			throw new Exception();
	}

//	private void handleIntent(Intent intent) {
//		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//			String query = intent.getStringExtra(SearchManager.QUERY);//获取搜素框里的值
//			doMixSearch(query);
//		}
//	}

	

//	private void doMixSearch(String query) {
//		DataHandler jLayer = getDataView().getDataHandler();
//		if (!getDataView().isFrozen()) {
//			//MixListView.originalMarkerList = jLayer.getMarkerList(); //xg注释
//			//MixMap.originalMarkerList = jLayer.getMarkerList();   //xg注释
//		}
//
//		ArrayList<Marker> searchResults = new ArrayList<Marker>();
//		Log.d("SEARCH-------------------0", "" + query);
//		if (jLayer.getMarkerCount() > 0) {
//			for (int i = 0; i < jLayer.getMarkerCount(); i++) {
//				Marker ma = jLayer.getMarker(i);
//				if (ma.getTitle().toLowerCase().indexOf(query.toLowerCase()) != -1) {
//					searchResults.add(ma);
//					/* the website for the corresponding title */
//				}
//			}
//		}
//		if (searchResults.size() > 0) {
//			getDataView().setFrozen(true);
//			jLayer.setMarkerList(searchResults);
//		} else
//			Toast.makeText(this,
//					"搜索失败",
//					Toast.LENGTH_LONG).show();
//	}

	/* ******* Getter and Setters ********** */

//	public boolean isZoombarVisible() {//缩放轴是否可见
//		return getMixViewData().getMyZoomBar() != null
//				&& getMixViewData().getMyZoomBar().getVisibility() == View.VISIBLE;
//	}
	
	public String getZoomLevel() {//得到缩放级别
		return getMixViewData().getZoomLevel();
	}
	
	/**
	 * @return the dWindow
	 */
	static PaintScreen getdWindow() {
		return dWindow;
	}


	/**
	 * @param dWindow
	 *            the dWindow to set
	 */
	static void setdWindow(PaintScreen dWindow) {
		MainActivity.dWindow = dWindow;
	}


	/**
	 * @return the dataView
	 */
	public static DataView getDataView() {
		return dataView;
	}

	/**
	 * @param dataView
	 *            the dataView to set
	 */
	static void setDataView(DataView dataView) {
		MainActivity.dataView = dataView;
	}

	
	//创建菜单
	public void creatMenu(){
//		Log.v("1","bengkui");
//		if(menu == null){
//           SatelliteMenu menu = new SatelliteMenu(this);
//		}
//		Log.v("2","bengkui");
////		     FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(  
////			      FrameLayout.LayoutParams.WRAP_CONTENT,  
////				        FrameLayout.LayoutParams.WRAP_CONTENT);  
//		
//		
//		
//		 FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(  
//			      60,  
//			       60); 
//		 Log.v("3","bengkui");
//				       //设置菜单出现的位置(悬浮于顶部)  
//				        params.topMargin = 0;  
//				       params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;  
//				       Log.v("4","bengkui");
//				       addContentView(menu, params);
//		Log.v("5","bengkui");
		Log.v("849","bengkui");
		
		//menu = (SatelliteMenu)findViewById(R.id.menu);
		if(menu == null){
			menu = new SatelliteMenu(this);
			List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
			items.add(new SatelliteMenuItem(5, R.drawable.person_btn));
			items.add(new SatelliteMenuItem(6, R.drawable.message_btn));
	        items.add(new SatelliteMenuItem(7, R.drawable.filter_btn));
	        items.add(new SatelliteMenuItem(8, R.drawable.throw_btn));
	        menu.addItems(items); 
	        Log.v("861","bengkui");
	        menu.setOnItemClickedListener(new SateliteClickedListener() {
				public void eventOccured(int id) {
					Log.v("T",id+"");
					if(id == 5){//个人
						isPreview=false;
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,PersonActivity.class);
						//MainActivity.this.startActivity(intent);
						MainActivity.this.startActivity(intent);
						MainActivity.this.onPause();
						MainActivity.this.finish();
						
					}
					if(id == 6){//消息
						isPreview=false;
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,MessageActivity.class);
						//MainActivity.this.startActivity(intent);
						MainActivity.this.startActivity(intent);
						MainActivity.this.onPause();
						MainActivity.this.finish();
						
					}
					if(id == 7){//过滤
						isPreview=false;
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,FilterPopActivity.class);
						//MainActivity.this.startActivity(intent);
						MainActivity.this.startActivity(intent);
						MainActivity.this.onPause();
						MainActivity.this.finish();
					   
						
					}
					if(id == 8){//新泡泡
						isPreview=false;
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,NewPopActivity.class);
						//MainActivity.this.startActivity(intent);
						MainActivity.this.startActivity(intent);
						MainActivity.this.onPause();
						MainActivity.this.finish();
					}
					
				}
			});
		}
		Log.v("855","bengkui");
		 FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(  
			      FrameLayout.LayoutParams.FILL_PARENT,  
			      FrameLayout.LayoutParams.FILL_PARENT); 
		 Log.v("858","bengkui");
		//设置菜单出现的位置(悬浮于顶部)  
	       
	       params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;  
	       Log.v("859","bengkui");
	       addContentView(menu, params);
	       Log.v("860","bengkui");
		
	}
	
	/**
	 * @author xg
	 *相机
	 */
	private class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{	
		private	Camera camera;	// 定义系统所用的照相机
		
		public CameraSurfaceView(Context context) {
			super(context);
			// 获得SurfaceView的SurfaceHolder
			cameraSurfaceHolder = getHolder();
	          // 为surfaceHolder添加一个回调监听器
			cameraSurfaceHolder.addCallback(this);
			//Log.v("chushihua","OK_______________OK");
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// 预览界面格式和大小变化时被调用	
		}

		
	
	
		public void surfaceCreated(SurfaceHolder holder) {
			//初次实例化预览界面时调用
			// 打开摄像头
			Log.v("chuangjian","OK_______________OK");
			if (!isPreview)
			{
				// 此处默认打开后置摄像头。
				// 通过传入参数可以打开前置摄像头
				camera = Camera.open(0);  //①
				camera.setDisplayOrientation(90);
			}
			if (camera != null && !isPreview)
			{
				try
				{
					//Log.v("yulan","OK_______________OK");
					Camera.Parameters parameters = camera.getParameters();
					// 设置预览照片的大小
					parameters.setPreviewSize(screenWidth, screenHeight);
					//Log.v("yulan1","OK_______________OK");
					// 设置预览照片时每秒显示多少帧的最小值和最大值
					parameters.setPreviewFpsRange(4, 10);
					//Log.v("yulan2","OK_______________OK");
					// 通过SurfaceView显示取景画面
					camera.setPreviewDisplay(cameraSurfaceHolder); 
					//Log.v("yulan4","OK_______________OK");
					// 开始预览
					camera.startPreview();  
					//Log.v("chushihuakaishi","OK_______________OK");
				}
				catch (Exception e)
				{   
					Log.v("yulancuowu","OK_______________OK");
					e.printStackTrace();
				}
				isPreview = true;
		}
			
			
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			//当预览界面被关闭时调用
			// 如果camera不为null ,释放摄像头
			if (camera != null)
			{
				if (isPreview) camera.stopPreview();
				camera.release();
				camera = null;
			}
		}
		

	 }

}




class AugmentedView extends View {//AR显示界面
	MainActivity app;
//	int xSearch = 200;
//	int ySearch = 10;
//	int searchObjWidth = 0;
//	int searchObjHeight = 0;

	Paint zoomPaint = new Paint();

	public AugmentedView(Context context) {
		super(context);

		try {
			app = (MainActivity) context;
			app.killOnError();
		} catch (Exception ex) {
			app.doError(ex);
		}
		
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			Log.v("draw","suc_______________0");

			app.killOnError();

			MainActivity.getdWindow().setWidth(canvas.getWidth());//屏幕宽
			MainActivity.getdWindow().setHeight(canvas.getHeight());//屏幕高
			MainActivity.getdWindow().setCanvas(canvas);//获得画布

			if (!MainActivity.getDataView().isInited()) {//若ar界面未初始化，对其进行初始化
				MainActivity.getDataView().init(MainActivity.getdWindow().getWidth(),
						MainActivity.getdWindow().getHeight());
			}
//			if (true) {
//				zoomPaint.setColor(Color.WHITE);
//				zoomPaint.setTextSize(14);
//				String startKM, endKM;
//				endKM = "80km";
//				startKM = "0km";
//	
//				canvas.drawText(startKM, canvas.getWidth() / 100 * 4,
//						canvas.getHeight() / 100 * 85, zoomPaint);
//				canvas.drawText(endKM, canvas.getWidth() / 100 * 99 + 25,
//						canvas.getHeight() / 100 * 85, zoomPaint);
//
//				int height = canvas.getHeight() / 100 * 85;
////				int zoomProgress = app.getZoomProgress();
////				Log.v("zoomProgress", "xgcheck:"+zoomProgress);
////				if (zoomProgress > 92 || zoomProgress < 6) {
////					height = canvas.getHeight() / 100 * 80;
////				}
////				Log.v("zoomLevel", "xgcheck:"+app.getZoomLevel());
////				canvas.drawText(app.getZoomLevel(), (canvas.getWidth()) / 100
////						* zoomProgress + 20, height, zoomPaint);
//			}

			MainActivity.getDataView().draw(MainActivity.getdWindow());//mark绘制
			Log.v("ok","sec_______________0");
		} catch (Exception ex) {
			app.doError(ex);
		}
	}
}

/**
 * Internal class that holds Mixview field Data.
 * 
 * @author A B
 */
class MixViewDataHolder {
	private final MixContext mixContext;
	private float[] RTmp;
	private float[] Rot;
	private float[] I;
	private float[] grav;
	private float[] mag;
	private SensorManager sensorMgr;
	private List<Sensor> sensors;
	private Sensor sensorGrav;
	private Sensor sensorMag;
	private int rHistIdx;
	private Matrix tempR;
	private Matrix finalR;
	private Matrix smoothR;
	private Matrix[] histR;
	private Matrix m1;
	private Matrix m2;
	private Matrix m3;
	private Matrix m4;
	private SeekBar myZoomBar;
	private WakeLock mWakeLock;//系统锁，保证系统部休眠
	private int compassErrorDisplayed;
	private String zoomLevel;
	private int zoomProgress;
	private TextView searchNotificationTxt;

	public MixViewDataHolder(MixContext mixContext) {
		this.mixContext=mixContext;
		this.RTmp = new float[9];
		this.Rot = new float[9];
		this.I = new float[9];
		this.grav = new float[3];
		this.mag = new float[3];
		this.rHistIdx = 0;
		this.tempR = new Matrix();
		this.finalR = new Matrix();
		this.smoothR = new Matrix();
		this.histR = new Matrix[60];
		this.m1 = new Matrix();
		this.m2 = new Matrix();
		this.m3 = new Matrix();
		this.m4 = new Matrix();
		this.compassErrorDisplayed = 0;
	}

	/* ******* Getter and Setters ********** */
	public MixContext getMixContext() {
		return mixContext;
	}

	public float[] getRTmp() {
		return RTmp;
	}

	public void setRTmp(float[] rTmp) {
		RTmp = rTmp;
	}

	public float[] getRot() {
		return Rot;
	}

	public void setRot(float[] rot) {
		Rot = rot;
	}

	public float[] getI() {
		return I;
	}

	public void setI(float[] i) {
		I = i;
	}

	public float[] getGrav() {
		return grav;
	}

	public void setGrav(float[] grav) {
		this.grav = grav;
	}

	public float[] getMag() {
		return mag;
	}

	public void setMag(float[] mag) {
		this.mag = mag;
	}

	public SensorManager getSensorMgr() {
		return sensorMgr;
	}

	public void setSensorMgr(SensorManager sensorMgr) {
		this.sensorMgr = sensorMgr;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	public Sensor getSensorGrav() {
		return sensorGrav;
	}

	public void setSensorGrav(Sensor sensorGrav) {
		this.sensorGrav = sensorGrav;
	}

	public Sensor getSensorMag() {
		return sensorMag;
	}

	public void setSensorMag(Sensor sensorMag) {
		this.sensorMag = sensorMag;
	}

	public int getrHistIdx() {
		return rHistIdx;
	}

	public void setrHistIdx(int rHistIdx) {
		this.rHistIdx = rHistIdx;
	}

	public Matrix getTempR() {
		return tempR;
	}

	public void setTempR(Matrix tempR) {
		this.tempR = tempR;
	}

	public Matrix getFinalR() {
		return finalR;
	}

	public void setFinalR(Matrix finalR) {
		this.finalR = finalR;
	}

	public Matrix getSmoothR() {
		return smoothR;
	}

	public void setSmoothR(Matrix smoothR) {
		this.smoothR = smoothR;
	}

	public Matrix[] getHistR() {
		return histR;
	}

	public void setHistR(Matrix[] histR) {
		this.histR = histR;
	}

	public Matrix getM1() {
		return m1;
	}

	public void setM1(Matrix m1) {
		this.m1 = m1;
	}

	public Matrix getM2() {
		return m2;
	}

	public void setM2(Matrix m2) {
		this.m2 = m2;
	}

	public Matrix getM3() {
		return m3;
	}

	public void setM3(Matrix m3) {
		this.m3 = m3;
	}

	public Matrix getM4() {
		return m4;
	}

	public void setM4(Matrix m4) {
		this.m4 = m4;
	}

	public SeekBar getMyZoomBar() {
		return myZoomBar;
	}

	public void setMyZoomBar(SeekBar myZoomBar) {
		this.myZoomBar = myZoomBar;
	}

	public WakeLock getmWakeLock() {
		return mWakeLock;
	}

	public void setmWakeLock(WakeLock mWakeLock) {
		this.mWakeLock = mWakeLock;
	}

	public int getCompassErrorDisplayed() {
		return compassErrorDisplayed;
	}

	public void setCompassErrorDisplayed(int compassErrorDisplayed) {
		this.compassErrorDisplayed = compassErrorDisplayed;
	}

	public String getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(String zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public int getZoomProgress() {
		return zoomProgress;
	}

	public void setZoomProgress(int zoomProgress) {
		this.zoomProgress = zoomProgress;
	}

	public TextView getSearchNotificationTxt() {
		return searchNotificationTxt;
	}

	public void setSearchNotificationTxt(TextView searchNotificationTxt) {
		this.searchNotificationTxt = searchNotificationTxt;
	}
}
	


