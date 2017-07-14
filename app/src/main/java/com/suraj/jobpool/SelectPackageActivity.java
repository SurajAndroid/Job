package com.suraj.jobpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import org.json.JSONException;

import java.math.BigDecimal;

import paypal.PayPalConfig;
import utils.Constant;

import static com.suraj.jobpool.CandidatepackageActivity.PAYPAL_REQUEST_CODE;

/**
 * Created by chauhan on 5/28/2017.
 */

public class SelectPackageActivity extends Activity {

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    LinearLayout ThertyCondidatelayout,TwentyCondidatelayout, TenCondidatelayout;
    private String paymentAmount;
    TextView fifityTxt, thertyTxt, tenTxt, theredTxt, secondTxt, firstTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employerpackage);

        init();
        clickListenr();
    }

    public void init(){
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Black.ttf");

        Typeface ttf = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Italic.ttf");
        ThertyCondidatelayout = (LinearLayout)findViewById(R.id.ThertyCondidatelayout);
        TwentyCondidatelayout = (LinearLayout)findViewById(R.id.TwentyCondidatelayout);
        TenCondidatelayout = (LinearLayout)findViewById(R.id.TenCondidatelayout);

        fifityTxt = (TextView)findViewById(R.id.fifityTxt);
        thertyTxt = (TextView)findViewById(R.id.thertyTxt);
        tenTxt = (TextView)findViewById(R.id.tenTxt);

        theredTxt  = (TextView)findViewById(R.id.theredTxt);
        secondTxt  = (TextView)findViewById(R.id.secondTxt);
        firstTxt  = (TextView)findViewById(R.id.firstTxt);

        fifityTxt.setTypeface(tf);
        thertyTxt.setTypeface(tf);
        tenTxt.setTypeface(tf);
        theredTxt.setTypeface(ttf);
        secondTxt.setTypeface(ttf);
        firstTxt.setTypeface(ttf);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    public void clickListenr(){

        ThertyCondidatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*paymentAmount = "500";
                getPayment();*/
                Intent intent = new Intent(SelectPackageActivity.this, InitialActivity.class);
                startActivity(intent);
            }
        });

        TwentyCondidatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*paymentAmount = "300";
                getPayment();*/
                Intent intent = new Intent(SelectPackageActivity.this, InitialActivity.class);
                startActivity(intent);
            }
        });

        TenCondidatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*paymentAmount = "100";
                getPayment();*/
                Intent intent = new Intent(SelectPackageActivity.this, InitialActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getPayment() {

        //Getting the amount from editText
        // paymentAmount = editTextAmount.getText().toString();
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "USD", "Your Payment is ",
                PayPalPayment.PAYMENT_INTENT_ORDER);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        final Dialog dialog = new Dialog(SelectPackageActivity.this);
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
}
