package com.example.dell.job;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    EditText input_username,input_password;
    CheckBox rememberCheck;
    LinearLayout loginLayout;
    TextView forgotPassword, registerTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        init();
        clickListener();
    }

    public void init(){
        input_username  = (EditText)findViewById(R.id.input_username);
        input_password = (EditText)findViewById(R.id.input_password);

        rememberCheck = (CheckBox)findViewById(R.id.rememberCheck);
        loginLayout = (LinearLayout)findViewById(R.id.loginLayout);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        registerTxtView = (TextView)findViewById(R.id.registerTxtView);

    }


    public  void clickListener(){

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
