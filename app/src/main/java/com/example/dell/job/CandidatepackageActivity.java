package com.example.dell.job;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import utils.Constant;

public class CandidatepackageActivity extends AppCompatActivity {

    LinearLayout threeMonthPackage, sixMonthPackage, oneYearPackage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatepackage);

        init();
        clickListener();
    }

    public void init(){
        threeMonthPackage = (LinearLayout)findViewById(R.id.threeMonthPackage);
        sixMonthPackage = (LinearLayout)findViewById(R.id.sixMonthPackage);
        oneYearPackage = (LinearLayout)findViewById(R.id.oneYearPackage);
    }

    public void clickListener(){

        threeMonthPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.PAYMENTAMOUNT = ""+3.00;
                Intent intent = new Intent(CandidatepackageActivity.this, PayMentActivity.class);
                startActivity(intent);
            }
        });

        sixMonthPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.PAYMENTAMOUNT = ""+6.00;
                Intent intent = new Intent(CandidatepackageActivity.this, PayMentActivity.class);
                startActivity(intent);
            }
        });

        oneYearPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.PAYMENTAMOUNT = ""+10.00;
                Intent intent = new Intent(CandidatepackageActivity.this, PayMentActivity.class);
                startActivity(intent);
            }
        });
    }
}
