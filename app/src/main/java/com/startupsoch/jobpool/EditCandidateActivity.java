package com.startupsoch.jobpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import adapter.BranchAdapter;
import adapter.JobRollAdapter;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

public class EditCandidateActivity extends Activity implements RequestReceiver {


    RelativeLayout parentLayout;
    RequestReceiver receiver;
    LinearLayout submitNowlayout;
    EditText nameEditTxt  ,email_idEditTxt, phoneEditTxt;
    RadioButton itiRadio, nonitiRadio;
    Spinner spinnerBranch, spinnerJobRole,spinnerCity;
    TextView   jobrolTxt, branchTxt;

    EditProfileActivity candidateActivity;
    SharedPreferences sharedPreferences;
    int pos;
    String[] citys = {"Select City", "Bidar", "Delhi", "Kalaburagi", "Hyderabad", "Indore","Coimbatore","Pune","Bengaluru"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_complete_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        clickListener();
    }

    public void init(){

        receiver = this;
        candidateActivity = new EditProfileActivity();
        sharedPreferences = getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.EMAIL = sharedPreferences.getString("email","");

        nameEditTxt = (EditText)findViewById(R.id.nameEditTxt);
        phoneEditTxt = (EditText)findViewById(R.id.phoneEditTxt);
        submitNowlayout = (LinearLayout)findViewById(R.id.registerNowLayout) ;

        email_idEditTxt = (EditText)findViewById(R.id.email_idEditTxt);
        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);

        spinnerBranch = (Spinner)findViewById(R.id.spinnerBranch);
        spinnerJobRole = (Spinner)findViewById(R.id.spinnerJobRole);
        spinnerCity = (Spinner)findViewById(R.id.spinnerCity);

        itiRadio = (RadioButton)findViewById(R.id.itiRadio);
        nonitiRadio = (RadioButton)findViewById(R.id.nonitiRadio);

        jobrolTxt = (TextView) findViewById(R.id.jobrolTxt);
        branchTxt = (TextView) findViewById(R.id.branchTxt);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citys);
        dataAdapter.setDropDownViewResource(R.layout.spinner_txt);
        spinnerCity.setAdapter(dataAdapter);

//      setprofileData();
        callSerivice();

    }


    public void callSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, EditCandidateActivity.this);
        employer.setAction(Constant.GET_FILTER_DATA);
        employer.execute();
    }

    public void updateprofile() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditCandidateActivity.this);
        getprofile.setAction(Constant.UPDATE_CANDIDATE_PROFILE);
        getprofile.execute();
    }

    public void getCandidateSerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditCandidateActivity.this);
        getprofile.setAction(Constant.GET_CANDIDATE_PROFILE);
        getprofile.execute();
    }

    public  void setprofileData(){
        try {
            if(!Global.candidatelist.get(0).getName().equals("null")){
                nameEditTxt.setText(Global.candidatelist.get(0).getName());
            }else {
                nameEditTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getPhone().equals("null")){
                phoneEditTxt.setText(Global.candidatelist.get(0).getPhone());
            }else {
                phoneEditTxt.setText("");
            }

            if(!Global.candidatelist.get(0).getEmail().equals("null")){
                email_idEditTxt.setText(Global.candidatelist.get(0).getEmail());
            }else {
                email_idEditTxt.setText("");
            }

            if(Global.candidatelist.get(0).getJobType().equalsIgnoreCase("iti")) {
                itiRadio.setChecked(true);
                nonitiRadio.setChecked(false);
                jobrolTxt.setVisibility(View.GONE);
                branchTxt.setVisibility(View.GONE);

                Constant.JOB_TYPE = itiRadio.getText().toString();
                BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                spinnerBranch.setAdapter(adapter);
                spinnerBranch.setEnabled(true);
            }else {
                nonitiRadio.setChecked(true);

                jobrolTxt.setVisibility(View.GONE);
                branchTxt.setVisibility(View.GONE);

                Constant.JOB_TYPE = nonitiRadio.getText().toString();
                BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                spinnerBranch.setAdapter(adapter);
                spinnerBranch.setSelection(Global.getFilterList.size() - 1);
                spinnerBranch.setEnabled(false);


            }

            for(int i =0;i<citys.length;i++){
                if(Global.candidatelist.get(0).getLocation().equals(citys[i])){
                    spinnerCity.setSelection(i);
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clickListener(){


        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.SPECILIZATION = Global.getFilterList.get(position).getIndustry();
                pos = position;

                JobRollAdapter adapter = new JobRollAdapter(getApplicationContext(), Global.getFilterList.get(position).getJobRolllist());
                spinnerJobRole.setAdapter(adapter);

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerJobRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.JOBROLL = Global.getFilterList.get(pos).getJobRolllist().get(position).getTitle().toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        itiRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(itiRadio.isChecked()){
                    nonitiRadio.setChecked(false);
                    jobrolTxt.setVisibility(View.GONE);
                    branchTxt.setVisibility(View.GONE);

                    Constant.JOB_TYPE = itiRadio.getText().toString();
                    BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                    spinnerBranch.setAdapter(adapter);
                    spinnerBranch.setEnabled(true);
                }
            }
        });

        nonitiRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(nonitiRadio.isChecked()){
                    itiRadio.setChecked(false);
                    itiRadio.setChecked(false);
                    jobrolTxt.setVisibility(View.GONE);
                    branchTxt.setVisibility(View.GONE);

                    Constant.JOB_TYPE = nonitiRadio.getText().toString();
                    BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                    spinnerBranch.setAdapter(adapter);
                    spinnerBranch.setSelection(Global.getFilterList.size() - 1);
                    spinnerBranch.setEnabled(false);
                }
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.LOCATION = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitNowlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameEditTxt.getText().length()!=0){
                    if(phoneEditTxt.getText().length()!=0){
                        if(phoneEditTxt.getText().length()>=10){
                            if(!Constant.LOCATION.equalsIgnoreCase("Location")){
                                if(email_idEditTxt.getText().length()!=0){
                                    if(Constant.emailValidation(email_idEditTxt.getText().toString())){

                                        Constant.USER_NAME = nameEditTxt.getText().toString();
                                        Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();

                                        Constant.EMAIL= email_idEditTxt.getText().toString();
                                        updateprofile();

                                    }else {
                                        Snackbar.make(parentLayout,"Enter valid email.",Snackbar.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Snackbar.make(parentLayout,"Enter email.",Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                Snackbar.make(parentLayout,"Select location.",Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(parentLayout,"Enter valid phone number.",Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(parentLayout,"Enter phone number.",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentLayout,"Enter name.",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if (result[0].equals("1")){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_name", "" + Global.candidatelist.get(0).getName());
            editor.commit();

            final Dialog dialog = new Dialog(EditCandidateActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertpopup);
            dialog.show();
            TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
            massageTxtView.setText(result[1]);
            LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
            submitLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    EditProfileActivity.setcandidateData(EditCandidateActivity.this);
                    MenuFragment.updateName(EditCandidateActivity.this);
                    finish();

                }
            });

        }else if(result[0].equals("01")){
            getCandidateSerivice();
        }else if(result[0].equals("001")){
            setprofileData();
        }
    }
}
