
package com.pop.show;

import static android.view.KeyEvent.KEYCODE_CAMERA;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;








import com.pop.activity.AboutActivity;
import com.pop.activity.BusnisePopActivity;
import com.pop.activity.OptionActivity;
import com.pop.activity.getPopActivity;
import com.pop.context.AppContext;
import com.pop.data.DataHandler;
import com.pop.data.DataSource;
import com.pop.gui.RadarPoints;
import com.pop.lib.MixUtils;
import com.pop.lib.gui.PaintScreen;
import com.pop.lib.gui.ScreenLine;
import com.pop.lib.marker.ImageMarker;
import com.pop.lib.marker.Marker;
import com.pop.lib.render.Camera;
import com.pop.mgr.downloader.DownloadManager;
import com.pop.mgr.downloader.DownloadMgrImpl;
import com.pop.mgr.downloader.DownloadRequest;
import com.pop.mgr.downloader.DownloadResult;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

/**
 * This class is able to update the markers and the radar. It also handles some
 * user events
 *这类能够更新标记和雷达。它也处理一些
 *用户事件
 *该类用于雷达的描绘
 * @author daniele
 * 
 */
public class DataView {

	/** current context */
	private MixContext mixContext;
	/** is the view Inited? */
	private boolean isInit;

	/** width and height of the view */
	private int width, height;

	/**
	 * _NOT_ the android camera, the class that takes care of the transformation
	 */
	private Camera cam;

	private MixState state = new MixState();

	/** The view can be "frozen" for debug purposes */
	/**调试用，将视图冻结*/
	private boolean frozen;
	boolean sureF = true;//确保只打开一个页面
	/** how many times to re-attempt download */
	/**尝试重新下载的次数*/
	private int retry;

	private Location curFix;
	private DataHandler dataHandler = new DataHandler();
	private float radius = 1;//搜索半径，单位为km,通过更改该数据即可改变搜索半径

	/** timer to refresh the browser */
	/**刷新浏览器的定时器*/
	private Timer refresh = null;
	private final long refreshDelay = 300 * 1000; // 更改为每5分钟刷新一次

	private boolean isLauncherStarted;

	private ArrayList<UIEvent> uiEvents = new ArrayList<UIEvent>();

	private RadarPoints radarPoints = new RadarPoints();
	private ScreenLine lrl = new ScreenLine();
	private ScreenLine rrl = new ScreenLine();
	//private float rx = 10, ry = 20;
	private float rx = 0, ry = 20;//rx=width - 100;为出现在右上角
	private float addX = 0, addY = 0;
	
	private List<Marker> markers;
	private Executor executor = Executors.newSingleThreadExecutor();
	boolean tag =true;
	/**
	 * Constructor
	 */
	public DataView(MixContext ctx) {
		this.mixContext = ctx;
	}

	public MixContext getContext() {
		return mixContext;
	}
    
	public Location getCurFix(){
		return curFix;
	}
	 
	public boolean isLauncherStarted() {
		return isLauncherStarted;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

	public boolean isDetailsView() {
		return state.isDetailsView();
	}

	public void setDetailsView(boolean detailsView) {
		state.setDetailsView(detailsView);
	}

	public void doStart() {
		state.nextLStatus = MixState.NOT_STARTED;
		mixContext.getLocationFinder().setLocationAtLastDownload(curFix);
	}

	public boolean isInited() {
		return isInit;
	}
    
	/**AR界面初始化*/
	public void init(int widthInit, int heightInit) {
		try {
			//界面初始化
			width = widthInit;
			height = heightInit;
            rx = width - 100;
			cam = new Camera(width, height, true);
			cam.setViewAngle(Camera.DEFAULT_VIEW_ANGLE);

			lrl.set(0, -RadarPoints.RADIUS);
			lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
			lrl.add(rx + RadarPoints.RADIUS, ry + RadarPoints.RADIUS);
			rrl.set(0, -RadarPoints.RADIUS);
			rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
			rrl.add(rx + RadarPoints.RADIUS, ry + RadarPoints.RADIUS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		frozen = false;
		isInit = true;
	}

	


//	public void requestData(DataSource datasource, double lat, double lon, double alt, float radius, String locale) {
//		DownloadRequest request = new DownloadRequest();
//		request.params = datasource.createRequestParams(lat, lon, alt, radius, locale);
//		request.source = datasource;
//		
//		mixContext.getDownloadManager().submitJob(request);
//		state.nextLStatus = MixState.PROCESSING;
//	}
    
	/**显示Mark,包括刷新的代码也在这里*/
	public void draw(PaintScreen dw) {
		mixContext.getRM(cam.transform);
		curFix = mixContext.getLocationFinder().getCurrentLocation();//得到当前地理位置信息
		//AppContext appContext = (AppContext) getApplicationContext();
		state.calcPitchBearing(cam.transform);//计算旋转值，将保存在state的curBearing和curpitch中

		// Load Layer
		//若首次运行该方法则发送HTTP请求检测周围兴趣点
		if (state.nextLStatus == MixState.NOT_STARTED && !frozen) {
			Log.v("load","xg___________");
			//loadDrawLayer();
			markers = new ArrayList<Marker>();
		}
		//兴趣点已经传输完毕
		else if (state.nextLStatus == MixState.PROCESSING) {
			//Log.v("Noload","xg___________");
			
		if(markers.size() == 0 ||tag){
			tag = false;
			DownloadMgrImpl dm = (DownloadMgrImpl) mixContext.getDownloadManager();//得到下载的数据
			 executor.execute(dm);
			Log.v("xiancheng226", "xiancheng226");
			while(dm.isTag()){//等待接收完成
				Log.v("xiancheng229", "xiancheng229");
			}
		
			Log.v("xiancheng231", "xiancheng231");
				retry = 0;
				state.nextLStatus = MixState.DONE;
				dataHandler = new DataHandler();
				markers.addAll(dm.getMarkers());
				dataHandler.addMarkers(markers);
				dataHandler.onLocationChanged(curFix);
				Log.v("jianchaguanbi", "jianchaguanbi");
				//this.getContext().getDownloadManager().switchOff();//关闭接受
					//刷新函数注释			
//				if (refresh == null) { //刷新线程尚未开始的话开始线程
//					refresh = new Timer(false);//非守护线程，在主线程结束后不会退出
//					Date date = new Date(System.currentTimeMillis()
//							+ refreshDelay);//当前时间的45s后
//					refresh.schedule(new TimerTask() {
//						@Override
//						public void run() {
//							callRefreshToast();//弹出信息提示框toast
//							refresh();
//						}
//					}, date, refreshDelay);
//				}
			}
		}

		// Update markers
		//更新mark
		dataHandler.updateActivationStatus(mixContext);
		for (int i = dataHandler.getMarkerCount() - 1; i >= 0; i--) {
			Marker ma = dataHandler.getMarker(i);
			Log.v("aacc","aacc" + ma.toString());
			Log.v("aacc","aacc" + ma.isActive());
			// if (ma.isActive() && (ma.getDistance() / 1000f < radius || ma
			// instanceof NavigationMarker || ma instanceof SocialMarker)) {
			if (ma.isActive() && (ma.getDistance() / 1000f < radius)) {//可见且距离在设置内才重绘

				// To increase performance don't recalculate position vector
				// for every marker on every draw call, instead do this only
				// after onLocationChanged and after downloading new marker
				// if (!frozen)
				// ma.update(curFix);
				//为了提高性能不重新计算位置信息向量
				//只在用户位置变换或下载新的marker的时候才重新计算 
				if (!frozen)
					ma.calcPaint(cam, addX, addY);
				ma.draw(dw);
			}
		}

		// Draw Radar
		//绘制雷达
		drawRadar(dw);

		// Get next event
		UIEvent evt = null;
		synchronized (uiEvents) {
			if (uiEvents.size() > 0) {
				evt = uiEvents.get(0);
				uiEvents.remove(0);
			}
		}
		if (evt != null) {
				handleClickEvent((ClickEvent) evt);
		}
		state.nextLStatus = MixState.PROCESSING;
	}

	/**
	 * Part of draw function, loads the layer.
	 * 将当前位置信息发送至http，搜索附近的mark
	 */
	
	


	/**
	 * Handles drawing radar and direction.
	 * @param PaintScreen screen that radar will be drawn to
	 * 处理绘制雷达和方向。
	 */
	private void drawRadar(PaintScreen dw) {
		String dirTxt = "";
		int bearing = (int) state.getCurBearing();
		int range = (int) (state.getCurBearing() / (360f / 16f));
		// TODO: get strings from the values xml file
		if (range == 15 || range == 0)
			dirTxt = "N";
		else if (range == 1 || range == 2)
			dirTxt = "NE";
		else if (range == 3 || range == 4)
			dirTxt = "E";
		else if (range == 5 || range == 6)
			dirTxt = "SE";
		else if (range == 7 || range == 8)
			dirTxt = "S";
		else if (range == 9 || range == 10)
			dirTxt = "SW";
		else if (range == 11 || range == 12)
			dirTxt = "W";
		else if (range == 13 || range == 14)
			dirTxt = "NW";

		radarPoints.view = this;//在该页面上绘制雷达RadarPoints可绘制雷达的圆和上面的点
		dw.paintObj(radarPoints, rx, ry, -state.getCurBearing(), 1);
		dw.setFill(false);//设置画笔为空心
		//dw.setColor(Color.argb(150, 0, 0, 220));//蓝色
		dw.setColor(Color.argb(255, 166, 202, 240));//蓝色
		
		//两条由圆心发出的线，表示当前的视觉角度
		dw.paintLine(lrl.x, lrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS);
		dw.paintLine(rrl.x, rrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS);
		dw.setColor(Color.rgb(255, 255, 255));
		dw.setFontSize(12);
        //绘制搜索距离
		radarText(dw, MixUtils.formatDist(radius * 1000), rx
				+ RadarPoints.RADIUS, ry + RadarPoints.RADIUS * 2 - 10, false);
		
		//绘制罗盘角度
		radarText(dw, "" + bearing + ((char) 176) + " " + dirTxt, rx
				+ RadarPoints.RADIUS, ry - 5, true);
	}



	boolean handleClickEvent(ClickEvent evt) {
		boolean evtHandled = false;

		// Handle event
//		if (state.nextLStatus == MixState.DONE) {
			// the following will traverse the markers in ascending order (by
			// distance) the first marker that
			// matches triggers the event.
			//TODO handle collection of markers. (what if user wants the one at the back)
			//遍历升序顺序的标记点（通过距离排序），匹配触发事件
		    //--------------------------------------------------------------------------
		    //事件处理由这里传入，可以使用mixcontext中的handle函数来响应事件（载入界面）
		    //-----------------------------------------------------------------------------
			for (int i = 0; i < dataHandler.getMarkerCount() && !evtHandled; i++) {
				Marker pm = dataHandler.getMarker(i);
				evtHandled = pm.fClick(evt.x, evt.y, mixContext, state);
				Log.v("eve:","xgeve:"+ evtHandled);
				if(evtHandled){
					if(pm.getType().equals(ImageMarker.geren)){
						if(sureF){
							sureF = false;
						 SharedPreferences mySharedPreferences = this.getContext().getSharedPreferences("pop", 
								  Activity.MODE_PRIVATE); 
						  SharedPreferences.Editor editor = mySharedPreferences.edit(); 
						  editor.putInt("id",pm.getPopid()); 	 
						  editor.commit(); 
//					 Intent intent = new Intent();
//	   			     intent.setClass(this.getContext(),getPopActivity.class);
	   			          Intent intent = new Intent(this.getContext(), getPopActivity.class);  
	   			          this.getContext().startActivity(intent);  
	   			          this.getContext().startActivity(intent);
	   			          sureF = true;
						}
					}
					else{
						 Intent intent = new Intent();
		   			     intent.setClass(this.getContext(),BusnisePopActivity.class);
		   			      this.getContext().startActivity(intent);
					}
				}
			}
//		}
		return evtHandled;
	}
    
	/**雷达上方显示的字*/
	private void radarText(PaintScreen dw, String txt, float x, float y, boolean bg) {
		float padw = 4, padh = 2;
		float w = dw.getTextWidth(txt) + padw * 2;
		float h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
		if (bg) {
			dw.setColor(Color.rgb(0, 0, 0));
			dw.setFill(true);
			dw.paintRect(x - w / 2, y - h / 2, w, h);
			dw.setColor(Color.rgb(255, 255, 255));
			dw.setFill(false);
			dw.paintRect(x - w / 2, y - h / 2, w, h);
		}
		dw.paintText(padw + x - w / 2, padh + dw.getTextAsc() + y - h / 2, txt,
				false);
	}
    
	/**将暂存点击操作在数组中，稍后再进行响应*/
	public void clickEvent(float x, float y) {
		synchronized (uiEvents) {
			uiEvents.add(new ClickEvent(x, y));
		}
	}

	public void keyEvent(int keyCode) {
		synchronized (uiEvents) {
			uiEvents.add(new KeyEvent(keyCode));
		}
	}

	public void clearEvents() {
		synchronized (uiEvents) {
			uiEvents.clear();
		}
	}

	public void cancelRefreshTimer() {
		if (refresh != null) {
			refresh.cancel();
		}
	}
	
	/**
	 * Re-downloads the markers, and draw them on the map.
	 * 重新下载标记，并绘制。
	 */
	public void refresh(){
		state.nextLStatus = MixState.NOT_STARTED;
	}
	
	private void callRefreshToast(){
		mixContext.getActualMixView().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(
						mixContext,
						"刷新泡泡中",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}

class UIEvent {
	public static final int CLICK = 0;
	public static final int KEY = 1;

	public int type;
}

class ClickEvent extends UIEvent {
	public float x, y;

	public ClickEvent(float x, float y) {
		this.type = CLICK;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

class KeyEvent extends UIEvent {
	public int keyCode;

	public KeyEvent(int keyCode) {
		this.type = KEY;
		this.keyCode = keyCode;
	}

	@Override
	public String toString() {
		return "(" + keyCode + ")";
	}
}
