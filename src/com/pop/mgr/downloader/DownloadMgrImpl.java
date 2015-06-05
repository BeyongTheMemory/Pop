
package com.pop.mgr.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pop.show.MixContext;
import com.pop.R;
import com.pop.activity.MainActivity;
import com.pop.http.HttpTools;
import com.pop.http.JsonTools;
import com.pop.lib.marker.Marker;
import com.pop.lib.reality.PhysicalPlace;
import com.pop.lib.marker.ImageMarker;











import com.pop.model.Pop;
import com.pop.model.PopList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
/**
 * 下载泡泡数据
 */
public class DownloadMgrImpl implements Runnable,DownloadManager {


	private MixContext ctx;
	private DownloadManagerState state = DownloadManagerState.Confused;
	//private LinkedBlockingQueue<ManagedDownloadRequest> todoList = new LinkedBlockingQueue<ManagedDownloadRequest>();//使用线程安全的阻塞队列
	private ConcurrentHashMap<String, DownloadResult> doneList = new ConcurrentHashMap<String, DownloadResult>();//线程安全的hashMap
	private List<Marker> markers;
	private boolean tag =true;

	public DownloadMgrImpl(MixContext ctx) {
		if (ctx == null) {
			throw new IllegalArgumentException("Mix Context IS NULL");
		}
		this.ctx = ctx;
		state=DownloadManagerState.OffLine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.downloader.DownloadManager#run()
	 */
	public void run() {
		if(tag){
			state=DownloadManagerState.OnLine;
		Log.v("aacc", "aaccshuaxin");
		double olatitude = ctx.getActualMixView().getDataView().getCurFix().getLatitude();	
		  double olongitude = ctx.getActualMixView().getDataView().getCurFix().getLongitude();
		  double altitude = ctx.getActualMixView().getDataView().getCurFix().getAltitude();
		  SharedPreferences mySharedPreferences = ctx.getActualMixView().getSharedPreferences("loacation", 
				  Activity.MODE_PRIVATE); 
		  SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		  editor.putString("latitude", olatitude+ ""); 
		  editor.putString("longitude", olongitude+""); 
		  editor.putString("altitude", altitude+""); 
		  editor.commit(); 
		  Log.v("aacc", "aaccshuaxin:"+olatitude);
		  Log.v("aacc", "aaccshuaxin:"+olongitude);
		  markers = new ArrayList<Marker>();
				HttpTools http = new HttpTools();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("latitude",olatitude+""));
				params.add(new BasicNameValuePair("logitude", olongitude+""));
				params.add(new BasicNameValuePair("range", 1+""));
				//Log.v("ok","caonima");
				Log.v("aacc","aacc:"+http.sendPostResult("http://121.40.120.82:8080/PopService/SearchPopServlet", params));
				String popString = http.sendPostResult("http://121.40.120.82:8080/PopService/SearchPopServlet", params);
				PopList popList = JsonTools.getPopFromResult(popString);			
			    List<Pop> adPops = popList.getAdList();
			   List<Pop> personPops = popList.getPersonalList();
			  List<Pop> pubPops = popList.getPublicList();
			   List<Pop> sightPops = popList.getSightList();
			if(adPops.size() > 0){
				for(int i =0;i< adPops.size();i++){
					Pop personPop = adPops.get(i); 
					ImageMarker imge = new ImageMarker(personPop.getId(),null,personPop.getLatitude(),personPop.getLongitude(),personPop.getHeight(),null,1,-1);
					imge.setDistance(PhysicalPlace.distanceBetween(olatitude, olongitude, imge.getLatitude(),imge.getLongitude()));
					imge.setBitmap(BitmapFactory.decodeResource(ctx.getActualMixView().getResources(),R.drawable.shangye));
					imge.setType(ImageMarker.shangye);
					imge.setPopid(personPop.getId());
					markers.add(imge);
				}
			}
			if(personPops.size() > 0){
				Log.v("aacc", "aacctianjia!!:tianjia!!");
				for(int i =0;i< personPops.size();i++){
					Pop personPop = personPops.get(i); 
					ImageMarker imge = new ImageMarker(personPop.getId(),null,personPop.getLatitude(),personPop.getLongitude(),personPop.getHeight(),null,1,-1);
					imge.setDistance(PhysicalPlace.distanceBetween(olatitude, olongitude, imge.getLatitude(),imge.getLongitude()));
					imge.setBitmap(BitmapFactory.decodeResource(ctx.getActualMixView().getResources(),R.drawable.geren));
					imge.setType(ImageMarker.geren);
					imge.setPopid(personPop.getId());
					markers.add(imge);
				}
			}
			if(pubPops.size() > 0){
				for(int i =0;i< pubPops.size();i++){
					Pop personPop = pubPops.get(i); 
					ImageMarker imge = new ImageMarker(personPop.getId(),null,personPop.getLatitude(),personPop.getLongitude(),personPop.getHeight(),null,1,-1);
					imge.setDistance(PhysicalPlace.distanceBetween(olatitude, olongitude, imge.getLatitude(),imge.getLongitude()));
					imge.setBitmap(BitmapFactory.decodeResource(ctx.getActualMixView().getResources(),R.drawable.gonggongxinxi));
					imge.setType(ImageMarker.gonggong);
					imge.setPopid(personPop.getId());
					markers.add(imge);
				}
			}
			if(sightPops.size() > 0){
				for(int i =0;i< sightPops.size();i++){
					Pop personPop = sightPops.get(i); 
					ImageMarker imge = new ImageMarker(personPop.getId(),null,personPop.getLatitude(),personPop.getLongitude(),personPop.getHeight(),null,1,-1);
					imge.setDistance(PhysicalPlace.distanceBetween(olatitude, olongitude, imge.getLatitude(),imge.getLongitude()));
					imge.setBitmap(BitmapFactory.decodeResource(ctx.getActualMixView().getResources(),R.drawable.jindian));
					imge.setType(ImageMarker.jindian);
					imge.setPopid(personPop.getId());
					markers.add(imge);
				}
				
			}
			Log.v("caonima","caonima"+markers.size());
			//new
			Log.v("aacc", "aaccshuaxin"+markers.size());
		
			tag=false;
		}
	}

	
	/*
	 * (non-Javadoc)
	 * 重置所有下载请求
	 * @see org.mixare.mgr.downloader.DownloadManager#purgeLists()
	 */
//	public synchronized void resetActivity() {
//		todoList.clear();
//		doneList.clear();
//	}

	/*
	 * (non-Javadoc)
	 *将URL传输到这里，可以得到相应信息该URL的ID
	 * @see
	 * org.mixare.mgr.downloader.DownloadManager#submitJob(org.mixare.mgr.downloader
	 * .DownloadRequest)
	 */
	

	/*
	 * (non-Javadoc)
	 * 得到最后一个下载结果
	 * @see
	 * org.mixare.mgr.downloader.DownloadManager#getReqResult(java.lang.String)
	 */
	public DownloadResult getReqResult(String jobId) {
		DownloadResult result = doneList.get(jobId);
		doneList.remove(jobId);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 得到下一个下载结果
	 * @see org.mixare.mgr.downloader.DownloadManager#getNextResult()
	 */
	public synchronized DownloadResult getNextResult() {
		DownloadResult result = null;
		if (!doneList.isEmpty()) {
			String nextId = doneList.keySet().iterator().next();
			result = doneList.get(nextId);
			doneList.remove(nextId);
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.downloader.DownloadManager#getResultSize()
	 */
	public int getResultSize(){
		return doneList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.downloader.DownloadManager#isDone()
	 */
//	public Boolean isDone() {
//		return todoList.isEmpty();
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.downloader.DownloadManager#goOnline()
	 */
	

	public void switchOff() {
		tag=false;
	}

	@Override
	public DownloadManagerState getState() {
		return state;
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	public boolean isTag() {
		return tag;
	}

	public void setTag(boolean tag) {
		this.tag = tag;
	}



	

}
