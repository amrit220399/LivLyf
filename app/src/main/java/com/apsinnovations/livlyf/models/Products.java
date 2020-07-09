package com.apsinnovations.livlyf.models;

import java.io.Serializable;

public class Products implements Serializable {
    int price, mrp, qty, shipping, discount;
    String ID, name, category, url;

    public void setID(String ID) {
        this.ID = ID;
    }

    public Products() {

    }

    public Products(String name, String category, String url, int price, int mrp, int qty, int shipping, int discount) {
        this.name = name;
        this.category = category;
        this.url = url;
        this.price = price;
        this.mrp = mrp;
        this.qty = qty;
        this.shipping = shipping;
        this.discount = discount;
    }

    public String getUrl() {
        return url;
    }

    public int getMrp() {
        return mrp;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public int getShipping() {
        return shipping;
    }

    public int getDiscount() {
        return discount;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Products{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", shipping=" + shipping +
                ", discount=" + discount +
                '}';
    }
}
