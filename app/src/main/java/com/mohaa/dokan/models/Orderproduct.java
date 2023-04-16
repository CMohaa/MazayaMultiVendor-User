package com.mohaa.dokan.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Orderproduct implements Serializable {

    public Orderproduct() {

    }
    private static final String TAG = "Orderproduct";



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("merchant_name")
    @Expose
    private String merchantName;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("order_number")
    @Expose
    private Long orderNumber;
    @SerializedName("owner_id")
    @Expose
    private Integer ownerId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("total_price")
    @Expose
    private Double totalPrice;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;

    public Orderproduct(Integer productId, String productName, String thumbImage, String merchantName, Integer merchantId, Long orderNumber, Integer ownerId, Integer quantity, Double price, Double totalPrice, String barcode, Long createdAt) {
        this.productId = productId;
        this.productName = productName;
        this.thumbImage = thumbImage;
        this.merchantName = merchantName;
        this.merchantId = merchantId;
        this.orderNumber = orderNumber;
        this.ownerId = ownerId;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.barcode = barcode;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
