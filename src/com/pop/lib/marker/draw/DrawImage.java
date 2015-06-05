
package com.pop.lib.marker.draw;

import com.pop.lib.gui.PaintScreen;
import com.pop.lib.render.MixVector;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Parcel;
import android.util.Log;

/**
 * A draw command that can be send by a plugin marker to draw an image on the client.
 * This class extends the DrawCommand, that stores the properties, so that it can be
 * transfered to the client.
 * 用于在绘制插件标记中的image
 * 继承自DrawCommand
 * 有更好的性能
 * @author A. Egal
 */
public class DrawImage extends DrawCommand{
	
	private static String CLASS_NAME = DrawImage.class.getName();
	
	private static String PROPERTY_NAME_VISIBLE = "visible";
	private static String PROPERTY_NAME_SIGNMARKER = "signMarker";
	private static String PROPERTY_NAME_IMAGE = "image";
	//private static String PROPERTY_NAME_DISTANCE = "distance";
	private boolean isScale = false;
	
	public static DrawImage init(Parcel in){
		Boolean visible = Boolean.valueOf(in.readString());
		ParcelableProperty signMarkerHolder = in.readParcelable(ParcelableProperty.class.getClassLoader());
		ParcelableProperty bitmapHolder = in.readParcelable(ParcelableProperty.class.getClassLoader());
		return new DrawImage(visible, (MixVector)signMarkerHolder.getObject(), (Bitmap)bitmapHolder.getObject());
	}
	
	public DrawImage(boolean visible,MixVector signMarker, Bitmap image) {
		super(CLASS_NAME);
		setProperty(PROPERTY_NAME_VISIBLE, visible);
		setProperty(PROPERTY_NAME_SIGNMARKER, new ParcelableProperty("org.mixare.lib.render.MixVector", signMarker));
		setProperty(PROPERTY_NAME_IMAGE,  new ParcelableProperty("android.graphics.Bitmap",image));
	}
	
//	public DrawImage(boolean visible,MixVector signMarker, Bitmap image,double distance) {
//		super(CLASS_NAME);
//		setProperty(PROPERTY_NAME_VISIBLE, visible);
//		setProperty(PROPERTY_NAME_SIGNMARKER, new ParcelableProperty("org.mixare.lib.render.MixVector", signMarker));
//		setProperty(PROPERTY_NAME_IMAGE,  new ParcelableProperty("android.graphics.Bitmap",image));
//		setProperty(PROPERTY_NAME_DISTANCE, distance);
//	}
//	
	
	@Override
	public void draw(PaintScreen dw){
		
		if (getBooleanProperty(PROPERTY_NAME_VISIBLE)) {
//			double distance = getDoubleProperty(PROPERTY_NAME_DISTANCE);
//			Log.v("dis","XDIS" + distance);
			MixVector signMarker = getMixVectorProperty(PROPERTY_NAME_SIGNMARKER);
			Bitmap bitmap = getBitmapProperty(PROPERTY_NAME_IMAGE);
			
			dw.setColor(Color.argb(155, 255, 255, 255));
			if(bitmap == null){
				Log.e("mixare-lib", "bitmap = null");
				return;
			}
			
			//透明度80%，越低越透明
			dw.paintBitmap(bitmap, signMarker.x - (bitmap.getWidth()/2), signMarker.y - (bitmap.getHeight() / 2),100);
		
		}
	}	
	
	
	
}
