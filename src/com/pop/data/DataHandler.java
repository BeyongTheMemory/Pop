

package com.pop.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.pop.show.MixContext;
import com.pop.activity.MainActivity;
import com.pop.lib.marker.Marker;

import android.location.Location;
import android.util.Log;

/**
 * DataHandler is the model which provides the Marker Objects with its data.
 * ģ���ṩ�ı�ǵĶ������ݡ�
 * DataHandler is also the Factory for new Marker objects.
 * Ҳ���µı�ǵĶ��󹤳���
 */
public class DataHandler {
	
	// complete marker list
	//��ɱ��
	private List<Marker> markerList = new ArrayList<Marker>();
	/**����mark����dataHandler�ı���markList��*/
	public void addMarkers(List<Marker> markers) {

		Log.v(MainActivity.TAG, "Marker before: "+markerList.size());
		for(Marker ma:markers) {
			if(!markerList.contains(ma))
				markerList.add(ma);
		}
		
		Log.d(MainActivity.TAG, "Marker count: "+markerList.size());
	}
	
	public void sortMarkerList() {
		Collections.sort(markerList); 
	}
	/**����ÿ����־�����û��ľ���,���������markList��ÿһ��Mark��distance������*/
	public void updateDistances(Location location) {//����ÿ����־�����û��ľ���
		for(Marker ma: markerList) {
			float[] dist=new float[3];
			Location.distanceBetween(ma.getLatitude(), ma.getLongitude(), location.getLatitude(), location.getLongitude(), dist);
			ma.setDistance(dist[0]);
		}
	}
	
	/**�жϸ�mark�Ƿ�Ӧ�ñ���浽��Ļ��*/
	public void updateActivationStatus(MixContext mixContext) {
		
		Hashtable<Class, Integer> map = new Hashtable<Class, Integer>();
				
		for(Marker ma: markerList) {

			Class<? extends Marker> mClass=ma.getClass();
			map.put(mClass, (map.get(mClass)!=null)?map.get(mClass)+1:1);
			
			boolean belowMax = (map.get(mClass) <= ma.getMaxObjects());
			//boolean dataSourceSelected = mixContext.isDataSourceSelected(ma.getDatasource());
			
			ma.setActive((belowMax));
		}
	}
    
	/**ʹ�ô����location���¼���ÿһ��Mark�����location�ľ���*/
	public void onLocationChanged(Location location) {
		updateDistances(location);
		sortMarkerList();
		for(Marker ma: markerList) {
			ma.update(location);
		}
	}
	
	/**
	 * @deprecated Nobody should get direct access to the list
	 */
	public List<Marker> getMarkerList() {
		return markerList;
	}
	
	/**
	 * @deprecated Nobody should get direct access to the list
	 */
	public void setMarkerList(List<Marker> markerList) {
		this.markerList = markerList;
	}

	public int getMarkerCount() {
		return markerList.size();
	}
	
	public Marker getMarker(int index) {
		return markerList.get(index);
	}
}
