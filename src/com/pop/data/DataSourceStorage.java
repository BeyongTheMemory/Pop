
package com.pop.data;

import com.pop.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Singleton slass that manages the storage of datasources
 * You can add, edit or delete a datasource through this class.
 */
public class DataSourceStorage {

	private SharedPreferences settings;
	
	private Context ctx;
	
	public static DataSourceStorage instance;
	
	private boolean customDataSourceSelected = false;
		
	public DataSourceStorage(Context ctx){
		this.ctx = ctx;
		settings = ctx.getSharedPreferences(DataSourceList.SHARED_PREFS, 0);
	}
	
	public static void init(Context ctx){
		instance = new DataSourceStorage(ctx);
	}
	
	public static DataSourceStorage getInstance() {
		return instance;
	}
	
	public static DataSourceStorage getInstance(Context ctx) {
		if(instance == null){
			instance = new DataSourceStorage(ctx);
		}
		return instance;
	}
	
	public void add(String name, String url, String type, String display, boolean visible) {
		SharedPreferences.Editor dataSourceEditor = settings.edit();
		dataSourceEditor.putString("DataSource"+getSize(), 
				name +"|"+ url +"|"+ type +"|"+ display +"|"+ String.valueOf(visible));
		dataSourceEditor.commit();
	}
	
	public void add(String id, String serialized){
		SharedPreferences.Editor dataSourceEditor = settings.edit();
		dataSourceEditor.putString(id, serialized);
		dataSourceEditor.commit();
	}
	
	public void clear(){
		SharedPreferences.Editor dataSourceEditor = settings.edit();
		dataSourceEditor.clear();
		dataSourceEditor.commit();
	}
	
	public void editVisibility(int i, boolean visible){
		String[] fields = getFields(i);
		if(fields.length == 5){
			fields[4] = String.valueOf(visible);
		
			SharedPreferences.Editor dataSourceEditor = settings.edit();
			dataSourceEditor.putString("DataSource"+i, 
					fields[0] +"|"+ fields[1] +"|"+ fields[2] +"|"+ fields[3] +"|"+ fields[4]);
			dataSourceEditor.commit();
		}
	}
	
	/**������������*/
	public void fillDefaultDataSources(){
		Log.v("DATASOUCESTOREGE-92","XGFIND");
		String[] datasources = ctx.getResources().getStringArray(R.array.defaultdatasources);//��ȡ����Դ��ַ
		if(datasources.length > getSize()){
			for(int i = 0; i < datasources.length; i++){
				int id = getSize();
				add("DataSource"+ id, datasources[i]);
				onCustomDataSourceSelected(id);
			}
		}
	}
	
	public String[] getFields(int i){
		return settings.getString("DataSource"+i, "").split("\\|", -1);
	}
	
	public int getSize(){
		return settings.getAll().size();
	}
	
	public void setCustomDataSourceSelected(boolean customDataSourceSelected){
		this.customDataSourceSelected = customDataSourceSelected;
	}
		
	private void onCustomDataSourceSelected(int id) {
		// if a custom data source is selected, then hide the datasources
		editVisibility(id, !customDataSourceSelected);
	}
}
