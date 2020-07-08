package com.apsinnovations.livlyf.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CartPrefMananger {
    private static final String PREF_NAME = "cart";
    private static final int ITEMS = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public CartPrefMananger(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public int getItemsCount() {
        return pref.getInt("items", 0);
    }

    public void setItemsCount(int items) {
        int updatedItems = pref.getInt("items", 0);
        updatedItems += items;
        editor.putInt("items", updatedItems);
        editor.commit();
    }
}
