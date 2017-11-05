package com.startupsoch.jobpool;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.startupsoch.jobpool.R;

import adapter.BranchAdapter;
import adapter.JobRollAdapter;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

public class EmployerActivity extends AppCompatActivity implements RequestReceiver {

    RequestReceiver receiver;
    EditText nameCompanyEditTxt, contactPersonEditTxt, email_idEditTxt, phoneEditTxt,
            passwordEditTxt, skillesEditTxt, noOfRquirmwntEditTxt, DiscriptionEditTxt;
    RadioButton itiRadio, nonitRadio, maleRadio, femaleRadio;
    Spinner spinnerBranch, spinnerJobRaoll, spinerExperience, spinnerCity;
    TextView branchTxt, jobTxt;
    TextView conditionTxtterms, conditionTxtPolicy;
    CheckBox termsCondiationCheck;
    LinearLayout SubmiTLayout;
    ScrollView parentLayout;
    String[] citys = {"Select City", "Bidar", "Gulbarga", "Indore", "Bengaluru", "Jabalpur"};
    String[] expirence = {"Select your Experience", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10+"};
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        clickListener();
    }

    public void init() {

        receiver = this;
        branchTxt = (TextView) findViewById(R.id.branchTxt);
        jobTxt = (TextView) findViewById(R.id.jobTxt);
        conditionTxtterms = (TextView) findViewById(R.id.conditionTxtterms);
        conditionTxtPolicy = (TextView) findViewById(R.id.conditionTxtPolicy);

        nameCompanyEditTxt = (EditText) findViewById(R.id.nameCompanyEditTxt);
        contactPersonEditTxt = (EditText) findViewById(R.id.contactPersonEditTxt);
        email_idEditTxt = (EditText) findViewById(R.id.email_idEditTxt);
        phoneEditTxt = (EditText) findViewById(R.id.phoneEditTxt);
        passwordEditTxt = (EditText) findViewById(R.id.passwordEditTxt);
        DiscriptionEditTxt = (EditText) findViewById(R.id.DiscriptionEditTxt);

        skillesEditTxt = (EditText) findViewById(R.id.skillesEditTxt);
        noOfRquirmwntEditTxt = (EditText) findViewById(R.id.noOfRquirmwntEditTxt);

        spinnerBranch = (Spinner) findViewById(R.id.spinnerBranch);
        spinnerJobRaoll = (Spinner) findViewById(R.id.spinnerJobRaoll);
        spinerExperience = (Spinner) findViewById(R.id.spinerExperience);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);

        itiRadio = (RadioButton) findViewById(R.id.itiRadio);
        nonitRadio = (RadioButton) findViewById(R.id.nonitRadio);
        maleRadio = (RadioButton) findViewById(R.id.maleRadio);
        femaleRadio = (RadioButton) findViewById(R.id.femaleRadio);
        maleRadio.setChecked(true);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, expirence);
        dataAdapter.setDropDownViewResource(R.layout.spinner_txt);
        spinerExperience.setAdapter(dataAdapter);

        ArrayAdapter<String> cty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citys);
        cty.setDropDownViewResource(R.layout.spinner_txt);
        spinnerCity.setAdapter(cty);



        parentLayout = (ScrollView) findViewById(R.id.parentLayout);
        termsCondiationCheck = (CheckBox) findViewById(R.id.termsCondiationCheck);
        SubmiTLayout = (LinearLayout) findViewById(R.id.SubmiTLayout);

        conditionTxtterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployerActivity.this, WebViewForTerms.class);
                intent.putExtra("URL", "http://jobpool.in/terms");
                startActivity(intent);
            }
        });
        conditionTxtPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployerActivity.this, WebViewForTerms.class);
                intent.putExtra("URL", "http://jobpool.in/policy");
                startActivity(intent);
            }
        });

        callgetFilterSerivice();
    }


    public void callgetFilterSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, EmployerActivity.this);
        employer.setAction(Constant.GET_FILTER_DATA);
        employer.execute();
    }


    public void clickListener() {

        itiRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (itiRadio.isChecked()) {
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
                if (nonitRadio.isChecked()) {
                    itiRadio.setChecked(false);
                    branchTxt.setVisibility(View.GONE);
                    jobTxt.setVisibility(View.GONE);
                    Constant.JOB_TYPE = nonitRadio.getText().toString();
                    BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                    spinnerBranch.setAdapter(adapter);
                    spinnerBranch.setSelection(Global.getFilterList.size() - 1);
                    spinnerBranch.setEnabled(false);
                }
            }
        });

        maleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (maleRadio.isChecked()) {
                    femaleRadio.setChecked(false);
                    Constant.GENDER = maleRadio.getText().toString();
                }
            }
        });

        femaleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (femaleRadio.isChecked()) {
                    maleRadio.setChecked(false);
                    Constant.GENDER = femaleRadio.getText().toString();
                }
            }
        });


        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.SPECILIZATION = Global.getFilterList.get(position).getIndustry();
                pos = position;
                JobRollAdapter adapter = new JobRollAdapter(getApplicationContext(), Global.getFilterList.get(position).getJobRolllist());
                spinnerJobRaoll.setAdapter(adapter);

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


        spinnerJobRaoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.JOBROLL = Global.getFilterList.get(pos).getJobRolllist().get(position).getTitle().toString();
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

    public void validation() {
        if (nameCompanyEditTxt.getText().length() != 0) {
            if (contactPersonEditTxt.getText().length() != 0) {
                if (email_idEditTxt.getText().length() != 0) {
                    if (phoneEditTxt.getText().length() != 0) {
                        if (passwordEditTxt.getText().length() != 0) {
                            if (!Constant.JOB_TYPE.equals("")) {
                                if (!Constant.EXPERIENCE.equals("Select your Experience")) {
                                    if (maleRadio.isChecked() || maleRadio.isChecked()) {
                                        if (skillesEditTxt.getText().length() != 0) {
                                            if (noOfRquirmwntEditTxt.getText().length() != 0) {
                                                if (!Constant.LOCATION.equals("Select City")) {
                                                    if (termsCondiationCheck.isChecked()) {
                                                        Constant.DISCRIPTION = DiscriptionEditTxt.getText().toString();
                                                        Constant.NO_OF_REQUIRMENT = noOfRquirmwntEditTxt.getText().toString();
                                                        Constant.SKILLES = skillesEditTxt.getText().toString();
                                                        Constant.COMPANY_NAME = nameCompanyEditTxt.getText().toString();
                                                        Constant.CONTACTPERSON = contactPersonEditTxt.getText().toString();
                                                        Constant.EMAIL = email_idEditTxt.getText().toString();
                                                        Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();
                                                        Constant.PASSWORD = passwordEditTxt.getText().toString();
                                                        callSerivice();
                                                    } else {
                                                        Snackbar.make(parentLayout, "Select Terms and Conditions.", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Snackbar.make(parentLayout, "Select City.", Snackbar.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Snackbar.make(parentLayout, "Select no of post.", Snackbar.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Snackbar.make(parentLayout, "Enter skills.", Snackbar.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Snackbar.make(parentLayout, "Select Candidate gender.", Snackbar.LENGTH_SHORT).show();
                                    }
                                } else
                                    Snackbar.make(parentLayout, "Select your experience.", Snackbar.LENGTH_SHORT).show();

                            } else {
                                Snackbar.make(parentLayout, "Select Job Type.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(parentLayout, "Enter Password.", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(parentLayout, "Enter Phone Number.", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(parentLayout, "Enter Email.", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(parentLayout, "Enter Contact Person Name.", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(parentLayout, "Enter Company Name.", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void callSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, EmployerActivity.this);
        employer.setAction(Constant.EMPLOYER_RAGISTRATION);
        employer.execute();
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if (result[0].equals("1")) {
            final Dialog dialog = new Dialog(EmployerActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertpopup);
            TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
            massageTxtView.setText(result[1]);
            LinearLayout submitLayout = (LinearLayout) dialog.findViewById(R.id.submitLayout);
            submitLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EmployerActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            dialog.show();
        } else {
            if(!result[1].equalsIgnoreCase("Branch list"))
            Snackbar.make(parentLayout, "" + result[1], Snackbar.LENGTH_SHORT).show();
        }
    }
}
