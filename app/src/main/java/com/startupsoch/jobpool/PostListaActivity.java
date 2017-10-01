package com.startupsoch.jobpool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.startupsoch.jobpool.R;

import adapter.PostAdapter;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by SURAJ on 9/3/2017.
 */

public class PostListaActivity extends Activity implements RequestReceiver{

    RelativeLayout parentLayout;
    RequestReceiver receiver;
    TextView postLayout;
    ListView postListVIew;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_all_post);

        init();
        clickListener();
    }

    public void init(){
        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);
        receiver = this;
        postLayout = (TextView)findViewById(R.id.postLayout);
        postListVIew = (ListView)findViewById(R.id.postListVIew);

        sharedPreferences = getApplicationContext().getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.COMPANY_ID = sharedPreferences.getString("user_id","");

        getALLPOSTSerivice();
    }

    public void getALLPOSTSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, PostListaActivity.this);
        employer.setAction(Constant.GET_ALL_POST);
        employer.execute();
    }

    public void clickListener(){

        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PostListaActivity.this,PostNewJobActivity.class);
                startActivity(intent);
            }
        });
;
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
            if(result[0].equals("01")){
                PostAdapter postListaActivity = new PostAdapter(PostListaActivity.this,PostListaActivity.this, Global.postJob_List);
                postListVIew.setAdapter(postListaActivity);
            }else {
                Snackbar.make(parentLayout,result[1],Snackbar.LENGTH_SHORT).show();
            }
    }
}
