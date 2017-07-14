package com.example.dell.job;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

public class EditCandidateActivity extends Activity implements RequestReceiver {

    Button saveandcontinue;
    ScrollView parentLayout;
    RequestReceiver receiver;
    LinearLayout submitNowlayout;
    EditText nameEditTxt, phoneEditTxt, locationEdit, experienceEditTxt, skillEditTxt,strenghtEdit, salaryEdit,addressEditTxt,objectiveEdit,briefDesEdit,email_idEditTxt;
    EditProfileActivity candidateActivity;
    SharedPreferences sharedPreferences;
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
        submitNowlayout = (LinearLayout)findViewById(R.id.submitNowlayout);
        nameEditTxt = (EditText)findViewById(R.id.nameEditTxt);
        phoneEditTxt = (EditText)findViewById(R.id.phoneEditTxt);
        locationEdit = (EditText)findViewById(R.id.locationEdit);
        experienceEditTxt = (EditText)findViewById(R.id.experienceEditTxt);
        skillEditTxt = (EditText)findViewById(R.id.skillEditTxt);
        strenghtEdit = (EditText)findViewById(R.id.strenghtEdit);
        salaryEdit = (EditText)findViewById(R.id.salaryEdit);
        addressEditTxt = (EditText)findViewById(R.id.addressEditTxt);
        objectiveEdit = (EditText)findViewById(R.id.objectiveEdit);
        briefDesEdit = (EditText)findViewById(R.id.briefDesEdit);
        email_idEditTxt = (EditText)findViewById(R.id.email_idEditTxt);
        parentLayout = (ScrollView)findViewById(R.id.parentLayout);
        setprofileData();
    }

    public void updateprofile() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditCandidateActivity.this);
        getprofile.setAction(Constant.UPDATE_CANDIDATE_PROFILE);
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
            if(!Global.candidatelist.get(0).getLocation().equals("null")){
                locationEdit.setText(Global.candidatelist.get(0).getLocation());
            }else {
                locationEdit.setText("");
            }
            if(!Global.candidatelist.get(0).getExperience().equals("null")){
                experienceEditTxt.setText(Global.candidatelist.get(0).getExperience());
            }else {
                experienceEditTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getSkill().equals("null")){
                skillEditTxt.setText(Global.candidatelist.get(0).getSkill());
            }else {
                skillEditTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getStrength().equals("null")){
                strenghtEdit.setText(Global.candidatelist.get(0).getStrength());
            }else {
                strenghtEdit.setText("");
            }
            if(!Global.candidatelist.get(0).getExpected_salary().equals("null")){
                salaryEdit.setText(Global.candidatelist.get(0).getExpected_salary());
            }else {
                salaryEdit.setText("");
            }

            if(!Global.candidatelist.get(0).getAddress().equals("null")){
                addressEditTxt.setText(Global.candidatelist.get(0).getAddress());
            }else {
                addressEditTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getObjective().equals("null")){
                objectiveEdit.setText(Global.candidatelist.get(0).getObjective());
            }else {
                objectiveEdit.setText("");
            }
            if(!Global.candidatelist.get(0).getBrief_description().equals("null")){
                briefDesEdit.setText(Global.candidatelist.get(0).getBrief_description());
            }else {
                briefDesEdit.setText("");
            }
            if(!Global.candidatelist.get(0).getEmail().equals("null")){
                email_idEditTxt.setText(Global.candidatelist.get(0).getEmail());
            }else {
                email_idEditTxt.setText("");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clickListener(){

        submitNowlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameEditTxt.getText().length()!=0){
                    if(phoneEditTxt.getText().length()!=0){
                        if(phoneEditTxt.getText().length()>=10){
                            if(locationEdit.getText().length()!=0){
                                if(experienceEditTxt.getText().length()!=0){
                                    if(skillEditTxt.getText().length()!=0){
                                        if(strenghtEdit.getText().length()!=0){
                                            if(salaryEdit.getText().length()!=0){
                                                if(addressEditTxt.getText().length()!=0){
                                                    if(objectiveEdit.getText().length()!=0){
                                                        if(briefDesEdit.getText().length()!=0){
                                                            if(email_idEditTxt.getText().length()!=0){
                                                                if(Constant.emailValidation(email_idEditTxt.getText().toString())){

                                                                    Constant.USER_NAME = nameEditTxt.getText().toString();
                                                                    Constant.PHONE_NUMBER = phoneEditTxt.getText().toString();
                                                                    Constant.LOCATION = locationEdit.getText().toString();
                                                                    Constant.EXPERIENCE = experienceEditTxt.getText().toString();
                                                                    Constant.SKILLES = skillEditTxt.getText().toString();
                                                                    Constant.STRENGHT = strenghtEdit.getText().toString();
                                                                    Constant.EXP_SALARY= salaryEdit.getText().toString();
                                                                    Constant.ADDRESS = addressEditTxt.getText().toString();
                                                                    Constant.OBJECTIVE = objectiveEdit.getText().toString();
                                                                    Constant.BRIEFDESCRIPTION = briefDesEdit.getText().toString();
                                                                    Constant.EMAIL= email_idEditTxt.getText().toString();
                                                                    updateprofile();

                                                                }else {
                                                                    Snackbar.make(parentLayout,"Enter valid email.!",Snackbar.LENGTH_SHORT).show();
                                                                }
                                                            }else {
                                                                Snackbar.make(parentLayout,"Enter email.!",Snackbar.LENGTH_SHORT).show();
                                                            }
                                                        }else {
                                                            Snackbar.make(parentLayout,"Enter about your self.!",Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    }else {
                                                        Snackbar.make(parentLayout,"Enter your objective.!",Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Snackbar.make(parentLayout,"Enter your address.!",Snackbar.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Snackbar.make(parentLayout,"Enter your salary expectation.!",Snackbar.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Snackbar.make(parentLayout,"Enter your strength.!",Snackbar.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Snackbar.make(parentLayout,"Enter skilles.!",Snackbar.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Snackbar.make(parentLayout,"Enter youe work experience.!",Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                Snackbar.make(parentLayout,"Enter location.!",Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(parentLayout,"Enter valid phone number.!",Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(parentLayout,"Enter phone number.!",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentLayout,"Enter name.!",Snackbar.LENGTH_SHORT).show();
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

        }
    }
}
