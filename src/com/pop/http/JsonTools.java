package com.pop.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.pop.model.Pop;
import com.pop.model.PopList;

public class JsonTools {

	/**
	 * 
	 * @param result
	 * @return
	 */
	public final static PopList getPopFromResult(String result){
		PopList list = new PopList();
		try{
			JSONObject jsonObject = new JSONObject(result);
			JSONArray adArray = (JSONArray) jsonObject.get("advertisement");
			JSONArray personalArray = (JSONArray) jsonObject.get("personal");
			JSONArray publicArray = (JSONArray) jsonObject.get("public");
			JSONArray sightArray = (JSONArray) jsonObject.get("sight");
			Log.v("caotama", "caotama");
			Log.v("caotama", "caotama");
			Log.v("caotama", "caotama");
			
			if (adArray != null){
				List<Pop> adList = new ArrayList<Pop>();
				for (int i = 0; i < adArray.length(); i++){
					JSONObject jb = adArray.getJSONObject(i);
					Pop pop = new Pop();
					pop.setType(1);
					pop.setHeight(Float.parseFloat(jb.getString("height")));
					pop.setLatitude(Float.parseFloat(jb.getString("latitude")));
					pop.setLongitude(Float.parseFloat(jb.getString("logitude")));
					pop.setId(Integer.parseInt(jb.getString("id")));
					adList.add(pop);
				}
				list.setAdList(adList);
			}
			
			if(personalArray != null){
				List<Pop> personalList = new ArrayList<Pop>();
				for (int i = 0; i < personalArray.length(); i++){
					JSONObject jb = personalArray.getJSONObject(i);
					Pop pop = new Pop();
					pop.setType(2);
					pop.setHeight(Float.parseFloat(jb.getString("height")));
					pop.setLatitude(Float.parseFloat(jb.getString("latitude")));
					pop.setLongitude(Float.parseFloat(jb.getString("logitude")));
					pop.setId(Integer.parseInt(jb.getString("id")));
					personalList.add(pop);
				}
				list.setPersonalList(personalList);
				
			}
			
			if(publicArray != null){
				List<Pop> publicList = new ArrayList<Pop>();
				for (int i = 0; i < publicArray.length(); i++){
					JSONObject jb = publicArray.getJSONObject(i);
					Pop pop = new Pop();
					pop.setType(3);
					pop.setHeight(Float.parseFloat(jb.getString("height")));
					pop.setLatitude(Float.parseFloat(jb.getString("latitude")));
					pop.setLongitude(Float.parseFloat(jb.getString("logitude")));
					pop.setId(Integer.parseInt(jb.getString("id")));
					publicList.add(pop);
				}
				list.setPublicList(publicList);
			}
			
			if(sightArray != null){
				List<Pop> sightList = new ArrayList<Pop>();
				for (int i = 0; i < sightArray.length(); i++){
					JSONObject jb = sightArray.getJSONObject(i);
					Pop pop = new Pop();
					pop.setType(4);
					pop.setHeight(Float.parseFloat(jb.getString("height")));
					pop.setLatitude(Float.parseFloat(jb.getString("latitude")));
					pop.setLongitude(Float.parseFloat(jb.getString("logitude")));
					pop.setId(Integer.parseInt(jb.getString("id")));
					sightList.add(pop);
				}
				list.setSightList(sightList);
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
	try {
		JSONObject jsonObject = new JSONObject(result);
		JSONArray messageArray = (JSONArray) jsonObject.get("message");
		JSONArray replyArray = (JSONArray) jsonObject.get("reply");
		// System.out.println(messageArray);
		for (int i = 0; i < messageArray.length(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			JSONObject jb = messageArray.getJSONObject(i);
			// map.put("messageLid", jb.getString("LID"));
			map.put("message", jb.getString("words"));
			// System.out.println(map);
			for (int k = 0; k < replyArray.length(); k++) {
				JSONObject object = replyArray.getJSONObject(k);
				if (jb.getString("LID").equals(object.getString("LID"))) {
					// map.put("RID", object.getString("RID"));
					map.put("r_words", object.getString("r_words"));
					// System.out.println(map);
				} else if (map.get("r_words") == null) {
					// map.put("RID", "");
					// map.put("r_words", "");
					// System.out.println(map);
				}
				// System.out.println(k);
			}
			// System.out.println(map);
			resultList.add(map);
			// System.out.println(dataList);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return resultList;
}*/
}
