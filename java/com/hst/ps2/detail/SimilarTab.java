package com.hst.ps2.detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hst.ps2.R;
import com.hst.ps2.comp.mSort;

import org.json.JSONArray;

public class SimilarTab extends Fragment {
    private static SimilarRecyclerViewAdapter adapter;
    private static JSONArray similar;
    public static SimilarTab newInstance(JSONArray mSimilar){
        //pass photos data
        similar=mSimilar;
        SimilarTab myTab = new SimilarTab();
        return myTab;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get tab view
        View view;
        if(similar==null||similar.length()==0){
            view=inflater.inflate(R.layout.error_page, container, false);
        }else{
            view=inflater.inflate(R.layout.similar_tab, container, false);
            //get recycler view
            RecyclerView recyclerView = view.findViewById(R.id.similar_tab);
            //set recyclerView's column number
            int numberOfColumns = 1;
            //set recyclerView's layout manager
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
            //set recyclerView's adapter
            adapter = new SimilarRecyclerViewAdapter(getContext(), similar,view);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }




}