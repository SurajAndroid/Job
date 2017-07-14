package com.example.dell.job;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import utils.Constant;
import utils.RequestReceiver;
import utils.WebserviceHelper;

public class EmployerActivity extends AppCompatActivity implements RequestReceiver{

    RequestReceiver receiver;
    EditText nameCompanyEditTxt, contactPersonEditTxt, email_idEditTxt, phoneEditTxt, passwordEditTxt,
            current_requirementEditTxt, experienceEditTxt, skillEditTxt, job_roleEditTxt, locationEditTxt,
            addressEditTxt;
    CheckBox termsCondiationCheck;
    LinearLayout SubmiTLayout;
    ScrollView parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        clickListener();
    }

    public void init(){

        receiver = this;
        nameCompanyEditTxt = (EditText)findViewById(R.id.nameCompanyEditTxt);
        contactPersonEditTxt = (EditText)findViewById(R.id.contactPersonEditTxt);
        email_idEditTxt = (EditText)findViewById(R.id.email_idEditTxt);
        phoneEditTxt = (EditText)findViewById(R.id.phoneEditTxt);
        passwordEditTxt = (EditText)findViewById(R.id.passwordEditTxt);
        current_requirementEditTxt = (EditText)findViewById(R.id.current_requirementEditTxt);
        experienceEditTxt = (EditText)findViewById(R.id.experienceEditTxt);
        skillEditTxt = (EditText)findViewById(R.id.skillEditTxt);
        job_roleEditTxt = (EditText)findViewById(R.id.job_roleEditTxt);
        locationEditTxt = (EditText)findViewById(R.id.locationEditTxt);
        addressEditTxt = (EditText)findViewById(R.id.addressEditTxt);

        parentLayout = (ScrollView)findViewById(R.id.parentLayout);
        termsCondiationCheck = (CheckBox)findViewById(R.id.termsCondiationCheck);
        SubmiTLayout = (LinearLayout)findViewById(R.id.SubmiTLayout);

    }

    public  void clickListener(){
        SubmiTLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    public void validation(){
        if(nameCompanyEditTxt.getText().length()!=0){
            if(contactPersonEditTxt.getText().length()!=0){
                if(email_idEditTxt.getText().length()!=0){
                    if(phoneEditTxt.getText().length()!=0){
                        if(passwordEditTxt.getText().length()!=0){
                            if(current_requirementEditTxt.getText().length()!=0){
                                if(experienceEditTxt.getText().length()!=0){
                                    if(skillEditTxt.getText().length()!=0){
                                        if(job_roleEditTxt.getText().length()!=0){
                                            if(locationEditTxt.getText().length()!=0){
                                                if(addressEditTxt.getText().length()!=0){

                                                    Constant.COMPANY_NAME = nameCompanyEditTxt.getText().toString();
                                                    Constant.CONTACTPERSON = contactPersonEditTxt.getText().toString();
                                                    Constant.EMAIL = email_idEditTxt.getText().toString();
                                                    Constant.PASSWORD = passwordEditTxt.getText().toString();
                                                    Constant.CURRENT_REQUIRMENT = current_requirementEditTxt.getText().toString();
                                                    Constant.EXPERIENCE = experienceEditTxt.getText().toString();
                                                    Constant.SKILLES = skillEditTxt.getText().toString();
                                                    Constant.JOBROLL = job_roleEditTxt.getText().toString();
                                                    Constant.LOCATION = locationEditTxt.getText().toString();
                                                    Constant.ADDRESS = addressEditTxt.getText().toString();
                                                    if(termsCondiationCheck.isChecked()){
                                                        callSerivice();

                                                    }else {
                                                        Snackbar.make(parentLayout,"Select Terms.!",Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Snackbar.make(parentLayout,"Enter Address.!",Snackbar.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Snackbar.make(parentLayout,"Enter Location.!",Snackbar.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Snackbar.make(parentLayout,"Enter your Role.!",Snackbar.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Snackbar.make(parentLayout,"Enter Skills.!",Snackbar.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Snackbar.make(parentLayout,"Enter your Experience.!",Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                Snackbar.make(parentLayout,"Enter current Requirment.!",Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(parentLayout,"Enter Password.!",Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(parentLayout,"Enter Phone Number.!",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentLayout,"Enter Email.!",Snackbar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(parentLayout,"Enter Contact Person Name.!",Snackbar.LENGTH_SHORT).show();
            }
        }else {
            Snackbar.make(parentLayout,"Enter Company Name.!",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void callSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, EmployerActivity.this);
        employer.setAction(Constant.EMPLOYER_RAGISTRATION);
        employer.execute();
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
            if(result[0].equals("1")){
                final Dialog dialog = new Dialog(EmployerActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertpopup);
                TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
                massageTxtView.setText(result[1]);
                LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
                submitLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmployerActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                dialog.show();
            }
    }
}
