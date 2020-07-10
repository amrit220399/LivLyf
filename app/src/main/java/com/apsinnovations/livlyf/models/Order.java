package com.apsinnovations.livlyf.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    String orderID;
    Timestamp timestamp;
    ArrayList<Products> items;
    double orderValue;
    int status;

    public Order() {
    }

    public Order(String orderID, Timestamp timestamp, ArrayList<Products> items, double orderValue, int status) {
        this.orderID = orderID;
        this.timestamp = timestamp;
        this.items = items;
        this.orderValue = orderValue;
        this.status = status;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + orderID + '\'' +
                ", timestamp=" + timestamp +
                ", items=" + items +
                ", orderValue=" + orderValue +
                ", status=" + status +
                '}';
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Products> getItems() {
        return items;
    }

    public void setItems(ArrayList<Products> items) {
        this.items = items;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
