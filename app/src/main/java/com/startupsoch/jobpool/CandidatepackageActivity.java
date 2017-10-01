package com.startupsoch.jobpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.startupsoch.jobpool.R;

import org.json.JSONException;

import java.math.BigDecimal;

import paypal.PayPalConfig;

public class CandidatepackageActivity extends AppCompatActivity {

    LinearLayout TenCandidate, ThertyCandidate, fiftyCondidate;
    LinearLayout tenComany, thertyCompany, fiftyCompany;
    TextView tenComTxt, thertyComTxt, fifityTxt  , theredTxt, secondTxt, firstTxt;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private String paymentAmount="45";
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatepackage);

        init();
        clickListener();
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void init(){

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Black.ttf");

        Typeface ttf = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Italic.ttf");


        tenComany = (LinearLayout)findViewById(R.id.tenComany);
        thertyCompany = (LinearLayout)findViewById(R.id.thertyCompany);
        fiftyCompany = (LinearLayout)findViewById(R.id.fiftyCompany);

        tenComTxt = (TextView)findViewById(R.id.tenComTxt);
        thertyComTxt = (TextView)findViewById(R.id.thertyComTxt);
        fifityTxt = (TextView)findViewById(R.id.fifityTxt);

        theredTxt = (TextView)findViewById(R.id.theredTxt);
        secondTxt = (TextView)findViewById(R.id.secondTxt);
        firstTxt = (TextView)findViewById(R.id.firstTxt);

        tenComTxt.setTypeface(tf);
        thertyComTxt.setTypeface(tf);
        fifityTxt.setTypeface(tf);

        theredTxt.setTypeface(ttf);
        secondTxt.setTypeface(ttf);
        firstTxt.setTypeface(ttf);

    }

    private void getPayment() {
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "USD", "Your Payment is ",
                PayPalPayment.PAYMENT_INTENT_ORDER);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);
                        final Dialog dialog = new Dialog(CandidatepackageActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alertpopup);
                        dialog.show();
                        TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
                        massageTxtView.setText("Payment Done.!");
                        LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
                        submitLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }


    public void clickListener(){


        tenComany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*paymentAmount = "100";
                getPayment();*/
                Intent intent = new Intent(CandidatepackageActivity.this, InitialActivity.class);
                startActivity(intent);
            }
        });

        thertyCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*paymentAmount = "300";
                getPayment();*/
                Intent intent = new Intent(CandidatepackageActivity.this, InitialActivity.class);
                startActivity(intent);
            }
        });

        fiftyCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* paymentAmount = "500";
                getPayment();*/
                Intent intent = new Intent(CandidatepackageActivity.this, InitialActivity.class);
                startActivity(intent);
            }
        });
    }
}
