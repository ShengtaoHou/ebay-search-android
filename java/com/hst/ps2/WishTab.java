package com.hst.ps2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;

import java.text.DecimalFormat;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class WishTab extends Fragment {

    public static WishRecyclerViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view;
        WishList curWishList = ((WishList)getApplicationContext());
        JSONArray itemsArr =curWishList.getList(); //get current wish list

        view= inflater.inflate(R.layout.wish_tab, container, false);

        //toggle error message
        if(itemsArr.length()==0){
            view.findViewById(R.id.wish_tab_error_text).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.wish_tab_error_text).setVisibility(View.GONE);
        }

        //get view
        RecyclerView recyclerView = view.findViewById(R.id.rvWish);
        //set view's layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //set view's adapter
        adapter = new WishRecyclerViewAdapter(getContext(),view);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
