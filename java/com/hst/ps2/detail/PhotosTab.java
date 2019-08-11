package com.hst.ps2.detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hst.ps2.R;

import org.json.JSONArray;

public class PhotosTab extends Fragment {
    private static PhotosRecyclerViewAdapter adapter;
    private static JSONArray photos;

    public static PhotosTab newInstance(JSONArray mPhotos){
        //pass photos data
        photos=mPhotos;
        PhotosTab myTab = new PhotosTab();
        return myTab;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //choose a view
        View view=inflater.inflate(R.layout.photos_tab, container, false);
        //choose a recyclerView inside the view
        RecyclerView recyclerView = view.findViewById(R.id.photos_tab);
        //set recyclerView's column number
        int numberOfColumns = 1;
        //set recyclerView's layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        //set recyclerView's adapter
        adapter = new PhotosRecyclerViewAdapter(getContext(), photos);
        recyclerView.setAdapter(adapter);

        return view;
    }
}