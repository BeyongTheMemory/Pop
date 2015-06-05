
package com.pop.lib.marker.draw;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pop.lib.gui.PaintScreen;
import com.pop.lib.render.MixVector;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This drawcommand class, can be implemented by subclasses that can draw items on the screen
 * through the plugin. This class can be created on the plugin side, and is responsible
 * for transfering objects to the "core" side, through the parcelable interface. 
 * The main task for this class are
 * - storing values in the properties attribute, 
 * - converting it and send to the "core" (through the aidl structure and parcelable interface)
 * - reverting the original (implentation) class in the "core" that can draw items on the screen *
 * 通过该类的子类，可以在屏幕上绘制图像
 * 该类的主要任务是
 * -成员属性存储变量
 * -转换并发送（通过AIDL结构和Parcelable接口） 
 * -还原，以便在屏幕上绘制
 * @author A.Egal
 *
 */
public abstract class DrawCommand implements Parcelable{

	private String command;
	
	private final Map<String, Object> properties=new LinkedHashMap<String, Object>();
	
	protected DrawCommand(String command){
		this.command=command;
	}
	
	protected void setProperty(String key,Object value){
		properties.put(key, value);
	}
	
	public Object getProperty(String key){
		return properties.get(key);
	}
	
	public abstract void draw(PaintScreen dw);
	

	
	Boolean getBooleanProperty(String key){
		return (Boolean)properties.get(key);
	}
	
	MixVector getMixVectorProperty(String key){
		return (MixVector)getParcelableProperty(key);
	}
	
	Bitmap getBitmapProperty(String key){
		return (Bitmap)getParcelableProperty(key);
	}
	
	String getStringProperty(String key){
		return (String)properties.get(key);
	}
	
	Float getFloatProperty(String key){
		return (Float)properties.get(key);
	}
	
	Integer getIntegerProperty(String key){
		return (Integer)properties.get(key);
	}
	
	Double getDoubleProperty(String key){
		return (Double)properties.get(key);
	}
	
	byte[] getByteArrayProperty(String key){
		return (byte[])properties.get(key);
	}
	
	Parcelable getParcelableProperty(String key){
		Parcelable o = (Parcelable)properties.get(key);
		if(o instanceof ParcelableProperty){
			return ((ParcelableProperty)o).getObject();
		}
		return o;
	}
	
	public static final Parcelable.Creator<DrawCommand> CREATOR = new Parcelable.Creator<DrawCommand>() {
		public DrawCommand createFromParcel(Parcel in) {
			return DrawCommand.buildObject(in);
		}
		
		public DrawCommand[] newArray(int size) {
			return new DrawCommand[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(command);
		for (String s: properties.keySet()) {
			
            if(properties.get(s) instanceof String){
            	dest.writeString(getStringProperty(s));
            } 
            else if(properties.get(s) instanceof Boolean){
            	dest.writeString(String.valueOf(getBooleanProperty(s)));
            }
			else if(properties.get(s) instanceof Float){
				dest.writeFloat(getFloatProperty(s));
			}			            	
			else if(properties.get(s) instanceof Integer){
				dest.writeInt(getIntegerProperty(s));
			}
			else if(properties.get(s) instanceof Double){
				dest.writeDouble(getDoubleProperty(s));
			}
			else if(properties.get(s) instanceof Byte[]){
				dest.writeByteArray(getByteArrayProperty(s));
			}
			else if(properties.get(s) instanceof ParcelableProperty){
				dest.writeParcelable((ParcelableProperty)properties.get(s), 0);
			}
        }
	}
	
	@SuppressWarnings("rawtypes")
	public static DrawCommand buildObject(Parcel in){
		String className = in.readString();
		String methodName = "init";
		Class[] args = new Class[1];
		args[0] = Parcel.class;
		try {
			Method m = Class.forName(className).getMethod(methodName, args);
			return (DrawCommand) m.invoke(null, in);			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
}
