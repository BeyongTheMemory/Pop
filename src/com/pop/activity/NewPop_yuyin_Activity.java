package com.pop.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;





import com.pop.R;
import com.pop.http.Communication;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * ʵ�ְ�ס˵������
 * ��ס˵������ʼ¼����ֹͣ¼������ʾ���б�����б���š�
 */
public class NewPop_yuyin_Activity extends Activity {
	/** log��� */
	private static final String LOG_TAG = "AudioRecordTest";
	/** �����ļ�����·�� */
	private String mFileName = null;
	/** ��ס˵����ť */
	private ImageView mBtnVoice;
	/** ������������ */
	private MediaPlayer mPlayer = null;
	/** �������¼�� */
	private MediaRecorder mRecorder = null;
	/** ��ʾ�����б� */
	/** �����б� */
	/** ¼���洢·�� */
	private static final String PATH = "/sdcard/MyVoiceForder/Record/";
	
	private TextView uploadButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newpop_yuyin_activity);
		initData();
		initView();
	}

	/** ��ʼ���ؼ� */
	private void initView() {
//		mVoidListView = (ListView) findViewById(R.id.voidList);
//		mAdapter = new MyListAdapter(this);
//		mVoidListView.setAdapter(mAdapter);
//		mVoidListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view,
//					int position, long arg3) {
//				try {
//					mPlayer.reset();
//					mPlayer.setDataSource(mVoicesList.get(position));
//					mPlayer.prepare();
//					mPlayer.start();
//				} catch (IOException e) {
//					Log.e(LOG_TAG, "����ʧ��");
//				}
//			}
//		});
		mBtnVoice = (ImageView) findViewById(R.id.yuyinimageView1);
		//mBtnVoice.setText("��ס˵��");
		mBtnVoice.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startVoice();
					break;
				case MotionEvent.ACTION_UP:
					stopVoice();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		uploadButton = (TextView) findViewById(R.id.writePrivacy_text);
		uploadButton.setOnClickListener(new UploadListener());
	}

	/** ��ʼ������ */
	private void initData() {
		//mVoicesList = new ArrayList<String>();
		mPlayer = new MediaPlayer();
	}

	/** ��ʼ¼�� */
	private void startVoice() {
		// ����¼������·��
		mFileName = PATH + UUID.randomUUID().toString() + ".amr";
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Log.i(LOG_TAG, "SD Card is not mounted,It is  " + state + ".");
		}
		File directory = new File(mFileName).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			Log.i(LOG_TAG, "Path to file could not be created");
		}
		Toast.makeText(getApplicationContext(), "��ʼ¼��", 0).show();
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		mRecorder.start();
	}

	/** ֹͣ¼�� */
	private void stopVoice() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
//		mVoicesList.add(mFileName);
//		mAdapter = new MyListAdapter(RecordActivity.this);
//		mVoidListView.setAdapter(mAdapter);
		Toast.makeText(getApplicationContext(), "����¼��" + mFileName, 0).show();
	}
	
    private class UploadListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Communication com = new Communication();
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("username", "a");
			map.put("latitude", 123.45156);
			map.put("logitude", 38.11);
			map.put("height", 2.35); 
			map.put("info", "");
			final String url = "http://121.40.120.82:8080/PopService/AmrServlet";
			final String imageName = "yuyin";
			new Thread(){
				public void run() {
					String result = com.communication02(url, map, mFileName, imageName);
					System.out.println(result);
					
					//Message msg = handler.obtainMessage();
				}
			}.start();
			NewPop_yuyin_Activity.this.finish();
		}
    	
    }

//	/** �����б������� */
//	private class MyListAdapter extends BaseAdapter {
//		LayoutInflater mInflater;
//
//		public MyListAdapter(Context context) {
//			mInflater = LayoutInflater.from(context);
//		}
//
//		@Override
//		public int getCount() {
//			return mVoicesList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//
//			return position;
//		}
//
//		@Override
//		public long getItemId(int position) {
//
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			convertView = mInflater.inflate(R.layout.item_voicelist, null);
//			TextView tv = (TextView) convertView.findViewById(R.id.tv_armName);
//			tv.setText(mVoicesList.get(position));
//			return convertView;
//		}
//	}
}