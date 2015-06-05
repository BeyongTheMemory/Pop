package com.example.craise.domain;

public class Seller {
private  int seller_id;
private int seller_isSale;
private String seller_name;
private String seller_address;
private String seller_phone;
private String seller_picture;
private String seller_comment;
private float  seller_rank,seller_distance;
public float getSeller_distance() {
	return seller_distance;
}

public void setSeller_distance(float seller_distance) {
	this.seller_distance = seller_distance;
}
private double seller_latitude,seller_longitude;
public int getSeller_id() {
	return seller_id;
}

public int getSeller_isSale() {
	return seller_isSale;
}

public String getSeller_picture() {
	return seller_picture;
}

public void setSeller_picture(String sellerPicture) {
	seller_picture = sellerPicture;
}

public String getSeller_comment() {
	return seller_comment;
}

public void setSeller_comment(String sellerComment) {
	seller_comment = sellerComment;
}

public float getSeller_rank() {
	return seller_rank;
}

public void setSeller_rank(float sellerRank) {
	seller_rank = sellerRank;
}

public double getSeller_latitude() {
	return seller_latitude;
}

public void setSeller_latitude(double sellerLatitude) {
	seller_latitude = sellerLatitude;
}

public double getSeller_longitude() {
	return seller_longitude;
}

public void setSeller_longitude(double sellerLongitude) {
	seller_longitude = sellerLongitude;
}

public void setSeller_isSale(int sellerIsSale) {
	seller_isSale = sellerIsSale;
}

public void setSeller_id(int sellerId) {
	seller_id = sellerId;
}
public String getSeller_name() {
	return seller_name;
}
public void setSeller_name(String sellerName) {
	seller_name = sellerName;
}
public String getSeller_address() {
	return seller_address;
}
public void setSeller_address(String sellerAddress) {
	seller_address = sellerAddress;
}
public String getSeller_phone() {
	return seller_phone;
}
public void setSeller_phone(String sellerPhone) {
	seller_phone = sellerPhone;
}


}
