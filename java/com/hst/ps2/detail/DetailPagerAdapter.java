package com.hst.ps2.detail;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.ps2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
//    JSONObject mData;

    JSONObject product;
    JSONObject shipping;
    JSONArray photos;
    JSONObject seller;
    JSONArray similar;

    public DetailPagerAdapter(FragmentManager fm, int NumOfTabs, JSONObject detail, JSONObject curItem) {
        super(fm);
        try {
            shipping=curItem.getJSONObject("shippingInfo");
            product=detail.getJSONObject("product");
            photos=detail.getJSONArray("photos");
            seller=detail.getJSONObject("seller");
            similar=detail.getJSONArray("similar");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProductTab tab1 = ProductTab.newInstance(product,shipping);
                return tab1;
            case 1:
                ShippingTab tab2 = ShippingTab.newInstance(product,shipping,seller);
                return tab2;
            case 2:
                PhotosTab tab3 = PhotosTab.newInstance(photos);
                return tab3;
            case 3:
                SimilarTab tab4 = SimilarTab.newInstance(similar);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}