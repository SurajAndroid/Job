package com.example.dell.job;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import utils.Constant;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by chauhan on 5/12/2017.
 */

public class CondidateRegisterActivity extends Activity implements RequestReceiver{

    EditText nameEditTxt, useridEditTxt, email_idEditTxt, passwordEditTxt, phoneEditTxt;
    CheckBox checkbox_male, checkbox_female,termsCondiationCheck;
    RelativeLayout parentLayout;
    LinearLayout registerNowLayout;
    RequestReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condidate_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        clickListener();
    }

    public  void init(){
        receiver = this;
        nameEditTxt = (EditText) findViewById(R.id.nameEditTxt);
        useridEditTxt  = (EditText) findViewById(R.id.useridEditTxt);
        email_idEditTxt  = (EditText) findViewById(R.id.email_idEditTxt);
        passwordEditTxt  = (EditText) findViewById(R.id.passwordEditTxt);
        phoneEditTxt  = (EditText) findViewById(R.id.phoneEditTxt);

        checkbox_male = (CheckBox)findViewById(R.id.checkbox_male);
        checkbox_female = (CheckBox)findViewById(R.id.checkbox_female);
        termsCondiationCheck = (CheckBox)findViewById(R.id.termsCondiationCheck);

        registerNowLayout = (LinearLayout)findViewById(R.id.registerNowLayout);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
    }

    public void clickListener(){

        checkbox_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkbox_male.isChecked()){
                    checkbox_female.setChecked(false);
                }
            }
        });

        checkbox_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkbox_female.isChecked()){
                    checkbox_male.setChecked(false);
                }
            }
        });

        registerNowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
                /*Intent intent = new Intent(CondidateRegisterActivity.this, CompleteProfileActivity.class);
                startActivity(intent);*/
            }
        });
    }

    public void callSerivice() {
        WebserviceHelper signup = new WebserviceHelper(receiver, CondidateRegisterActivity.this);
        signup.setAction(Constant.CONDIDATE_RAGISTRATION);
        signup.execute();
    }


    public  void validation(){
        if(nameEditTxt.getText().length()!=0){
            if(useridEditTxt.getText().length()!=0){
                if(email_idEditTxt.getText().length()!=0){
                   if(passwordEditTxt.getText().length()!=0){
                        if(phoneEditTxt.getText().length()!=0){
                            if(checkbox_male.isChecked() || checkbox_female.isChecked()){
                                if(checkbox_male.isChecked()){
                                    Constant.GENDER = "Male";
                                }else {
                                    Constant.GENDER = "Female";
                                }
                                if(termsCondiationCheck.isChecked()){
                                    Constant.NAME = nameEditTxt.getText().toString();
                                    Constant.USER_NAME = useridEditTxt.getText().toString();
                                    Constant.EMAIL = email_idEditTxt.getText().toString();
                                    Constant.PASSWORD = passwordEditTxt.getText().toString();
                                    Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();
                                    callSerivice();
                                }else {
                                    Snackbar.make(parentLayout,"Select terms & Condition.!",Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                Snackbar.make(parentLayout,"Select Gender!",Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(parentLayout,"Enter Phone No!",Snackbar.LENGTH_SHORT).show();
                        }
                   }else {
                       Snackbar.make(parentLayout,"Enter Password!",Snackbar.LENGTH_SHORT).show();
                   }
                }else {
                    Snackbar.make(parentLayout,"Enter Email!",Snackbar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(parentLayout,"Enter UserId!",Snackbar.LENGTH_SHORT).show();
            }
        }else {
            Snackbar.make(parentLayout,"Enter Name!",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if(result[0].equals("1")){

            final Dialog dialog = new Dialog(CondidateRegisterActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertpopup);
            TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
            massageTxtView.setText(result[1]);
            LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
            submitLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CondidateRegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            dialog.show();

        }
    }
}
