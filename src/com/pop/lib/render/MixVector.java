
package com.pop.lib.render;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.FloatMath;

/**
 * This class holds information of a point in a three-dimensional coordinate
 * system. It holds the values for the x-, y- and z-axis, which can be modified
 * through several methods. (for example adding and subtracting points) The
 * distance from the origin of the coordinate system to the point represents the
 * vector. The application uses vectors to describe distances on the map.
 * 三维坐标点信息，程序使用该向量描述其在地图上的距离
 * @author daniele
 * 
 */
public class MixVector implements Parcelable{
	public float x;
	public float y;
	public float z;

	public MixVector() {
		this(0, 0, 0);
	}

	public MixVector(MixVector v) {
		this(v.x, v.y, v.z);
	}

	public MixVector(float v[]) {
		this(v[0], v[1], v[2]);
	}

	public MixVector(float x, float y, float z) {
		set(x, y, z);
	}

	public MixVector(Parcel in){
		readParcel(in);
	}

	public static final Parcelable.Creator<MixVector> CREATOR = new Parcelable.Creator<MixVector>() {
		public MixVector createFromParcel(Parcel in) {
			return new MixVector(in);
		}

		public MixVector[] newArray(int size) {
			return new MixVector[size];
		}
	};

	@Override
	public boolean equals(Object obj) {
		MixVector v = (MixVector) obj;
		return (v.x == x && v.y == y && v.z == z);
	}

	public boolean equals(float x, float y, float z) {
		return (this.x == x && this.y == y && this.z == z);
	}
	
	@Override
	public int hashCode() {
		Float xf = x;
		Float yf = y;
		Float zf = z;
		return xf.hashCode()+yf.hashCode()+zf.hashCode();
	}

	@Override
	public String toString() {
		return "<" + x + ", " + y + ", " + z + ">";
	}

	public void set(MixVector v) {
		set(v.x, v.y, v.z);
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	 /**向量相加*/
	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
    /**向量相加*/
	public void add(MixVector v) {
		add(v.x, v.y, v.z);
	}

	public void sub(float x, float y, float z) {
		add(-x, -y, -z);
	}

	public void sub(MixVector v) {
		add(-v.x, -v.y, -v.z);
	}

	public void mult(float s) {
		x *= s;
		y *= s;
		z *= s;
	}

	public void divide(float s) {
		x /= s;
		y /= s;
		z /= s;
	}

	public float length() {
		//长度
		return (float) FloatMath.sqrt(x * x + y * y + z * z);
	}

	public float length2D() {
		return (float) FloatMath.sqrt(x * x + z * z);
	}

	public void norm() {
		//标准化
		divide(length());
	}

	public float dot(MixVector v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public void cross(MixVector u, MixVector v) {
		//叉积，得正交向量
		float x = u.y * v.z - u.z * v.y;
		float y = u.z * v.x - u.x * v.z;
		float z = u.x * v.y - u.y * v.x;
		this.x = x;
		this.y = y;
		this.z = z;
	}
    
	/**矩阵乘以向量*/
	public void prod(Matrix m) {
		//矩阵乘以向量
		float xTemp = m.a1 * x + m.a2 * y + m.a3 * z;
		float yTemp = m.b1 * x + m.b2 * y + m.b3 * z;
		float zTemp = m.c1 * x + m.c2 * y + m.c3 * z;

		x = xTemp;
		y = yTemp;
		z = zTemp;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(x);
		dest.writeFloat(y);
		dest.writeFloat(z);
	}

	public void readParcel(Parcel in) {
		x = in.readFloat();
		y = in.readFloat();
		z = in.readFloat();
	}
}