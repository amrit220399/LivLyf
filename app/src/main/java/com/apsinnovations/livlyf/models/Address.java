package com.apsinnovations.livlyf.models;

import java.io.Serializable;

public class Address implements Serializable {
    String name, addressLine1, addressLine2, landmark, pinCode, mobile;

    public Address() {
    }

    public Address(String name, String addressLine1, String addressLine2, String landmark, String pinCode, String mobile) {
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.landmark = landmark;
        this.pinCode = pinCode;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", landmark='" + landmark + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
