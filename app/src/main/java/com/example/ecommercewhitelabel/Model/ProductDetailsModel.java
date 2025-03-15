package com.example.ecommercewhitelabel.Model;

public class ProductDetailsModel {
    String productName,productPrice,productRating;
    int wishListImgToggle;

    public ProductDetailsModel(String productName, String productPrice, String productRating, int wishListImgToggle) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productRating = productRating;
        this.wishListImgToggle = wishListImgToggle;
    }

    public int getWishListImgToggle() {
        return wishListImgToggle;
    }

    public void setWishListImgToggle(int wishListImgToggle) {
        this.wishListImgToggle = wishListImgToggle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating(String productRating) {
        this.productRating = productRating;
    }
}
