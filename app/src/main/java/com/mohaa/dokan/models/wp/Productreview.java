package com.mohaa.dokan.models.wp;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Productreview implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("date_created_gmt")
    @Expose
    private String dateCreatedGmt;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reviewer")
    @Expose
    private String reviewer;
    @SerializedName("reviewer_email")
    @Expose
    private String reviewerEmail;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("reviewer_avatar_urls")
    @Expose
    private ReviewerAvatarUrls reviewerAvatarUrls;
    @SerializedName("_links")
    @Expose
    private Links links;

    public Productreview(Integer productId, String reviewer, String reviewerEmail, String review, Integer rating) {
        this.productId = productId;
        this.reviewer = reviewer;
        this.reviewerEmail = reviewerEmail;
        this.review = review;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreatedGmt() {
        return dateCreatedGmt;
    }

    public void setDateCreatedGmt(String dateCreatedGmt) {
        this.dateCreatedGmt = dateCreatedGmt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public ReviewerAvatarUrls getReviewerAvatarUrls() {
        return reviewerAvatarUrls;
    }

    public void setReviewerAvatarUrls(ReviewerAvatarUrls reviewerAvatarUrls) {
        this.reviewerAvatarUrls = reviewerAvatarUrls;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public class Collection implements Serializable {

        @SerializedName("href")
        @Expose
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }
    public class Links implements Serializable {

        @SerializedName("self")
        @Expose
        private List<Self> self = null;
        @SerializedName("collection")
        @Expose
        private List<Collection> collection = null;
        @SerializedName("up")
        @Expose
        private List<Up> up = null;
        @SerializedName("reviewer")
        @Expose
        private List<Reviewer> reviewer = null;

        public List<Self> getSelf() {
            return self;
        }

        public void setSelf(List<Self> self) {
            this.self = self;
        }

        public List<Collection> getCollection() {
            return collection;
        }

        public void setCollection(List<Collection> collection) {
            this.collection = collection;
        }

        public List<Up> getUp() {
            return up;
        }

        public void setUp(List<Up> up) {
            this.up = up;
        }

        public List<Reviewer> getReviewer() {
            return reviewer;
        }

        public void setReviewer(List<Reviewer> reviewer) {
            this.reviewer = reviewer;
        }

    }
    public class Reviewer  implements Serializable{

        @SerializedName("embeddable")
        @Expose
        private Boolean embeddable;
        @SerializedName("href")
        @Expose
        private String href;

        public Boolean getEmbeddable() {
            return embeddable;
        }

        public void setEmbeddable(Boolean embeddable) {
            this.embeddable = embeddable;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }
    public class ReviewerAvatarUrls implements Serializable{

        @SerializedName("24")
        @Expose
        private String _24;
        @SerializedName("48")
        @Expose
        private String _48;
        @SerializedName("96")
        @Expose
        private String _96;

        public String get24() {
            return _24;
        }

        public void set24(String _24) {
            this._24 = _24;
        }

        public String get48() {
            return _48;
        }

        public void set48(String _48) {
            this._48 = _48;
        }

        public String get96() {
            return _96;
        }

        public void set96(String _96) {
            this._96 = _96;
        }

    }
    public class Self implements Serializable {

        @SerializedName("href")
        @Expose
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }
    public class Up implements Serializable {

        @SerializedName("href")
        @Expose
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }
}