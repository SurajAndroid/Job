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
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.startupsoch.jobpool.R;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

import adapter.MembershipAdapter;
import dtos.MembershipDTO;
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
    ArrayList<MembershipDTO> pacakgeList;

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
        sharedPreferences = getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
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

            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (sharedPreferences.getString("user_type", "").equals("candidate")){

                editor.putString("company_show_intrest", "" + Constant.COMPANY_SHOW_INTERST);
                editor.putString("no_of_applicant", "" + Constant.NO_OF_APPLIED);
                editor.putString("out_of_apply", "" + Constant.OUT_OFF_APPLY);
                editor.commit();
                MenuFragment.SetInterestvalue();

            }else {

                editor.putString("out_of_download", "" + Constant.OUT_OF_DOWNLOAD);
                editor.putString("no_of_download", "" + Constant.NOOF_DOWNLOAD);
                editor.putString("out_of_post", "" + Constant.OUT_OF_POST);
                editor.putString("no_of_post", "" + Constant.NO_OF_POST);
                editor.commit();
                MenuFragment.SetPostedvalue();
            }



            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("Sohel ", "working ..  .. ");
                    Intent intent = new Intent(SelectPackageActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            }, 1000);*/

        } else {
            if (result[0].equals("01")) {

                if (sharedPreferences.getString("user_type", "").equals("candidate")) {

                    Constant.PACKAGE_TYPE = sharedPreferences.getString("user_type", "");
                    pacakgeList = new ArrayList<>();
                    for(int i=0;i<Global.membershipPack_List.size();i++){
                        if(Global.membershipPack_List.get(i).getPackage_type().equals("candidate")){
                            pacakgeList.add(Global.membershipPack_List.get(i));
                        }
                    }
                    MembershipAdapter membershipAdapter = new MembershipAdapter(SelectPackageActivity.this, SelectPackageActivity.this, pacakgeList);
                    listViewMemberShip.setAdapter(membershipAdapter);
                    membershipAdapter.setCustomListener(SelectPackageActivity.this);
                } else {
                    Constant.PACKAGE_TYPE = sharedPreferences.getString("user_type", "");
                    pacakgeList = new ArrayList<>();
                    for(int i=0;i<Global.membershipPack_List.size();i++){
                        if(Global.membershipPack_List.get(i).getPackage_type().equals("employer")){
                            pacakgeList.add(Global.membershipPack_List.get(i));
                        }
                    }
                    MembershipAdapter membershipAdapter = new MembershipAdapter(SelectPackageActivity.this, SelectPackageActivity.this, pacakgeList);
                    listViewMemberShip.setAdapter(membershipAdapter);
                    membershipAdapter.setCustomListener(SelectPackageActivity.this);
                }



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
            sharedPreferences = getApplicationContext().getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
            Constant.USER_ID = sharedPreferences.getString("user_id","");
            Constant.PACKAGE_NAME = pacakgeList.get(position).getPackage_name();
            if(pacakgeList.get(position).getPackage_name().equals("Free Trail") || pacakgeList.get(position).getPackage_name().equals("Trial Pack")){
                callSerivice();
            }else {
                Intent intent=new Intent(SelectPackageActivity.this,InitialActivity.class);
                intent.putExtra("pay_amount",""+pacakgeList.get(position).getPackage_price());
                startActivity(intent);
//                callSerivice();
            }

        }
    }
}