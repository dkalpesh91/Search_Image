package com.example.searchimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchImageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText searchET;
    ImageAdapter adapter;
    ProgressDialog dialog;

    ArrayList<Data> heroList = new ArrayList<>();
    ImageSearchViewModel model = null;
    private SwipeRefreshLayout swipeContainer;
    int pageNO = 1;
    String searchKey = "vanilla";
    boolean isrefresh = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_image);

        recyclerView = findViewById(R.id.recyclerview);
        searchET = findViewById(R.id.search_et);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / 210 + 0.5); // +0.5 for correct rounding to int.


        recyclerView.setLayoutManager(new GridLayoutManager(this, noOfColumns));
        adapter = new ImageAdapter(SearchImageActivity.this, heroList);
        recyclerView.setAdapter(adapter);

        model = ViewModelProviders.of(this).get(ImageSearchViewModel.class);
        model.getHeroes("" + pageNO, searchKey).observe(this, new Observer<ArrayList<Data>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Data> dataResponse) {
                if (isrefresh) {
                    heroList.addAll(dataResponse);
                } else {
                    heroList = dataResponse;
                }
                adapter.setImageList(heroList);
                adapter.notifyDataSetChanged();

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                swipeContainer.setRefreshing(false);
                isrefresh = false;
//                try {
//                    if(heroList.size() != 0)
//                    recyclerView.smoothScrollToPosition(heroList.size() - 1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });


        findViewById(R.id.search_icon_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                dialog = ProgressDialog.show(SearchImageActivity.this, "",
                        "Loading. Please wait...", true);


                if (null != model) {
                    pageNO = 1;
                    searchKey = searchET.getText().toString();
                    model.getHeroes("" + pageNO, searchKey);
                }
            }
        });

//        searchET.addTextChangedListener(
//                new TextWatcher() {
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    private Timer timer = new Timer();
//                    private final long DELAY = 250;
//
//                    @Override
//                    public void afterTextChanged(final Editable s) {
//                        timer.cancel();
//                        timer = new Timer();
//                        timer.schedule(
//                                new TimerTask() {
//                                    @Override
//                                    public void run() {
//                                        if (null != model) {
//                                            pageNO = 1;
//                                            searchKey = searchET.getText().toString();
//                                            model.getHeroes("" + pageNO, searchKey);
//                                        }
//                                    }
//                                },
//                                DELAY
//                        );
//                    }
//                }
//        );

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isrefresh = true;
                pageNO = pageNO + 1;
                model.getHeroes("" + pageNO, searchKey);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
