package com.startupsoch.jobpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.startupsoch.jobpool.R;

import org.json.JSONException;

import java.math.BigDecimal;

import adapter.MembershipAdapter;
import listners.CustomButtonListener;
import paypal.PayPalConfig;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

import static com.startupsoch.jobpool.CandidatepackageActivity.PAYPAL_REQUEST_CODE;

/**
 * Created by chauhan on 5/28/2017.
 */

public class SelectPackageActivity extends Activity implements RequestReceiver, CustomButtonListener {

    ListView listViewMemberShip;
    RequestReceiver receiver;
    ImageView membershipBtnBack;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employerpackage);

        init();
        receiver = this;
        getALLMemberShipSerivice();

    }

    private void getALLMemberShipSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, SelectPackageActivity.this);
        employer.setAction(Constant.GET_PACK_LIST);
        employer.execute();
    }

    public void callSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, SelectPackageActivity.this);
        employer.setAction(Constant.SELECT_PACK);
        employer.execute();
    }

    public void init() {
        listViewMemberShip = (ListView) findViewById(R.id.listViewMemberShip);
        membershipBtnBack = (ImageView) findViewById(R.id.membershipBtnBack);

        membershipBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void requestFinished(String[] result) throws Exception {
        if (result[0].equals("001")) {
            Snackbar.make(listViewMemberShip, result[1], Snackbar.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("Sohel ", "working ..  .. ");
                    Intent intent = new Intent(SelectPackageActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            }, 1000);
        } else {
            if (result[0].equals("01")) {
                MembershipAdapter membershipAdapter = new MembershipAdapter(SelectPackageActivity.this, SelectPackageActivity.this, Global.membershipPack_List);
                listViewMemberShip.setAdapter(membershipAdapter);
                membershipAdapter.setCustomListener(SelectPackageActivity.this);
            } else {
                Snackbar.make(listViewMemberShip, result[1], Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onButtonClick(int position, String buttonText) {
        if (buttonText.equalsIgnoreCase("btn_click")) {
            /*Constant.SELECTED_PACK = Global.membershipPack_List.get(position).getPackage_name();
            SharedPreferences sharedPreferences = getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
            Constant.USER_ID = sharedPreferences.getString("user_id", "");
            callSerivice();*/

            Intent intent=new Intent(SelectPackageActivity.this,InitialActivity.class);
            intent.putExtra("pay_amount",""+Global.membershipPack_List.get(position).getPackage_price());
            startActivity(intent);
        }
    }
}