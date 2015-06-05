
package com.pop.show;


import com.pop.activity.MainActivity;
import com.pop.lib.MixContextInterface;
import com.pop.lib.render.Matrix;
import com.pop.mgr.downloader.DownloadManager;
import com.pop.mgr.downloader.DownloadManagerFactory;
import com.pop.mgr.location.LocationFinder;
import com.pop.mgr.location.LocationFinderFactory;
//import org.mixare.mgr.webcontent.WebContentManager;
//import org.mixare.mgr.webcontent.WebContentManagerFactory;




import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Cares about location management and about the data (source, inputstream) 
 * 位置信息管理和数据输出
 * 将当前位置信息发送到这里后该类将负责搜索周围的兴趣点
 * 过滤设置也存在此处
 */
public class MixContext extends ContextWrapper implements MixContextInterface{

	// TAG for logging
	public static final String TAG = "Mixare";

	private MainActivity mixView;

	private Matrix rotationM = new Matrix();//旋转矩阵？

	/** Responsible for all download */
	/**负责所有下载*/
	private DownloadManager downloadManager;

	/** Responsible for all location tasks */
	/**负责所有位置的服务*/
	private LocationFinder locationFinder;

	/** Responsible for data Source Management */
	/**负责数据源管理 */
	//private DataSourceManager dataSourceManager;

	/** Responsible for Web Content */
	/**负责网站内容 */
	//private WebContentManager webContentManager;
  
	
	public MixContext(MainActivity appCtx) {
		super(appCtx);
		mixView = appCtx;

		// TODO: RE-ORDER THIS SEQUENCE... IS NECESSARY?
		//getDataSourceManager().refreshDataSources();//检查所有数据源

		//-------------------------------------------------------------这里可能不对
//		if (!getDataSourceManager().isAtLeastOneDatasourceSelected()) {
//			rotationM.toIdentity();//若未选择数据源，转化为单位矩阵
//		}
		getLocationFinder().switchOn();//打开定位
		getLocationFinder().findLocation();//开始定位,仅仅得到最后一次定位时的位置信息
	}

	public String getStartUrl() {//因是获取点击标记的对应网页
		Intent intent = ((Activity) getActualMixView()).getIntent();
		if (intent.getAction() != null
				&& intent.getAction().equals(Intent.ACTION_VIEW)) {
			Log.v("intent.getData().toString","xgtest"+intent.getData().toString());
			return intent.getData().toString();
		} else {
			return "";
		}
	}
    
	/**设置为rotationM*/
	public void getRM(Matrix dest) {
		synchronized (rotationM) {
			dest.set(rotationM);
		}
	}

	/**
	 * Shows a webpage with the given url when clicked on a marker.
	 * 点击标记时显示网页
	 */


	public void doResume(MainActivity mixView) {
		setActualMixView(mixView);
	}

	public void updateSmoothRotation(Matrix smoothR) {
		synchronized (rotationM) {
			rotationM.set(smoothR);//更新平移矩阵
		}
	}

//	public DataSourceManager getDataSourceManager() {
//		if (this.dataSourceManager == null) {
//			dataSourceManager = DataSourceManagerFactory
//					.makeDataSourceManager(this);
//		}
//		return dataSourceManager;
//	}

	public LocationFinder getLocationFinder() {
		if (this.locationFinder == null) {
			locationFinder = LocationFinderFactory.makeLocationFinder(this);
		}
		return locationFinder;
	}

	public DownloadManager getDownloadManager() {
		if (this.downloadManager == null) {
			downloadManager = DownloadManagerFactory.makeDownloadManager(this);
			getLocationFinder().setDownloadManager(downloadManager);
		}
		return downloadManager;
	}

	
	public MainActivity getActualMixView() {
		synchronized (mixView) {
			return this.mixView;
		}
	}

	private void setActualMixView(MainActivity mv) {
		synchronized (mixView) {
			this.mixView = mv;
		}
	}
    /**获取ContentResolver的实例，该实例用于共享数据*/
	public ContentResolver getContentResolver() {
		ContentResolver out = super.getContentResolver();
		if (super.getContentResolver() == null) {
			out = getActualMixView().getContentResolver();
		}
		return out;
	}
	
	/**
	 * Toast POPUP notification
	 * 弹出下方通知
	 * @param string message
	 */
	public void doPopUp(final String string){
       Toast.makeText(this,string,Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast POPUP notification
	 * 
	 * @param connectionGpsDialogText
	 */
	public void doPopUp(int RidOfString) {
        doPopUp(this.getString(RidOfString));
	}

	@Override
	public void loadMixViewWebPage(String url) throws Exception {
		// TODO 自动生成的方法存根
		
	}
}
