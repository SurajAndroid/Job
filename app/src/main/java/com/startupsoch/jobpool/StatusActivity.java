package com.startupsoch.jobpool;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.startupsoch.jobpool.R;

import utils.Constant;
import utils.SavedData;

public class StatusActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        final Dialog dialog = new Dialog(StatusActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_alert);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView takePhotoTxt = (TextView)dialog.findViewById(R.id.takePhotoTxt);
        SavedData.savePack(Constant.PACKAGE_NAME);
        takePhotoTxt.setText("Your packge "+ Constant.PACKAGE_NAME +" update Successfully");
        TextView cancelTxt = (TextView) dialog.findViewById(R.id.cancelTxt);
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                finish();
            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
