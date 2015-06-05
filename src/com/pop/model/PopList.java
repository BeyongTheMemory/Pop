package com.pop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopList {

	private List<Pop> adList;
	private List<Pop> personalList;
	private List<Pop> publicList;
	private List<Pop> sightList;
	
	
	public PopList(){
		adList = new ArrayList<Pop>();
		personalList = new ArrayList<Pop>();
		publicList = new ArrayList<Pop>();
		sightList = new ArrayList<Pop>();	
	}
	
	public List<Pop> getAdList() {
		return adList;
	}
	public void setAdList(List<Pop> adList) {
		this.adList = adList;
	}
	public List<Pop> getPersonalList() {
		return personalList;
	}
	public void setPersonalList(List<Pop> personalList) {
		this.personalList = personalList;
	}
	public List<Pop> getPublicList() {
		return publicList;
	}
	public void setPublicList(List<Pop> publicList) {
		this.publicList = publicList;
	}
	public List<Pop> getSightList() {
		return sightList;
	}
	public void setSightList(List<Pop> sightList) {
		this.sightList = sightList;
	}
	
	
	
	
}
