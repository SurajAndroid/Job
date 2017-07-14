package com.example.dell.job;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import adapter.CandidateSearchAdapter;
import adapter.EmployeeSearchAdapter;
import utils.Global;

/**
 * Created by chauhan on 5/14/2017.
 */

public class AllListActivity extends SlidingFragmentActivity {

    ListView searchListView;
    CandidateSearchAdapter adapter;
    SlidingMenu sm;
    LinearLayout slidMenuLayout;
    SharedPreferences sharedPreferences;
    RelativeLayout parentLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview_activity);

        init();
        clickListener();

        setBehindView();


        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setSlidingEnabled(false);

     }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    private void setBehindView() {
        setBehindContentView(R.layout.menu_slide);
        //transaction fragment to sliding menu
        transactionFragments(MenuFragment.newInstance(), R.id.menu_slide);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void transactionFragments(Fragment fragment, int viewResource) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(viewResource, fragment);
        ft.commit();
        toggle();
    }

    public  void init(){

        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);
        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        searchListView = (ListView)findViewById(R.id.searchListView);
        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        adapter = new CandidateSearchAdapter(AllListActivity.this,AllListActivity.this, Global.searchcandidatelist);
        searchListView.setAdapter(adapter);

    }

    public void clickListener(){

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getString("status","").equals("1")){
                    sm.toggle();
                }else {
                    Snackbar.make(parentLayout,"Please Login.!",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

}
