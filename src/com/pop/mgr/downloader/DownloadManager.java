
package com.pop.mgr.downloader;

import java.util.List;

import com.pop.lib.marker.Marker;

/**
*发送位置信息到服务器
*并解析服务器返回的数据
*建立泡泡
 * This class establishes a connection and downloads the data for each entry in
 * its todo list one after another.
 */
public interface DownloadManager {

	/**
	 * Possible state of this Manager
	 * 状态信息
	 */
	enum DownloadManagerState {
		OnLine, //manage downlad request 
		OffLine, // No OnLine
		Downloading, //Process some Download Request
		Confused, // Internal state not congruent 内部状态不一致？
	}

	/**
	 * Reset all Request and Responce
	 * 重置所有的请求和响应
	 */
	//void resetActivity();

	/**
	 * Submit new DownloadRequest
	 * 提交新的下载请求
	 * @param job
	 * @return reference Of Job or null if job is rejected 下载被拒绝时返回null
	 */
	//String submitJob(DownloadRequest job);

	/**
	 * Get result of job if exist, null otherwise
	 * 如果不存在返回Null
	 * @param jobId reference of Job
	 * @return result 
	 */
	DownloadResult getReqResult(String jobId);

	/**
	 * Pseudo Iterator on results 
	 * 下一个结果
	 * @return actual Download Result
	 */
	DownloadResult getNextResult();

	/**
	 * Gets the number of downloaded results
	 * 得到结果数据的大小
	 * @return the number of results
	 */
	int getResultSize();
	
	/**
	 * check if all Download request is done
	 *  检查是否所有数据接收完毕
	 * @return BOOLEAN
	 */
	//Boolean isDone();

	/**
	 * 线程开始
	 */
	//void switchOn();

	/**
	 * 线程停止
	 */
	void switchOff();

	/**
	 * Request state of service
	 * @return
	 */
	DownloadManagerState getState();


}