package com.hst.ps2;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WishList extends Application
{
    private HashMap<String,JSONObject> map=new HashMap<>();
    private double totalPrice=0.0;
    public double getTotalPrice(){
        return totalPrice;
    }
    public int getSize(){
        return map.size();
    }
    public JSONArray getList()
    {
        JSONArray arr=new JSONArray();
        for(String key:map.keySet()){
            arr.put(map.get(key));
        }
        return arr;
    }

    public void setList(JSONArray arr)
    {
        if(arr==null)return;
        for(int i=0;i<arr.length();i++){
            try{
                JSONObject obj=arr.getJSONObject(i);
                String key=obj.getString("id");
                String priceStr=obj.getString("price");
                double price=Double.parseDouble(priceStr);
                if(!map.containsKey(key)){
                    map.put(key,obj);
                    totalPrice+=price;
                }
            }catch(JSONException e){
                Log.i("wishlist error", "setList fail");
            }
        }
    }
    public void addItem(JSONObject obj){
        try{
            String key=obj.getString("id");
            String priceStr=obj.getString("price");
            double price=Double.parseDouble(priceStr);
            if(!map.containsKey(key)){
                map.put(key,obj);
                totalPrice+=price;
            }
        }catch(JSONException e){
            Log.i("wishlist error", "addItem fail");
        }
    }

    public void removeItem(JSONObject obj){
        try{
            String key=obj.getString("id");
            String priceStr=obj.getString("price");
            double price=Double.parseDouble(priceStr);
            if(map.containsKey(key)){
                map.remove(key);
                totalPrice-=price;
            }
        }catch(JSONException e){
            Log.i("wishlist error", "removeItem fail");
        }
    }
    public boolean hasItem(String key){
        if(key==null)return false;
        return map.containsKey(key);
    }
}//End Class