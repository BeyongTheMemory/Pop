package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class MessageNoticeActivity extends Activity implements
OnClickListener{
	private LinearLayout linearlayout_remindtime, linearlayout_remindtime2,
	linearlayout_everydayremind, linearlayout_activitynotice;
private ImageView imageview_back, imageview_everydayremind,
	imageview_activitynotice;
//private TextView textview_back, textview_remindtimeshow;
private TextView  textview_remindtimeshow;
public void onCreate(Bundle savedInstanceState) {
	Window window = this.getWindow();
    // »•±ÍÃ‚¿∏
   window.requestFeature(window.FEATURE_NO_TITLE);
	super.onCreate(savedInstanceState);
	init();
}

void init() {
setContentView(R.layout.messagenotice_activity);
linearlayout_remindtime = (LinearLayout) findViewById(R.id.linearlayout_remindtime);
imageview_back = (ImageView) findViewById(R.id.imageview_messageremindback);
imageview_everydayremind = (ImageView) findViewById(R.id.imageview_everydayremind);
imageview_activitynotice = (ImageView) findViewById(R.id.imageview_activitynotice);
//textview_back = (TextView) findViewById(R.id.textview_messagemindback);
linearlayout_remindtime2 = (LinearLayout) findViewById(R.id.linearLayout_remindtime2);
textview_remindtimeshow = (TextView) findViewById(R.id.textview_remindtimeshow);
linearlayout_everydayremind = (LinearLayout) findViewById(R.id.linearlayout_everydayremind);
linearlayout_activitynotice = (LinearLayout) findViewById(R.id.linearlayout_activitynotice);

imageview_back.setOnClickListener(this);
imageview_everydayremind.setOnClickListener(this);
imageview_activitynotice.setOnClickListener(this);
//textview_back.setOnClickListener(this);
linearlayout_remindtime2.setOnClickListener(this);
textview_remindtimeshow.setOnClickListener(this);
linearlayout_everydayremind.setOnClickListener(this);
linearlayout_activitynotice.setOnClickListener(this);

}

int i = 1;
int j = 1;

public void onClick(View v) {
switch (v.getId()) {
case R.id.imageview_messageremindback:
//case R.id.textview_messagemindback:
//	finish();
//	break;
case R.id.linearlayout_everydayremind:
case R.id.imageview_everydayremind:
	i = -i;
	if (i == -1) {
		imageview_everydayremind
				.setBackgroundResource(R.drawable.bg_settings_drag_on);
		linearlayout_remindtime.setVisibility(View.VISIBLE);
	} else {
		imageview_everydayremind
				.setBackgroundResource(R.drawable.bg_settings_drag_off);
		linearlayout_remindtime.setVisibility(View.GONE);
	}
	break;
case R.id.linearLayout_remindtime2:
case R.id.textview_remindtimeshow:

	TimePickerDialog dialog = new TimePickerDialog(this, listener, 0,
			0, true);
	dialog.show();
	break;
case R.id.linearlayout_activitynotice:
case R.id.imageview_activitynotice:
	j = -j;
	if (j == -1) {
		imageview_activitynotice
				.setBackgroundResource(R.drawable.bg_settings_drag_on);
	} else {
		imageview_activitynotice
				.setBackgroundResource(R.drawable.bg_settings_drag_off);
	}

default:
	break;
}

}

OnTimeSetListener listener = new OnTimeSetListener() {

public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	if (minute < 10) {
		textview_remindtimeshow.setText(hourOfDay + ":0" + minute);
	} else {
		textview_remindtimeshow.setText(hourOfDay + ":" + minute);
	}

}
};

}
