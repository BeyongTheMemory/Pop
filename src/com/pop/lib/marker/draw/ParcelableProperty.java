
package com.pop.lib.marker.draw;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A draw property which contains a parcelable object with its classname so it
 * can be loaded. This class should be used, when a plugin wants to send a 'unknown' parcelable object to the core
 * This is needed because the classname of a parcel should be known before the core can convert that into an object.
 * 一个绘制性能包括其className Parcelable对象因此
 *可以加载。这个类应该被使用，当一个插件要发送一个“未知”Parcelable对象的核心
 *这是因为包裹的类名前应将核心可以转换成一个对象。
 * @author A. Egal
 */
public class ParcelableProperty implements Parcelable {

	private String className;
	private Parcelable object;

	public ParcelableProperty(String className, Parcelable object) {
		this.className = className;
		this.object = object;
	}

	public static final Parcelable.Creator<ParcelableProperty> CREATOR = new Parcelable.Creator<ParcelableProperty>() {
		public ParcelableProperty createFromParcel(Parcel in) {
			return new ParcelableProperty(in);
		}

		public ParcelableProperty[] newArray(int size) {
			return new ParcelableProperty[size];
		}
	};

	private ParcelableProperty(Parcel in) {
		className = in.readString();
		object = in.readParcelable(getClassLoader());
	}

	public ClassLoader getClassLoader() {
		try {
			return Class.forName(className).getClassLoader();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException();
		}
	}

	public Parcelable getObject() {
		return object;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(className);
		dest.writeParcelable(object, 0);
	}

}
