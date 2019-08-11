package com.hst.ps2.comp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class priceComp implements Comparator<JSONObject>
{
    @Override
    public int compare(JSONObject a, JSONObject b) {
        double valA=0.0;
        double valB=0.0;

        try {
            valA = a.getDouble("price");
            valB = b.getDouble("price");
        }
        catch (JSONException e) {
        }
        if(valA>valB){
            return 1;
        }else if(valA==valB){
            return 0;
        }else{
            return -1;
        }
    }
}