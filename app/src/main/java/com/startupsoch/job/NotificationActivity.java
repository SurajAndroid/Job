package com.startupsoch.job;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import adapter.NotifictionAdapter;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by chauhan on 5/14/2017.
 */

public class NotificationActivity extends SlidingFragmentActivity implements RequestReceiver{

    RequestReceiver receiver;
    ListView notificationList;
    NotifictionAdapter adapter;
    RelativeLayout parentLayout;
    public static  SlidingMenu sm;
    LinearLayout slidMenuLayout;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notofication);

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

            super.onBackPressed();
            Intent intent = new Intent(NotificationActivity.this, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

    }

    public static void closeMenu(){
        sm.toggle();
    }

    public void notificationService() {
        WebserviceHelper employer = new WebserviceHelper(receiver, NotificationActivity.this);
        employer.setAction(Constant.NOTIFICATION_API);
        employer.execute();
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
        try{
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }catch (Exception e){
            e.printStackTrace();
        }
        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);
        sharedPreferences = getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.USER_ID = sharedPreferences.getString("user_id","");
        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        notificationList = (ListView)findViewById(R.id.notificationList);

        if(sharedPreferences.getString("user_type", "").equals("candidate")){
            Constant.TYPE = "candidate";
            notificationService();
        }else {
            Constant.TYPE = "employer";
            notificationService();
        }

}

public void clickListener(){

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
            if(Global.notificationList.size()!=0){
                adapter = new NotifictionAdapter(NotificationActivity.this, Global.notificationList);
                notificationList.setAdapter(adapter);
            }else {
                Snackbar.make(parentLayout,"No notification found.!",Snackbar.LENGTH_SHORT).show();
            }

        }
}

}
