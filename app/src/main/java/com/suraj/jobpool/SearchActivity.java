package com.suraj.jobpool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;

import adapter.CandidateSearchAdapter;
import adapter.EmployeeSearchAdapter;
import adapter.SkillesAdapter;
import dtos.SkillesDTO;
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
    Spinner spinner;
    String TAG;
    AutoCompleteTextView lookingJob;
    String[] citys = {"Location", "Bangalore", "Bider","Delhi", "Kalaburagi","Hydrabad","Indore","Pune"};
    String [] skilles = {"Android","PHP","Xamarin",".Net","iOS","Ionic","Angular JS","Node JS","ASP .Net","MVC","React Native"};
    SkillesDTO skillesDTO;
    ArrayList<SkillesDTO> list = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        init();
        clickListener();


        for(int i=0;i<skilles.length;i++){
            skillesDTO = new SkillesDTO( skilles[i],false);
            list.add(skillesDTO);
        }

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

        TAG = SearchActivity.class.getSimpleName();
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
        lookingJob = (AutoCompleteTextView)findViewById(R.id.lookingJob);

        spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,skilles);
        lookingJob.setAdapter(adapter);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citys);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
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
        Snackbar.make(parentLayout,"Please click BACK again to exit.!", Snackbar.LENGTH_SHORT).show();
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


        lookingJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constant.SKILLES = adapterView.getItemAtPosition(i).toString();
            }
        });
        skillesediTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(SearchActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.skilles_dialog);
                dialog1.show();
                ListView myListView = (ListView)dialog1.findViewById(R.id.myListView);
                LinearLayout SubmiTLayout= (LinearLayout)dialog1.findViewById(R.id.SubmiTLayout);
                final EditText editText2 = (EditText)dialog1.findViewById(R.id.editText2);
                final SkillesAdapter skillesAdapter  = new SkillesAdapter(SearchActivity.this,list);
                myListView.setAdapter(skillesAdapter);
                Constant.setListViewHeightBasedOnChildren(myListView);
                editText2.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                        // TODO Auto-generated method stub
                        skillesAdapter.getFilter().filter(arg0);
                    }
                });
                SubmiTLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder builder = new StringBuilder();
                        dialog1.dismiss();
//                        Toast.makeText(getApplicationContext(),"Liat Size "+ Global.skilleslist.size(),Toast.LENGTH_SHORT).show();
                        for(int i=0;i<Global.skilleslist.size();i++){
                            builder.append(Global.skilleslist.get(i));
                            if(i!=Global.skilleslist.size()-1){
                                builder.append(", ");
                            }
                        }
                        skillesediTxt.setText(builder.toString());
                    }
                });
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.LOCATION = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

               /* InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
                if( Constant.SKILLES.length()!=0){
                    if(!Constant.LOCATION .equalsIgnoreCase("Location")){
                          if(sharedPreferences.getString("user_type","").equals("candidate")){
                          companySearchApiSerivice();
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

    public void companySearchApiSerivice() {
        WebserviceHelper Companysearch = new WebserviceHelper(receiver, SearchActivity.this);
        Companysearch.setAction(Constant.COMPANY_API);
        Companysearch.execute();
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
            if(result[0].equals("01")){
                if(sharedPreferences.getString("user_type","").equals("candidate")){
                    employeeSearchAdapter = new EmployeeSearchAdapter(SearchActivity.this,SearchActivity.this, Global.companylist);
                    searchListView.setAdapter(employeeSearchAdapter);
                }else {
                    candidateSearchAdapter = new CandidateSearchAdapter(SearchActivity.this,SearchActivity.this, Global.candidatelist,TAG);
                    searchListView.setAdapter(candidateSearchAdapter);
                }
            }else if(result[0].equals("0001")){
                        Intent intent = new Intent(SearchActivity.this, AllListActivity.class);
                        startActivity(intent);
            }else {
                Snackbar.make(parentLayout,""+result[1],Snackbar.LENGTH_SHORT).show();
            }
    }
}
