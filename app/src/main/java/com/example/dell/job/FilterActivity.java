package com.example.dell.job;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import adapter.FilterAdapter;
import adapter.NotifictionAdapter;
/**
 * Created by chauhan on 5/23/2017.
 */

public class FilterActivity extends SlidingFragmentActivity{

    ListView fileterList;
    LinearLayout sortLayout, salaryLayout, locationLayout, roleLayout, educationLayout
            ,industryLayout, departmentLayout, linearLayout;
    SlidingMenu sm;
    LinearLayout slidMenuLayout;
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

        fileterList = (ListView)findViewById(R.id.fileterList);
        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        sortLayout = (LinearLayout)findViewById(R.id. sortLayout);
        salaryLayout = (LinearLayout)findViewById(R.id. salaryLayout);
        locationLayout = (LinearLayout)findViewById(R.id. locationLayout);
        roleLayout = (LinearLayout)findViewById(R.id. roleLayout);
        educationLayout = (LinearLayout)findViewById(R.id. educationLayout);
        industryLayout = (LinearLayout)findViewById(R.id. industryLayout);
        departmentLayout = (LinearLayout)findViewById(R.id. departmentLayout);
        linearLayout = (LinearLayout)findViewById(R.id. linearLayout);

        FilterAdapter filterAdapter = new FilterAdapter(FilterActivity.this);
        fileterList.setAdapter(filterAdapter);
    }

    public void clickListener(){

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Coming Soon.!",Toast.LENGTH_SHORT).show();
            }
        });

        sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLayout.setBackgroundResource(R.color.white);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                departmentLayout.setBackgroundResource(R.color.yellow);
            }
        });

        salaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.white);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                departmentLayout.setBackgroundResource(R.color.yellow);
            }
        });

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.white);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                departmentLayout.setBackgroundResource(R.color.yellow);
            }
        });

        roleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.white);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                departmentLayout.setBackgroundResource(R.color.yellow);
            }
        });

        educationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.white);
                industryLayout.setBackgroundResource(R.color.yellow);
                departmentLayout.setBackgroundResource(R.color.yellow);
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
                departmentLayout.setBackgroundResource(R.color.yellow);
            }
        });

        departmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLayout.setBackgroundResource(R.color.yellow);
                salaryLayout.setBackgroundResource(R.color.yellow);
                locationLayout.setBackgroundResource(R.color.yellow);
                roleLayout.setBackgroundResource(R.color.yellow);
                educationLayout.setBackgroundResource(R.color.yellow);
                industryLayout.setBackgroundResource(R.color.yellow);
                departmentLayout.setBackgroundResource(R.color.white);
            }
        });

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.toggle();
            }
        });

    }

}
