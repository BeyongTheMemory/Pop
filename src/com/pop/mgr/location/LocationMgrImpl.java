
package com.pop.mgr.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.pop.show.MixContext;
import com.pop.R;
import com.pop.activity.MainActivity;
import com.pop.mgr.downloader.DownloadManager;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * This class is repsonsible for finding the location, and sending it back to
 * the mixcontext.
 * 定位用，提供用户位置信息
 * @author A. Egal
 */
class LocationMgrImpl implements LocationFinder {

	private LocationManager lm;
    private String bestLocationProvider;
	private final MixContext mixContext;
	private Location curLoc;
	private Location locationAtLastDownload;
	private LocationFinderState state;
	private final LocationObserver lob;
	private List<LocationResolver> locationResolvers;

	// frequency and minimum distance for update
	// this values will only be used after there's a good GPS fix
	// see back-off pattern discussion
	// http://stackoverflow.com/questions/3433875/how-to-force-gps-provider-to-get-speed-in-android
	// thanks Reto Meier for his presentation at gddde 2010
	//更新的频率和最小的距离，使用这个值酱油很好的GPS定位
	private final long freq = 5000; // 5 seconds
	private final float dist = 20; // 20 meters

	public LocationMgrImpl(MixContext mixContext) {
		this.mixContext = mixContext;
		this.lob = new LocationObserver(this);
		this.state = LocationFinderState.Inactive;
		this.locationResolvers = new ArrayList<LocationResolver>();
	}

	/*通过消息提供者搜寻信息
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mixare.mgr.location.LocationFinder#findLocation(android.content.Context
	 * )
	 */
	public void findLocation() {

		// fallback for the case where GPS and network providers are disabled
		//在GPS和网络供应商被禁用的情况下回退
		Location hardFix = new Location("reverseGeocoded");//初始化的location为google上的location,即坐标用于谷歌地图中

		// Frangart, Eppan, Bozen, Italy
		//默认坐标？
		hardFix.setLatitude(46.480302);
		hardFix.setLongitude(11.296005);
		hardFix.setAltitude(300);

		try {
			requestBestLocationUpdates();
			//temporary set the current location, until a good provider is found 
			//临时设置的当前位置
			curLoc = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), true));
		} catch (Exception ex2) {
			// ex2.printStackTrace();
			//找不到位置服务时临时设置，并弹出对话框
			curLoc = hardFix;
			mixContext.doPopUp("请开启网络或GPS以获取位置信息！");

		}
	}

	private void requestBestLocationUpdates() {
		Timer timer = new Timer();
		for (String p : lm.getAllProviders()) {//取得所有Providers
			if(lm.isProviderEnabled(p)){
				LocationResolver lr = new LocationResolver(lm, p, this);
				locationResolvers.add(lr);
				lm.requestLocationUpdates(p, 0, 0, lr);//请求位置信息的更新String provider, long minTime, float minDistance, LocationListener listener
			}
		}
		//等待20s再次进行定位
		timer.schedule(new LocationTimerTask(),20* 1000); //wait 20 seconds for the location updates to find the location
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mixare.mgr.location.LocationFinder#locationCallback(android.content
	 * .Context)
	 *找到位置信息提供商并判断其是否是最佳的
	 */
	public void locationCallback(String provider) {
		Location foundLocation = lm.getLastKnownLocation(provider);//获取缓存中的位置信息
		if (bestLocationProvider != null) {//有最佳位置信息提供者
			Location bestLocation = lm
					.getLastKnownLocation(bestLocationProvider);
			if (foundLocation.getAccuracy() < bestLocation.getAccuracy()) {//通过定位误差判断最近位置信息及位置信息提供者是否需要改变
				curLoc = foundLocation;
				bestLocationProvider = provider;
			}
		} else {//获取位置信息失败
			Log.v("Loc","LocError");
			curLoc = foundLocation;
			bestLocationProvider = provider;
		}
		setLocationAtLastDownload(curLoc);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.location.LocationFinder#getCurrentLocation()
	 * 获取当前位置
	 */
	public Location getCurrentLocation() {
		if (curLoc == null) {//若信息获取失败
			MainActivity mixView = mixContext.getActualMixView();
			Toast.makeText(
					mixView,
					"无法定位！", Toast.LENGTH_LONG)
					.show();
			throw new RuntimeException("No GPS Found");
		}
		synchronized (curLoc) {
			return curLoc;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.location.LocationFinder#getLocationAtLastDownload()
	 */
	public Location getLocationAtLastDownload() {
		return locationAtLastDownload;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mixare.mgr.location.LocationFinder#setLocationAtLastDownload(android
	 * .location.Location)
	 */
	public void setLocationAtLastDownload(Location locationAtLastDownload) {
		this.locationAtLastDownload = locationAtLastDownload;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mixare.mgr.location.LocationFinder#setDownloadManager(org.mixare.
	 * mgr.downloader.DownloadManager)
	 */
	public void setDownloadManager(DownloadManager downloadManager) {
		getObserver().setDownloadManager(downloadManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.location.LocationFinder#getGeomagneticField()
	 */
	public GeomagneticField getGeomagneticField() {//获取地磁信息实例，该实例可将地理坐标转化为WGS84坐标
		Location location = getCurrentLocation();
		GeomagneticField gmf = new GeomagneticField(
				(float) location.getLatitude(),
				(float) location.getLongitude(),
				(float) location.getAltitude(), System.currentTimeMillis());
		return gmf;
	}

	public void setPosition(Location location) {//设置位置信息
		synchronized (curLoc) {
			curLoc = location;
		}
		mixContext.getActualMixView().refresh();
		Location lastLoc = getLocationAtLastDownload();
		if (lastLoc == null) {
			setLocationAtLastDownload(location);
		}
	}

	@Override
	public void switchOn() {//开始更新位置信息
		if (!LocationFinderState.Active.equals(state)) {
			lm = (LocationManager) mixContext
					.getSystemService(Context.LOCATION_SERVICE);
			state = LocationFinderState.Confused;
		}
	}

	@Override
	public void switchOff() {//停止更新位置信息
		if (lm != null) {
			lm.removeUpdates(getObserver());
			state = LocationFinderState.Inactive;
		}
	}

	@Override
	public LocationFinderState getStatus() {
		return state;
	}

	private synchronized LocationObserver getObserver() {
		return lob;
	}

	class LocationTimerTask extends TimerTask {

		@Override
		public void run() {
			//remove all location updates
			//移除所有位置监听器，停止位置更新
			for(LocationResolver locationResolver: locationResolvers){
				lm.removeUpdates(locationResolver);//移除监听
			}
			if(bestLocationProvider != null){//有最佳位置监听器存在时
				lm.removeUpdates(getObserver());//地图标记更新
				state=LocationFinderState.Confused;
				mixContext.getActualMixView().runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						lm.requestLocationUpdates(bestLocationProvider, freq, dist, getObserver());	//开始位置监听	
					}
				});
				state=LocationFinderState.Active;
			}
			else{ //no location found没有位置监听器可以被使用，不停弹出提示框
				mixContext.getActualMixView().runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						Toast.makeText(mixContext.getActualMixView(), 
								"无法定位！", Toast.LENGTH_LONG);
					}
				});
				
			}
			
			
		}

	}


}