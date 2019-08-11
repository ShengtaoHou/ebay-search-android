package com.hst.ps2.comp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class nameComp implements Comparator<JSONObject>
{
    @Override
    public int compare(JSONObject a, JSONObject b) {
        String valA = new String();
        String valB = new String();

        try {
            valA = (String) a.get("productName");
            valB = (String) b.get("productName");
        }
        catch (JSONException e) {
        }

        return valA.compareTo(valB);
    }
}