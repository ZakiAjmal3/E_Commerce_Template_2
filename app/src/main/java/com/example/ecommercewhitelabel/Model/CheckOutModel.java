package com.example.ecommercewhitelabel.Model;

public class CheckOutModel {
    String productTitle,productPrice,productImg;

    public CheckOutModel(String productTitle, String productPrice, String productImg) {
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productImg = productImg;
    }
    public String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }
}
