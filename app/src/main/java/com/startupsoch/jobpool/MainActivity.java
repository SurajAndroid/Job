package com.startupsoch.jobpool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.startupsoch.jobpool.R;

import java.io.File;

public class MainActivity extends Activity {

	File myDir;
	TextView txt2,txt3;
	LinearLayout LinearLayout1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		txt2 = (TextView) findViewById(R.id.textView2);
		LinearLayout1 = (LinearLayout)findViewById(R.id.LinearLayout1);


		txt2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
			}

		});
	}





}
