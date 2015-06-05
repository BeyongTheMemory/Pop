package com.example.craise.domain;

public class Good {
	
	private int goods_id;
	private int details_id;
	private int allCategories_id;
	private int specificCategories_id;
	private String goods_name;
	private float goods_oldPrice;
	private float goods_price;
	private String goods_picturePath;
	private String goods_saleInfo;
	private String goods_extra;
	private String goods_scheme;
	private int goods_salerNum;
	private int goods_isLatest;
	private int goods_grade;
	private int  goods_comment;
	private int goods_isFreeOrder;
	private int seller_id;
	
	public int getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public int getDetails_id() {
		return details_id;
	}
	public void setDetails_id(int details_id) {
		this.details_id = details_id;
	}
	public int getAllCategories_id() {
		return allCategories_id;
	}
	public void setAllCategories_id(int allCategories_id) {
		this.allCategories_id = allCategories_id;
	}
	public int getSpecificCategories_id() {
		return specificCategories_id;
	}
	public void setSpecificCategories_id(int specificCategories_id) {
		this.specificCategories_id = specificCategories_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public float getGoods_oldPrice() {
		return goods_oldPrice;
	}
	public void setGoods_oldPrice(float goods_oldPrice) {
		this.goods_oldPrice = goods_oldPrice;
	}
	public float getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(float goods_price) {
		this.goods_price = goods_price;
	}
	public String getGoods_picturePath() {
		return goods_picturePath;
	}
	public void setGoods_picturePath(String goods_picturePath) {
		this.goods_picturePath = goods_picturePath;
	}
	public String getGoods_saleInfo() {
		return goods_saleInfo;
	}
	public void setGoods_saleInfo(String goods_saleInfo) {
		this.goods_saleInfo = goods_saleInfo;
	}
	public String getGoods_extra() {
		return goods_extra;
	}
	public void setGoods_extra(String goods_extra) {
		this.goods_extra = goods_extra;
	}
	public String getGoods_scheme() {
		return goods_scheme;
	}
	public void setGoods_scheme(String goods_scheme) {
		this.goods_scheme = goods_scheme;
	}
	public int getGoods_salerNum() {
		return goods_salerNum;
	}
	public void setGoods_salerNum(int goods_salerNum) {
		this.goods_salerNum = goods_salerNum;
	}
	public int getGoods_isLatest() {
		return goods_isLatest;
	}
	public void setGoods_isLatest(int goods_isLatest) {
		this.goods_isLatest = goods_isLatest;
	}
	public int getGoods_grade() {
		return goods_grade;
	}
	public void setGoods_grade(int goods_grade) {
		this.goods_grade = goods_grade;
	}
	public int getGoods_comment() {
		return goods_comment;
	}
	public void setGoods_comment(int goods_comment) {
		this.goods_comment = goods_comment;
	}
	public int getGoods_isFreeOrder() {
		return goods_isFreeOrder;
	}
	public void setGoods_isFreeOrder(int goods_isFreeOrder) {
		this.goods_isFreeOrder = goods_isFreeOrder;
	}
	
}
