package com.example.androidfinalprojectcoffeeapp;

public class InputChecker {
  
  public boolean haveANumber(String str) {
    for (int c = 0; c < str.length(); c++) {
      if (Character.isDigit(str.charAt(c))) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean haveCapital(String str) {
    for (int c = 0; c < str.length(); c++) {
      if (str.charAt(c) >= 'A' && str.charAt(c) <= 'Z') {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean checkLettersNumbersOnly(String str) {
    return str.matches("[a-zA-Z0-9]+");
  }
  
  public boolean checkNumbersOnly(String str) {
    return str.matches("[0-9]+");
  }
  
  public boolean checkEmail(String str) {
    return str.matches("[a-zA-Z]{1}[a-zA-Z._0-9]{4,15}\\@\\w{3,12}\\.(com|edu|ca|org){1}");
  }
  
  public boolean checkLength(String str, int desiredLength) {
    return str.length() >= desiredLength ? true : false;
  }
  
  public boolean checkLettersOnly(String str) {
    return str.matches("[a-zA-Z]+");
  }
  
  public boolean checkPassword(String pass) {
    return checkLength(pass, 8) && haveANumber(pass) && haveCapital(pass) && checkLettersNumbersOnly(pass);
  }
  
  
}
