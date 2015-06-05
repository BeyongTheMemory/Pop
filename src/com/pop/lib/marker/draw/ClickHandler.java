
package com.pop.lib.marker.draw;

import com.pop.lib.MixContextInterface;
import com.pop.lib.MixStateInterface;
import com.pop.lib.MixUtils;
import com.pop.lib.gui.Label;
import com.pop.lib.gui.ScreenLine;
import com.pop.lib.render.MixVector;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A click validator class to handle the clicks on the core side, 
 * the plugin will create a click validator and send it to the core
 * And the core will validate the click and then reacts on the click. 
 * This is done because we are unable to send a MixContext and
 * a MixState to the plugin because they cannot be parselable. 
 * --
 * The main function of this class is to be a container of objects, sending
 * them to the core, and use them to check for clicks and handle it.
 * @author A. Egal
 * 管理单击事件的类
 * 插件将创建一个点击验证器发送到核心
 * 核心将验证点击再点击。
 * 这是因为我们无法发送mixcontext和mixstate到插件，因其未实现parcelable接口
 * 函数的主要功能是将一个对象的容器，发送
 * 他们的核心，并利用它们来检查点击和处理。
 */
public class ClickHandler implements Parcelable{

	private String url;
	private boolean active;
	private Label txtLab;
	private MixVector signMarker;
	private MixVector cMarker;
	private final ScreenLine pPt = new ScreenLine();
	
	public ClickHandler(String url, boolean active, Label txtLab, MixVector signMarker, MixVector cMarker){
		this.url = url;
		this.active = active;
		this.txtLab = txtLab;
		this.signMarker = signMarker;
		this.cMarker = cMarker;		
	}
	
	public ClickHandler(Parcel in){
		readFromParcel(in);
	}
	
	/**
	 * A click handler without a click valid check.
	 * 没有做有效性检测的点击处理函数
	 */
	public boolean fakeClick(MixContextInterface ctx, MixStateInterface state){
		return state.handleEvent(ctx, url);
	}
	
	/**进行了有效性检测的点击处理函数*/
	public boolean handleClick(float x, float y, MixContextInterface ctx, MixStateInterface state){
		if(isClickValid(x, y)){
			return state.handleEvent(ctx, url);
		}
		return false;
	}
	
	/**判断是否在点击范围内*/
	private boolean isClickValid(float x, float y) {
	
		float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y,
				signMarker.x, signMarker.y);
		//if the marker is not active (i.e. not shown in AR view) we don't have to check it for clicks
		//AR未显示时不需检测点击
		if (!active)
			return false;

		//TODO adapt the following to the variable radius!
		//自适应后的可变半径
		//根据方框坐标进行判定点击是否有效
		pPt.x = x - signMarker.x;
		pPt.y = y - signMarker.y;
		pPt.rotate((float) Math.toRadians(-(currentAngle + 90)));
		pPt.x += txtLab.getX();
		pPt.y += txtLab.getY();

		float objX = txtLab.getX() - txtLab.getWidth() / 2;
		float objY = txtLab.getY() - txtLab.getHeight() / 2;
		float objW = txtLab.getWidth();
		float objH = txtLab.getHeight();

		if (pPt.x > objX && pPt.x < objX + objW && pPt.y > objY
				&& pPt.y < objY + objH) {
			return true;
		} else {
			return false;
		}
	}
	
	public static final Parcelable.Creator<ClickHandler> CREATOR = new Parcelable.Creator<ClickHandler>() {
		public ClickHandler createFromParcel(Parcel in) {
			return new ClickHandler(in);
		}
		
		public ClickHandler[] newArray(int size) {
			return new ClickHandler[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);	
		dest.writeString(String.valueOf(active));	
		dest.writeParcelable(txtLab, 0);
		dest.writeParcelable(signMarker, 0);
		dest.writeParcelable(cMarker, 0);
	}
	
	public void readFromParcel(Parcel in){
		url = in.readString();
		active = Boolean.valueOf(in.readString());
		txtLab = in.readParcelable(Label.class.getClassLoader());
		signMarker = in.readParcelable(MixVector.class.getClassLoader());
		cMarker = in.readParcelable(MixVector.class.getClassLoader());
	}

}
