package com.hst.ps2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hst.ps2.list.ListPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.hst.ps2.MainActivity.KEYWORD;
import static com.hst.ps2.MainActivity.SEARCH_URL;

public class SearchTab extends Fragment {
    private static final String[] ZIPCODE=new String[]{"90007","91007","90016"};
    private static String currentLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.search_tab, container, false);
//        getResources().getStringArray(R.array.list_of_zipcode);

        //add nearby search listener
        CheckBox nearbyBtn = (CheckBox)view.findViewById(R.id.nearby_enable);

        nearbyBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                   if(isChecked){
                       LinearLayout mlayout=(LinearLayout)view.findViewById(R.id.nearby_layout);
                       mlayout.setVisibility(LinearLayout.VISIBLE);
                       getLocation();
                   }else{
                       LinearLayout mlayout=(LinearLayout)view.findViewById(R.id.nearby_layout);
                       mlayout.setVisibility(LinearLayout.GONE);
                   }
               }
           }
        );

        //radio group listener
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.m_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if(checkedId==R.id.radio_current){
                    AutoCompleteTextView myauto=view.findViewById(R.id.zipcode);
                    myauto.setFocusable(false);
                    myauto.setEnabled(false);
                    myauto.setText("");
                }else{
                    AutoCompleteTextView myauto=view.findViewById(R.id.zipcode);
                    myauto.setFocusableInTouchMode(true);
                    myauto.setEnabled(true);
                }
            }
        });

        //set autocomplete
        final AutoCompleteTextView zipcodeView = (AutoCompleteTextView) view.findViewById(R.id.zipcode);

        zipcodeView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String str=zipcodeView.getText().toString();
//                Log.i("zipcode", "onFocusChange: "+str);
                if(str==null||str=="")return;
                
                String url ="http://hst571hw8.us-east-2.elasticbeanstalk.com/auto/"+str;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try{
                                    JSONArray zipArr=new JSONArray(response);
                                    String[] zipcode=new String[zipArr.length()];
                                    for(int i=0;i<zipArr.length();i++){
                                        zipcode[i]=zipArr.getString(i);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                            android.R.layout.simple_dropdown_item_1line, zipcode);
                                    zipcodeView.setAdapter(adapter);
                                    zipcodeView.showDropDown();
                                }catch(JSONException e){

                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //set search button
        Button searchBtn=((Button)view.findViewById(R.id.search_button));
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch(view);
            }
        });

        Button clearBtn=((Button)view.findViewById(R.id.clear_button));
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearch(view);
            }
        });
        return view;
    }
    /** Called when the user taps the Send button */
    public void startSearch(View view) {
        final Intent intent = new Intent(view.getContext(), ListPage.class);

        EditText keywordText = (EditText) view.findViewById(R.id.keyword);
        String keyword = keywordText.getText().toString();
        if(keyword.equalsIgnoreCase(""))
        {
            TextView keyword_error = (TextView) view.findViewById(R.id.keyword_error);
            keyword_error.setVisibility(TextView.VISIBLE);

            Toast toast = Toast.makeText(view.getContext(), "Please fix all fields with errors", Toast.LENGTH_LONG);
            View toastView = toast.getView();
            int backgroundColor = ResourcesCompat.getColor(toast.getView().getResources(), R.color.colorToast, null);
            toastView.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
            TextView text = (TextView) toastView.findViewById(android.R.id.message);
            text.setTextColor(Color.parseColor("#000000"));
            toast.show();

            return;
        }else{
            TextView keyword_error = (TextView) view.findViewById(R.id.keyword_error);
            keyword_error.setVisibility(TextView.GONE);
        }

        //category
        int categoryId=((Spinner)view.findViewById(R.id.spinner_category)).getSelectedItemPosition();
        String category="0";

        switch(categoryId){
            case 0:
                category="0";
                break;
            case 1:
                category="550";
                break;
            case 2:
                category="2984";
                break;
            case 3:
                category="267";
                break;
            case 4:
                category="11450";
                break;
            case 5:
                category="58058";
                break;
            case 6:
                category="26395";
                break;
            case 7:
                category="11233";
                break;
            case 8:
                category = "1249";
                break;
            default:
                category="0";
        }

        boolean New = ((CheckBox) view.findViewById(R.id.New)).isChecked();
        boolean used = ((CheckBox) view.findViewById(R.id.used)).isChecked();
        boolean unspec = ((CheckBox) view.findViewById(R.id.unspec)).isChecked();
        boolean pickup = ((CheckBox) view.findViewById(R.id.pickup)).isChecked();
        boolean free = ((CheckBox) view.findViewById(R.id.free)).isChecked();

        boolean nearby_enable=((CheckBox) view.findViewById(R.id.nearby_enable)).isChecked();

        String distance=((TextView)view.findViewById(R.id.distance)).getText().toString();


        if(distance==null||distance.length()==0){
            distance="10";
        }
//        Log.i("distance", "startSearch: "+distance);

        boolean radio_current=((RadioButton)view.findViewById(R.id.radio_current)).isChecked();
        String zipcode;

        if(radio_current){
            zipcode=currentLocation;
        }else{
            zipcode=((TextView)view.findViewById(R.id.zipcode)).getText().toString();
        }

        //zip code error handle
        if(!radio_current){
            if(!validZipCode(zipcode)){
                TextView zipcode_error = (TextView) view.findViewById(R.id.zipcode_error);
                zipcode_error.setVisibility(TextView.VISIBLE);

                Toast toast = Toast.makeText(view.getContext(), "Please fix all fields with errors", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                int backgroundColor = ResourcesCompat.getColor(toast.getView().getResources(), R.color.colorToast, null);
                toastView.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                TextView text = (TextView) toastView.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#000000"));
                toast.show();
                return;
            }else{
                TextView zipcode_error = (TextView) view.findViewById(R.id.zipcode_error);
                zipcode_error.setVisibility(TextView.GONE);
            }
        }

        String url="";
        if(nearby_enable){  //url has zip code
            url ="http://hsthw9.us-east-2.elasticbeanstalk.com/productSearch?keyword="+keyword+"&category="+category+"&zipcode="+zipcode+"&maxDistance="+distance+"&New="+New+"&unspec="+unspec+"&used="+used+"&free="+free+"&pickup="+pickup;
        }else{             //url don't has zipcode
            url ="http://hsthw9.us-east-2.elasticbeanstalk.com/productSearch?keyword="+keyword+"&category="+category+"&zipcode=0&maxDistance=10&New="+New+"&unspec="+unspec+"&used="+used+"&free="+free+"&pickup="+pickup;
        }
        Log.i("Search", "url: "+url);

        intent.putExtra("name","main");
        intent.putExtra(SEARCH_URL,url);
        intent.putExtra(KEYWORD,keyword);
        startActivity(intent);


        //development mode
//        intent.putExtra(ITEMS_INFO,"[{\"index\":1,\"image\":\"http://thumbs3.ebaystatic.com/pict/4014668980784040_1.jpg\",\"title\":\"Apple iPhone 7 \\\"Factory Unlocked\\\" 32GB 4G LTE iOS WiFi Smartphone\",\"id\":\"401466898078\",\"price\":\"207.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":2,\"image\":\"http://thumbs2.ebaystatic.com/pict/3817975492494040_2.jpg\",\"title\":\"Apple iPhone 6 16GB \\\"Factory Unlocked\\\" 4G LTE 8MP Camera iOS WiFi Smartphone\",\"id\":\"381797549249\",\"price\":\"104.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":3,\"image\":\"http://thumbs2.ebaystatic.com/pict/2642317372054040_2.jpg\",\"title\":\"Apple iPhone 7 32GB Unlocked Excellent\",\"id\":\"264231737205\",\"price\":\"209.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":4,\"image\":\"http://thumbs2.ebaystatic.com/pict/4014815156054040_1.jpg\",\"title\":\"Apple iPhone 7 Plus 128GB Factory Unlocked 4G LTE iOS WiFi Smartphone\",\"id\":\"401481515605\",\"price\":\"339.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":5,\"image\":\"http://thumbs4.ebaystatic.com/pict/2642346276314040_1.jpg\",\"title\":\"Apple iPhone 7 Plus 32GB Unlocked Excellent\",\"id\":\"264234627631\",\"price\":\"309.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":6,\"image\":\"http://thumbs2.ebaystatic.com/m/mBxjdAJhgwFYBQeHdgda-gw/140.jpg\",\"title\":\"Apple iPhone SE 32GB Space Gray Straight Talk / Straight Talk  - NEW\",\"id\":\"223483586189\",\"price\":\"114.99\",\"shipping\":\"Free Shipping\",\"zip\":\"62221\",\"seller\":\"aklsales618\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"0\"],\"returnsAccepted\":[\"false\"]}},{\"index\":7,\"image\":\"http://thumbs2.ebaystatic.com/m/mNxK3WthNhsOW1JSp0jpzDg/140.jpg\",\"title\":\"Apple iPhone 6 - 32GB - Space Gray (Straight Talk) A1549 (CDMA + GSM)\",\"id\":\"202654668493\",\"price\":\"79.0\",\"shipping\":\"8.0\",\"zip\":\"35650\",\"seller\":\"sshill\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"8.0\"}],\"shippingType\":[\"Flat\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":8,\"image\":\"http://thumbs3.ebaystatic.com/pict/3818850030944040_2.jpg\",\"title\":\"Apple iPhone 6S 16GB \\\"Factory Unlocked\\\" 4G LTE WiFi iOS 12MP Camera Smartphone\",\"id\":\"381885003094\",\"price\":\"129.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":9,\"image\":\"http://thumbs3.ebaystatic.com/m/mftyFu3TBCGbSccTsz62goA/140.jpg\",\"title\":\" Apple iPhone 7, 32GB, Silver, Fully Unlocked, Excellent Condition\",\"id\":\"264164142450\",\"price\":\"209.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":10,\"image\":\"http://thumbs3.ebaystatic.com/pict/3822825671904040_1.jpg\",\"title\":\"Apple iPhone 7 128GB \\\"Factory Unlocked\\\" 4G LTE iOS WiFi Smartphone\",\"id\":\"382282567190\",\"price\":\"224.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":11,\"image\":\"http://thumbs4.ebaystatic.com/pict/2324166095514040_7.jpg\",\"title\":\"Apple iPhone 7 Plus a1784 32GB GSM Unlocked -Very Good\",\"id\":\"232416609551\",\"price\":\"304.99\",\"shipping\":\"Free Shipping\",\"zip\":\"17406\",\"seller\":\"mywitoutlet\",\"wishlist\":false,\"condition\":\"Seller refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":12,\"image\":\"http://thumbs1.ebaystatic.com/m/mkVtursEZpX1yUnEh8ZagWg/140.jpg\",\"title\":\"***IPHONE 7 32GB BLACK FACTORY UNLOCKED! APPLE 32 GB GSM MATTE BRAND NEW!***\",\"id\":\"273189058712\",\"price\":\"349.77\",\"shipping\":\"Free Shipping\",\"zip\":\"96826\",\"seller\":\"ryans_games\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":13,\"image\":\"http://thumbs2.ebaystatic.com/m/mFfuzfTyt0RW3vWG0lzH2dw/140.jpg\",\"title\":\"Apple iPhone 6 Plus - 16GB - Space Gray (Unlocked) A1522 (CDMA + GSM)\",\"id\":\"223483654721\",\"price\":\"179.99\",\"shipping\":\"Free Shipping\",\"zip\":\"19020\",\"seller\":\"debgdesigners1\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"2\"],\"returnsAccepted\":[\"false\"]}},{\"index\":14,\"image\":\"http://thumbs1.ebaystatic.com/pict/4014747140884040_1.jpg\",\"title\":\"Apple iPhone 7 Plus 32GB Factory Unlocked 4G LTE iOS WiFi Smartphone\",\"id\":\"401474714088\",\"price\":\"299.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":15,\"image\":\"http://thumbs1.ebaystatic.com/pict/2642304692244040_1.jpg\",\"title\":\"Apple iPhone 6 Plus 16GB Unlocked Excellent\",\"id\":\"264230469224\",\"price\":\"164.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":16,\"image\":\"http://thumbs3.ebaystatic.com/m/mvnzRTxy_ZZ8CFqfsHVrYrA/140.jpg\",\"title\":\"Apple iPhone SE 32GB Space Gray GSM Unlocked AT&T T-Mobile New Sealed Grey\",\"id\":\"264225979450\",\"price\":\"210.95\",\"shipping\":\"Free Shipping\",\"zip\":\"33066\",\"seller\":\"us_electronics_seller\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"0\"],\"returnsAccepted\":[\"true\"]}},{\"index\":17,\"image\":\"http://thumbs1.ebaystatic.com/m/myjdpiL13R7xOpmvesMIELA/140.jpg\",\"title\":\"Apple iPhone 6 - 64GB - Rose Gold A1549 (Great Condition)\",\"id\":\"273808321820\",\"price\":\"109.99\",\"shipping\":\"Free Shipping\",\"zip\":\"10467\",\"seller\":\"artfly23\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"3\"],\"returnsAccepted\":[\"false\"]}},{\"index\":18,\"image\":\"http://thumbs1.ebaystatic.com/pict/2642346702924040_1.jpg\",\"title\":\"Apple iPhone 8 64GB Unlocked Excellent\",\"id\":\"264234670292\",\"price\":\"339.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":19,\"image\":\"http://thumbs4.ebaystatic.com/m/msJ7QBiYcpF3Z92KS0jnnzA/140.jpg\",\"title\":\"Apple iPhone 6s Plus - 64GB - Gold (Unlocked) \",\"id\":\"264285219755\",\"price\":\"249.99\",\"shipping\":\"Free Shipping\",\"zip\":\"32738\",\"seller\":\"123_mcvicar\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":20,\"image\":\"http://thumbs1.ebaystatic.com/pict/1621568572004040_5.jpg\",\"title\":\"NEW Apple iPhone 6S PLUS (A1634 Factory Unlocked) - All Colors & Capacity\",\"id\":\"162156857200\",\"price\":\"349.99\",\"shipping\":\"Free Shipping\",\"zip\":\"60453\",\"seller\":\"om-wireless\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"0\"],\"returnsAccepted\":[\"true\"]}},{\"index\":21,\"image\":\"http://thumbs4.ebaystatic.com/pict/2527648342674040_6.jpg\",\"title\":\"Apple iPhone 7 32GB GSM Unlocked Smartphone\",\"id\":\"252764834267\",\"price\":\"209.99\",\"shipping\":\"Free Shipping\",\"zip\":\"11103\",\"seller\":\"cellfeee\",\"wishlist\":false,\"condition\":\"Seller refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":22,\"image\":\"http://thumbs1.ebaystatic.com/pict/3822506531644040_1.jpg\",\"title\":\"Apple iPhone 6S Plus 64GB \\\"Factory Unlocked\\\" 4G LTE 12MP Camera iOS Smartphone\",\"id\":\"382250653164\",\"price\":\"202.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":23,\"image\":\"http://thumbs4.ebaystatic.com/m/mQ3ztiEfcSOTwZa6yTnfcVQ/140.jpg\",\"title\":\"Apple iPhone 6s Plus - 16GB - Rose Gold (AT&T)\",\"id\":\"303127228595\",\"price\":\"230.0\",\"shipping\":\"Free Shipping\",\"zip\":\"39601\",\"seller\":\"wimcdowel0\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"FreePickup\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"3\"],\"returnsAccepted\":[\"false\"]}},{\"index\":24,\"image\":\"http://thumbs4.ebaystatic.com/pict/2642347158274040_1.jpg\",\"title\":\"Apple iPhone X 64GB A1901 GSM Unlocked Excellent\",\"id\":\"264234715827\",\"price\":\"595.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":25,\"image\":\"http://thumbs1.ebaystatic.com/m/mOhH3Rz-WOLdfucb9yB4AfA/140.jpg\",\"title\":\"Apple iPhone 6 - 64GB - Gold (Unlocked) A1549 (CDMA + GSM)\",\"id\":\"113719564632\",\"price\":\"120.0\",\"shipping\":\"13.0\",\"zip\":\"95051\",\"seller\":\"goodwyn23dickson6\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"13.0\"}],\"shippingType\":[\"Flat\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"2\"],\"returnsAccepted\":[\"false\"]}},{\"index\":26,\"image\":\"http://thumbs2.ebaystatic.com/pict/1532327998414040_2.jpg\",\"title\":\"Apple iPhone XR 128GB - All Colors! GSM & CDMA Unlocked!! Brand New!\",\"id\":\"153232799841\",\"price\":\"865.0\",\"shipping\":\"Free Shipping\",\"zip\":\"03865\",\"seller\":\"alldayzip\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":27,\"image\":\"http://thumbs4.ebaystatic.com/pict/2529455738994040_10.jpg\",\"title\":\"Apple iPhone 7 Plus 128GB GSM Unlocked Smartphone\",\"id\":\"252945573899\",\"price\":\"349.99\",\"shipping\":\"Free Shipping\",\"zip\":\"11103\",\"seller\":\"cellfeee\",\"wishlist\":false,\"condition\":\"Seller refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":28,\"image\":\"http://thumbs3.ebaystatic.com/m/m8iOx8Lzb9bM7I2jM4COrKw/140.jpg\",\"title\":\"Apple iPhone 7 128GB Matte Black AT&T Great Condition\",\"id\":\"293050042678\",\"price\":\"220.0\",\"shipping\":\"Free Shipping\",\"zip\":\"11102\",\"seller\":\"antnewtown\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"3\"],\"returnsAccepted\":[\"false\"]}},{\"index\":29,\"image\":\"http://thumbs1.ebaystatic.com/pict/4017373239124040_1.jpg\",\"title\":\"iPhone 7, 32/128/256 GB, Unlocked (CDMA + GSM), All Colors\",\"id\":\"401737323912\",\"price\":\"169.0\",\"shipping\":\"N/A\",\"zip\":\"84663\",\"seller\":\"expressmobileco\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingType\":[\"Calculated\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"false\"]}},{\"index\":30,\"image\":\"http://thumbs4.ebaystatic.com/pict/1534481701754040_1.jpg\",\"title\":\"Apple iPhone 7 - 32GB, 128GB 256GB (Unlocked) Black, Gold, Silver - Choose Color\",\"id\":\"153448170175\",\"price\":\"195.0\",\"shipping\":\"Free Shipping\",\"zip\":\"40291\",\"seller\":\"wikiwoo\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":31,\"image\":\"http://thumbs4.ebaystatic.com/pict/2331308620634040_3.jpg\",\"title\":\"Apple iPhone 7 Plus 32 128 256GB Factory GSM Unlocked AT&T / T-Mobile Smartphone\",\"id\":\"233130862063\",\"price\":\"315.99\",\"shipping\":\"Free Shipping\",\"zip\":\"20879\",\"seller\":\"buyspry\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":32,\"image\":\"http://thumbs2.ebaystatic.com/pict/2642287830454040_1.jpg\",\"title\":\"Apple iPhone 6s 64GB Unlocked Excellent\",\"id\":\"264228783045\",\"price\":\"164.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":33,\"image\":\"http://thumbs2.ebaystatic.com/pict/1837616583534040_1.jpg\",\"title\":\"Apple iPhone 6s Plus - 16GB 32GB 64GB 128gb (Unlocked) - Choose Your Color\",\"id\":\"183761658353\",\"price\":\"219.0\",\"shipping\":\"Free Shipping\",\"zip\":\"40291\",\"seller\":\"wikiwoo\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":34,\"image\":\"http://thumbs3.ebaystatic.com/m/mVWVyMtpCs8FK6-PMkiF4ZA/140.jpg\",\"title\":\"Apple iPhone 6 - 16GB - Gold (Factory GSM Unlocked; AT&T / T-Mobile) Smartphone\",\"id\":\"112393276566\",\"price\":\"120.99\",\"shipping\":\"Free Shipping\",\"zip\":\"11213\",\"seller\":\"emb-phones\",\"wishlist\":false,\"condition\":\"Seller refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":35,\"image\":\"http://thumbs4.ebaystatic.com/pict/1627888882314040_1.jpg\",\"title\":\"Apple iPhone X 64GB -  Unlocked -USA Model -Apple Warranty -Brand New!\",\"id\":\"162788888231\",\"price\":\"845.0\",\"shipping\":\"Free Shipping\",\"zip\":\"03865\",\"seller\":\"alldayzip\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":36,\"image\":\"http://thumbs3.ebaystatic.com/pict/4011019272824040_3.jpg\",\"title\":\"Apple iPhone 6 64GB \\\"Factory Unlocked\\\" 4G LTE WiFi 8MP Camera Smartphone\",\"id\":\"401101927282\",\"price\":\"127.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":37,\"image\":\"http://thumbs1.ebaystatic.com/m/m003Vwmxm_ugQgkiFGsIKzg/140.jpg\",\"title\":\"Apple iPhone 6 - 16GB - Space Gray (GSM+CDMA) **C Grade**\",\"id\":\"143216702060\",\"price\":\"100.0\",\"shipping\":\"Free Shipping\",\"zip\":\"77040\",\"seller\":\"htxmobile\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"2\"],\"returnsAccepted\":[\"true\"]}},{\"index\":38,\"image\":\"http://thumbs1.ebaystatic.com/pict/2330440918284040_3.jpg\",\"title\":\"Apple iPhone 8 Plus a1897 64GB GSM Unlocked - Good\",\"id\":\"233044091828\",\"price\":\"399.99\",\"shipping\":\"Free Shipping\",\"zip\":\"17406\",\"seller\":\"mywitoutlet\",\"wishlist\":false,\"condition\":\"Seller refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":39,\"image\":\"http://thumbs1.ebaystatic.com/m/mqhYZ0bCL6T8QBg4brjIAVA/140.jpg\",\"title\":\"NEW Apple iPhone SE - 32GB - Silver (Walmart Family Mobile) A1662 (CDMA + GSM)\",\"id\":\"123703614132\",\"price\":\"109.95\",\"shipping\":\"Free Shipping\",\"zip\":\"97325\",\"seller\":\"andymc503\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":40,\"image\":\"http://thumbs4.ebaystatic.com/pict/2642347182834040_1.jpg\",\"title\":\"Apple iPhone X 256GB Unlocked Excellent\",\"id\":\"264234718283\",\"price\":\"645.99\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":41,\"image\":\"http://thumbs3.ebaystatic.com/m/m4lc_WVrOi_tbZo7PX4R7nA/140.jpg\",\"title\":\"Apple iPhone 6 - 32GB - Gold (Straight Talk) A1549 (CDMA + GSM)\",\"id\":\"173874323382\",\"price\":\"110.0\",\"shipping\":\"Free Shipping\",\"zip\":\"04401\",\"seller\":\"seew21\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"2\"],\"returnsAccepted\":[\"false\"]}},{\"index\":42,\"image\":\"http://thumbs1.ebaystatic.com/pict/2533019276884040_4.jpg\",\"title\":\"Apple iPhone 6S 32GB Unlocked GSM iOS Smartphone\",\"id\":\"253301927688\",\"price\":\"154.99\",\"shipping\":\"Free Shipping\",\"zip\":\"11103\",\"seller\":\"cellfeee\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":43,\"image\":\"http://thumbs1.ebaystatic.com/m/menaYzVQ5ERnZkHurResD9A/140.jpg\",\"title\":\"Apple iPhone 8 64GB Space Gray Verizon A1863 (CDMA + GSM)  Unlocked \",\"id\":\"283413685628\",\"price\":\"399.99\",\"shipping\":\"Free Shipping\",\"zip\":\"08830\",\"seller\":\"mywirelessgadgets\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"false\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":44,\"image\":\"http://thumbs3.ebaystatic.com/pict/3620036190984040_2.jpg\",\"title\":\"Apple iPhone 6S 64GB \\\"Factory Unlocked\\\" 4G LTE 12MP Camera iOS Smartphone\",\"id\":\"362003619098\",\"price\":\"144.95\",\"shipping\":\"Free Shipping\",\"zip\":\"07014\",\"seller\":\"bidallies\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":45,\"image\":\"http://thumbs2.ebaystatic.com/m/mxdx-u77z046TtI2em0zCLQ/140.jpg\",\"title\":\"Apple iPhone SE, 64GB, Space Gray, Fully Unlocked \",\"id\":\"264164137113\",\"price\":\"124.82\",\"shipping\":\"Free Shipping\",\"zip\":\"33179\",\"seller\":\"smarter.phone\",\"wishlist\":false,\"condition\":\"Manufacturer refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":46,\"image\":\"http://thumbs3.ebaystatic.com/pict/2830059625904040_1.jpg\",\"title\":\"Apple iPhone 6 16GB / 64GB / 128GB - Space Gray / Silver / Gold\",\"id\":\"283005962590\",\"price\":\"109.99\",\"shipping\":\"Free Shipping\",\"zip\":\"32771\",\"seller\":\"itsworthmore\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"0\"],\"returnsAccepted\":[\"true\"]}},{\"index\":47,\"image\":\"http://thumbs1.ebaystatic.com/pict/1531847599684040_3.jpg\",\"title\":\"Apple iPhone XS 64GB - All Colors - GSM & CDMA UNLOCKED\",\"id\":\"153184759968\",\"price\":\"1075.0\",\"shipping\":\"Free Shipping\",\"zip\":\"03865\",\"seller\":\"alldayzip\",\"wishlist\":false,\"condition\":\"New\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":48,\"image\":\"http://thumbs4.ebaystatic.com/pict/1517438947634040_6.jpg\",\"title\":\"Apple iPhone 5 16GB 32GB 64GB - Black/White - Unlocked T-Mobile Cricket MetroPCS\",\"id\":\"151743894763\",\"price\":\"79.95\",\"shipping\":\"Free Shipping\",\"zip\":\"32224\",\"seller\":\"mobiledistributions\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"false\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":49,\"image\":\"http://thumbs3.ebaystatic.com/pict/2521788364864040_8.jpg\",\"title\":\"Apple iPhone 6 Plus 16GB Unlocked GSM iOS Smartphone\",\"id\":\"252178836486\",\"price\":\"169.99\",\"shipping\":\"Free Shipping\",\"zip\":\"11103\",\"seller\":\"cellfeee\",\"wishlist\":false,\"condition\":\"Used\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}},{\"index\":50,\"image\":\"http://thumbs4.ebaystatic.com/pict/2529455718594040_6.jpg\",\"title\":\"Apple iPhone 7 Plus 32GB GSM Unlocked Smartphone\",\"id\":\"252945571859\",\"price\":\"299.99\",\"shipping\":\"Free Shipping\",\"zip\":\"11103\",\"seller\":\"cellfeee\",\"wishlist\":false,\"condition\":\"Seller refurbished\",\"shippingInfo\":{\"shippingServiceCost\":[{\"@currencyId\":\"USD\",\"__value__\":\"0.0\"}],\"shippingType\":[\"Free\"],\"shipToLocations\":[\"Worldwide\"],\"expeditedShipping\":[\"true\"],\"oneDayShippingAvailable\":[\"true\"],\"handlingTime\":[\"1\"],\"returnsAccepted\":[\"true\"]}}]");
//        //intent.putExtra(ITEMS_INFO,"No Records");
//        intent.putExtra("keyword",keyword);
//        startActivity(intent);
    }
    public boolean validZipCode(String zipcode){
        return zipcode.matches("[0-9]{5}");
    }
    public void getLocation(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://ip-api.com/json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    try{
                        JSONObject res=new JSONObject(response);
                        currentLocation=res.getString("zip");
                        //Toast.makeText(getContext(),  "Get current location"+currentLocation, Toast.LENGTH_SHORT).show();
                    }catch(JSONException e){
                        Toast.makeText(getContext(),  "Get current location fail", Toast.LENGTH_SHORT).show();
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void clearSearch(View view){


//        ((CheckBox) view.findViewById(R.id.New)).setChecked(false);
//        ((CheckBox) view.findViewById(R.id.used)).setChecked(false);
//        ((CheckBox) view.findViewById(R.id.unspec)).setChecked(false);
//        ((CheckBox) view.findViewById(R.id.pickup)).setChecked(false);
//        ((CheckBox) view.findViewById(R.id.free)).setChecked(false);
//
//        ((Spinner)view.findViewById(R.id.spinner_category)).setSelection(0);

        ((CheckBox) view.findViewById(R.id.nearby_enable)).setChecked(false);
        ((TextView)view.findViewById(R.id.distance)).setText("");
        ((RadioButton)view.findViewById(R.id.radio_current)).setChecked(true);
        ((TextView)view.findViewById(R.id.zipcode)).setText("");

        ((EditText) view.findViewById(R.id.keyword)).setText("");
        //clear error infomation
        TextView keyword_error = (TextView) view.findViewById(R.id.keyword_error);
        keyword_error.setVisibility(TextView.GONE);
        TextView zipcode_error = (TextView) view.findViewById(R.id.zipcode_error);
        zipcode_error.setVisibility(TextView.GONE);
    }
}