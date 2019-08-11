package com.hst.ps2.detail;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hst.ps2.R;
import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShippingTab extends Fragment {
    private static JSONObject product;
    private static JSONObject shipping;
    private static JSONObject seller;
    public static ShippingTab newInstance(JSONObject product1,JSONObject shipping1,JSONObject seller1){
        //pass photos data
        product=product1;
        shipping=shipping1;
        seller=seller1;
        ShippingTab myTab = new ShippingTab();
        return myTab;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shipping_tab, container, false);

        //seller
        String storeName="";  //store name
        String buyProduct="";  //store link
        String feedbackScore="";
        int popularity=0;
        Boolean star=false;
        String feedbackRating=""; //star color
        //shipping
        String shipping_cost="";
        String global_shipping="";
        String handle_time="";
        String condition="";
        //return policy
        String policy="";
        String return_within="";
        String refund_mode="";
        String shipped_by="";

        try{
            storeName=seller.getString("storeName");
            buyProduct=seller.getString("buyProduct");

            feedbackScore=seller.getString("feedbackScore");
            popularity=seller.getInt("popularity");

            feedbackRating=seller.getString("feedbackRating");
            star=seller.getBoolean("score");

            //shipping
            JSONArray shipping_cost_arr=shipping.getJSONArray("shippingServiceCost");
            JSONObject shipping_cost_obj=shipping_cost_arr.getJSONObject(0);
            shipping_cost=shipping_cost_obj.getString("__value__");
            if(shipping_cost.equals("0.0")){
                shipping_cost="Free Shipping";
            }else{
                shipping_cost="$"+shipping_cost;
            }

            JSONArray global_shipping_arr=shipping.getJSONArray("shipToLocations");
            global_shipping=global_shipping_arr.getString(0);

            JSONArray handle_time_arr=shipping.getJSONArray("handlingTime");
            handle_time=handle_time_arr.getString(0);

            condition=product.getString("condition");

            //return policy
            refund_mode=product.getString("refund");
            shipped_by=product.getString("shippingCostPaidBy");
            JSONArray policy_arr=shipping.getJSONArray("returnsAccepted");
            policy=policy_arr.getString(0);
            if(policy.equals("true")){
                policy="Return Accepted";
            }else{
                policy="Return No Accepted";
            }

            String tmp=product.getString("returnPolicy");
            return_within=tmp.substring(tmp.length()-7);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        //store name
        if(storeName.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.store_area)).setVisibility(View.GONE);
        }else{
            TextView store=((TextView)view.findViewById(R.id.store));
            String storeNameUrl="<a href=\""+buyProduct+"\">"+storeName+"</a>";
            store.setMovementMethod(LinkMovementMethod.getInstance());
            store.setText(Html.fromHtml(storeNameUrl));
        }

        //feedback score
        if(feedbackScore.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.feedback_score_area)).setVisibility(View.GONE);
        }else{
            ((TextView)view.findViewById(R.id.feedback_score)).setText(feedbackScore);
        }

        //popularity
        if(popularity==0){
            ((LinearLayout)view.findViewById(R.id.popularity_area)).setVisibility(View.GONE);
        }else{
            CircularScoreView circularScoreView = (CircularScoreView) view.findViewById(R.id.popularity);
            circularScoreView.setScore(popularity);
        }

        //feedback star
        ImageView starImg=((ImageView)view.findViewById(R.id.feedback_star));
        if(star){ //Shooting
            starImg.setImageResource(R.drawable.star_circle);
            //Log.i("color", "color1: "+feedbackRating);
            int pos=feedbackRating.indexOf("Shooting");
            if(pos!=-1){
                String tmp=feedbackRating.substring(0,pos);
                //Log.i("color", "color2 name: "+tmp);
                starImg.setColorFilter(getColor(tmp));
            }else{
                starImg.setColorFilter(getColor(feedbackRating));
            }
        }else{  //no Shooting
            starImg.setImageResource(R.drawable.star_circle_outline);
            //Log.i("color", "color3 name: "+feedbackRating);
            starImg.setColorFilter(getColor(feedbackRating));
        }

        //shipping cost
        if(shipping_cost.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.shipping_cost_area)).setVisibility(View.GONE);
        }else {
            ((TextView) view.findViewById(R.id.shipping_cost)).setText(shipping_cost);
        }

        //global shipping
        if(global_shipping.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.gloabal_area)).setVisibility(View.GONE);
        }else {
            ((TextView)view.findViewById(R.id.gloabal)).setText(global_shipping);
        }

        //handling time
        if(handle_time.equals("0")){
            ((LinearLayout)view.findViewById(R.id.handling_area)).setVisibility(View.GONE);
        }else if(handle_time.equals("1")){
            ((TextView)view.findViewById(R.id.handling)).setText(handle_time+"day");
        }else{
            ((TextView)view.findViewById(R.id.handling)).setText(handle_time+"days");
        }

        //condition
        if(condition.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.condition_area)).setVisibility(View.GONE);
        }else{
            ((TextView)view.findViewById(R.id.condition)).setText(condition);
        }


        //policy
        if(policy.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.policy_area)).setVisibility(View.GONE);
        }else{
            ((TextView)view.findViewById(R.id.policy)).setText(policy);
        }

        //return within
        if(return_within.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.return_within_area)).setVisibility(View.GONE);
        }else{
            ((TextView)view.findViewById(R.id.return_within)).setText(return_within);
        }

        //refund mode
        if(refund_mode.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.return_mode_area)).setVisibility(View.GONE);
        }else{
            ((TextView)view.findViewById(R.id.return_mode)).setText(refund_mode);
        }

        //shipped by
        if(shipped_by.equals("N/A")){
            ((LinearLayout)view.findViewById(R.id.shipped_by_area)).setVisibility(View.GONE);
        }else{
            ((TextView)view.findViewById(R.id.shipped_by)).setText(shipped_by);
        }
        return view;
    }
    public int getColor(String str){
        switch(str){
            case "Blue":
                return Color.BLUE;
            case "Green":
                return Color.GREEN;
            case "Purple":
                return Color.parseColor("purple");
            case "Red":
                return Color.RED;
            case "Silver":
                return Color.parseColor("silver");
            case "Turquoise":
                return Color.parseColor("#40E0D0");
            case "Yellow":
                return Color.YELLOW;
        }
        return Color.BLACK;
    }
}