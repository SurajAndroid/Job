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

        viewProfileService();
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
            if (!Global.searchcandidatelist.get(position).getSkill().equals("null")) {
                SkillesTxt.setText(Global.searchcandidatelist.get(position).getSkill());
            } else {
                SkillesTxt.setText("");
            }

            if (!Global.searchcandidatelist.get(position).getExperience().equals("null")) {
                ExpirenceTxt.setText(Global.searchcandidatelist.get(position).getExperience());
            } else {
                ExpirenceTxt.setText("");
            }

            if (Global.searchcandidatelist.get(position).getUserName().equals("null")) {
                userIDTxt.setText("");
            } else {
                userIDTxt.setText(Global.searchcandidatelist.get(position).getUserName());
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
      /*      if (!Global.candidatelist.get(position).getSkill().equals("null")) {
                SkillesTxt.setText(Global.candidatelist.get(position).getSkill());
            } else {
                SkillesTxt.setText("");
            }*/

           /* if (!Global.candidatelist.get(position).getExperience().equals("null")) {
                ExpirenceTxt.setText(Global.candidatelist.get(position).getExperience());
            } else {
                ExpirenceTxt.setText("");
            }

            if (Global.candidatelist.get(position).getUserName().equals("null")) {
                userIDTxt.setText("");
            } else {
                userIDTxt.setText(Global.candidatelist.get(position).getUserName());
            }

            if (!Global.candidatelist.get(position).getName().equals("null")) {
                userNameTxt.setText(Global.candidatelist.get(position).getName().toUpperCase());
            } else {
                userNameTxt.setText("");
            }*/

            try {
                Picasso.with(getApplicationContext()).load(Global.candidatelist.get(position).getUserImage())
                        .placeholder(R.drawable.placeholder).into(ProfileImage);
            } catch (Exception e) {

            }
        }

    }

    public void clickListenr() {
        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.toggle();
            }
        });
        downloadTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTxtView.setBackgroundResource(R.color.yellow);
                contactTxtView.setBackgroundResource(R.color.colorAccent);
//                savePdf();
                new DownloadTask(ProfileActivity.this, Global.candidatelist.get(position).getResume());
            }
        });

        contactTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Click Listener",Toast.LENGTH_SHORT).show();

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
                        /*Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ConTact");
                        emailIntent.putExtra(Intent.EXTRA_TEXT,  "");
                        startActivity(Intent.createChooser(emailIntent, "Send Email..."));*/
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


    public void savePdf() {
        Snackbar.make(parentLayout, "Download Completed.!", Snackbar.LENGTH_SHORT).show();
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/JobPool/Resume/" + "Name" + Global.candidatelist.get(position).getName() + timeStamp + ".pdf";
        // Create New Blank Document
        Document document = new Document(PageSize.A4);
        // Create Directory in External Storage
        String root = Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/JobPool/Resume");
        myDir.mkdirs();
        // Create Pdf Writer for Writting into New Created Document
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            // Open Document for Writting into document
            document.open();
            // User Define Method
            addMetaData(document);
            addTitlePage(document);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Close Document after writting all content
        document.close();

    }

    // Set PDF document Properties
    public void addMetaData(Document document)

    {
        document.addTitle("RESUME");
        document.addSubject("Person Info");
        document.addKeywords("Personal,	Education, Skills");
        document.addAuthor("TAG");
        document.addCreator("TAG");
    }

    public void addTitlePage(Document document) throws DocumentException {
        // Font Style for Document

        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL);
        Font titleFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 40, Font.BOLD);

        String name = Global.candidatelist.get(position).getName();

        //Font titleFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 50, Font.BOLD);
        Paragraph p = new Paragraph();
        p.add("RESUME –" + "" + name + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n");
        p.setAlignment(Element.ALIGN_CENTER);
        p.setFont(titleFont);

        document.add(p);


        // Start New Paragraph
        Paragraph prHead1 = new Paragraph();
        // Set Font in this Paragraph
        prHead1.setFont(titleFont);

        // Add item into Paragraph
        prHead1.add("RESUME –" + Global.candidatelist.get(position).getName());

        // Create Table into Document with 1 Row
        PdfPTable myTable = new PdfPTable(1);
        // 100.0f mean width of table is same as Document size
        myTable.setWidthPercentage(100.0f);

        // Create New Cell into Table
        PdfPCell myCell = new PdfPCell(new Paragraph(""));
        //myCell.setBorder(Rectangle.BOTTOM);
        String city = "";
        if (Global.candidatelist.get(position).getLocation().equals("null")) {
            city = "";
        } else {
            city = Global.candidatelist.get(position).getLocation();
        }


        myTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        Phrase phrase = new Phrase();
        phrase.add(
                new Chunk("\n" + "City:  ", new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD))
        );
        phrase.add(new Chunk("" + city, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));


        String Skills = " ";
        if (Global.candidatelist.get(position).getSkill().equals("null")) {
            Skills = "";
        } else {
            Skills = Global.candidatelist.get(position).getSkill();
        }
        Phrase phrase1 = new Phrase();

        phrase1.add(
                new Chunk("\n" + "Skills:  ", new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD))
        );
        phrase1.add(new Chunk("" + Skills, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));


        String Experience = "";
        if (Global.candidatelist.get(position).getExperience().equals("null")) {
            Experience = "";
        } else {
            Experience = Global.candidatelist.get(position).getExperience();
        }
        Phrase phrase2 = new Phrase();

        phrase2.add(
                new Chunk("\n" + "Experience: ", new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD))
        );
        phrase2.add(new Chunk("" + Experience, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));


        String Address = "173 Mig colony ,Indore";
        if (Global.candidatelist.get(position).getAddress().toString().equals("null")) {
            Address = "";
        } else {
            Address = Global.candidatelist.get(position).getAddress().toString();
        }

        Phrase phrase3 = new Phrase();
        phrase3.add(
                new Chunk("\n" + "Address: ", new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD))
        );
        phrase3.add(new Chunk("" + Address, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));


        myTable.addCell(phrase);
        myTable.addCell(phrase1);
        myTable.addCell(phrase2);
        myTable.addCell(phrase3);


        document.add(myTable);
        String about_detail = "";
        if (!Global.candidatelist.get(position).getBrief_description().equals("null")) {
            about_detail = Global.candidatelist.get(position).getBrief_description();
        }
        Paragraph prProfile = new Paragraph();
        prProfile.setFont(titleFont);
        prProfile.add("" + "\n" + "\n");
        prProfile.add("\n \n About us : \n ");
        prProfile.setLeading(30.0f);

        prProfile.setFont(normal1);
        prProfile
                .add("\n" + about_detail);

        prProfile.setFont(smallBold);
        document.add(prProfile);

        // Create new Page in PDF
        document.newPage();
    }

    @Override
    public void requestFinished(String[] result) throws Exception {

    }

    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
/*    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/resume.doc");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }*/


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
