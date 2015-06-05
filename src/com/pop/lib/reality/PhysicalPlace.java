
package com.pop.lib.reality;

import com.pop.lib.render.MixVector;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 存储地理信息（经度和纬度，高度）
 *已知两地距离得到目标经纬度，大圆定理
 * 将地理位置转化为向量，以及其逆运算
 */

public class PhysicalPlace implements Parcelable {

	double latitude;
	double longitude;
	double altitude;

	public PhysicalPlace() {

	}

	public static final Parcelable.Creator<PhysicalPlace> CREATOR = new Parcelable.Creator<PhysicalPlace>() {
		public PhysicalPlace createFromParcel(Parcel in) {
			return new PhysicalPlace(in);
		}

		public PhysicalPlace[] newArray(int size) {
			return new PhysicalPlace[size];
		}
	};

	public PhysicalPlace(Parcel in){
		readParcel(in);
	}

	public PhysicalPlace(PhysicalPlace pl) {
		this.setTo(pl.latitude, pl.longitude, pl.altitude);
	}

	public PhysicalPlace(double latitude, double longitude, double altitude) {
		this.setTo(latitude, longitude, altitude);
	}

	public void setTo(double latitude, double longitude, double altitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	public void setTo(PhysicalPlace pl) {
		this.latitude = pl.latitude;
		this.longitude = pl.longitude;
		this.altitude = pl.altitude;
	}

	@Override
	public String toString() {
		return "(lat=" + latitude + ", lng=" + longitude + ", alt=" + altitude
				+ ")";
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
    
    
    //已知两地距离得到目标经纬度，大圆定理
	public static void calcDestination(double lat1Deg, double lon1Deg,
			double bear, double d, PhysicalPlace dest) {
	
       
		double brng = Math.toRadians(bear);
		double lat1 = Math.toRadians(lat1Deg);
		double lon1 = Math.toRadians(lon1Deg);
		double R = 6371.0 * 1000.0;

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R)
				+ Math.cos(lat1) * Math.sin(d / R) * Math.cos(brng));
		double lon2 = lon1
				+ Math.atan2(Math.sin(brng) * Math.sin(d / R) * Math.cos(lat1),
						Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2));

		dest.setLatitude(Math.toDegrees(lat2));
		dest.setLongitude(Math.toDegrees(lon2));
	}
    
    //将地理位置转化为向量
	public static void convLocToVec(Location org, PhysicalPlace gp, MixVector v) {
		float[] z = new float[1];
		z[0] = 0;
		Location.distanceBetween(org.getLatitude(), org.getLongitude(),
				gp.getLatitude(), org.getLongitude(), z);
		float[] x = new float[1];
		Location.distanceBetween(org.getLatitude(), org.getLongitude(),
				org.getLatitude(), gp.getLongitude(), x);
		double y = gp.getAltitude() - org.getAltitude();
		if (org.getLatitude() < gp.getLatitude())
			z[0] *= -1;
		if (org.getLongitude() > gp.getLongitude())
			x[0] *= -1;

		v.set(x[0], (float) y, z[0]);
	}
    
    //将向量转换成地理位置信息
	public static void convertVecToLoc(MixVector v, Location org, Location gp) {
		double brngNS = 0, brngEW = 90;
		if (v.z > 0)
			brngNS = 180;
		if (v.x < 0)
			brngEW = 270;

		PhysicalPlace tmp1Loc = new PhysicalPlace();
		PhysicalPlace tmp2Loc = new PhysicalPlace();
		PhysicalPlace.calcDestination(org.getLatitude(), org.getLongitude(),
				brngNS, Math.abs(v.z), tmp1Loc);
		PhysicalPlace.calcDestination(tmp1Loc.getLatitude(),
				tmp1Loc.getLongitude(), brngEW, Math.abs(v.x), tmp2Loc);

		gp.setLatitude(tmp2Loc.getLatitude());
		gp.setLongitude(tmp2Loc.getLongitude());
		gp.setAltitude(org.getAltitude() + v.y);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeDouble(altitude);
	}

	public void readParcel(Parcel in) {
		latitude = in.readDouble();
		longitude = in.readDouble();
		altitude = in.readDouble();
	}
	
	/**计算两点距离*/
	public static double distanceBetween(double startLatitude,double startLongitude,double endLatitude,double endLongitude){
		float r[] = new float[1];
		r[0] = 0;
		Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude,r);	
		return r[0];
	}
}