
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
 * ��λ�ã��ṩ�û�λ����Ϣ
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
	//���µ�Ƶ�ʺ���С�ľ��룬ʹ�����ֵ���ͺܺõ�GPS��λ
	private final long freq = 5000; // 5 seconds
	private final float dist = 20; // 20 meters

	public LocationMgrImpl(MixContext mixContext) {
		this.mixContext = mixContext;
		this.lob = new LocationObserver(this);
		this.state = LocationFinderState.Inactive;
		this.locationResolvers = new ArrayList<LocationResolver>();
	}

	/*ͨ����Ϣ�ṩ����Ѱ��Ϣ
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mixare.mgr.location.LocationFinder#findLocation(android.content.Context
	 * )
	 */
	public void findLocation() {

		// fallback for the case where GPS and network providers are disabled
		//��GPS�����繩Ӧ�̱����õ�����»���
		Location hardFix = new Location("reverseGeocoded");//��ʼ����locationΪgoogle�ϵ�location,���������ڹȸ��ͼ��

		// Frangart, Eppan, Bozen, Italy
		//Ĭ�����ꣿ
		hardFix.setLatitude(46.480302);
		hardFix.setLongitude(11.296005);
		hardFix.setAltitude(300);

		try {
			requestBestLocationUpdates();
			//temporary set the current location, until a good provider is found 
			//��ʱ���õĵ�ǰλ��
			curLoc = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), true));
		} catch (Exception ex2) {
			// ex2.printStackTrace();
			//�Ҳ���λ�÷���ʱ��ʱ���ã��������Ի���
			curLoc = hardFix;
			mixContext.doPopUp("�뿪�������GPS�Ի�ȡλ����Ϣ��");

		}
	}

	private void requestBestLocationUpdates() {
		Timer timer = new Timer();
		for (String p : lm.getAllProviders()) {//ȡ������Providers
			if(lm.isProviderEnabled(p)){
				LocationResolver lr = new LocationResolver(lm, p, this);
				locationResolvers.add(lr);
				lm.requestLocationUpdates(p, 0, 0, lr);//����λ����Ϣ�ĸ���String provider, long minTime, float minDistance, LocationListener listener
			}
		}
		//�ȴ�20s�ٴν��ж�λ
		timer.schedule(new LocationTimerTask(),20* 1000); //wait 20 seconds for the location updates to find the location
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mixare.mgr.location.LocationFinder#locationCallback(android.content
	 * .Context)
	 *�ҵ�λ����Ϣ�ṩ�̲��ж����Ƿ�����ѵ�
	 */
	public void locationCallback(String provider) {
		Location foundLocation = lm.getLastKnownLocation(provider);//��ȡ�����е�λ����Ϣ
		if (bestLocationProvider != null) {//�����λ����Ϣ�ṩ��
			Location bestLocation = lm
					.getLastKnownLocation(bestLocationProvider);
			if (foundLocation.getAccuracy() < bestLocation.getAccuracy()) {//ͨ����λ����ж����λ����Ϣ��λ����Ϣ�ṩ���Ƿ���Ҫ�ı�
				curLoc = foundLocation;
				bestLocationProvider = provider;
			}
		} else {//��ȡλ����Ϣʧ��
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
	 * ��ȡ��ǰλ��
	 */
	public Location getCurrentLocation() {
		if (curLoc == null) {//����Ϣ��ȡʧ��
			MainActivity mixView = mixContext.getActualMixView();
			Toast.makeText(
					mixView,
					"�޷���λ��", Toast.LENGTH_LONG)
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
	public GeomagneticField getGeomagneticField() {//��ȡ�ش���Ϣʵ������ʵ���ɽ���������ת��ΪWGS84����
		Location location = getCurrentLocation();
		GeomagneticField gmf = new GeomagneticField(
				(float) location.getLatitude(),
				(float) location.getLongitude(),
				(float) location.getAltitude(), System.currentTimeMillis());
		return gmf;
	}

	public void setPosition(Location location) {//����λ����Ϣ
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
	public void switchOn() {//��ʼ����λ����Ϣ
		if (!LocationFinderState.Active.equals(state)) {
			lm = (LocationManager) mixContext
					.getSystemService(Context.LOCATION_SERVICE);
			state = LocationFinderState.Confused;
		}
	}

	@Override
	public void switchOff() {//ֹͣ����λ����Ϣ
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
			//�Ƴ�����λ�ü�������ֹͣλ�ø���
			for(LocationResolver locationResolver: locationResolvers){
				lm.removeUpdates(locationResolver);//�Ƴ�����
			}
			if(bestLocationProvider != null){//�����λ�ü���������ʱ
				lm.removeUpdates(getObserver());//��ͼ��Ǹ���
				state=LocationFinderState.Confused;
				mixContext.getActualMixView().runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						lm.requestLocationUpdates(bestLocationProvider, freq, dist, getObserver());	//��ʼλ�ü���	
					}
				});
				state=LocationFinderState.Active;
			}
			else{ //no location foundû��λ�ü��������Ա�ʹ�ã���ͣ������ʾ��
				mixContext.getActualMixView().runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						Toast.makeText(mixContext.getActualMixView(), 
								"�޷���λ��", Toast.LENGTH_LONG);
					}
				});
				
			}
			
			
		}

	}


}