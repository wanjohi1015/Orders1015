package com.blueprint211.delreq.Model;

public class Orders {


    String title, userEmail;
    String productid, imageUrl;
    int price, quantity;


    public Orders() {
    }

    public Orders(String title, String productid, String imageUrl,  String userEmail, int price, int quantity) {
        this.title = title;
        this.productid = productid;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;

        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

