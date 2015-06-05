package com.pop.activity.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {
	private boolean judge = true;//判断
	private String showWords = "";//默认显示的内容
	public MyEditText(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
	
	public MyEditText(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
		// TODO 自动生成的构造函数存根
	}
	
	public MyEditText(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO 自动生成的构造函数存根
	}
	
	protected void onDraw(Canvas canvas){
		if(judge){
		Paint paint = new Paint();
		paint.setTextSize(20);
		paint.setColor(Color.GRAY);
		canvas.drawText(showWords, 10, getHeight()/2 + 5, paint);
		}
		else{
			Paint paint = new Paint();
			paint.setTextSize(20);
			paint.setColor(Color.GRAY);
			canvas.drawText("", 10, getHeight()/2 + 5, paint);
		}
		super.onDraw(canvas);
	}
	
	public boolean getJudge(){
		return judge;
	}
	
	
	public void setJudge(boolean judge){
		this.judge = judge;
	}

	public String getShowWords() {
		return showWords;
	}

	public void setShowWords(String showWords) {
		this.showWords = showWords;
	}
	
	
	

}
