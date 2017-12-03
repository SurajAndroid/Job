package com.startupsoch.jobpool;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.startupsoch.jobpool.R;

import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

public class EditemployeActivity extends AppCompatActivity implements RequestReceiver {

    RequestReceiver receiver;
    EditText nameCompanyEditTxt, contactPersonEditTxt, email_idEditTxt, phoneEditTxt,
            current_requirementEditTxt, experienceEditTxt, skillEditTxt, job_roleEditTxt, locationEditTxt,
            addressEditTxt;
    CheckBox termsCondiationCheck;
    LinearLayout SubmiTLayout;
    ScrollView parentLayout;
    SharedPreferences sharedPreferences;
    MenuFragment menuFragment;
    EditProfileActivity editProfileActivity;
    Spinner spinner;
    String[] citys = {"Select City", "Bidar", "Delhi", "Kalaburagi", "Hyderabad", "Indore","Coimbatore","Pune","Bengaluru"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_employer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        clickListener();
    }

    public void init(){

        receiver = this;

        editProfileActivity = new EditProfileActivity();
        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.EMAIL = sharedPreferences.getString("email","");

        nameCompanyEditTxt = (EditText)findViewById(R.id.nameCompanyEditTxt);
        contactPersonEditTxt = (EditText)findViewById(R.id.contactPersonEditTxt);
        email_idEditTxt = (EditText)findViewById(R.id.email_idEditTxt);
        phoneEditTxt = (EditText)findViewById(R.id.phoneEditTxt);

        current_requirementEditTxt = (EditText)findViewById(R.id.current_requirementEditTxt);
        experienceEditTxt = (EditText)findViewById(R.id.experienceEditTxt);
        skillEditTxt = (EditText)findViewById(R.id.skillEditTxt);
        job_roleEditTxt = (EditText)findViewById(R.id.job_roleEditTxt);
        locationEditTxt = (EditText)findViewById(R.id.locationEditTxt);
        addressEditTxt = (EditText)findViewById(R.id.addressEditTxt);

        parentLayout = (ScrollView)findViewById(R.id.parentLayout);
        termsCondiationCheck = (CheckBox)findViewById(R.id.termsCondiationCheck);
        SubmiTLayout = (LinearLayout)findViewById(R.id.SubmiTLayout);
        spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citys);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
//      setCompanyData();
        getCompanySerivice();
    }

    public  void clickListener(){

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.LOCATION = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*if(current_requirementEditTxt.getText().length()!=0){
            if(skillEditTxt.getText().length()!=0){
                if(!Constant.LOCATION.equalsIgnoreCase("Location")){
                    Constant.COMPANY_NAME =  nameCompanyEditTxt.getText().toString();
                    Constant.CONTACTPERSON = contactPersonEditTxt.getText().toString();
                    Constant.EMAIL = email_idEditTxt.getText().toString();
                    Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();
                    Constant.CURRENT_REQUIRMENT = current_requirementEditTxt.getText().toString();
                    Constant.EXPERIENCE = experienceEditTxt.getText().toString();
                    Constant.SKILLES = skillEditTxt.getText().toString();
                    Constant.JOBROLL = job_roleEditTxt.getText().toString();
                    Constant.LOCATION = locationEditTxt.getText().toString();
                    Constant.ADDRESS = addressEditTxt.getText().toString();
                    callSerivice();
                }else {
                    Snackbar.make(parentLayout,"Select location.!",Snackbar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(parentLayout,"Enter skilles.!",Snackbar.LENGTH_SHORT).show();
            }
        }else {
            Snackbar.make(parentLayout,"Enter current requirement.!",Snackbar.LENGTH_SHORT).show();
        }*/

        SubmiTLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameCompanyEditTxt.getText().length()!=0){
                    if(contactPersonEditTxt.getText().length()!=0){
                        if(email_idEditTxt.getText().length()!=0){
                            if(Constant.emailValidation(email_idEditTxt.getText().toString())){
                                if(phoneEditTxt.getText().length()>=10){
                                    Constant.COMPANY_NAME =  nameCompanyEditTxt.getText().toString();
                                    Constant.CONTACTPERSON = contactPersonEditTxt.getText().toString();
                                    Constant.EMAIL = email_idEditTxt.getText().toString();
                                    Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();
                                    callSerivice();
                                }else {
                                    Snackbar.make(parentLayout,"Enter valid phone number.!",Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                Snackbar.make(parentLayout,"Enter valid email.!",Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(parentLayout,"Enter email.!",Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(parentLayout,"Enter contact person.!",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentLayout,"Enter company name.!",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void setCompanyData(){
        try{
            if(!Global.companylist.get(0).getCompany_name().equals("null")){
                nameCompanyEditTxt.setText(Global.companylist.get(0).getCompany_name());
            }else {
                nameCompanyEditTxt.setText("");
            }
            if(!Global.companylist.get(0).getContact_person().equals("null")){
                contactPersonEditTxt.setText(Global.companylist.get(0).getContact_person());
            }else {
                contactPersonEditTxt.setText("");
            }

            if(!Global.companylist.get(0).getEmail().equals("null")){
                email_idEditTxt.setText(Global.companylist.get(0).getEmail());
            }else {
                email_idEditTxt.setText("");
            }
            if(!Global.companylist.get(0).getPhone().equals("null")){
                phoneEditTxt.setText(Global.companylist.get(0).getPhone());
            }else {
                phoneEditTxt.setText("");
            }
            if(!Global.companylist.get(0).getCurrent_requirment().equals("null")){
                current_requirementEditTxt.setText(Global.companylist.get(0).getCurrent_requirment());
            }else {
                current_requirementEditTxt.setText("");
            }

            if(!Global.companylist.get(0).getExperience().equals("null")){
                experienceEditTxt.setText(Global.companylist.get(0).getExperience());
            }else {
                experienceEditTxt.setText("");
            }
            if(!Global.companylist.get(0).getSkill().equals("null")){
                skillEditTxt.setText(Global.companylist.get(0).getSkill());
            }else {
                skillEditTxt.setText("");
            }

            if(!Global.companylist.get(0).getJob_role().equals("null")){
                job_roleEditTxt.setText(Global.companylist.get(0).getJob_role());
            }else {
                job_roleEditTxt.setText("");
            }
            if(!Global.companylist.get(0).getLocation().equals("null")){
                locationEditTxt.setText(Global.companylist.get(0).getLocation());
            }else {
                locationEditTxt.setText("");
            }

            if(!Global.companylist.get(0).getAddress().equals("null")){
                addressEditTxt.setText(Global.companylist.get(0).getAddress());
            }else {
                addressEditTxt.setText("");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getCompanySerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditemployeActivity.this);
        getprofile.setAction(Constant.GET_COMPANY_PROFILE);
        getprofile.execute();
    }

    public void callSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, EditemployeActivity.this);
        employer.setAction(Constant.UPDATE_EMPLOYER_PROFILE);
        employer.execute();
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
            if(result[0].equals("1")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("company_name", "" + Global.companylist.get(0).getCompany_name());
                editor.commit();
                menuFragment.updateName(EditemployeActivity.this);
                final Dialog dialog = new Dialog(EditemployeActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertpopup);
                dialog.show();
                TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
                massageTxtView.setText(result[1]);
                LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
                submitLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                editProfileActivity.setcompanyData(EditemployeActivity.this);
                dialog.dismiss();
                finish();
                    }
                });

            }else if(result[0].equals("01")){
                setCompanyData();
            }
    }
}
