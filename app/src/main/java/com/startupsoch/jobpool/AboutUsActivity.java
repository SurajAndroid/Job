package com.startupsoch.jobpool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.startupsoch.jobpool.R;

/**
 * Created by chauhan on 5/27/2017.
 */

public class AboutUsActivity extends Activity {

    LinearLayout backLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_activity);

        init();
        clickListener();
    }

    public void init(){
        backLayout = (LinearLayout)findViewById(R.id.backLayout);
    }

    public void clickListener(){
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }
}
