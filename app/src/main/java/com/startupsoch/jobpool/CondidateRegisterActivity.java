package com.startupsoch.jobpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.startupsoch.jobpool.R;

import java.io.File;

import adapter.BranchAdapter;
import adapter.JobRollAdapter;
import utils.Constant;
import utils.Global;
import utils.PathUtils;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by chauhan on 5/12/2017.
 */

public class CondidateRegisterActivity extends Activity implements RequestReceiver {

    EditText nameEditTxt, email_idEditTxt, passwordEditTxt, phoneEditTxt;
    Spinner spinnerBranch, spinnerJobRole, spinnerCity, spinnerGender;
    RadioButton itiRadio, nonitiRadio;
    CheckBox checkbox_male, checkbox_female, termsCondiationCheck;
    RelativeLayout parentLayout;
    LinearLayout registerNowLayout;
    RequestReceiver receiver;
    TextView conditionTxt, conditionTxtterms, conditionTxtPolicy;
    String[] citys = {"Select City", "Bidar", "Gulbarga", "Indore", "Bengaluru", "Jabalpur"};
    String[] gender = {"Gender", "Male", "Female"};
    LinearLayout uploadLayout;
    TextView uploadTxt, jobrolTxt, branchTxt;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condidate_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        clickListener();
    }

    public void init() {

        receiver = this;
        nameEditTxt = (EditText) findViewById(R.id.nameEditTxt);
        email_idEditTxt = (EditText) findViewById(R.id.email_idEditTxt);
        passwordEditTxt = (EditText) findViewById(R.id.passwordEditTxt);
        phoneEditTxt = (EditText) findViewById(R.id.phoneEditTxt);

        checkbox_male = (CheckBox) findViewById(R.id.checkbox_male);
        checkbox_female = (CheckBox) findViewById(R.id.checkbox_female);
        termsCondiationCheck = (CheckBox) findViewById(R.id.termsCondiationCheck);

//      conditionTxt = (TextView) findViewById(R.id.conditionTxt);
        conditionTxtterms = (TextView) findViewById(R.id.conditionTxtterms);
        conditionTxtPolicy = (TextView) findViewById(R.id.conditionTxtPolicy);

        spinnerBranch = (Spinner) findViewById(R.id.spinnerBranch);
        spinnerJobRole = (Spinner) findViewById(R.id.spinnerJobRole);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        itiRadio = (RadioButton) findViewById(R.id.itiRadio);
        nonitiRadio = (RadioButton) findViewById(R.id.nonitiRadio);

        uploadTxt = (TextView) findViewById(R.id.uploadTxt);
        jobrolTxt = (TextView) findViewById(R.id.jobrolTxt);
        branchTxt = (TextView) findViewById(R.id.branchTxt);

        registerNowLayout = (LinearLayout) findViewById(R.id.registerNowLayout);
        uploadLayout = (LinearLayout) findViewById(R.id.uploadLayout);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citys);
        dataAdapter.setDropDownViewResource(R.layout.spinner_txt);
        spinnerCity.setAdapter(dataAdapter);

        ArrayAdapter<String> spinnergender = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, gender);
        spinnergender.setDropDownViewResource(R.layout.spinner_txt);
        spinnerGender.setAdapter(spinnergender);

        conditionTxtterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CondidateRegisterActivity.this, WebViewForTerms.class);
                intent.putExtra("URL", "http://jobpool.in/terms");
                startActivity(intent);
//                new Intent(CondidateRegisterActivity.this, WebViewForTerms.class);
            }
        });
        conditionTxtPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CondidateRegisterActivity.this, WebViewForTerms.class);
                intent.putExtra("URL", "http://jobpool.in/policy");
                startActivity(intent);
//                new Intent(CondidateRegisterActivity.this, WebViewForTerms.class);
            }
        });

        callgetFilterSerivice();

    }

    public void callgetFilterSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, CondidateRegisterActivity.this);
        employer.setAction(Constant.GET_FILTER_DATA);
        employer.execute();
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
                PathUtils.getPath(getApplicationContext(), selectedFileURI);
//                Toast.makeText(getApplicationContext(), ""+PathUtils.getPath(getApplicationContext(),selectedFileURI) , Toast.LENGTH_SHORT).show();
                Constant.DOCUMENT = PathUtils.getPath(getApplicationContext(), selectedFileURI);
            }
        }
    }


    public void clickListener() {

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

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Constant.LOCATION = citys[position];

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

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.GENDER = gender[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        itiRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (itiRadio.isChecked()) {
                    nonitiRadio.setChecked(false);
                    jobrolTxt.setVisibility(View.GONE);
                    branchTxt.setVisibility(View.GONE);
                    ;
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
                if (nonitiRadio.isChecked()) {
                    itiRadio.setChecked(false);
                    jobrolTxt.setVisibility(View.GONE);
                    branchTxt.setVisibility(View.GONE);
                    ;
                    Constant.JOB_TYPE = nonitiRadio.getText().toString();
                    BranchAdapter adapter = new BranchAdapter(getApplicationContext(), Global.getFilterList);
                    spinnerBranch.setAdapter(adapter);
                    spinnerBranch.setSelection(Global.getFilterList.size() - 1);
                    spinnerBranch.setEnabled(false);
                }
            }
        });

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

        checkbox_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkbox_male.isChecked()) {
                    checkbox_female.setChecked(false);
                }
            }
        });

        checkbox_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkbox_female.isChecked()) {
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

    public void validation() {
        if (nameEditTxt.getText().length() != 0) {
            if (email_idEditTxt.getText().length() != 0) {
                if (passwordEditTxt.getText().length() != 0) {
                    if (phoneEditTxt.getText().length() != 0) {
                        if (!Constant.JOB_TYPE.equals("")) {
                            if (!Constant.LOCATION.equals("Select City")) {
                                if (!Constant.GENDER.equalsIgnoreCase("Gender")) {
                                    if (termsCondiationCheck.isChecked()) {
                                        Constant.NAME = nameEditTxt.getText().toString();
                                        Constant.EMAIL = email_idEditTxt.getText().toString();
                                        Constant.PASSWORD = passwordEditTxt.getText().toString();
                                        Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();
                                        callSerivice();
                                    } else {
                                        Snackbar.make(parentLayout, "Select terms & Condition.", Snackbar.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Snackbar.make(parentLayout, "Select Gender", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(parentLayout, "Select location", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(parentLayout, "Select Job Type", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(parentLayout, "Enter Phone No", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(parentLayout, "Enter Password", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(parentLayout, "Enter Email", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(parentLayout, "Enter Name", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void requestFinished(String[] result) throws Exception {
        if (result[0].equals("1")) {

            final Dialog dialog = new Dialog(CondidateRegisterActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertpopup);
            TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
            massageTxtView.setText(result[1]);
            LinearLayout submitLayout = (LinearLayout) dialog.findViewById(R.id.submitLayout);
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
        } else if(result[0].equals("01")){

        }else {
            Snackbar.make(parentLayout, "" + result[1], Snackbar.LENGTH_SHORT).show();
        }
    }
}
