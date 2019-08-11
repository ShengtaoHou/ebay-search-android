package com.hst.ps2.detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hst.ps2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

public class ProductTab extends Fragment {
    private String brandValue="N/A";
    private LinearLayout mGallery;
    private String[] mImgIds;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;
    private static JSONObject product;
    private static JSONObject shipping;
    public static ProductTab newInstance(JSONObject mProduct,JSONObject mShipping) {
        product=mProduct;
        shipping=mShipping;
        ProductTab myTab = new ProductTab();
        return myTab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mInflater = inflater; //
        View view=inflater.inflate(R.layout.product_tab, container, false);

        initView(view);

        return view;
    }

    private void initView(View mview)
    {
        LinearLayout mGallery = (LinearLayout)mview.findViewById(R.id.gallery); //


        JSONArray imgArr=null;  //image array
        String title="";
        String price="";
        String shipping_info="";
        String subtitle="";
        JSONArray specArr=null;
        String specHTml="";
        try {
            imgArr = product.getJSONArray("images");
            title=product.getString("title");
            price=product.getString("price");

            JSONArray shipping_cost_arr=shipping.getJSONArray("shippingServiceCost");
            JSONObject shipping_cost_obj=shipping_cost_arr.getJSONObject(0);
            shipping_info=shipping_cost_obj.getString("__value__");

            //"shippingServiceCost"
            subtitle=product.getString("subtitle");

            specArr=product.getJSONArray("itemSpec");
            specHTml=parseSpec(specArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("tag", "shipping info: "+shipping_info);

        //set up title
        TextView product_title=(TextView)mview.findViewById(R.id.product_title);
        product_title.setText(title);

        //set up price
        TextView product_price=(TextView)mview.findViewById(R.id.product_price);
        product_price.setText("$"+price);

        //set up shipping
        TextView product_shipping=(TextView)mview.findViewById(R.id.product_shipping);
        if(shipping_info.equals("0.0")){
            product_shipping.setText(" With Free Shipping");
        }else{
            product_shipping.setText(" With $"+shipping_info);
        }


        //set up subtitle
        if(subtitle.equals("N/A")){
            ((LinearLayout)mview.findViewById(R.id.subtitle_area)).setVisibility(View.GONE);
        }else{
            ((TextView)mview.findViewById(R.id.product_subtitle)).setText(subtitle);
        }

        //set up price2
        TextView product_price2=(TextView)mview.findViewById(R.id.product_price2);
        product_price2.setText("$"+price);


        //setup spec
        TextView specView=(TextView)mview.findViewById(R.id.product_spec);
        specView.setText(Html.fromHtml(specHTml, null, new UlTagHandler()));

        //set up brand
        if(brandValue.equals("N/A")){
            ((LinearLayout)mview.findViewById(R.id.brand_area)).setVisibility(View.GONE);
        }else{
            ((TextView)mview.findViewById(R.id.product_brand)).setText(brandValue);
        }

        // inflate images
        for(int i=0;i<imgArr.length();i++){

            View view = mInflater.inflate(R.layout.gallery_item, mGallery, false);

            ImageView img = (ImageView) view.findViewById(R.id.gallery_item_image);

            try {
                Picasso.with(getContext()).load(imgArr.getString(i)).into(img);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mGallery.addView(view);

        }

    }
    public String parseSpec(JSONArray arr){
        String res="";
        String tmp="";
        for(int i=0;i<arr.length();i++){
            try {
                JSONObject obj=arr.getJSONObject(i);
                String specName=obj.getString("Name");
                JSONArray specValueArr=obj.getJSONArray("Value");
                String specValue=specValueArr.getString(0);

                if(specName.equals("Brand")){
                    brandValue=specValue;
                    continue;
                }
                tmp=tmp+"<li>"+specValue+"</li>";

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(!brandValue.equals("N/A")){
            res="<ul><li>"+brandValue+"</li>"+tmp+"</ul>";
        }else{
            res="<ul>"+tmp+"</ul>";
        }

        return res;
    }

    public class UlTagHandler implements Html.TagHandler{
        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {
            if(tag.equals("ul") && !opening) output.append("\n");
            if(tag.equals("li") && opening) output.append("\n\t•");
        }
    }
}