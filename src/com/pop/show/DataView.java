
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
 *�����ܹ����±�Ǻ��״��Ҳ����һЩ
 *�û��¼�
 *���������״�����
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
	/**�����ã�����ͼ����*/
	private boolean frozen;
	boolean sureF = true;//ȷ��ֻ��һ��ҳ��
	/** how many times to re-attempt download */
	/**�����������صĴ���*/
	private int retry;

	private Location curFix;
	private DataHandler dataHandler = new DataHandler();
	private float radius = 1;//�����뾶����λΪkm,ͨ�����ĸ����ݼ��ɸı������뾶

	/** timer to refresh the browser */
	/**ˢ��������Ķ�ʱ��*/
	private Timer refresh = null;
	private final long refreshDelay = 300 * 1000; // ����Ϊÿ5����ˢ��һ��

	private boolean isLauncherStarted;

	private ArrayList<UIEvent> uiEvents = new ArrayList<UIEvent>();

	private RadarPoints radarPoints = new RadarPoints();
	private ScreenLine lrl = new ScreenLine();
	private ScreenLine rrl = new ScreenLine();
	//private float rx = 10, ry = 20;
	private float rx = 0, ry = 20;//rx=width - 100;Ϊ���������Ͻ�
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
    
	/**AR�����ʼ��*/
	public void init(int widthInit, int heightInit) {
		try {
			//�����ʼ��
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
    
	/**��ʾMark,����ˢ�µĴ���Ҳ������*/
	public void draw(PaintScreen dw) {
		mixContext.getRM(cam.transform);
		curFix = mixContext.getLocationFinder().getCurrentLocation();//�õ���ǰ����λ����Ϣ
		//AppContext appContext = (AppContext) getApplicationContext();
		state.calcPitchBearing(cam.transform);//������תֵ����������state��curBearing��curpitch��

		// Load Layer
		//���״����и÷�������HTTP��������Χ��Ȥ��
		if (state.nextLStatus == MixState.NOT_STARTED && !frozen) {
			Log.v("load","xg___________");
			//loadDrawLayer();
			markers = new ArrayList<Marker>();
		}
		//��Ȥ���Ѿ��������
		else if (state.nextLStatus == MixState.PROCESSING) {
			//Log.v("Noload","xg___________");
			
		if(markers.size() == 0 ||tag){
			tag = false;
			DownloadMgrImpl dm = (DownloadMgrImpl) mixContext.getDownloadManager();//�õ����ص�����
			 executor.execute(dm);
			Log.v("xiancheng226", "xiancheng226");
			while(dm.isTag()){//�ȴ��������
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
				//this.getContext().getDownloadManager().switchOff();//�رս���
					//ˢ�º���ע��			
//				if (refresh == null) { //ˢ���߳���δ��ʼ�Ļ���ʼ�߳�
//					refresh = new Timer(false);//���ػ��̣߳������߳̽����󲻻��˳�
//					Date date = new Date(System.currentTimeMillis()
//							+ refreshDelay);//��ǰʱ���45s��
//					refresh.schedule(new TimerTask() {
//						@Override
//						public void run() {
//							callRefreshToast();//������Ϣ��ʾ��toast
//							refresh();
//						}
//					}, date, refreshDelay);
//				}
			}
		}

		// Update markers
		//����mark
		dataHandler.updateActivationStatus(mixContext);
		for (int i = dataHandler.getMarkerCount() - 1; i >= 0; i--) {
			Marker ma = dataHandler.getMarker(i);
			Log.v("aacc","aacc" + ma.toString());
			Log.v("aacc","aacc" + ma.isActive());
			// if (ma.isActive() && (ma.getDistance() / 1000f < radius || ma
			// instanceof NavigationMarker || ma instanceof SocialMarker)) {
			if (ma.isActive() && (ma.getDistance() / 1000f < radius)) {//�ɼ��Ҿ����������ڲ��ػ�

				// To increase performance don't recalculate position vector
				// for every marker on every draw call, instead do this only
				// after onLocationChanged and after downloading new marker
				// if (!frozen)
				// ma.update(curFix);
				//Ϊ��������ܲ����¼���λ����Ϣ����
				//ֻ���û�λ�ñ任�������µ�marker��ʱ������¼��� 
				if (!frozen)
					ma.calcPaint(cam, addX, addY);
				ma.draw(dw);
			}
		}

		// Draw Radar
		//�����״�
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
	 * ����ǰλ����Ϣ������http������������mark
	 */
	
	


	/**
	 * Handles drawing radar and direction.
	 * @param PaintScreen screen that radar will be drawn to
	 * ��������״�ͷ���
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

		radarPoints.view = this;//�ڸ�ҳ���ϻ����״�RadarPoints�ɻ����״��Բ������ĵ�
		dw.paintObj(radarPoints, rx, ry, -state.getCurBearing(), 1);
		dw.setFill(false);//���û���Ϊ����
		//dw.setColor(Color.argb(150, 0, 0, 220));//��ɫ
		dw.setColor(Color.argb(255, 166, 202, 240));//��ɫ
		
		//������Բ�ķ������ߣ���ʾ��ǰ���Ӿ��Ƕ�
		dw.paintLine(lrl.x, lrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS);
		dw.paintLine(rrl.x, rrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS);
		dw.setColor(Color.rgb(255, 255, 255));
		dw.setFontSize(12);
        //������������
		radarText(dw, MixUtils.formatDist(radius * 1000), rx
				+ RadarPoints.RADIUS, ry + RadarPoints.RADIUS * 2 - 10, false);
		
		//�������̽Ƕ�
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
			//��������˳��ı�ǵ㣨ͨ���������򣩣�ƥ�䴥���¼�
		    //--------------------------------------------------------------------------
		    //�¼����������ﴫ�룬����ʹ��mixcontext�е�handle��������Ӧ�¼���������棩
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
    
	/**�״��Ϸ���ʾ����*/
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
    
	/**���ݴ��������������У��Ժ��ٽ�����Ӧ*/
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
	 * �������ر�ǣ������ơ�
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
						"ˢ��������",
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
