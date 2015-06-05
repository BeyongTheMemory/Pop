
package com.pop.mgr.downloader;

import com.pop.data.DataSource;
/**��Ա������DataSouce souce��String params
 * souce�б�������Դ���ͺ���Ӧ����վ
 * params���淢��������վʱ�ĺ�׺*/
public class DownloadRequest {
	private DataSource source;
	private String params;
	
	public DownloadRequest(DataSource source) {
		this(source, "");
	}

	public DownloadRequest(DataSource source, String params) {
		super();
		if (source==null){
			throw new IllegalArgumentException("DataSource is NULL");
		}
		if (!source.isWellFormed()){
			throw new IllegalArgumentException("DataSource is not well formed");
		}
		
		this.source = source;
		this.params = params;
	}

	public DataSource getSource() {
		return source;
	}

	public void setSource(DataSource source) {
		this.source = source;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	public String toString(){
		return " type: "
				+ getSource().getType() + ", params: "
				+ getParams() + ", url: "
				+ getSource().getUrl();
	}

}
