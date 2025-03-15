package com.example.ecommercewhitelabel.Model;

public class SingleOrderPageItemsModel {
    String productImgUrl,productTitle,productPrice;
    public SingleOrderPageItemsModel(String productImgUrl, String productTitle, String productPrice) {
        this.productImgUrl = productImgUrl;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
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
}
