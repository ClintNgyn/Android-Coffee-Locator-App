package com.example.androidfinalprojectcoffeeapp;

public class ExampleItemMaps {
    private String image;
    private String address;
    private String phonenumber;
    private String type;
    private String lat;
    private String longitude;

    public ExampleItemMaps(String image, String address, String phonenumber, String type, String lat, String longitude) {
        this.image = image;
        this.address = address;
        this.phonenumber = phonenumber;
        this.type = type;
        this.lat = lat;
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getType() {
        return type;
    }

    public String getLat() {
        return lat;
    }

    public String getLongitude() {
        return longitude;
    }
}
