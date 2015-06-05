
package com.pop.lib.gui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class has the ability to set up the main view and it paints objects on the screen 
 * 建议主视图，描绘屏幕物体
 */

public class PaintScreen implements Parcelable{
	Canvas canvas;
	int width, height;
	Paint paint = new Paint();
	Paint bPaint = new Paint();

	public PaintScreen() {
		paint.setTextSize(16);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
	}

	public PaintScreen(Parcel in){
		readFromParcel(in);
		paint.setTextSize(16);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
	}

	public static final Parcelable.Creator<PaintScreen> CREATOR = new Parcelable.Creator<PaintScreen>() {
		public PaintScreen createFromParcel(Parcel in) {
			return new PaintScreen(in);
		}

		public PaintScreen[] newArray(int size) {
			return new PaintScreen[size];
		}
	};

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setFill(boolean fill) {
		if (fill)
			paint.setStyle(Paint.Style.FILL);
		else
			paint.setStyle(Paint.Style.STROKE);
	}

	public void setColor(int c) {
		paint.setColor(c);
	}

	public void setStrokeWidth(float w) {
		paint.setStrokeWidth(w);
	}

	public void paintLine(float x1, float y1, float x2, float y2) {
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	public void paintRect(float x, float y, float width, float height) {
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	public void paintRoundedRect(float x, float y, float width, float height) {//圆角边矩形
		//rounded edges. patch by Ignacio Avellino
		RectF rect = new RectF(x, y, x + width, y + height);
		canvas.drawRoundRect(rect, 15F, 15F, paint);
	}

	public void paintBitmap(Bitmap bitmap, float left, float top) {
		canvas.drawBitmap(bitmap, left, top, paint);
	}
	
	/**绘图带透明度的*/
	public void paintBitmap(Bitmap bitmap, float left, float top,int a) {
		 	            
		          paint .setStyle( Paint.Style.STROKE );   //空心  
	              paint .setAlpha( a );   // Bitmap透明度(0 ~ 100)  
		          canvas.drawBitmap(bitmap, left, top, paint);
	}

	public void paintPath(Path path,float x, float y, float width, float height, float rotation, float scale) {
		canvas.save();
		canvas.translate(x + width / 2, y + height / 2);
		canvas.rotate(rotation);
		canvas.scale(scale, scale);
		canvas.translate(-(width / 2), -(height / 2));
		canvas.drawPath(path, paint);
		canvas.restore();
	}

	public void paintCircle(float x, float y, float radius) {
		canvas.drawCircle(x, y, radius, paint);
	}

	public void paintText(float x, float y, String text, boolean underline) {
		paint.setUnderlineText(underline);
		canvas.drawText(text, x, y, paint);
	}

	public void paintObj(ScreenObj obj, float x, float y, float rotation,
			float scale) {
		canvas.save();//锁画布
		canvas.translate(x + obj.getWidth() / 2, y + obj.getHeight() / 2);//原点移动到指定地点
		canvas.rotate(rotation);//旋转
		canvas.scale(scale, scale);//放大，第一和第二个参数分别为水平和竖直方向的放大倍数
		canvas.translate(-(obj.getWidth() / 2), -(obj.getHeight() / 2));
		obj.paint(this);
		canvas.restore();
	}

	public float getTextWidth(String txt) {
		return paint.measureText(txt);
	}

	public float getTextAsc() {
		return -paint.ascent();
	}

	public float getTextDesc() {
		return paint.descent();
	}

	public float getTextLead() {
		return 0;
	}

	public void setFontSize(float size) {
		paint.setTextSize(size);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(width);
		dest.writeInt(height);
	}


	public void readFromParcel(Parcel in) {
		height = in.readInt();
		width  = in.readInt();
		canvas = new Canvas();
	}

}