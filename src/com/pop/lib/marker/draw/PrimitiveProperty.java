
package com.pop.lib.marker.draw;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A draw property which contains a primitive value with its classname so it can
 * be loaded. This class should be used, when a plugin wants to send a 'unknown'
 * primitive to the core This is needed because the classname of a parcel should
 * be known before the core can convert that into an object.
 * 
 *绘制属性包含一个原始值与它的类名，所以它可以
 *被加载。这个类应该被使用，当一个插件要发送一个“未知”
 *原始的核心是因为包裹的类名应该
 *在核心可以转换成一个对象。
 * @author A. Egal
 */
public class PrimitiveProperty implements Parcelable {

	private String primitivename;
	private Object object;

	public enum primitive {
		STRING, INT, DOUBLE, FLOAT, LONG, BYTE;
	}

	public PrimitiveProperty(String primitivename, Object object) {
		this.primitivename = primitivename;
		this.object = object;
	}

	public PrimitiveProperty(Parcel in) {
		primitivename = in.readString();
		if (primitivename.equals(primitive.STRING.name())) {
			object = in.readString();
		}
		else if (primitivename.equals(primitive.INT.name())) {
			object = in.readInt();
		}
		else if (primitivename.equals(primitive.DOUBLE.name())) {
			object = in.readDouble();
		}
		else if (primitivename.equals(primitive.FLOAT.name())) {
			object = in.readFloat();
		}
		else if (primitivename.equals(primitive.LONG.name())) {
			object = in.readLong();
		}
		else if (primitivename.equals(primitive.BYTE.name())) {
			in.readByteArray((byte[])object);
		}
	}

	public static final Parcelable.Creator<PrimitiveProperty> CREATOR = new Parcelable.Creator<PrimitiveProperty>() {
		public PrimitiveProperty createFromParcel(Parcel in) {
			return new PrimitiveProperty(in);
		}

		public PrimitiveProperty[] newArray(int size) {
			return new PrimitiveProperty[size];
		}
	};

	public Object getObject() {
		return object;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(primitivename);
		if (primitivename.equals(primitive.STRING.name())) {
			dest.writeString((String)object);
		}
		else if (primitivename.equals(primitive.INT.name())) {
			dest.writeInt((Integer)object);
		}
		else if (primitivename.equals(primitive.DOUBLE.name())) {
			dest.writeDouble((Double)object);
		}
		else if (primitivename.equals(primitive.FLOAT.name())) {
			dest.writeFloat((Float)object);
		}
		else if (primitivename.equals(primitive.LONG.name())) {
			dest.writeLong((Long)object);
		}
		else if (primitivename.equals(primitive.BYTE.name())) {
			dest.writeByteArray((byte[])object);
		}
	}

}
