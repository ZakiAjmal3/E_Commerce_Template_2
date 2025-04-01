package com.example.ecommercewhitelabel.Model;

public class RatingAndReviewModel {
    String ratingId,rating,reviewerName,reviewDate,reviewContent;
    public RatingAndReviewModel(String reviewId,String ratingPoint, String reviewerName, String reviewDate, String reviewContent) {
        this.ratingId = reviewId;
        this.rating = ratingPoint;
        this.reviewerName = reviewerName;
        this.reviewDate = reviewDate;
        this.reviewContent = reviewContent;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
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
