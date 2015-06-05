
package com.pop.mgr.downloader;

import com.pop.show.MixContext;

/**
 * Factory Of DownloadManager
 */
public class DownloadManagerFactory {
	
	/**
	 * Hide implementation Of DownloadManager
	 * @param mixContext
	 * @return DownloadManager
	 */
	public static DownloadManager makeDownloadManager(MixContext mixContext){
		return new DownloadMgrImpl(mixContext);
	}
}
