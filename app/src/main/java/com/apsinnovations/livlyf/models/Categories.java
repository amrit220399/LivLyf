package com.apsinnovations.livlyf.models;

import java.io.Serializable;

public class Categories implements Serializable {
    private String name, url;

    public Categories(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
