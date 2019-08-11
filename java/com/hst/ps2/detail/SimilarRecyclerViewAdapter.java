package com.hst.ps2.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.ps2.R;
import com.hst.ps2.comp.dayLeftComp;
import com.hst.ps2.comp.mSort;
import com.hst.ps2.comp.nameComp;
import com.hst.ps2.comp.priceComp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class SimilarRecyclerViewAdapter extends RecyclerView.Adapter<SimilarRecyclerViewAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener{

    private JSONArray mData;
    private LayoutInflater mInflater;
    private Context context;
    // data is passed into the constructor
    private static Spinner spinner1;
    private static Spinner spinner2;
    SimilarRecyclerViewAdapter(Context context, JSONArray jsonArr,View mview) {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.mData =jsonArr;

        //setup spinner
        spinner1 = (Spinner) mview.findViewById(R.id.spinner_sortby);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(context,
                R.array.sortby_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(sAdapter);
        spinner1.setOnItemSelectedListener(this);

        //setup spinner
        spinner2 = (Spinner) mview.findViewById(R.id.spinner_order);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> sAdapter2 = ArrayAdapter.createFromResource(context,
                R.array.order_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //diabled at the beginning
        spinner2.setEnabled(false);
        spinner2.setClickable(false);
        //Apply the adapter to the spinner
        spinner2.setAdapter(sAdapter2);
        spinner2.setOnItemSelectedListener(this);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.similar_item, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String url1="";
        try{
            JSONObject simObj=mData.getJSONObject(position);
            String mName=simObj.getString("productName").toUpperCase();
            holder.productName.setText(mName);

            if(simObj.getString("shippingCost").equals("0.00")){
                holder.shippingCost.setText("Free Shipping");
            }else{
                holder.shippingCost.setText("$"+simObj.getString("shippingCost"));
            }

            if(simObj.getInt("dayLeft")<=1){
                holder.dayLeft.setText(simObj.getInt("dayLeft")+" Day Left");
            }else{
                holder.dayLeft.setText(simObj.getInt("dayLeft")+" Days Left");
            }

            holder.price.setText("$"+simObj.getString("price"));

            String imageUri=simObj.getString("url");
            Picasso.with(context).load(imageUri).into(holder.itemImage);

            url1=simObj.getString("viewItemURL");
        }catch(JSONException e) {
            Log.i("similar", "json error");
        }
        final String url2=url1;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url2));
                v.getContext().startActivity(i);
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData==null?0:mData.length();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView productName;
        TextView shippingCost;
        TextView dayLeft;
        TextView price;
        ViewHolder(View itemView) {
            super(itemView);
            itemImage=(ImageView)itemView.findViewById(R.id.similar_item_image);
            productName=(TextView)itemView.findViewById(R.id.similar_productName);
            shippingCost=(TextView)itemView.findViewById(R.id.similar_shippingCost);
            dayLeft=(TextView)itemView.findViewById(R.id.similar_dayLeft);
            price=(TextView)itemView.findViewById(R.id.similar_price);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
            mSort ms=new mSort();
            //toggle disable and enable
            if(spinner1.getSelectedItemPosition()==0){
                spinner2.setEnabled(false);
                spinner2.setClickable(false);
            }else{
                spinner2.setEnabled(true);
                spinner2.setClickable(true);
            }
            mData=ms.sort(mData,spinner1.getSelectedItemPosition(),spinner2.getSelectedItemPosition());
            notifyDataSetChanged();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
