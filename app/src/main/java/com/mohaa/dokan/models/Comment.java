package com.mohaa.dokan.models;



public class Comment {
    private int id;
    private int usr_id;
    private int product_id;
    private String review;
    private String reviewer;
    private String reviewer_email;
    private int rate;

    public Comment(int usr_id, int product_id, String review, String reviewer, String reviewer_email, int rate) {
        this.usr_id = usr_id;
        this.product_id = product_id;
        this.review = review;
        this.reviewer = reviewer;
        this.reviewer_email = reviewer_email;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewer_email() {
        return reviewer_email;
    }

    public void setReviewer_email(String reviewer_email) {
        this.reviewer_email = reviewer_email;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
