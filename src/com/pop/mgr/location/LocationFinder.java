
package com.pop.mgr.location;

import com.pop.mgr.downloader.DownloadManager;

import android.hardware.GeomagneticField;
import android.location.Location;

/**
 * This class is repsonsible for finding the location, and sending it back to
 * the mixcontext.
 * 该类用于定位，并把位置信息发送给mixcontext
 */
public interface LocationFinder {

	/**
	 * Possible status of LocationFinder
	 * 该类的可能状态
	 */
	public enum LocationFinderState {
		Active, // Providing Location Information提供位置信息
		Inactive, // No-Active不提供位置信息
		Confused // Same problem in internal state
	}

	/**
	 * Finds the location through the providers  
	 * 通过消息提供者搜寻信息
	 * @param ctx
	 * @return
	 */
	void findLocation();

	/**
	 * A working location provider has been found: check if 
	 * the found location has the best accuracy.
	 * 找到位置信息提供商并判断其是否是最佳的
	 */
	void locationCallback(String provider);
	
	/**
	 * Returns the current location
	 * 返回当前位置信息.
	 */
	Location getCurrentLocation();

	/**
	 * Gets the location that was used in the last download for
	 * datasources.
	 * 使用最后一次更新得到的位置信息
	 * @return
	 */
	Location getLocationAtLastDownload();

	/**
	 * Sets the property to the location with the last successfull download.
	 * 设置最后一次更新得到的位置信息
	 */
	void setLocationAtLastDownload(Location locationAtLastDownload);

	/**
	 * Set the DownloadManager manager at this service
	 * 
	 * @param downloadManager
	 */
	void setDownloadManager(DownloadManager downloadManager);

	/**
	 * Request to active the service
	 */
	void switchOn();

	/**
	 * Request to deactive the service
	 */
	void switchOff();

	/**
	 * Status of service
	 * 返回状态
	 * @return
	 */
	LocationFinderState getStatus();

	/**
	 * 
	 * @return GeomagneticField
	 */
	GeomagneticField getGeomagneticField();

}