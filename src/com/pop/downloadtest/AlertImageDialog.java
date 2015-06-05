package com.pop.downloadtest;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pop.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class AlertImageDialog {

	private Context context;
	private Bitmap bitmap;
	private ImageView imageView;
	private Dialog dialog;
	private LayoutInflater inflater;
	private ImageMemoryCache imageCache;
	private ImageFileCache imageFileCache;
	private View largeVIew;
	
	public AlertImageDialog(Context context, Bitmap bitmap){
		this.context = context;
		this.bitmap = bitmap;
		System.out.println(bitmap);
		inflater = LayoutInflater.from(context);
		imageCache = new ImageMemoryCache(context);
		imageFileCache = new ImageFileCache();
	}
	
	public void show(String type, String id){
		creat(type, id);
	}
	
	public void creat(String type, String id){
		largeVIew = inflater.inflate(R.layout.dialog_image, null);
		dialog = new Dialog(context,R.style.Dialog_Fullscreen);
		dialog.setContentView(largeVIew);
		imageView = (ImageView) dialog.findViewById(R.id.large_image);
		imageView.setImageBitmap(bitmap);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		});
		largeVIew.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		});
		dialog.show();
		new MyAsyncTask().execute(type, id);
	}
	
	public class MyAsyncTask extends AsyncTask<String, Void,String>{

		protected void onPreExecute(){
			super.onPreExecute();
		}
		
		String nameURL = "http://121.40.120.82:8080/PopService/GetPicNameServlet";
		String picURl = "http://121.40.120.82:8080/PopService/GetPictureServlet";
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpTools httpTools = new HttpTools();
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("type", params[0]));
			param.add(new BasicNameValuePair("id", params[1]));
			
			String imageName = httpTools.sendPostResult(nameURL, param);
			//System.out.println(imageName);
			Bitmap tmpBitmap = imageCache.getBitmapFromCache(imageName);
			System.out.println("�ڴ滺��" + tmpBitmap);
			
			if (tmpBitmap == null){
				//���ļ������л�ȡ
				tmpBitmap = imageFileCache.getImage(imageName);
				System.out.println("����" + tmpBitmap);
				if (tmpBitmap == null){
					//����ͼƬ
					tmpBitmap = httpTools.downLoadPic(picURl, param);
					if (tmpBitmap != null){
						//��ӵ��ļ����ڴ滺����
						//imageFileCache.saveBitmap(tmpBitmap, imageName);
						imageCache.addBitmapToCache(imageName, tmpBitmap);
					}else{
						System.out.println("error");
						return "error";
					}
				}else{
					//��ӵ��ڴ滺����
					imageCache.addBitmapToCache(imageName, tmpBitmap);
				}
			}
			bitmap = tmpBitmap;
			
			System.out.println(bitmap);
			return "success";
		}
		
		/*���������doInBackground����
		 * ������ui�߳��У����Ը���ui����
		 * ����result��doInBackgroud���ص�ֵ
		 */
		protected void onPostExecute(String result){
			if (result.equals("success")){
				imageView.setImageBitmap(bitmap);	
			}else{
				Toast.makeText(context, "����ʧ��!", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
