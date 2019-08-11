package com.hst.ps2.list;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hst.ps2.MainActivity;
import com.hst.ps2.R;
import com.hst.ps2.detail.DetailPage;

import org.json.JSONArray;
import org.json.JSONException;

//import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.hst.ps2.MainActivity.KEYWORD;

public class ListPage extends AppCompatActivity{

    public static final String DETAIL_URL = "DETAILURL";
    public static final String CUR_ITEM = "CUR";
    MyRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page);
        ((LinearLayout)findViewById(R.id.resultBar)).setVisibility(View.GONE);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        if(intent.getStringExtra("name").equals("detail")){
            JSONArray itemsArr=null;
            final String keyword=intent.getStringExtra(KEYWORD);
            try {
                itemsArr = new JSONArray(intent.getStringExtra("list_result"));

                //hide progress bar;
                ((ProgressBar)findViewById(R.id.progress_bar1)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.progress_bar_label1)).setVisibility(View.GONE);

                //set result bar
                ((TextView)findViewById(R.id.resultBar2)).setText(""+itemsArr.length());
                ((TextView)findViewById(R.id.resultBar4)).setText(intent.getStringExtra(KEYWORD));
                ((LinearLayout)findViewById(R.id.resultBar)).setVisibility(View.VISIBLE);

                //get view
                RecyclerView recyclerView = findViewById(R.id.rvNumbers);
                //set view's layout manager
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                //set view's adapter
                adapter = new MyRecyclerViewAdapter(this, itemsArr,keyword);
                recyclerView.setAdapter(adapter);
            }
            catch (JSONException e){
//            Toast.makeText(this, "No records", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.error_page);
            }

        }else{
            String search_url = intent.getStringExtra(MainActivity.SEARCH_URL);
            final String keyword=intent.getStringExtra(KEYWORD);
            //String items_info="";
            final Context context=this;
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONArray itemsArr=null;
                            try {
                                itemsArr = new JSONArray(response);

                                //hide progress bar;
                                ((ProgressBar)findViewById(R.id.progress_bar1)).setVisibility(View.GONE);
                                ((TextView)findViewById(R.id.progress_bar_label1)).setVisibility(View.GONE);

                                //set result bar
                                ((TextView)findViewById(R.id.resultBar2)).setText(""+itemsArr.length());
                                ((TextView)findViewById(R.id.resultBar4)).setText(keyword);
                                ((LinearLayout)findViewById(R.id.resultBar)).setVisibility(View.VISIBLE);

                                //get view
                                RecyclerView recyclerView = findViewById(R.id.rvNumbers);
                                //set view's layout manager
                                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                                //set view's adapter
                                adapter = new MyRecyclerViewAdapter(context, itemsArr,keyword);
                                recyclerView.setAdapter(adapter);
                            }
                            catch (JSONException e){
//            Toast.makeText(this, "No records", Toast.LENGTH_SHORT).show();
                                setContentView(R.layout.error_page);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Search", "error: "+error);
                    setContentView(R.layout.error_page);
                }
            });
            queue.add(stringRequest);
        }
    }
}
