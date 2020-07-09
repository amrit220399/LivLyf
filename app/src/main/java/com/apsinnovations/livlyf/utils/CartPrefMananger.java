package com.apsinnovations.livlyf.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.apsinnovations.livlyf.models.Products;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CartPrefMananger {
    private static final String PREF_NAME = "cart";
    private static final String TAG = "CartPrefMananger";
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

    public ArrayList<Products> getCartItems(){
        Gson gson=new Gson();
        Map<String,?> map=pref.getAll();
        ArrayList<Products> products=new ArrayList<>();
        for(Map.Entry<String,?> entry:map.entrySet()){
            if(entry.getKey().equals("items")){
                continue;
            }
            String json=pref.getString(entry.getKey(),"");
            Products myProduct=gson.fromJson(json,Products.class);
            products.add(myProduct);
        }
        return products;

//        return Collections.singletonList(gson.fromJson(json, Products.class));
//        Type type=new TypeToken<ArrayList<Products>>(){}.getType();
//        Log.i(TAG, "getCartItems: json "+json);
//        Log.i(TAG, "getCartItems: type"+type);
//        return gson.fromJson(json,type);
    }

    public void setCartItem(Products products){
//        String name=pref.getString("itemName","");
        Gson gson=new Gson();
        String json=gson.toJson(products);
//        Log.i(TAG, "setCartItem: JSON INITIAL"+json);
        editor.putString(products.getID(),json);
//        if(name.isEmpty()){
//            editor.putString("itemName",json);
//        }else{
//            String merged="{"+name.concat(",").concat(json).concat("}");
//            Log.i(TAG, "setCartItem: Name "+name);
//            Log.i(TAG, "setCartItem: JSON"+json);
//            Log.i(TAG, "setCartItem: merged"+merged);
//            editor.putString("itemName",merged);
//        }
    }
}
