
package com.pop.mgr.downloader;
/**类属性有DownloadRequest content和String uniqueKey,uniquekey由hash函数生成
 *该类用于给DownloadRequest分配唯一的码值 
 */
class ManagedDownloadRequest {
	
	private DownloadRequest content;
	
	private String uniqueKey;

	public ManagedDownloadRequest(final DownloadRequest content) {
		this.content = content;
		this.uniqueKey = "" + System.currentTimeMillis()+"_"+hashCode();
	}
    
	/*
	 * 得到该ManagerdDownloadRequest对应的downloadrequest
	 */
	public DownloadRequest getOriginalRequest() {
		return content;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((uniqueKey == null) ? 0 : uniqueKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManagedDownloadRequest other = (ManagedDownloadRequest) obj;
		return getOriginalRequest().getSource().getName().equals(other.getOriginalRequest().getSource().getName());
	}
	
	

}
