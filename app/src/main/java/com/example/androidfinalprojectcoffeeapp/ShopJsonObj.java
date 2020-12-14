package com.example.androidfinalprojectcoffeeapp;

import java.io.Serializable;

public class ShopJsonObj implements Serializable {
  private String img;
  private String address;
  private String phonenumber;
  private String Type;
  private String lat;
  private String longitude;
  private boolean isHeartChecked;
  private double distance;
  
  
  public ShopJsonObj(String img, String address, String phonenumber, String type, String lat, String longitude,
                     boolean isHeartChecked, double distance) {
    this.img = img;
    this.address = address;
    this.phonenumber = phonenumber;
    Type = type;
    this.lat = lat;
    this.longitude = longitude;
    this.isHeartChecked = isHeartChecked;
    this.distance = distance;
    
  }
  
  public String getImg() {
    return img;
  }
  
  public void setImg(String img) {
    this.img = img;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public String getPhoneNumber() {
    return phonenumber;
  }
  
  public void setPhoneNumber(String phonenumber) {
    this.phonenumber = phonenumber;
  }
  
  public String getType() {
    return Type;
  }
  
  public void setType(String type) {
    Type = type;
  }
  
  public String getLatitude() {
    return lat;
  }
  
  public void setLatitude(String lat) {
    this.lat = lat;
  }
  
  public String getLongitude() {
    return longitude;
  }
  
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }
  
  public boolean isHeartChecked() {
    return isHeartChecked;
  }
  
  public void setHeartChecked(boolean heartChecked) {
    isHeartChecked = heartChecked;
  }
  
  public double getDistance() {
    return distance;
  }
  
  public void setDistance(double distance) {
    this.distance = distance;
  }
}
