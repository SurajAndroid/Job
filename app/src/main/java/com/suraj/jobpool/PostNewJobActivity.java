package com.suraj.jobpool;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import adapter.BranchAdapter;
import adapter.JobRollAdapter;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

public class PostNewJobActivity extends AppCompatActivity implements RequestReceiver {

    RequestReceiver receiver;
    EditText  skillesEditTxt, noOfRquirmwntEditTxt, DiscriptionEditTxt;
    RadioButton itiRadio, nonitRadio, maleRadio, femaleRadio;
    Spinner spinnerBranch, spinnerJobRaoll, spinerExperience, spinnerCity;
    LinearLayout SubmiTLayout;
    ScrollView parentLayout;
    TextView branchTxt, jobTxt;
    int pos;
    SharedPreferences sharedPreferences;

    String[] citys = {"Select City", "Bengaluru", "Bidar","Delhi","Hydrabad","Indore", "Kalaburagi","Pune"};
    String[] expirence = {"Select Experience", "0", "1","2","3","4", "5","6","7","8","9", "10+"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_job);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        clickListener();
    }

    public void init(){

        sharedPreferences = getApplicationContext().getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.COMPANY_NAME = sharedPreferences.getString("company_name","");
        Constant.COMPANY_ID = sharedPreferences.getString("user_id","");
        Constant.JOB_TYPE="";
        receiver = this;
        branchTxt = (TextView)findViewById(R.id.branchTxt);
        jobTxt = (TextView)findViewById(R.id.jobTxt);
        skillesEditTxt = (EditText)findViewById(R.id.skillesEditTxt);
        noOfRquirmwntEditTxt = (EditText)findViewById(R.id.noOfRquirmwntEditTxt);
        DiscriptionEditTxt = (EditText)findViewById(R.id.DiscriptionEditTxt);

        spinnerBranch = (Spinner)findViewById(R.id.spinnerBranch);
        spinnerJobRaoll = (Spinner)findViewById(R.id.spinnerJobRaoll);
        spinerExperience = (Spinner)findViewById(R.id.spinerExperience);
        spinnerCity = (Spinner)findViewById(R.id.spinnerCity);

        itiRadio = (RadioButton)findViewById(R.id.itiRadio);
        nonitRadio = (RadioButton)findViewById(R.id.nonitRadio);
        maleRadio = (RadioButton)findViewById(R.id.maleRadio);
        femaleRadio = (RadioButton)findViewById(R.id.femaleRadio);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citys);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(dataAdapter);

        ArrayAdapter<String> exp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, expirence);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerExperience.setAdapter(exp);


        parentLayout = (ScrollView)findViewById(R.id.parentLayout);
        SubmiTLayout = (LinearLayout)findViewById(R.id.SubmiTLayout);
        Constant.EXPERIENCE = "Select Experience";
        callgetSerivice();
    }

    public void callgetSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, PostNewJobActivity.this);
        employer.setAction(Constant.GET_FILTER_DATA);
        employer.execute();
    }


    public  void clickListener(){



        itiRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(itiRadio.isChecked()){
                    nonitRadio.setChecked(false);
                    Constant.JOB_TYPE = itiRadio.getText().toString();
                    branchTxt.setVisibility(View.GONE);
                    jobTxt.setVisibility(View.GONE);
                    BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                    spinnerBranch.setAdapter(adapter);
                    spinnerBranch.setEnabled(true);
                }
            }
        });

        nonitRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(nonitRadio.isChecked()){
                    itiRadio.setChecked(false);
                    branchTxt.setVisibility(View.GONE);
                    jobTxt.setVisibility(View.GONE);
                    Constant.JOB_TYPE = nonitRadio.getText().toString();
                    BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                    spinnerBranch.setAdapter(adapter);
                    spinnerBranch.setSelection(Global.getFilterList.size()-1);
                    spinnerBranch.setEnabled(false);
                }
            }
        });

        maleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(maleRadio.isChecked()){
                    femaleRadio.setChecked(false);
                    Constant.GENDER = maleRadio.getText().toString();
                }
            }
        });

        femaleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(femaleRadio.isChecked()){
                    maleRadio.setChecked(false);
                    Constant.GENDER = femaleRadio.getText().toString();
                }
            }
        });


        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.SPECILIZATION = Global.getFilterList.get(position).getIndustry();
                pos = position;
                JobRollAdapter adapter = new JobRollAdapter(getApplicationContext(),Global.getFilterList.get(position).getJobRolllist());
                spinnerJobRaoll.setAdapter(adapter);

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerJobRaoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.JOBROLL = Global.getFilterList.get(pos).getJobRolllist().get(position).getTitle().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.LOCATION = citys[position];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinerExperience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.EXPERIENCE = expirence[position];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SubmiTLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    public void validation(){
        if(!Constant.JOB_TYPE.equals("")){
            if(!Constant.EXPERIENCE.equals("Select Experience")){
                if(maleRadio.isChecked() || femaleRadio.isChecked()){
                    if(skillesEditTxt.getText().length()!=0){
                        if(noOfRquirmwntEditTxt.getText().length()!=0){
                            if(!Constant.LOCATION.equals("Select City")){
                                Constant.DISCRIPTION = DiscriptionEditTxt.getText().toString();
                                Constant.NO_OF_REQUIRMENT = noOfRquirmwntEditTxt.getText().toString();
                                Constant.SKILLES = skillesEditTxt.getText().toString();
                                callSerivice();
                            }else {
                                Snackbar.make(parentLayout,"Select City.",Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(parentLayout,"Select no of post.",Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(parentLayout,"Enter skills.",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentLayout,"Select Candidate gender.",Snackbar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(parentLayout, "Select experience.", Snackbar.LENGTH_SHORT).show();
            }
        }else {
            Snackbar.make(parentLayout,"Select Job Type.",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void callSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, PostNewJobActivity.this);
        employer.setAction(Constant.POST_JOB);
        employer.execute();
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
            if(result[0].equals("001")){
                final Dialog dialog = new Dialog(PostNewJobActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertpopup);
                TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
                massageTxtView.setText(result[1]);
                LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
                submitLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });

                dialog.show();
            }else {

            }
    }
}
