
package com.pop.mgr.downloader;

import java.util.ArrayList;
import java.util.List;

import com.pop.data.DataSource;
import com.pop.lib.marker.Marker;

/**保存有存储Mark的List，以及该Result的ID*/

public class DownloadResult {
	
	private DataSource dataSource;
	private String params;
	private List<Marker> markers;

	private boolean error;
	private String errorMsg = "";
	private DownloadRequest errorRequest;
	private String idOfDownloadRequest;
	
	

	public DownloadResult() {
		super();
		this.dataSource = null;
		this.params = "";
		this.markers = new ArrayList<Marker>();
		this.error = true;
		this.errorMsg = "DUMMY OBJECT";
		this.errorRequest = null;
		this.idOfDownloadRequest="";
	}

	
	
	public String getIdOfDownloadRequest() {
		return idOfDownloadRequest;
	}



	public void setIdOfDownloadRequest(String idRequest) {
		idOfDownloadRequest = idRequest;
	}



	public List<Marker> getMarkers() {
		return markers;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource source) {
		this.dataSource = source;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
		if (!error){
			errorMsg="";
		}
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public DownloadRequest getErrorRequest() {
		return errorRequest;
	}

	public void setErrorRequest(DownloadRequest errorRequest) {
		this.errorRequest = errorRequest;
	}

	public void setError(Exception ex, DownloadRequest request) {
		error=true;
		errorMsg=ex.getMessage();
		errorRequest=request;
	}
	
	
	public void setAccomplish(String idOfDownloadRequest, List<Marker> markers, DataSource ds ) {
		setIdOfDownloadRequest(idOfDownloadRequest);
		setMarkers(markers);
		setDataSource(ds);
		setError(false);
		errorMsg="NO ERROR";
		errorRequest=null;
	}

}
