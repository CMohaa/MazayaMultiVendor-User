package com.mohaa.dokan.models;


import java.io.Serializable;

public class PendingProduct implements Serializable {

    public PendingProduct() {

    }
    private static final String TAG = "PendingProduct";
    private int id;
    private int product_id;
    private int parentid;
    private int cat_id;
    private int merchant_id;
    private String merchant_name;
    private String product_name;
    private String product_shortname;
    private String product_desc;
    private int quantity;
    private double price;
    private double total_price;
    private String barcode;
    private String type;
    private String thumb_image;
    private int stock_quantity;
    private double discount;
    private long created_at;

    public PendingProduct(int product_id, int parentid, int cat_id, int merchant_id, String merchant_name, String product_name, String product_shortname, String product_desc, int quantity, double price, double total_price, String barcode, String type, String thumb_image, int stock_quantity, double discount, long created_at) {
        this.product_id = product_id;
        this.parentid = parentid;
        this.cat_id = cat_id;
        this.merchant_id = merchant_id;
        this.merchant_name = merchant_name;
        this.product_name = product_name;
        this.product_shortname = product_shortname;
        this.product_desc = product_desc;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
        this.barcode = barcode;
        this.type = type;
        this.thumb_image = thumb_image;
        this.stock_quantity = stock_quantity;
        this.discount = discount;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_shortname() {
        return product_shortname;
    }

    public void setProduct_shortname(String product_shortname) {
        this.product_shortname = product_shortname;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
