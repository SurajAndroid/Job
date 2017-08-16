package com.suraj.jobpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.StringTokenizer;

import utils.Constant;
import utils.PathUtils;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by chauhan on 5/12/2017.
 */


public class CondidateRegisterActivity extends Activity implements RequestReceiver {

    EditText nameEditTxt, email_idEditTxt, passwordEditTxt, phoneEditTxt, jobrole, user_idEditTxt;
    CheckBox checkbox_male, checkbox_female,termsCondiationCheck;
    RelativeLayout parentLayout;
    LinearLayout registerNowLayout;
    RequestReceiver receiver;
    TextView conditionTxt;
    String[] citys = {"Select City", "Bangalore", "Bider","Delhi","Hydrabad","Indore", "Kalaburagi","Pune"};
    Spinner spinner;
    LinearLayout uploadLayout;
    TextView uploadTxt;

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
        user_idEditTxt  = (EditText) findViewById(R.id.user_idEditTxt);
        email_idEditTxt  = (EditText) findViewById(R.id.email_idEditTxt);
        passwordEditTxt  = (EditText) findViewById(R.id.passwordEditTxt);
        phoneEditTxt  = (EditText) findViewById(R.id.phoneEditTxt);

        jobrole = (EditText)findViewById(R.id.jobrole);


        checkbox_male = (CheckBox)findViewById(R.id.checkbox_male);
        checkbox_female = (CheckBox)findViewById(R.id.checkbox_female);
        termsCondiationCheck = (CheckBox)findViewById(R.id.termsCondiationCheck);
        conditionTxt = (TextView)findViewById(R.id.conditionTxt);

        uploadTxt = (TextView)findViewById(R.id.uploadTxt);

        registerNowLayout = (LinearLayout)findViewById(R.id.registerNowLayout);
        uploadLayout =  (LinearLayout)findViewById(R.id.uploadLayout);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        conditionTxt.setText(Html.fromHtml("<p><u>TERMS AND CONDITIONS</u></p>").toString());

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citys);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Uri selectedFileURI = data.getData();
                File file = new File(selectedFileURI.getPath().toString());
                Log.d("", "File : " + file.getName());
                String uploadedFileName = file.getName().toString();
                uploadTxt.setText(uploadedFileName);
                PathUtils.getPath(getApplicationContext(),selectedFileURI);
//                Toast.makeText(getApplicationContext(), ""+PathUtils.getPath(getApplicationContext(),selectedFileURI) , Toast.LENGTH_SHORT).show();
                Constant.DOCUMENT = PathUtils.getPath(getApplicationContext(),selectedFileURI);
            }
        }
    }



    public void clickListener(){

        uploadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            1);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.LOCATION = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        conditionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Coming Soon..!
            }
        });

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
            if(user_idEditTxt.getText().length()!=0){
                if(email_idEditTxt.getText().length()!=0){
                   if(passwordEditTxt.getText().length()!=0){
                        if(phoneEditTxt.getText().length()!=0){
                            if(jobrole.getText().length()!=0){
                                if(checkbox_male.isChecked() || checkbox_female.isChecked()){
                                    if(checkbox_male.isChecked()){
                                        Constant.GENDER = "Male";
                                    }else {
                                        Constant.GENDER = "Female";
                                    }
                                    if(termsCondiationCheck.isChecked()){
                                        if(!Constant.LOCATION.equals("Location")){
                                            Constant.NAME = nameEditTxt.getText().toString();
                                            Constant.USER_NAME = user_idEditTxt.getText().toString();
                                            Constant.EMAIL = email_idEditTxt.getText().toString();
                                            Constant.PASSWORD = passwordEditTxt.getText().toString();
                                            Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();
                                            callSerivice();
                                        }else {
                                            Snackbar.make(parentLayout,"Select location",Snackbar.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Snackbar.make(parentLayout,"Select terms & Condition.",Snackbar.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Snackbar.make(parentLayout,"Select Gender",Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                Snackbar.make(parentLayout,"Enter Job Role.",Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(parentLayout,"Enter Phone No",Snackbar.LENGTH_SHORT).show();
                        }
                   }else {
                       Snackbar.make(parentLayout,"Enter Password",Snackbar.LENGTH_SHORT).show();
                   }
                }else {
                    Snackbar.make(parentLayout,"Enter Email",Snackbar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(parentLayout,"Enter UserId",Snackbar.LENGTH_SHORT).show();
            }
        }else {
            Snackbar.make(parentLayout,"Enter Name",Snackbar.LENGTH_SHORT).show();
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
                    dialog.dismiss();
                    Intent intent = new Intent(CondidateRegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            dialog.show();
        }else {
            Snackbar.make(parentLayout,""+result[1],Snackbar.LENGTH_SHORT).show();
        }
    }
}
