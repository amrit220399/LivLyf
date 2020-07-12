package com.apsinnovations.livlyf.models;

public class Category {
    private int drawable;
    private String name;
    private int color;

    public Category(int drawable, String name, int color) {
        this.drawable = drawable;
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "drawable=" + drawable +
                ", name='" + name + '\'' +
                '}';
    }
}
