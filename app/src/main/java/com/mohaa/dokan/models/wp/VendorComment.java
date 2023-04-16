package com.mohaa.dokan.models.wp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorComment implements Serializable {

    @SerializedName("comment_id")
    @Expose
    private Integer commentId;
    @SerializedName("comment_author")
    @Expose
    private String commentAuthor;
    @SerializedName("comment_author_email")
    @Expose
    private String commentAuthorEmail;
    @SerializedName("comment_author_url")
    @Expose
    private String commentAuthorUrl;
    @SerializedName("comment_author_avatar")
    @Expose
    private String commentAuthorAvatar;
    @SerializedName("comment_content")
    @Expose
    private String commentContent;
    @SerializedName("comment_permalink")
    @Expose
    private String commentPermalink;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("comment_post_ID")
    @Expose
    private String commentPostID;
    @SerializedName("comment_approved")
    @Expose
    private String commentApproved;
    @SerializedName("comment_date")
    @Expose
    private String commentDate;
    @SerializedName("rating")
    @Expose
    private Integer rating;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getCommentAuthorEmail() {
        return commentAuthorEmail;
    }

    public void setCommentAuthorEmail(String commentAuthorEmail) {
        this.commentAuthorEmail = commentAuthorEmail;
    }

    public String getCommentAuthorUrl() {
        return commentAuthorUrl;
    }

    public void setCommentAuthorUrl(String commentAuthorUrl) {
        this.commentAuthorUrl = commentAuthorUrl;
    }

    public String getCommentAuthorAvatar() {
        return commentAuthorAvatar;
    }

    public void setCommentAuthorAvatar(String commentAuthorAvatar) {
        this.commentAuthorAvatar = commentAuthorAvatar;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentPermalink() {
        return commentPermalink;
    }

    public void setCommentPermalink(String commentPermalink) {
        this.commentPermalink = commentPermalink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentPostID() {
        return commentPostID;
    }

    public void setCommentPostID(String commentPostID) {
        this.commentPostID = commentPostID;
    }

    public String getCommentApproved() {
        return commentApproved;
    }

    public void setCommentApproved(String commentApproved) {
        this.commentApproved = commentApproved;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
