package com.startupsoch.jobpool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.startupsoch.jobpool.R;

import adapter.CandidateSearchAdapter;
import adapter.EmployeeSearchAdapter;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;

/**
 * Created by chauhan on 5/14/2017.
 */

public class AllListActivity extends SlidingFragmentActivity implements RequestReceiver {

    RequestReceiver receiver;
    ListView searchListView;
    CandidateSearchAdapter adapter;
    EmployeeSearchAdapter employeeSearchAdapter;
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
        sm.setSlidingEnabled(true);

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

        receiver = this;
        String TAG = AllListActivity.class.getSimpleName();

        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);
        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        searchListView = (ListView)findViewById(R.id.searchListView);

        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("user_type","").equals("candidate")){
            employeeSearchAdapter = new EmployeeSearchAdapter(getApplicationContext(),AllListActivity.this, Global.companySearchlist,receiver);
            searchListView.setAdapter(employeeSearchAdapter);
            }else {
            adapter = new CandidateSearchAdapter(AllListActivity.this,AllListActivity.this, Global.searchcandidatelist, TAG);
            searchListView.setAdapter(adapter);
        }

    }

    public void clickListener(){

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getString("status","").equals("1")){
                    sm.toggle();
                }else {
                    Snackbar.make(parentLayout,"Please Login.!", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void requestFinished(String[] result) throws Exception {
            if(result[0].equals("0101")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("no_of_applicant", "" + Constant.NO_OF_APPLIED);
                editor.putString("out_of_apply", "" + Constant.OUT_OFF_APPLY);
                editor.commit();
                MenuFragment.SetInterestvalue();
                final Dialog dialog = new Dialog(AllListActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertpopup);
                TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
                massageTxtView.setText(result[1]);
                LinearLayout submitLayout = (LinearLayout) dialog.findViewById(R.id.submitLayout);
                submitLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }else {
                Snackbar.make(parentLayout,""+result[1], Snackbar.LENGTH_SHORT).show();
            }
    }
}
