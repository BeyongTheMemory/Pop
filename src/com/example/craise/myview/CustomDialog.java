package com.example.craise.myview;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class CustomDialog extends Dialog {
	
	private static int default_width=240;//默认宽度
	private static int default_height=360;//默认高度
	
	public CustomDialog(Context context, int layout, int style) {
		this(context, default_width, default_height, layout, style);
	}

	public CustomDialog(Context context, int width, int height, int layout,
			int style){
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		float density = getDensity(context);
		params.width = (int) (width * density);
		params.height = (int) (height * density);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}
	
	
  public class MyBuilder extends Builder{

		public MyBuilder(Context arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		@Override
		public AlertDialog create() {
			// TODO Auto-generated method stub
			return super.create();
		}

		@Override
		public Builder setCancelable(boolean cancelable) {
			// TODO Auto-generated method stub
			return super.setCancelable(cancelable);
		}
		
    }
}
