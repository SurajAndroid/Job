package com.example.dell.job;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import adapter.CandidateSearchAdapter;
import adapter.EmployeeSearchAdapter;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by chauhan on 5/13/2017.
 */

public class SearchActivity extends SlidingFragmentActivity implements RequestReceiver {

    ListView searchListView;
    EditText skillesediTxt, locationEdit;
    LinearLayout searchLayout, slidMenuLayout ;
    EmployeeSearchAdapter adapter;
    SlidingMenu sm;
    RelativeLayout layout;
    RelativeLayout filterLayout, parentLayout;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences;
    RequestReceiver receiver;
    EmployeeSearchAdapter employeeSearchAdapter;
    CandidateSearchAdapter candidateSearchAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

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

    public void init(){

        try{
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }catch (Exception e){
            e.printStackTrace();
        }

        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        receiver = this;
        layout = (RelativeLayout)findViewById(R.id.layout);


        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        slidMenuLayout = (LinearLayout) findViewById(R.id.slidMenuLayout);
        skillesediTxt = (EditText) findViewById(R.id.skillesediTxt);
        locationEdit = (EditText) findViewById(R.id.locationEdit);
        searchListView = (ListView) findViewById(R.id.searchListView);

        filterLayout = (RelativeLayout)findViewById(R.id.filterLayout);
        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);


        Constant.EMAIL = sharedPreferences.getString("email","");
        if(sharedPreferences.getString("user_type","").equals("candidate")){
            getEmployeeTopTenSerivice();
        }else {
            getCandidatetopTenSerivice();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Snackbar.make(parentLayout,"Please click BACK again to exit.!",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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

    public void clickListener(){

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,FilterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.toggle();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if(skillesediTxt.getText().length()!=0){
                    if(locationEdit.getText().length()!=0){

                        Constant.SKILLES = skillesediTxt.getText().toString();
                        Constant.LOCATION = locationEdit.getText().toString();

                        if(sharedPreferences.getString("user_type","").equals("candidate")){
                            Toast.makeText(getApplicationContext(),"Company Searching Comming Soon.!",Toast.LENGTH_LONG).show();
                        }else {
                            searchApiSerivice();
                        }

                    }else {
                        Snackbar.make(parentLayout,"Please enter location.!",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentLayout,"Please enter skilles",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getCandidatetopTenSerivice() {
        WebserviceHelper candidateTop = new WebserviceHelper(receiver, SearchActivity.this);
        candidateTop.setAction(Constant.CADIDATE_TOP_TEN);
        candidateTop.execute();
    }

    public void getEmployeeTopTenSerivice() {
        WebserviceHelper employeeTop = new WebserviceHelper(receiver, SearchActivity.this);
        employeeTop.setAction(Constant.EMPLOYEETOP_TEN);
        employeeTop.execute();
    }

    public void searchApiSerivice() {
        WebserviceHelper searchAPI = new WebserviceHelper(receiver, SearchActivity.this);
        searchAPI.setAction(Constant.SEARCH_API);
        searchAPI.execute();
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
            if(result[0].equals("01")){
                if(sharedPreferences.getString("user_type","").equals("candidate")){
                    employeeSearchAdapter = new EmployeeSearchAdapter(SearchActivity.this,SearchActivity.this, Global.companylist);
                    searchListView.setAdapter(employeeSearchAdapter);
                }else {
                    candidateSearchAdapter = new CandidateSearchAdapter(SearchActivity.this,SearchActivity.this, Global.candidatelist);
                    searchListView.setAdapter(candidateSearchAdapter);
                }
            }else if(result[0].equals("0001")){
                        Intent  intent = new Intent(SearchActivity.this, AllListActivity.class);
                        startActivity(intent);
            }else {
                Snackbar.make(parentLayout,""+result[1],Snackbar.LENGTH_SHORT).show();
            }
    }
}
