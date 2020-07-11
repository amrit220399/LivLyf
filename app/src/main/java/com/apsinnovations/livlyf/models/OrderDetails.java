package com.apsinnovations.livlyf.models;

import java.io.Serializable;

public class OrderDetails implements Serializable {
    private Address address;
    private Order order;

    public OrderDetails() {
    }

    public OrderDetails(Address address, Order order) {
        this.address = address;
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "address=" + address +
                ", order=" + order +
                '}';
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
