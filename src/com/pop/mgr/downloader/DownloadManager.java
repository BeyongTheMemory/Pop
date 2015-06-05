
package com.pop.mgr.downloader;

import java.util.List;

import com.pop.lib.marker.Marker;

/**
*����λ����Ϣ��������
*���������������ص�����
*��������
 * This class establishes a connection and downloads the data for each entry in
 * its todo list one after another.
 */
public interface DownloadManager {

	/**
	 * Possible state of this Manager
	 * ״̬��Ϣ
	 */
	enum DownloadManagerState {
		OnLine, //manage downlad request 
		OffLine, // No OnLine
		Downloading, //Process some Download Request
		Confused, // Internal state not congruent �ڲ�״̬��һ�£�
	}

	/**
	 * Reset all Request and Responce
	 * �������е��������Ӧ
	 */
	//void resetActivity();

	/**
	 * Submit new DownloadRequest
	 * �ύ�µ���������
	 * @param job
	 * @return reference Of Job or null if job is rejected ���ر��ܾ�ʱ����null
	 */
	//String submitJob(DownloadRequest job);

	/**
	 * Get result of job if exist, null otherwise
	 * ��������ڷ���Null
	 * @param jobId reference of Job
	 * @return result 
	 */
	DownloadResult getReqResult(String jobId);

	/**
	 * Pseudo Iterator on results 
	 * ��һ�����
	 * @return actual Download Result
	 */
	DownloadResult getNextResult();

	/**
	 * Gets the number of downloaded results
	 * �õ�������ݵĴ�С
	 * @return the number of results
	 */
	int getResultSize();
	
	/**
	 * check if all Download request is done
	 *  ����Ƿ��������ݽ������
	 * @return BOOLEAN
	 */
	//Boolean isDone();

	/**
	 * �߳̿�ʼ
	 */
	//void switchOn();

	/**
	 * �߳�ֹͣ
	 */
	void switchOff();

	/**
	 * Request state of service
	 * @return
	 */
	DownloadManagerState getState();


}