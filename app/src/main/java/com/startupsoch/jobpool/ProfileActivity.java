package com.startupsoch.jobpool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.squareup.picasso.Picasso;
import com.startupsoch.jobpool.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by Suraj Shakya on 5/14/2017.
 */

public class ProfileActivity extends SlidingFragmentActivity implements RequestReceiver {

    LinearLayout slidMenuLayout;
    TextView downloadTxtView, contactTxtView;
    SlidingMenu sm;
    RelativeLayout parentLayout;
    File myDir;
    ImageView ProfileImage;
    TextView candidateName;
    TextView userNameTxt, userIDTxt, CityTxt, SkillesTxt, ExpirenceTxt;
    int position;
    String TAG;
    RequestReceiver receiver;
    SharedPreferences sharedPreferences;

    EditText profileJobType, profileSpecialization, profileJobRole, profileCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        init();
        clickListenr();

        setBehindView();
        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setSlidingEnabled(false);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void init() {

        receiver = this;
        String data = getIntent().getExtras().getString("position", "");
        TAG = getIntent().getExtras().getString("Tag", "");
        position = Integer.parseInt(data);

        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.COMPANY_ID = sharedPreferences.getString("user_id", "");

        try {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        slidMenuLayout = (LinearLayout) findViewById(R.id.slidMenuLayout);
        downloadTxtView = (TextView) findViewById(R.id.downloadTxtView);
        contactTxtView = (TextView) findViewById(R.id.contactTxtView);

        candidateName = (TextView) findViewById(R.id.candidateName);

        ProfileImage = (ImageView) findViewById(R.id.ProfileImage);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);

        userNameTxt = (TextView) findViewById(R.id.userNameTxt);
        userIDTxt = (TextView) findViewById(R.id.userIDTxt);
        CityTxt = (TextView) findViewById(R.id.CityTxt);
        SkillesTxt = (TextView) findViewById(R.id.SkillesTxt);
        ExpirenceTxt = (TextView) findViewById(R.id.ExpirenceTxt);

        profileJobType = (EditText) findViewById(R.id.profileJobType);
        profileSpecialization = (EditText) findViewById(R.id.profileSpecialization);
        profileJobRole = (EditText) findViewById(R.id.profileJobRole);
        profileCity = (EditText) findViewById(R.id.profileCity);

        setProfielData();

    }

    public void viewProfileService() {
        WebserviceHelper employer = new WebserviceHelper(receiver, ProfileActivity.this);
        employer.setAction(Constant.VIEW_PROFILE_API);
        employer.execute();
    }

    public void setProfielData() {

        if (TAG.equalsIgnoreCase("AllListActivity")) {
            candidateName.setText(Global.searchcandidatelist.get(position).getName().toUpperCase());

            Log.e("Sohel ","    "+ Global.searchcandidatelist.get(position).getJobRole());
            Log.e("Sohel ","    "+ Global.searchcandidatelist.get(position).getJobType());
            Log.e("Sohel ","    "+ Global.searchcandidatelist.get(position).getSpecialization());

            if (!Global.searchcandidatelist.get(position).getJobRole().equals("null")) {
                profileJobRole.setText(Global.searchcandidatelist.get(position).getJobRole());
            } else {
                profileJobRole.setText("");
            }
            if (!Global.searchcandidatelist.get(position).getJobType().equals("null")) {
                profileJobType.setText(Global.searchcandidatelist.get(position).getJobType());
            } else {
                profileJobType.setText("");
            }
            if (!Global.searchcandidatelist.get(position).getSpecialization().equals("null")) {
                profileSpecialization.setText(Global.searchcandidatelist.get(position).getSpecialization());
            } else {
                profileSpecialization.setText("");
            }

            if (!Global.searchcandidatelist.get(position).getLocation().equals("null")) {
                CityTxt.setText(Global.searchcandidatelist.get(position).getLocation());
                profileCity.setText(Global.searchcandidatelist.get(position).getLocation());
            } else {
                CityTxt.setText("");
                profileCity.setText("");
            }

            if (!Global.searchcandidatelist.get(position).getName().equals("null")) {
                userNameTxt.setText(Global.searchcandidatelist.get(position).getName().toUpperCase());
            } else {
                userNameTxt.setText("");
            }

            try {
                Picasso.with(getApplicationContext()).load(Global.searchcandidatelist.get(position).getUserImage())
                        .placeholder(R.drawable.placeholder).into(ProfileImage);
            } catch (Exception e) {

            }
        } else {
            candidateName.setText(Global.candidatelist.get(position).getName().toUpperCase());

            if (!Global.candidatelist.get(position).getJobRole().equals("null")) {
                profileJobRole.setText(Global.candidatelist.get(position).getJobRole());
            } else {
                profileJobRole.setText("");
            }
            if (!Global.candidatelist.get(position).getJobType().equals("null")) {
                profileJobType.setText(Global.candidatelist.get(position).getJobType());
            } else {
                profileJobType.setText("");
            }
            if (!Global.candidatelist.get(position).getSpecialization().equals("null")) {
                profileSpecialization.setText(Global.candidatelist.get(position).getSpecialization());
            } else {
                profileSpecialization.setText("");
            }

            if (!Global.candidatelist.get(position).getLocation().equals("null")) {
                CityTxt.setText(Global.candidatelist.get(position).getLocation());
                profileCity.setText(Global.candidatelist.get(position).getLocation());
            } else {
                CityTxt.setText("");
                profileCity.setText("");
            }

            if (!Global.candidatelist.get(position).getLocation().equals("null")) {
                CityTxt.setText(Global.candidatelist.get(position).getLocation());
            } else {
                CityTxt.setText("");
            }

              try {
                Picasso.with(getApplicationContext()).load(Global.candidatelist.get(position).getUserImage())
                        .placeholder(R.drawable.placeholder).into(ProfileImage);
            } catch (Exception e) {

            }
        }

    }


    public void DownloadSerivice() {
        WebserviceHelper searchAPI = new WebserviceHelper(receiver, ProfileActivity.this);
        searchAPI.setAction(Constant.UPDATE_DWONLOAD);
        searchAPI.execute();
    }


    public void clickListenr() {

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.toggle();
            }
        });


        contactTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"ContACT",Toast.LENGTH_SHORT).show();
            }
        });

        downloadTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadTxtView.setBackgroundResource(R.color.yellow);
                contactTxtView.setBackgroundResource(R.color.colorAccent);
                DownloadSerivice();


            }
        });

        contactTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTxtView.setBackgroundResource(R.color.colorAccent);
                contactTxtView.setBackgroundResource(R.color.yellow);
                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.share_dialog);
                TextView mailTxt = (TextView) dialog.findViewById(R.id.mailTxt);
                TextView messageTxt = (TextView) dialog.findViewById(R.id.messageTxt);
                TextView emailTxt = (TextView) dialog.findViewById(R.id.emailTxt);
                TextView phoneTxt = (TextView) dialog.findViewById(R.id.phoneTxt);
                if (TAG.equalsIgnoreCase("AllListActivity")) {
                    emailTxt.setText("Email : " + Global.searchcandidatelist.get(position).getEmail());
                    phoneTxt.setText("Phone : " + Global.searchcandidatelist.get(position).getPhone());
                } else {
                    emailTxt.setText("Email : " + Global.candidatelist.get(position).getEmail());
                    phoneTxt.setText("Phone : " + Global.candidatelist.get(position).getPhone());
                }

                mailTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ConTact");
                        emailIntent.putExtra(Intent.EXTRA_TEXT,  "");
                        startActivity(Intent.createChooser(emailIntent, "Send Email..."));
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{Global.candidatelist.get(0).getEmail().toString()});
                        i.putExtra(Intent.EXTRA_SUBJECT, "");
                        i.putExtra(Intent.EXTRA_TEXT, "");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ProfileActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                messageTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                + Global.candidatelist.get(0).getPhone())));
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private void setBehindView() {
        setBehindContentView(R.layout.menu_slide);
        transactionFragments(MenuFragment.newInstance(), R.id.menu_slide);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void transactionFragments(Fragment fragment, int viewResource) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(viewResource, fragment);
        ft.commit();
        toggle();
    }


    @Override
    public void requestFinished(String[] result) throws Exception {

        if(result[0].equals("001")){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("out_of_download", "" + Constant.OUT_OF_DOWNLOAD);
            editor.putString("no_of_download", "" + Constant.NOOF_DOWNLOAD);
            editor.commit();
            MenuFragment.SetPostedvalue();
            new DownloadTask(ProfileActivity.this, Global.candidatelist.get(position).getResume());

        }else {
            Toast.makeText(getApplicationContext(),""+result[1],Toast.LENGTH_SHORT).show();
        }
    }



    public class DownloadTask {

        private static final String TAG = "Download Task";
        private Context context;

        private String downloadUrl = "", downloadFileName = "";
        private ProgressDialog progressDialog;

        public DownloadTask(Context context, String downloadUrl) {
            this.context = context;

            this.downloadUrl = downloadUrl;


            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
            Log.e(TAG, downloadFileName);

            //Start Downloading Task
            new DownloadingTask().execute();
        }

        private class DownloadingTask extends AsyncTask<Void, Void, Void> {

            File apkStorage = null;
            File outputFile = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (outputFile != null) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 3000);

                        Log.e(TAG, "Download Failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    //Change button text if exception occurs

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

                }
                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage());

                    }


                    //Get File if SD card is present
                    if (new CheckForSDCard().isSDCardPresent()) {

                        apkStorage = new File(
                                Environment.getExternalStorageDirectory() + "/"
                                        + "JOBPOOL RESUME");
                    } else
                        Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e(TAG, "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                }

                return null;
            }
        }
    }

    public class CheckForSDCard {
        //Check If SD Card is present or not method
        public boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }
}
