package com.hst.ps2.comp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class dayLeftComp implements Comparator<JSONObject>
{
    @Override
    public int compare(JSONObject a, JSONObject b) {
        int valA = 0;
        int valB = 0;

        try {
            valA = a.getInt("dayLeft");
            valB = b.getInt("dayLeft");
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
