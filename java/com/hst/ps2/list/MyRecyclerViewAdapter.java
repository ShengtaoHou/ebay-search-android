package com.hst.ps2.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
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
import com.hst.ps2.WishList;
import com.hst.ps2.detail.DetailPage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.icu.lang.UCharacter.toUpperCase;
import static com.hst.ps2.list.ListPage.CUR_ITEM;
import static com.hst.ps2.list.ListPage.DETAIL_URL;
import static com.hst.ps2.MainActivity.KEYWORD;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private JSONArray mData;
    private LayoutInflater mInflater;
    private Context context;
    private String keyword;
    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, JSONArray data, String keyword) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this.keyword=keyword;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String idstr="";
        JSONObject jsonObject=null;
        try{
            jsonObject=mData.getJSONObject(position);
            idstr=jsonObject.getString("id");
            String mtitle=jsonObject.getString("title").toUpperCase();
            if(mtitle.length()>50){
                StringBuilder sb=new StringBuilder(mtitle.substring(0,50));
                sb.append("...");
                mtitle=sb.toString();
            }
            holder.itemCondition.setText(jsonObject.getString("condition"));
            holder.itemTitle.setText(mtitle);
            holder.itemZipcode.setText("Zip:"+jsonObject.getString("zip"));
            holder.itemShipping.setText(jsonObject.getString("shipping"));
            holder.itemPrice.setText("$"+jsonObject.getString("price"));
            Log.i("cur json", "json: "+jsonObject);
            Log.i("condition", "value: "+jsonObject.getString("condition"));


            String imageUri = jsonObject.getString("image");
            Picasso.with(context).load(imageUri).into(holder.itemImage);

            WishList mlist=((WishList)context.getApplicationContext());
            if(mlist.hasItem(idstr)){
                holder.itemCart.setImageResource(R.drawable.remove_shopping);
                holder.itemCart.setTag(R.drawable.remove_shopping);
            }else{
                holder.itemCart.setImageResource(R.drawable.add_shopping);
                holder.itemCart.setTag(R.drawable.add_shopping);
            }

        }catch(JSONException e) {
            Log.i("TAG", "json object error");
            holder.itemTitle.setText("N/A");
            holder.itemZipcode.setText("N/A");
            holder.itemShipping.setText("N/A");
            holder.itemPrice.setText("N/A");
            holder.itemCondition.setText("N/A");
        }
        final JSONObject curObj=jsonObject;
        final String id=idstr;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context=v.getContext();
                final Intent intent = new Intent(v.getContext(), DetailPage.class);

                //call getsingle item api using id and jump to detail page

                String url ="http://hsthw9.us-east-2.elasticbeanstalk.com/item/"+id;
                Log.i("Search single", url);

                intent.putExtra("name","list");
                intent.putExtra("list_result", mData.toString());
                intent.putExtra(KEYWORD,keyword);
                intent.putExtra(CUR_ITEM,curObj.toString());
                intent.putExtra(DETAIL_URL,url);
                intent.putExtra("position","0");
                context.startActivity(intent);



            //DEV MODE
            //intent.putExtra(CUR_ITEM,"{\"index\":1,\"image\":\"http://thumbs1.ebaystatic.com/pict/2634162325324040_4.jpg\",\"title\":\"3 Pack 90 Degree iPhone Cable Right Angle lightning Cable USB Charger &Data Cord\",\"id\":\"263416232532\",\"price\":\"15.49\",\"shipping\":\"Free Shipping\",\"zip\":\"91745\",\"seller\":\"w3d-trading\",\"wishlist\":false,\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"0\"],\"returnsAccepted\":[\"true\"]}}");
            //intent.putExtra(DETAIL_INFO,"{\"product\":{\"images\":[\"https://i.ebayimg.com/00/s/NTc3WDc3MA==/z/UssAAOSwS3dbmWBH/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzYzWDE0Njc=/z/cx0AAOSwky1bmWBx/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NjEwWDk3MA==/z/I4QAAOSwo2dbmWAr/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/MTAwMFgxMDAw/z/xKkAAOSwk6lbmWA~/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/ODEwWDEzOTE=/z/fVkAAOSw76dbmWBO/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/5fEAAOSw9i9bmWBV/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NDM0WDU2OA==/z/4HoAAOSwYxdbmWBc/$_57.JPG?set_id=8800005007\"],\"title\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey  \",\"subtitle\":\"Same Day Shipping+Free Gift($12 Value)IN BOX OEM USA \",\"price\":189.99,\"location\":\"Los Angeles, California\",\"returnPolicy\":\"Returns Accepted within 30 Days\",\"refund\":\"Money back or replacement (buyer's choice)\",\"shippingCostPaidBy\":\"Seller\",\"condition\":\"New\",\"itemSpec\":[{\"Name\":\"Model\",\"Value\":[\"Apple Iphone 6\"]},{\"Name\":\"Modified Item\",\"Value\":[\"No\"]},{\"Name\":\"Contract\",\"Value\":[\"Without Contract\"]},{\"Name\":\"Custom Bundle\",\"Value\":[\"No\"]},{\"Name\":\"Cellular Band\",\"Value\":[\"GSM\"]},{\"Name\":\"Network Technology\",\"Value\":[\"GSM\"]},{\"Name\":\"Non-Domestic Product\",\"Value\":[\"No\"]},{\"Name\":\"Lock Status\",\"Value\":[\"Factory Unlocked\"]},{\"Name\":\"MPN\",\"Value\":[\"MGAN2LL/A.\"]},{\"Name\":\"Network\",\"Value\":[\"Factory Unlocked\"]},{\"Name\":\"Brand\",\"Value\":[\"Apple\"]}]},\"photos\":[\"https://i.ebayimg.com/00/s/MTUyOVgxNjAw/z/DFYAAOSwA3dYGMt9/$_57.JPG?set_id=880000500F\",\"https://bsl.cwa.sellercloud.com/images/products/275997.jpg\",\"https://i.ebayimg.com/images/g/7AUAAOSwhztbKsRV/s-l1600.jpg\",\"https://d3d71ba2asa5oz.cloudfront.net/12005722/images/iph6sunl-d_838__2.jpg\",\"https://bsl.cwa.sellercloud.com/images/products/274152.jpg\",\"https://d3d71ba2asa5oz.cloudfront.net/12015576/images/apple_iphone6plus_gray_verizon_grade3_good_right.jpg\",\"https://i.ebayimg.com/images/g/-ekAAOSwYPNcL9T~/s-l1600.jpg\",\"https://d3d71ba2asa5oz.cloudfront.net/12015576/images/apple_iphone6plus_gray_verizon_grade3_good_left.jpg\"],\"seller\":{\"feedbackScore\":1645,\"popularity\":98.2,\"feedbackRating\":\"Red\",\"score\":false,\"topRated\":true,\"storeName\":\"cheapgoods\",\"buyProduct\":\"https://stores.ebay.com/id=1074461351\"},\"similar\":[{\"productName\":\"NEW Apple iPhone 6 16GB 32GB 64GB 128GB UNLOCKED Gold Silver Space Gray GSM NB\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/ma1XPxROqVAAVdIxpalrntw.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/NEW-Apple-iPhone-6-16GB-32GB-64GB-128GB-UNLOCKED-Gold-Silver-Space-Gray-GSM-NB/254193298027?_trkparms=%26itm%3D254193298027&_trksid=p0\",\"price\":169.95,\"shippingCost\":\"0.00\",\"dayLeft\":22},{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey I\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/maoPmjQtpvPRytCcy82kKaw.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/NEW-Apple-iPhone-6-16GB-64GB-128GB-GSM-CDMA-Factory-Unlocked-Gold-Silver-Grey-I/183756428434?_trkparms=%26itm%3D183756428434&_trksid=p0\",\"price\":169.99,\"shippingCost\":\"0.00\",\"dayLeft\":15},{\"productName\":\"Apple iPhone 6 16GB 64GB 128GB Factory Unlocked AT&T Verizon T-Mobile Sprint\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mF7q7VHNLtxwmDtfxGNDgdA.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-16GB-64GB-128GB-Factory-Unlocked-AT-T-Verizon-T-Mobile-Sprint/192311861863?_trkparms=mehot%3Dpp%26%26itm%3D192311861863&_trksid=p0\",\"price\":99.99,\"shippingCost\":\"0.00\",\"dayLeft\":27},{\"productName\":\"Apple iPhone 6 - (UNLOCKED) | Gray Gold Silver | 16GB 64GB 128GB GSM + CDMA\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/maMaIDrnmGywvETHUj6s4QQ.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-UNLOCKED-Gray-Gold-Silver-16GB-64GB-128GB-GSM-CDMA/163365632858?_trkparms=%26itm%3D163365632858&_trksid=p0\",\"price\":189.99,\"shippingCost\":\"0.00\",\"dayLeft\":23},{\"productName\":\"Brand New Apple iPhone 6S 16GB 64GB 128GB Unlocked GSM/CDMA AT&T Verizon \",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mvuHsyw41SyEE0t-8VsbV6A.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Brand-New-Apple-iPhone-6S-16GB-64GB-128GB-Unlocked-GSM-CDMA-AT-T-Verizon/254193340171?_trkparms=mehot%3Dpp%26%26itm%3D254193340171&_trksid=p0\",\"price\":199.95,\"shippingCost\":\"0.00\",\"dayLeft\":22},{\"productName\":\"Unlocked Apple iPhone 6, 6 Plus Sealed in Box GSM 16GB 64GB 128GB\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mtPyG6aRqf6oqw8hR3Mzk0A.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Unlocked-Apple-iPhone-6-6-Plus-Sealed-in-Box-GSM-16GB-64GB-128GB/143173467796?_trkparms=%26itm%3D143173467796&_trksid=p0\",\"price\":169.95,\"shippingCost\":\"0.00\",\"dayLeft\":29},{\"productName\":\"Apple iPhone 6 64GB - Unlocked Excellent\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mPBmNMEF0qHIRx9Tq1kvEHw.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-64GB-Unlocked-Excellent/264244816123?_trkparms=mehot%3Dpp%26%26itm%3D264244816123&_trksid=p0\",\"price\":134.99,\"shippingCost\":\"0.00\",\"dayLeft\":1},{\"productName\":\"Apple iPhone 6s - 16GB 32GB 64GB 128GB - Unlocked AT&T Verizon T-Mobile Sprint\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/m0L_WXzlRPW0S-W7vLjqvHw.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6s-16GB-32GB-64GB-128GB-Unlocked-AT-T-Verizon-T-Mobile-Sprint/382716323249?_trkparms=mehot%3Dpp%26%26itm%3D382716323249&_trksid=p0\",\"price\":121.99,\"shippingCost\":\"0.00\",\"dayLeft\":22},{\"productName\":\"iPhone 6 - (UNLOCKED ATT MetroPCS T-Mobile) | Gray Gold Silver | 16GB 64GB 128GB\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/muUyuKowRme3boOXlrPcA-w.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/iPhone-6-UNLOCKED-ATT-MetroPCS-T-Mobile-Gray-Gold-Silver-16GB-64GB-128GB/192519974151?_trkparms=mehot%3Dpp%26%26itm%3D192519974151&_trksid=p0\",\"price\":119.95,\"shippingCost\":\"0.00\",\"dayLeft\":3},{\"productName\":\"Apple iPhone 6 16GB Gold GSM Factory Unlocked Smartphone\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mEnXz1dCkCxuRwxl5_8CtnA.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-16GB-Gold-GSM-Factory-Unlocked-Smartphone/254182939090?_trkparms=%26itm%3D254182939090&_trksid=p0\",\"price\":174,\"shippingCost\":\"5.00\",\"dayLeft\":14},{\"productName\":\"Apple iPhone 6 Smartphone 16GB 64GB 128GB | Verizon Unlocked ATT TMobile Sprint\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mtk-gk5KiT-obgVzILrGyjQ.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-Smartphone-16GB-64GB-128GB-Verizon-Unlocked-ATT-TMobile-Sprint/192734735517?_trkparms=mehot%3Dpp%26%26itm%3D192734735517&_trksid=p0\",\"price\":106.23,\"shippingCost\":\"0.00\",\"dayLeft\":6},{\"productName\":\"Apple iPhone 6 Factory Unlocked Verizon T-Mobile AT&T 128GB | 64GB | 32GB |16GB\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mAEk7EHEvozggwZ4SBvA58w.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-Factory-Unlocked-Verizon-T-Mobile-AT-T-128GB-64GB-32GB-16GB/292369952629?_trkparms=mehot%3Dag%26%26itm%3D292369952629&_trksid=p0\",\"price\":139.88,\"shippingCost\":\"0.00\",\"dayLeft\":22},{\"productName\":\"New Apple iPhone 6 16GB 32GB 64GB 128GB Factory Unlocked T-Mobile AT&T Verizon \",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mQiH4kioYKBixcTgJ2u5MXg.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/New-Apple-iPhone-6-16GB-32GB-64GB-128GB-Factory-Unlocked-T-Mobile-AT-T-Verizon/192580600148?_trkparms=mehot%3Dpp%26%26itm%3D192580600148&_trksid=p0\",\"price\":203.99,\"shippingCost\":\"0.00\",\"dayLeft\":6},{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey I\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/maoPmjQtpvPRytCcy82kKaw.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/NEW-Apple-iPhone-6-16GB-64GB-128GB-GSM-CDMA-Factory-Unlocked-Gold-Silver-Grey-I/183756428434?var=691354990644&_trkparms=%26itm%3D691354990644&_trksid=p0\",\"price\":209.99,\"shippingCost\":\"0.00\",\"dayLeft\":15},{\"productName\":\"NEW IPHONE 6-16G-GSM/CDMA Factory Unlocked  ATT Tmobil Verizon GOLD-SILVER-GRAY \",\"url\":\"https://securethumbs.ebay.com/d/l140/m/m6unZqyKDWbIHEnTtJ3Az8w.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/NEW-IPHONE-6-16G-GSM-CDMA-Factory-Unlocked-ATT-Tmobil-Verizon-GOLD-SILVER-GRAY/302243181640?_trkparms=mehot%3Dag%26%26itm%3D302243181640&_trksid=p0\",\"price\":189,\"shippingCost\":\"0.00\",\"dayLeft\":8},{\"productName\":\" Apple iPhone 6 Plus-16GB 64GB 128GB GSM -Factory Unlocked- Gold Gray Silver\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mo6ASqde1trdFWnlfeyKwtg.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-Plus-16GB-64GB-128GB-GSM-Factory-Unlocked-Gold-Gray-Silver/223319258155?var=522108410453&_trkparms=%26itm%3D522108410453&_trksid=p0\",\"price\":183.93,\"shippingCost\":\"0.00\",\"dayLeft\":24},{\"productName\":\" Apple iPhone 6 Plus-16GB 64GB 128GB GSM -Factory Unlocked- Gold Gray Silver\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mo6ASqde1trdFWnlfeyKwtg.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-Plus-16GB-64GB-128GB-GSM-Factory-Unlocked-Gold-Gray-Silver/223319258155?var=522108410452&_trkparms=%26itm%3D522108410452&_trksid=p0\",\"price\":183.93,\"shippingCost\":\"0.00\",\"dayLeft\":24},{\"productName\":\" Apple iPhone 6 Plus-16GB 64GB 128GB GSM -Factory Unlocked- Gold Gray Silver\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mo6ASqde1trdFWnlfeyKwtg.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-Plus-16GB-64GB-128GB-GSM-Factory-Unlocked-Gold-Gray-Silver/223319258155?var=522108410451&_trkparms=%26itm%3D522108410451&_trksid=p0\",\"price\":183.93,\"shippingCost\":\"0.00\",\"dayLeft\":24},{\"productName\":\" Apple iPhone 6 Plus-16GB 64GB 128GB GSM -Factory Unlocked- Gold Gray Silver\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mo6ASqde1trdFWnlfeyKwtg.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6-Plus-16GB-64GB-128GB-GSM-Factory-Unlocked-Gold-Gray-Silver/223319258155?_trkparms=%26itm%3D223319258155&_trksid=p0\",\"price\":183.93,\"shippingCost\":\"0.00\",\"dayLeft\":24},{\"productName\":\"Apple iPhone 6S ✤16GB 64GB 128GB✤ AT&T T-Mobile Sprint Verizon Unlocked GSM CDMA\",\"url\":\"https://securethumbs.ebay.com/d/l140/m/mIiReV1jrWHXyqi-C0kYweA.jpg\",\"viewItemURL\":\"https://www.ebay.com/itm/Apple-iPhone-6S-16GB-64GB-128GB-AT-T-T-Mobile-Sprint-Verizon-Unlocked-GSM-CDMA/183383599015?_trkparms=mehot%3Dpp%26%26itm%3D183383599015&_trksid=p0\",\"price\":125,\"shippingCost\":\"0.00\",\"dayLeft\":28}],\"fbInfo\":{\"productName\":\"NEW Apple iPhone 6 16GB 64GB 128GB GSM/ CDMA Factory Unlocked Gold Silver Grey  \",\"price\":189.99,\"link\":\"https://www.ebay.com/itm/NEW-Apple-iPhone-6-16GB-64GB-128GB-GSM-CDMA-Factory-Unlocked-Gold-Silver-Grey-/282389702634?var=\"}}");
            //context.startActivity(intent);
            }
        });

        holder.itemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store current jsonobejct into wishlist class;
                ImageView cart=(ImageView) v.findViewById(R.id.item_shopping_cart);
                int drawableId = (Integer)cart.getTag();
                if(drawableId==R.drawable.add_shopping){
                    WishList curList=((WishList)context.getApplicationContext());
                    curList.addItem(curObj);

                    cart.setImageResource(R.drawable.remove_shopping);
                    cart.setTag(R.drawable.remove_shopping);

                    String short_title="";
                    String long_title=holder.itemTitle.getText().toString();
                    if(long_title.length()>40){
                        short_title=long_title.substring(0,40)+"...";
                    }else{
                        short_title=long_title;
                    }

                    Toast toast = Toast.makeText(context, short_title+" was added to wishlist", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    int backgroundColor = ResourcesCompat.getColor(toast.getView().getResources(), R.color.colorToast, null);
                    toastView.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    TextView text = (TextView) toastView.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#000000"));
                    toast.show();
                }else{
                    WishList curList=((WishList)context.getApplicationContext());
                    curList.removeItem(curObj);

                    cart.setImageResource(R.drawable.add_shopping);
                    cart.setTag(R.drawable.add_shopping);
                    //Toast.makeText(v.getContext(),  holder.itemTitle.getText().toString()+" was removed to wishlist", Toast.LENGTH_SHORT).show();
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
                }
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData==null?0:mData.length();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        int id;
        TextView itemTitle;
        TextView itemZipcode;
        TextView itemShipping;
        TextView itemPrice;
        TextView itemCondition;
        ImageView itemImage;
        ImageView itemCart;
        ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemZipcode=itemView.findViewById(R.id.item_zipcode);
            itemShipping = itemView.findViewById(R.id.item_shipping);
            itemPrice=itemView.findViewById(R.id.item_price);
            itemCondition=itemView.findViewById(R.id.item_condition);
            itemImage=(ImageView)itemView.findViewById(R.id.item_image);
            itemCart=(ImageView)itemView.findViewById(R.id.item_shopping_cart);
        }

    }

    // convenience method for getting data at click position
    Object getItem(int id) {

        try{
            return mData.get(id);
        }catch (JSONException e) {
            Log.i("TAG", "message error");
            return null;
        }
    }
}
