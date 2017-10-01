package com.startupsoch.jobpool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.startupsoch.jobpool.R;

/**
 * Created by Admin1 on 9/12/2017.
 */

public class WebViewForTerms extends AppCompatActivity {
    WebView webView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView1 = (WebView) findViewById(R.id.webView1);

        webView1.setWebViewClient(new WebViewClient());

        String URl = getIntent().getExtras().getString("URL");
        webView1.loadUrl(URl);
//        webView1.loadUrl("http://jobpool.in/terms");
    }
}
