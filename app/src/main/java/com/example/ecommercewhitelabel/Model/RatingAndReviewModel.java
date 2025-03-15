package com.example.ecommercewhitelabel.Model;

public class RatingAndReviewModel {
    String rating,reviewerName,reviewDate,reviewContent;
    public RatingAndReviewModel(String rating, String reviewerName, String reviewDate, String reviewContent) {
        this.rating = rating;
        this.reviewerName = reviewerName;
        this.reviewDate = reviewDate;
        this.reviewContent = reviewContent;
    }
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
