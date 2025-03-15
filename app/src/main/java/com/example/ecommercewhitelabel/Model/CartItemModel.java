package com.example.ecommercewhitelabel.Model;

public class CartItemModel {
    String productTitle, productPrice, productSize, productColor, productImg,productQuantity;

    public CartItemModel(String productTitle, String productPrice, String productSize, String productColor, String productImg, String productQuantity) {
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productImg = productImg;
        this.productQuantity = productQuantity;
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

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }
}
