package com.hst.ps2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hst.ps2.R;
import com.hst.ps2.detail.DetailPage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import static com.hst.ps2.list.ListPage.CUR_ITEM;
import static com.hst.ps2.list.ListPage.DETAIL_URL;

public class WishRecyclerViewAdapter extends RecyclerView.Adapter<WishRecyclerViewAdapter.ViewHolder> {

    public static JSONArray wishData;
    private LayoutInflater mInflater;
    private static Context context;
    private static View parentView;
    // data is passed into the constructor
    WishRecyclerViewAdapter(Context context, View view) {
        WishList curWishList = ((WishList)context.getApplicationContext());
        JSONArray itemsArr =curWishList.getList(); //get current wish list

        this.mInflater = LayoutInflater.from(context);
        this.wishData = itemsArr;
        this.context=context;
        this.parentView=view;
        updateSummary();
    }
    public static void updateSummary(){
        WishList curWishList = ((WishList)context.getApplicationContext());
        JSONArray itemsArr =curWishList.getList(); //get current wish list
        //set summary area
        String wish_num_str="Wishlist total("+curWishList.getSize()+" items):";
        TextView wish_num=((TextView)parentView.findViewById(R.id.wish_num));
        wish_num.setText(wish_num_str);

        DecimalFormat df=new DecimalFormat("0.00");
        String fprice = df.format(curWishList.getTotalPrice());
        String wish_price_str="$"+fprice;

        TextView wish_price=((TextView)parentView.findViewById(R.id.wish_price));
        wish_price.setText(wish_price_str);

        //toggle error message
        if(wishData.length()==0){
            parentView.findViewById(R.id.wish_tab_error_text).setVisibility(View.VISIBLE);
        }else{
            parentView.findViewById(R.id.wish_tab_error_text).setVisibility(View.GONE);
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wish_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String idStr="";
        JSONObject jsonObject=null;
        try{
            jsonObject=wishData.getJSONObject(position);
            idStr=jsonObject.getString("id");
            String mtitle=jsonObject.getString("title").toUpperCase();
            if(mtitle.length()>50){
                StringBuilder sb=new StringBuilder(mtitle.substring(0,50));
                sb.append("...");
                mtitle=sb.toString();
            }
            holder.itemTitle.setText(mtitle);
            holder.itemZipcode.setText("Zip:"+jsonObject.getString("zip"));
            holder.itemShipping.setText(jsonObject.getString("shipping"));
            holder.itemPrice.setText("$"+jsonObject.getString("price"));
            String imageUri = jsonObject.getString("image");
            Picasso.with(context).load(imageUri).into(holder.itemImage);

            WishList mlist=((WishList)context.getApplicationContext());
            if(mlist.hasItem(idStr)){
                holder.itemCart.setImageResource(R.drawable.remove_shopping);
                holder.itemCart.setTag(R.drawable.remove_shopping);
            }else{
                holder.itemCart.setImageResource(R.drawable.add_shopping);
                holder.itemCart.setTag(R.drawable.add_shopping);
            }

            holder.itemCondition.setText(jsonObject.getString("condition"));
        }catch(JSONException e) {
            Log.i("TAG", "wish tab json error");
        }
        final JSONObject curObj=jsonObject;
        final String id=idStr;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //call getsingle item api using id and jump to detail page
            final Intent intent = new Intent(v.getContext(), DetailPage.class);
            String url ="http://hsthw9.us-east-2.elasticbeanstalk.com/item/"+id;
            intent.putExtra(CUR_ITEM,curObj.toString());
            intent.putExtra(DETAIL_URL,url);
            intent.putExtra("name","wishtab");
            intent.putExtra("position",""+position);
            context.startActivity(intent);
            //dev
            //intent.putExtra(CUR_ITEM,"{\"index\":1,\"image\":\"http://thumbs1.ebaystatic.com/pict/2634162325324040_4.jpg\",\"title\":\"3 Pack 90 Degree iPhone Cable Right Angle lightning Cable USB Charger &Data Cord\",\"id\":\"263416232532\",\"price\":\"15.49\",\"shipping\":\"Free Shipping\",\"zip\":\"91745\",\"seller\":\"w3d-trading\",\"wishlist\":false,\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"0\"],\"returnsAccepted\":[\"true\"]}}");
            // intent.putExtra(DETAIL_URL,"{\"product\":{\"images\":[\"https://i.ebayimg.com/00/s/NTc3WDc3MA==/z/UssAAOSwS3dbmWBH/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzYzWDE0Njc=/z/cx0AAOSwky1bmWBx/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NjEwWDk3MA==/z/I4QAAOSwo2dbmWAr/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/MTAwMFgxMDAw/z/xKkAAOSwk6lbmWA~/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/ODEwWDEzOTE=/z/fVkAAOSw76dbmWBO/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/5fEAAOSw9i9bmWBV/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NDM0WDU2OA==/z/4HoAAOSwYxdbmWBc/$_57.JPG?set_id=8800005007\"],\"title\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey  \",\"subtitle\":\"Same Day Shipping+Free Gift($12 Value)IN BOX OEM USA \",\"price\":189.99,\"location\":\"Los Angeles, California\",\"returnPolicy\":\"Returns Accepted within 30 Days\",\"itemSpec\":[{\"Name\":\"Model\",\"Value\":[\"Apple Iphone 6\"]},{\"Name\":\"Modified Item\",\"Value\":[\"No\"]},{\"Name\":\"Contract\",\"Value\":[\"Without Contract\"]},{\"Name\":\"Custom Bundle\",\"Value\":[\"No\"]},{\"Name\":\"Cellular Band\",\"Value\":[\"GSM\"]},{\"Name\":\"Network Technology\",\"Value\":[\"GSM\"]},{\"Name\":\"Non-Domestic Product\",\"Value\":[\"No\"]},{\"Name\":\"Lock Status\",\"Value\":[\"Factory Unlocked\"]},{\"Name\":\"MPN\",\"Value\":[\"MGAN2LL/A.\"]},{\"Name\":\"Network\",\"Value\":[\"Factory Unlocked\"]},{\"Name\":\"Brand\",\"Value\":[\"Apple\"]}]},\"photos\":[\"https://i.ebayimg.com/00/s/MTUyOVgxNjAw/z/DFYAAOSwA3dYGMt9/$_57.JPG?set_id=880000500F\",\"https://d3d71ba2asa5oz.cloudfront.net/12005722/images/iph6sunl-d_838__2.jpg\",\"https://i.ebayimg.com/images/g/99kAAOSw2vJcaoMc/s-l1600.jpg\",\"https://bsl.cwa.sellercloud.com/images/products/232784.jpg\",\"https://i.ebayimg.com/images/g/0EIAAOSwQTxahg~i/s-l1600.png\",\"https://s3-eu-west-1.amazonaws.com/images.linnlive.com/345d573a4a65bbf1f90ef3a271a58654/2b723374-3c5a-4094-bf18-c8778924d5c0.jpg\",\"https://i.ebayimg.com/images/g/Se4AAOSwfplcKkaU/s-l1600.jpg\",\"https://bsl.cwa.sellercloud.com/images/products/274152.jpg\"],\"seller\":{\"feedbackScore\":1642,\"popularity\":98.2,\"feedbackRating\":\"Red\",\"score\":false,\"topRated\":true,\"storeName\":\"cheapgoods\",\"buyProduct\":\"https://stores.ebay.com/id=1074461351\"},\"similar\":[{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey I\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/maoPmjQtpvPRytCcy82kKaw.jpg\",\"price\":179.99,\"shippingCost\":\"0.00\",\"dayLeft\":20},{\"productName\":\"Apple iPhone 6 16GB 64GB 128GB Factory Unlocked AT&T Verizon T-Mobile Sprint\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mF7q7VHNLtxwmDtfxGNDgdA.jpg\",\"price\":99.99,\"shippingCost\":\"0.00\",\"dayLeft\":3},{\"productName\":\"Apple iPhone 6 16GB 64GB 128GB GSM Unlocked AT&T T-Mobile Cricket Black Gold New\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/muUyuKowRme3boOXlrPcA-w.jpg\",\"price\":149.49,\"shippingCost\":\"0.00\",\"dayLeft\":21},{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey I\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/maoPmjQtpvPRytCcy82kKaw.jpg\",\"price\":179.99,\"shippingCost\":\"0.00\",\"dayLeft\":20},{\"productName\":\"NEW Apple iPhone 6 16GB 64GB  GSM/ CDMA Factory Unlocked Gold Silver Grey Sale!!\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mjUK7bWUsOiRaNs0-6k2j4Q.jpg\",\"price\":179.99,\"shippingCost\":\"0.00\",\"dayLeft\":9},{\"productName\":\"NEW APPLE IPHONE 6 VERIZON CDMA AT&T GSM UNLOCKED 16-64-128GB ALL COLORS\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mjUK7bWUsOiRaNs0-6k2j4Q.jpg\",\"price\":179.99,\"shippingCost\":\"0.00\",\"dayLeft\":8},{\"productName\":\"Apple iPhone 6 Plus 16GB 64GB 128GB Factory Unlocked AT&T Verizon T-Mobile\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mLuYJMWmpsoAwJdLa3ykzzw.jpg\",\"price\":154.99,\"shippingCost\":\"0.00\",\"dayLeft\":3},{\"productName\":\"Apple iPhone 6 Smartphone 16GB 64GB 128GB AT&T Verizon Unlocked T-Mobile Sprint \",\"url\":\"https://securethumbs.ebay.com/d/l140/m/meImRyF9oDrEsrr6ipYSnyg.jpg\",\"price\":94,\"shippingCost\":\"0.00\",\"dayLeft\":2},{\"productName\":\"Apple iPhone 6 - (UNLOCKED) | Gray Gold Silver | 16GB 64GB 128GB GSM + CDMA\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/maMaIDrnmGywvETHUj6s4QQ.jpg\",\"price\":189.99,\"shippingCost\":\"0.00\",\"dayLeft\":29},{\"productName\":\"Unlocked Apple iPhone 6, 6 Plus Sealed in Box GSM 16GB 64GB 128GB\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mtPyG6aRqf6oqw8hR3Mzk0A.jpg\",\"price\":179.95,\"shippingCost\":\"0.00\",\"dayLeft\":4},{\"productName\":\"Apple iPhone 6s - 16GB 32GB 64GB 128GB - Unlocked AT&T Verizon T-Mobile Sprint\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/m0L_WXzlRPW0S-W7vLjqvHw.jpg\",\"price\":119.99,\"shippingCost\":\"0.00\",\"dayLeft\":28},{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB UNLOCKED Gold Silver Space Gray GSM CDMA\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mou9IXBHO3rJh7ZHK2_g7KA.jpg\",\"price\":179.99,\"shippingCost\":\"0.00\",\"dayLeft\":28},{\"productName\":\"NEW Apple iPhone 6 16GB 32GB 64GB 128GB UNLOCKED Gold Silver Space Gray GSM\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/ma1XPxROqVAAVdIxpalrntw.jpg\",\"price\":209.95,\"shippingCost\":\"0.00\",\"dayLeft\":16},{\"productName\":\"Brand New Apple iPhone 6S 16GB 64GB 128GB Factory Unlocked GSM/CDMA AT&T Verizon\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mlX7eMvoUmFxs4izik9eoKw.jpg\",\"price\":214.99,\"shippingCost\":\"0.00\",\"dayLeft\":6},{\"productName\":\"Apple iPhone 6 16GB / 64GB / 128GB - Space Gray / Silver / Gold\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/msv313qmgGcg22VenWr5NGA.jpg\",\"price\":99.99,\"shippingCost\":\"0.00\",\"dayLeft\":28},{\"productName\":\"iPhone 6 - (UNLOCKED ATT MetroPCS T-Mobile) | Gray Gold Silver | 16GB 64GB 128GB\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/muUyuKowRme3boOXlrPcA-w.jpg\",\"price\":119.95,\"shippingCost\":\"0.00\",\"dayLeft\":8},{\"productName\":\"Apple iPhone SE 16-32-64-128GB Pink-Gray-Silver-Gold GSM+CDMA Unlocked [A1662]\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mZ6Qfp2yGoUEVlMic1eTTkw.jpg\",\"price\":186.99,\"shippingCost\":\"0.00\",\"dayLeft\":0},{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey I\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/maoPmjQtpvPRytCcy82kKaw.jpg\",\"price\":209.99,\"shippingCost\":\"0.00\",\"dayLeft\":20},{\"productName\":\"Apple iPhone 6 - 16GB/64GB/128GB (Unlocked) Support any sim card\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mt6GbVOjDcQqfLIkH3YZmew.jpg\",\"price\":220,\"shippingCost\":\"0.00\",\"dayLeft\":2},{\"productName\":\"Apple iPhone 6 Plus - 16GB, 64GB 128GB ALL COLORS (Factory GSM Unlocked)\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/m11r5dbPEDT5S3CU8lfjD3A.jpg\",\"price\":194.99,\"shippingCost\":\"0.00\",\"dayLeft\":25}],\"fbInfo\":{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey  \",\"price\":189.99,\"link\":\"https://www.ebay.com/itm/NEW-Apple-iPhone-6-16GB-64GB-128GB-GSM-CDMA-Factory-Unlocked-Gold-Silver-Grey-/282389702634?var=\"}}");
            //v.getContext().startActivity(intent);
            }
        });


        holder.itemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //removed from wishlist
                WishList curList=((WishList)context.getApplicationContext());
                curList.removeItem(curObj);

                //remove from view
                wishData.remove(position);
                notifyDataSetChanged();

                //toggle error message
                if(wishData.length()==0){
                    parentView.findViewById(R.id.wish_tab_error_text).setVisibility(View.VISIBLE);
                }else{
                    parentView.findViewById(R.id.wish_tab_error_text).setVisibility(View.GONE);
                }

                //update summary area
                String wish_num_str="Wishlist total("+curList.getSize()+" items):";
                TextView wish_num=((TextView)parentView.findViewById(R.id.wish_num));
                wish_num.setText(wish_num_str);

                DecimalFormat df=new DecimalFormat("0.00");
                String fprice = df.format(curList.getTotalPrice());
                String wish_price_str="$"+fprice;
                TextView wish_price=((TextView)parentView.findViewById(R.id.wish_price));
                wish_price.setText(wish_price_str);

                String short_title="";
                String long_title=holder.itemTitle.getText().toString();
                if(long_title.length()>40){
                    short_title=long_title.substring(0,40)+"...";
                }else{
                    short_title=long_title;
                }

                Toast toast = Toast.makeText(context, short_title+" was remove from wishlist", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                int backgroundColor = ResourcesCompat.getColor(toast.getView().getResources(), R.color.colorToast, null);
                toastView.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                TextView text = (TextView) toastView.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#000000"));
                toast.show();
                //toast information
                //Toast.makeText(v.getContext(),  holder.itemTitle.getText().toString()+" was remove from wishlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return wishData==null?0:wishData.length();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        int id;
        TextView itemTitle;
        TextView itemZipcode;
        TextView itemShipping;
        TextView itemPrice;
        ImageView itemImage;
        ImageView itemCart;
        TextView itemCondition;
        ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.wish_item_title);
            itemZipcode=itemView.findViewById(R.id.wish_item_zipcode);
            itemShipping = itemView.findViewById(R.id.wish_item_shipping);
            itemPrice=itemView.findViewById(R.id.wish_item_price);
            itemImage=(ImageView)itemView.findViewById(R.id.wish_item_image);
            itemCart=(ImageView)itemView.findViewById(R.id.wish_item_shopping_cart);
            itemCondition=(TextView)itemView.findViewById(R.id.wish_item_condition);
        }

    }
}
