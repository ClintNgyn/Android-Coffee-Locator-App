package com.example.androidfinalprojectcoffeeapp;

public class User {
  private String fName, lName, username, email, password, imageUrl;
  
  public User(String fName, String lName, String username, String email, String password, String imageUrl) {
    this.fName = fName;
    this.lName = lName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.imageUrl = imageUrl;
  }
  
  public String getfName() {
    return fName;
  }
  
  public String getlName() {
    return lName;
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getEmail() {
    return email;
  }
  
  public String getPassword() {
    return password;
  }
  
  public String getImageUrl() {
    return imageUrl;
  }
  
  public void setfName(String fName) {
    this.fName = fName;
  }
  
  public void setlName(String lName) {
    this.lName = lName;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
