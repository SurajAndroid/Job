package com.startupsoch.jobpool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.startupsoch.jobpool.R;

import adapter.ExperienceAdapter;
import adapter.FilterAdapter;
import adapter.FilterGenderAdapter;
import adapter.GenderAdapter;
import dtos.MaleDTO;
import dtos.YearDTO;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by chauhan on 5/23/2017.
 */

public class FilterActivity extends SlidingFragmentActivity implements RequestReceiver {

    SharedPreferences sharedPreferences;
    RequestReceiver receiver;
    ListView fileterList;
    LinearLayout genderLayout;
    LinearLayout sortLayout, salaryLayout, locationLayout, roleLayout, educationLayout
            ,industryLayout, expLayout, linearLayout;
    SlidingMenu sm;
    LinearLayout slidMenuLayout;
    RadioButton maleRadio,femaleRadio;

    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

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

        receiver = this;
        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        maleRadio = (RadioButton)findViewById(R.id.maleRadio);
        femaleRadio = (RadioButton)findViewById(R.id.femaleRadio);

        genderLayout = (LinearLayout)findViewById(R.id.genderLayout);
        fileterList = (ListView)findViewById(R.id.fileterList);
        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        sortLayout = (LinearLayout)findViewById(R.id. sortLayout);
        salaryLayout = (LinearLayout)findViewById(R.id. salaryLayout);
        locationLayout = (LinearLayout)findViewById(R.id. locationLayout);
        roleLayout = (LinearLayout)findViewById(R.id. roleLayout);
        educationLayout = (LinearLayout)findViewById(R.id. educationLayout);
        industryLayout = (LinearLayout)findViewById(R.id. industryLayout);
        expLayout = (LinearLayout)findViewById(R.id. expLayout);
        linearLayout = (LinearLayout)findViewById(R.id. linearLayout);

        getFilterAPI();
    }



    public void getFilterAPI() {
        WebserviceHelper employer = new WebserviceHelper(receiver, FilterActivity.this);
        employer.setAction(Constant.GET_FILTER_API);
        employer.execute();
    }

    public void ApplyFilterAPI() {
        WebserviceHelper apply = new WebserviceHelper(receiver, FilterActivity.this);
        apply.setAction(Constant.APPLY_FILTER);
        apply.execute();
    }


    public void ApplyCustomerFilterAPI() {
        WebserviceHelper customerFilter = new WebserviceHelper(receiver, FilterActivity.this);
        customerFilter.setAction(Constant.APPLY_CUSTOMER_FILTER);
        customerFilter.execute();
    }

    public void clickListener(){


        maleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(maleRadio.isChecked()){
                    femaleRadio.setChecked(false);
                }
            }
        });

        femaleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(femaleRadio.isChecked()){
                    maleRadio.setChecked(false);
                }
            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString("user_type", "").equals("candidate")) {
                    ApplyCustomerFilterAPI();
                } else {
                    ApplyFilterAPI();
                }

            }
        });

        sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderLayout.setVisibility(View.GONE);
                fileterList.setVisibility(View.VISIBLE);
                sortLayout.setBackgroundResource(R.color.white);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                expLayout.setBackgroundResource(R.color.yellow);

            }
        });

        salaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                genderLayout.setVisibility(View.GONE);
                fileterList.setVisibility(View.VISIBLE);

                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.white);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                expLayout.setBackgroundResource(R.color.yellow);

            }
        });

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderLayout.setVisibility(View.GONE);
                fileterList.setVisibility(View.VISIBLE);
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.white);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                expLayout.setBackgroundResource(R.color.yellow);

                FilterAdapter filterAdapter = new FilterAdapter(FilterActivity.this, Global.cityList);
                fileterList.setAdapter(filterAdapter);
            }
        });

        roleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderLayout.setVisibility(View.GONE);
                fileterList.setVisibility(View.VISIBLE);
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.white);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                expLayout.setBackgroundResource(R.color.yellow);

                FilterAdapter filterAdapter = new FilterAdapter(FilterActivity.this, Global.roleList);
                fileterList.setAdapter(filterAdapter);
            }
        });

        educationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderLayout.setVisibility(View.GONE);
                fileterList.setVisibility(View.VISIBLE);
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.white);
                industryLayout.setBackgroundResource(R.color.yellow);
                expLayout.setBackgroundResource(R.color.yellow);

                FilterAdapter filterAdapter = new FilterAdapter(FilterActivity.this, Global.educationList);
                fileterList.setAdapter(filterAdapter);

            }
        });

        industryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.white);
                expLayout.setBackgroundResource(R.color.yellow);

                GenderAdapter filterAdapter = new GenderAdapter(FilterActivity.this);
                fileterList.setAdapter(filterAdapter);
            }
        });

        expLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderLayout.setVisibility(View.GONE);
                fileterList.setVisibility(View.VISIBLE);
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                expLayout.setBackgroundResource(R.color.white);

                ExperienceAdapter filterAdapter = new ExperienceAdapter(FilterActivity.this);
                fileterList.setAdapter(filterAdapter);

            }
        });

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.toggle();
            }
        });

    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if(result[0].equals("1")){

        Global.experienceList.clear();
        Global.genderList.clear();
        for(int i=0;i<11;i++){
            YearDTO yearDTO = new YearDTO();
            yearDTO.setYear(""+i);
            yearDTO.setFlage(false);
            Global.experienceList.add(yearDTO);
        }

        MaleDTO maleDTO = new MaleDTO();
        maleDTO.setGender("Male");
        maleDTO.setFlage(false);
        Global.genderList.add(maleDTO);
            maleDTO = new MaleDTO();
            maleDTO.setGender("Female");
            maleDTO.setFlage(false);
        Global.genderList.add(maleDTO);

        FilterAdapter filterAdapter = new FilterAdapter(FilterActivity.this, Global.cityList);
        fileterList.setAdapter(filterAdapter);

        }else if(result[0].equals("0001")){
            Intent intent = new Intent(FilterActivity.this,AllListActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(FilterActivity.this,AllListActivity.class);
            startActivity(intent);
        }
    }
}
