
package com.pop.mgr.location;

import com.pop.mgr.downloader.DownloadManager;

import android.hardware.GeomagneticField;
import android.location.Location;

/**
 * This class is repsonsible for finding the location, and sending it back to
 * the mixcontext.
 * �������ڶ�λ������λ����Ϣ���͸�mixcontext
 */
public interface LocationFinder {

	/**
	 * Possible status of LocationFinder
	 * ����Ŀ���״̬
	 */
	public enum LocationFinderState {
		Active, // Providing Location Information�ṩλ����Ϣ
		Inactive, // No-Active���ṩλ����Ϣ
		Confused // Same problem in internal state
	}

	/**
	 * Finds the location through the providers  
	 * ͨ����Ϣ�ṩ����Ѱ��Ϣ
	 * @param ctx
	 * @return
	 */
	void findLocation();

	/**
	 * A working location provider has been found: check if 
	 * the found location has the best accuracy.
	 * �ҵ�λ����Ϣ�ṩ�̲��ж����Ƿ�����ѵ�
	 */
	void locationCallback(String provider);
	
	/**
	 * Returns the current location
	 * ���ص�ǰλ����Ϣ.
	 */
	Location getCurrentLocation();

	/**
	 * Gets the location that was used in the last download for
	 * datasources.
	 * ʹ�����һ�θ��µõ���λ����Ϣ
	 * @return
	 */
	Location getLocationAtLastDownload();

	/**
	 * Sets the property to the location with the last successfull download.
	 * �������һ�θ��µõ���λ����Ϣ
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
	 * ����״̬
	 * @return
	 */
	LocationFinderState getStatus();

	/**
	 * 
	 * @return GeomagneticField
	 */
	GeomagneticField getGeomagneticField();

}