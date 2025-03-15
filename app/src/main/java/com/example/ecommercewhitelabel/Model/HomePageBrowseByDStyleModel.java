package com.example.ecommercewhitelabel.Model;

public class HomePageBrowseByDStyleModel {
    String dressName;
    int dressImg;

    public HomePageBrowseByDStyleModel(String dressName, int dressImg) {
        this.dressName = dressName;
        this.dressImg = dressImg;
    }

    public String getDressName() {
        return dressName;
    }

    public void setDressName(String dressName) {
        this.dressName = dressName;
    }

    public int getDressImg() {
        return dressImg;
    }

    public void setDressImg(int dressImg) {
        this.dressImg = dressImg;
    }
}
