package com.example.dell.job;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import utils.Constant;

/**
 * Created by chauhan on 5/28/2017.
 */

public class SelectPackageActivity extends Activity {

    LinearLayout ThertyCondidatelayout,TwentyCondidatelayout, TenCondidatelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employerpackage);

        init();
        clickListenr();
    }

    public void init(){
        ThertyCondidatelayout = (LinearLayout)findViewById(R.id.ThertyCondidatelayout);
        TwentyCondidatelayout = (LinearLayout)findViewById(R.id.TwentyCondidatelayout);
        TenCondidatelayout = (LinearLayout)findViewById(R.id.TenCondidatelayout);
    }

    public void clickListenr(){

        ThertyCondidatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.PAYMENTAMOUNT = ""+3.00;
                Intent intent = new Intent(SelectPackageActivity.this, PayMentActivity.class);
                startActivity(intent);
            }
        });

        TwentyCondidatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.PAYMENTAMOUNT = ""+2.00;
                Intent intent = new Intent(SelectPackageActivity.this, PayMentActivity.class);
                startActivity(intent);
            }
        });

        TenCondidatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.PAYMENTAMOUNT = ""+1.00;
                Intent intent = new Intent(SelectPackageActivity.this, PayMentActivity.class);
                startActivity(intent);
            }
        });
    }
}
