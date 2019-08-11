package com.hst.ps2.comp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class mSort {
    public JSONArray sort(JSONArray jsonArr,int sortby,int order){
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();

        //JSONarray to list
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                jsonValues.add(jsonArr.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //sort list
        if(sortby==1){
            Collections.sort( jsonValues, new nameComp());
        }else if(sortby==2){
            Collections.sort( jsonValues, new priceComp());
        }else if(sortby==3){
            Collections.sort( jsonValues, new dayLeftComp());
        }else{
            //default
        }

        //list to JSONArray
        if(order==0){
            for (int i = 0; i <jsonArr.length() ; i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
        }else{
            for (int i = jsonArr.length()-1; i >=0 ; i--) {
                sortedJsonArray.put(jsonValues.get(i));
            }
        }

        return sortedJsonArray;
    }
    public JSONArray reverse(JSONArray jsonArr){
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();

        //JSONArray to list
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                jsonValues.add(jsonArr.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //list to JSONArray
        for (int i = jsonArr.length()-1; i >=0 ; i--) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }
}
