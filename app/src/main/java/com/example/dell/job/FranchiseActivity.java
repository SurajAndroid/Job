package com.example.dell.job;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by chauhan on 5/12/2017.
 */

public class FranchiseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.franchise_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
