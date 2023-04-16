package com.mohaa.dokan.models.wp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VendorEmpty implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("social")
    @Expose
    private Social social;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("show_email")
    @Expose
    private Boolean showEmail;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("gravatar")
    @Expose
    private String gravatar;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("products_per_page")
    @Expose
    private Integer productsPerPage;
    @SerializedName("show_more_product_tab")
    @Expose
    private Boolean showMoreProductTab;
    @SerializedName("toc_enabled")
    @Expose
    private Boolean tocEnabled;
    @SerializedName("store_toc")
    @Expose
    private Object storeToc;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("rating")
    @Expose
    private Rating rating;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("registered")
    @Expose
    private String registered;
    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("trusted")
    @Expose
    private Boolean trusted;
    @SerializedName("_links")
    @Expose
    private Links links;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Social getSocial() {
        return social;
    }

    public void setSocial(Social social) {
        this.social = social;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getShowEmail() {
        return showEmail;
    }

    public void setShowEmail(Boolean showEmail) {
        this.showEmail = showEmail;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getGravatar() {
        return gravatar;
    }

    public void setGravatar(String gravatar) {
        this.gravatar = gravatar;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public Integer getProductsPerPage() {
        return productsPerPage;
    }

    public void setProductsPerPage(Integer productsPerPage) {
        this.productsPerPage = productsPerPage;
    }

    public Boolean getShowMoreProductTab() {
        return showMoreProductTab;
    }

    public void setShowMoreProductTab(Boolean showMoreProductTab) {
        this.showMoreProductTab = showMoreProductTab;
    }

    public Boolean getTocEnabled() {
        return tocEnabled;
    }

    public void setTocEnabled(Boolean tocEnabled) {
        this.tocEnabled = tocEnabled;
    }

    public Object getStoreToc() {
        return storeToc;
    }

    public void setStoreToc(Object storeToc) {
        this.storeToc = storeToc;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(Payment String) {
        this.payment = payment;
    }

    public Boolean getTrusted() {
        return trusted;
    }

    public void setTrusted(Boolean trusted) {
        this.trusted = trusted;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
    public class Address implements Serializable {

        @SerializedName("street_1")
        @Expose
        private String street1;
        @SerializedName("street_2")
        @Expose
        private String street2;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("zip")
        @Expose
        private String zip;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;

        public String getStreet1() {
            return street1;
        }

        public void setStreet1(String street1) {
            this.street1 = street1;
        }

        public String getStreet2() {
            return street2;
        }

        public void setStreet2(String street2) {
            this.street2 = street2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

    }
    public class Bank  implements Serializable{

        @SerializedName("ac_name")
        @Expose
        private String acName;
        @SerializedName("ac_number")
        @Expose
        private String acNumber;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
        @SerializedName("bank_addr")
        @Expose
        private String bankAddr;
        @SerializedName("routing_number")
        @Expose
        private String routingNumber;
        @SerializedName("iban")
        @Expose
        private String iban;
        @SerializedName("swift")
        @Expose
        private String swift;

        public String getAcName() {
            return acName;
        }

        public void setAcName(String acName) {
            this.acName = acName;
        }

        public String getAcNumber() {
            return acNumber;
        }

        public void setAcNumber(String acNumber) {
            this.acNumber = acNumber;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankAddr() {
            return bankAddr;
        }

        public void setBankAddr(String bankAddr) {
            this.bankAddr = bankAddr;
        }

        public String getRoutingNumber() {
            return routingNumber;
        }

        public void setRoutingNumber(String routingNumber) {
            this.routingNumber = routingNumber;
        }

        public String getIban() {
            return iban;
        }

        public void setIban(String iban) {
            this.iban = iban;
        }

        public String getSwift() {
            return swift;
        }

        public void setSwift(String swift) {
            this.swift = swift;
        }

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

    }
    public class Payment implements Serializable{

        @SerializedName("bank")
        @Expose
        private Bank bank;
        @SerializedName("paypal")
        @Expose
        private Paypal paypal;

        public Bank getBank() {
            return bank;
        }

        public void setBank(Bank bank) {
            this.bank = bank;
        }

        public Paypal getPaypal() {
            return paypal;
        }

        public void setPaypal(Paypal paypal) {
            this.paypal = paypal;
        }

    }
    public class Paypal implements Serializable {

        @SerializedName("email")
        @Expose
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }
    public class Rating implements Serializable{

        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("count")
        @Expose
        private Integer count;

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
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

    public class Social implements Serializable {

        @SerializedName("fb")
        @Expose
        private Boolean fb;
        @SerializedName("gplus")
        @Expose
        private Boolean gplus;
        @SerializedName("twitter")
        @Expose
        private Boolean twitter;
        @SerializedName("pinterest")
        @Expose
        private Boolean pinterest;
        @SerializedName("linkedin")
        @Expose
        private Boolean linkedin;
        @SerializedName("youtube")
        @Expose
        private Boolean youtube;
        @SerializedName("instagram")
        @Expose
        private Boolean instagram;
        @SerializedName("flickr")
        @Expose
        private Boolean flickr;

        public Boolean getFb() {
            return fb;
        }

        public void setFb(Boolean fb) {
            this.fb = fb;
        }

        public Boolean getGplus() {
            return gplus;
        }

        public void setGplus(Boolean gplus) {
            this.gplus = gplus;
        }

        public Boolean getTwitter() {
            return twitter;
        }

        public void setTwitter(Boolean twitter) {
            this.twitter = twitter;
        }

        public Boolean getPinterest() {
            return pinterest;
        }

        public void setPinterest(Boolean pinterest) {
            this.pinterest = pinterest;
        }

        public Boolean getLinkedin() {
            return linkedin;
        }

        public void setLinkedin(Boolean linkedin) {
            this.linkedin = linkedin;
        }

        public Boolean getYoutube() {
            return youtube;
        }

        public void setYoutube(Boolean youtube) {
            this.youtube = youtube;
        }

        public Boolean getInstagram() {
            return instagram;
        }

        public void setInstagram(Boolean instagram) {
            this.instagram = instagram;
        }

        public Boolean getFlickr() {
            return flickr;
        }

        public void setFlickr(Boolean flickr) {
            this.flickr = flickr;
        }

    }

}
