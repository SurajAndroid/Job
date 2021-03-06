package com.startupsoch.job;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


/**
 * Created by abhisheik on 24/2/17.
 */
public class SplashScreen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesLoginStatus;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_activity);



        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        sharedPreferencesLoginStatus = getSharedPreferences("activity_status",Context.MODE_PRIVATE);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(sharedPreferences.getString("status","").equals("1")){

                    if(!sharedPreferences.getString("user_type","").equals("candidate")){

                        if(sharedPreferencesLoginStatus.getString("first_detect","").equals("0")){
                            Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(SplashScreen.this, SearchActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(SplashScreen.this, SearchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

//                    Intent intent = new Intent(SplashScreen.this, SearchActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
