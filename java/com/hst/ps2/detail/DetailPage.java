package com.hst.ps2.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.hst.ps2.MainActivity;
import com.hst.ps2.R;
import com.hst.ps2.WishList;
import com.hst.ps2.WishRecyclerViewAdapter;
import com.hst.ps2.WishTab;
import com.hst.ps2.list.ListPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import static com.hst.ps2.list.ListPage.CUR_ITEM;
import static com.hst.ps2.list.ListPage.DETAIL_URL;

import static com.hst.ps2.MainActivity.KEYWORD;

public class DetailPage extends AppCompatActivity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    JSONObject fbInfo;
    String list_result;
    String keyword;
    String previousName;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        //set facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //get intent
        Intent intent = getIntent();
        final String detail_url=intent.getStringExtra(DETAIL_URL);
        final String cur_str=intent.getStringExtra(CUR_ITEM);
        list_result=intent.getStringExtra("list_result");
        keyword=intent.getStringExtra(KEYWORD);
        previousName=intent.getStringExtra("name");
        String pos=intent.getStringExtra("position");
        Log.i("pos", ": "+pos);

        position=Integer.parseInt(pos);
        Log.i("posi", ": "+position);
        //send request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, detail_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        renderDetailPage(response,cur_str);
                        ((ProgressBar)findViewById(R.id.progress_bar2)).setVisibility(View.GONE);
                        ((TextView)findViewById(R.id.progress_bar_label2)).setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("search error", "onErrorResponse: "+error);
            }
        });
        queue.add(stringRequest);
    }//on create
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();
                if(previousName.equals("list")){
                    final Intent intent = new Intent(this, ListPage.class);
                    intent.putExtra("name","detail");
                    intent.putExtra("list_result", list_result);
                    intent.putExtra(KEYWORD,keyword);
                    startActivity(intent);
                }else{
                    WishRecyclerViewAdapter.wishData.remove(position);
                    WishRecyclerViewAdapter.updateSummary();
                    WishTab.adapter.notifyDataSetChanged();
                    onBackPressed();
                }
                return true;
            case R.id.facebook_share:
                String link="";
                String quote="";
                try {
                    link=fbInfo.getString("link");
                    quote="Buy "+fbInfo.getString("productName")+" at $"+fbInfo.getString("price")+" from link below";
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(link))
                            .setQuote(quote)
                            .setShareHashtag(new ShareHashtag.Builder().setHashtag("#CSCI571Spring2019Ebay").build())
                            .build();
                    shareDialog.show(content);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void renderDetailPage(String detail_str,String cur_str){
        JSONObject detail = null;
        JSONObject curItem=null;

        try {
            detail = new JSONObject(detail_str);
            curItem=new JSONObject(cur_str);
            fbInfo=detail.getJSONObject("fbInfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject curObj=curItem;

        //set toolbar title
        try {
            JSONObject product=detail.getJSONObject("product");
            this.getSupportActionBar().setTitle(product.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //set tablayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Product").setIcon(R.drawable.tab_product));
        tabLayout.addTab(tabLayout.newTab().setText("Shipping").setIcon(R.drawable.tab_shipping));
        tabLayout.addTab(tabLayout.newTab().setText("Photos").setIcon(R.drawable.tab_photos));
        tabLayout.addTab(tabLayout.newTab().setText("Similar").setIcon(R.drawable.tab_similar));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //set adapter for view page container
        final ViewPager viewPager = (ViewPager) findViewById(R.id.detail_container);
        final DetailPagerAdapter adapter = new DetailPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),detail,curItem);

        viewPager.setAdapter(adapter);

        //add tab select listener
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //init fab
        final FloatingActionButton fab = findViewById(R.id.fab);

        String title="";
        String idStr="";
        try{
            title= curObj.getString("title");
            idStr=curObj.getString("id");
        }catch (JSONException e){

        }
        WishList mlist=((WishList)getApplicationContext());
        if(mlist.hasItem(idStr)){
            fab.setImageResource(R.drawable.remove_shopping);
            fab.setColorFilter(Color.WHITE);
            fab.setTag(R.drawable.remove_shopping);

        }else{
            fab.setImageResource(R.drawable.add_shopping);
            fab.setColorFilter(Color.WHITE);
            fab.setTag(R.drawable.add_shopping);

        }

        final String title2=title;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int drawableId = (Integer)fab.getTag();

                if(drawableId==R.drawable.add_shopping){
                    WishList curList=((WishList)view.getContext().getApplicationContext());
                    curList.addItem(curObj);

                    fab.setImageResource(R.drawable.remove_shopping);
                    fab.setColorFilter(Color.WHITE);
                    fab.setTag(R.drawable.remove_shopping);

                    String short_title="";
                    String long_title=title2;
                    if(long_title.length()>40){
                        short_title=long_title.substring(0,40)+"...";
                    }else{
                        short_title=long_title;
                    }

                    Toast toast = Toast.makeText(view.getContext(), short_title+" was add to wishlist", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    int backgroundColor = ResourcesCompat.getColor(toast.getView().getResources(), R.color.colorToast, null);
                    toastView.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    TextView text = (TextView) toastView.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#000000"));
                    toast.show();

                    //Toast.makeText(view.getContext(),  title2+" was added from wishlist", Toast.LENGTH_SHORT).show();
                }else{
                    WishList curList=((WishList)view.getContext().getApplicationContext());
                    curList.removeItem(curObj);

                    fab.setImageResource(R.drawable.add_shopping);
                    fab.setColorFilter(Color.WHITE);
                    fab.setTag(R.drawable.add_shopping);

                    String short_title="";
                    String long_title=title2;
                    if(long_title.length()>40){
                        short_title=long_title.substring(0,40)+"...";
                    }else{
                        short_title=long_title;
                    }

                    Toast toast = Toast.makeText(view.getContext(), short_title+" was remove from wishlist", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    int backgroundColor = ResourcesCompat.getColor(toast.getView().getResources(), R.color.colorToast, null);
                    toastView.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
                    TextView text = (TextView) toastView.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#000000"));
                    toast.show();
                    //Toast.makeText(view.getContext(),  title2+" was removed to wishlist", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
