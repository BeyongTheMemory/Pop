
package com.pop.lib.marker;

import java.net.URLDecoder;

import com.pop.lib.gui.Label;
import com.pop.lib.gui.TextObj;
import com.pop.lib.marker.draw.ClickHandler;
import com.pop.lib.marker.draw.DrawCommand;
import com.pop.lib.marker.draw.ParcelableProperty;
import com.pop.lib.marker.draw.PrimitiveProperty;
import com.pop.lib.reality.PhysicalPlace;
import com.pop.lib.render.Camera;
import com.pop.lib.render.MixVector;

import android.location.Location;
import android.util.Log;

/**
 * A plugin marker that should be extended by marker plugins.
 * ��һ�����͵ı�־��
 * @author A. Egal
 *
 */
public abstract class PluginMarker implements Marker{
	
	private String ID;
	protected String title;
	protected boolean underline = false;
	private String URL;
	protected PhysicalPlace mGeoLoc;
    //Ŀ��͹۲��߾���
	protected double distance;
	// The marker color
	private int colour;

	private boolean active;
	// Draw properties
	//�ɼ���
	protected boolean isVisible;

	public MixVector cMarker = new MixVector();
	protected MixVector signMarker = new MixVector();

	protected MixVector locationVector = new MixVector();//�������Ŀ���������û�λ�õ�������Ϣ
	private MixVector origin = new MixVector(0, 0, 0);
	private MixVector upV = new MixVector(0, 1, 0);
	public Label txtLab = new Label();
	protected TextObj textBlock;
	
	public PluginMarker(int id, String title, double latitude, double longitude, double altitude, String link, int type, int colour) {
		super();

		this.active = true;
		this.title = title;
		this.mGeoLoc = new PhysicalPlace(latitude,longitude,altitude);
		if (link != null && link.length() > 0) {
			URL = "webpage:" + URLDecoder.decode(link);
			this.underline = true;
		}
		this.colour = colour;

		this.ID= id + "##"+ type +"##"+title;

	}	
	
	public String getURL() {
		return URL;
	}

	public double getLatitude() {
		return mGeoLoc.getLatitude();
	}

	public double getLongitude() {
		return mGeoLoc.getLongitude();
	}

	public double getAltitude() {
		return mGeoLoc.getAltitude();
	}

	public MixVector getLocationVector() {
		return locationVector;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		this.ID = iD;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getColour() {
		return colour;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public ClickHandler fClick() {
		return new ClickHandler(URL, active, txtLab, signMarker, cMarker);
	}

	public abstract int getMaxObjects();

	public MixVector getCMarker() {
		return cMarker;
	}

	public MixVector getSignMarker() {
		return signMarker;
	}

	public boolean getUnderline() {
		return underline;
	}

	public String getTitle() {
		return title;
	}

	public abstract DrawCommand[] remoteDraw();
	
	public void update(Location curGPSFix) {
		//��ǰλ����Ϣת��Ϊ����
		
		// An elevation of 0.0 probably means that the elevation of the
		// POI is not known and should be set to the users GPS height
		// Note: this could be improved with calls to
		// http://www.geonames.org/export/web-services.html#astergdem
		// to estimate the correct height with DEM models like SRTM, AGDEM or
		// GTOPO30
		//���θ߶�Ϊ0��ζ��POI�в���ȷ��������Ϊ�û�GPS�߶�
		if (mGeoLoc.getAltitude() == 0.0)
			mGeoLoc.setAltitude(curGPSFix.getAltitude());

		//�����û������POI������
		PhysicalPlace.convLocToVec(curGPSFix, mGeoLoc, locationVector);
	}

	public void calcPaint(Camera viewCam, float addX, float addY) {
		cCMarker(origin, viewCam, addX, addY);
		calcV(viewCam);
	}
	
	private void cCMarker(MixVector originalPoint, Camera viewCam, float addX, float addY) {
		// Temp properties
		//����ת������Ļ
		MixVector tmpa = new MixVector(originalPoint);//ԭ������(0,0,0)
		MixVector tmpc = new MixVector(upV);//����������(0,1,0)
		tmpa.add(locationVector); //3 ����λ������
		tmpc.add(locationVector); //3����λ������
		tmpa.sub(viewCam.lco); //4��ȥ��0,0,0��
		tmpc.sub(viewCam.lco); //4��ȥ��0,0,0��
		tmpa.prod(viewCam.transform); //5�����������Ա�׼��������1,0,0,0,1,0,0,0,1��
		tmpc.prod(viewCam.transform); //5  ���������Ա�׼��������1,0,0,0,1,0,0,0,1��
		Log.v("orgPoint","ZHUAN:"+originalPoint.x+" ; "+originalPoint.y+" ; "+originalPoint.z+" ; ");
		Log.v("Camera","ZHUAN:"+ viewCam.toString());
		Log.v("locationVector","ZHUAN:"+locationVector.x+" ; "+locationVector.y+" ; "+locationVector.z+" ; ");
		Log.v("tmpa","ZHUAN:"+tmpa.x+" ; "+tmpa.y+" ; "+tmpa.z+" ; ");
		Log.v("tmpc","ZHUAN:"+tmpc.x+" ; "+tmpc.y+" ; "+tmpc.z+" ; ");
         //����a,cת�����������ϵ
		MixVector tmpb = new MixVector();
		viewCam.projectPoint(tmpa, tmpb, addX, addY); //6,ת������Ļ��
		cMarker.set(tmpb); //7
		Log.v("Cmarker","ZHUAN:"+tmpb.x+" ; "+tmpb.y+" ; "+tmpb.z+" ; ");
		viewCam.projectPoint(tmpc, tmpb, addX, addY); //6,ת������Ļ��
		signMarker.set(tmpb); //7
		Log.v("signMarker","ZHUAN:"+tmpb.x+" ; "+tmpb.y+" ; "+tmpb.z+" ; ");
	}

	private void calcV(Camera viewCam) {
		//�Ƿ���ʾ
		//�����裡����������������������
		isVisible = false;
		
		if (cMarker.z < -1f) {
			isVisible = true;

			//if (MixUtils.pointInside(cMarker.x, cMarker.y, 0, 0,
			//		viewCam.width, viewCam.height)) {
			//}
		}
	}
	
	public void setTxtLab(Label txtLab) {
		this.txtLab = txtLab;
	}

	public Label getTxtLab() {
		return txtLab;
	}
	
	public void setExtras(String name, ParcelableProperty parcelableProperty){
		//can be overriden
	}
	
	public void setExtras(String name, PrimitiveProperty primitiveProperty){
		//can be overriden
	}
	

}
