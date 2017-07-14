package com.example.dell.job;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.Global;

/**
 * Created by Suraj Shakya on 5/14/2017.
 */

public class ProfileActivity extends SlidingFragmentActivity {

    LinearLayout slidMenuLayout;
    TextView downloadTxtView, contactTxtView;
    SlidingMenu sm;
    RelativeLayout parentLayout;
    File myDir;
    ImageView ProfileImage;
    TextView cityTxt, skillesTxt, experiencetTxt, addressTxt, aboutUsTxt,candidateName;
    int position;
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

    public void init(){

        String data = getIntent().getExtras().getString("position","");
        position = Integer.parseInt(data);

        try{
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }catch (Exception e){
            e.printStackTrace();
        }

        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        downloadTxtView = (TextView)findViewById(R.id.downloadTxtView);
        contactTxtView = (TextView)findViewById(R.id.contactTxtView);
        candidateName = (TextView)findViewById(R.id.candidateName);

        cityTxt = (TextView)findViewById(R.id.cityTxt);
        skillesTxt  = (TextView)findViewById(R.id.skillesTxt);
        experiencetTxt  = (TextView)findViewById(R.id.experiencetTxt);
        addressTxt  = (TextView)findViewById(R.id.addressTxt);
        aboutUsTxt  = (TextView)findViewById(R.id.aboutUsTxt);
        ProfileImage = (ImageView)findViewById(R.id.ProfileImage);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        setProfielData();

    }

    public void setProfielData(){


        candidateName.setText(Global.candidatelist.get(position).getName());
        if(!Global.candidatelist.get(position).getLocation().equals("null")){
            cityTxt.setText(Global.candidatelist.get(position).getLocation());
        }else {
            cityTxt.setText("");
        }
        if(!Global.candidatelist.get(position).getSkill().equals("null")){
            skillesTxt.setText(Global.candidatelist.get(position).getSkill());
        }else {
            skillesTxt.setText("");
        }

        if(!Global.candidatelist.get(position).getExperience().equals("null")){
            experiencetTxt.setText(Global.candidatelist.get(position).getExperience());
        }else {
            experiencetTxt.setText("");
        }

        if(Global.candidatelist.get(position).getAddress().equals("null")){
            addressTxt.setText("");
        }else {
            addressTxt.setText(Global.candidatelist.get(position).getAddress());
        }

        if(!Global.candidatelist.get(position).getBrief_description().equals("null")){
            aboutUsTxt.setText(Global.candidatelist.get(position).getBrief_description());
        }else {
            aboutUsTxt.setText("");
        }

        try{
            Picasso.with(getApplicationContext()).load(Global.candidatelist.get(position).getUserImage())
                    .placeholder(R.drawable.placeholder).into(ProfileImage);
        }catch (Exception e){

        }
    }

    public void clickListenr(){

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
                savePdf();
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
                TextView mailTxt = (TextView)dialog.findViewById(R.id.mailTxt);
                TextView messageTxt = (TextView)dialog.findViewById(R.id.messageTxt);

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
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{Global.candidatelist.get(0).getEmail().toString()});
                        i.putExtra(Intent.EXTRA_SUBJECT, "");
                        i.putExtra(Intent.EXTRA_TEXT   , "");
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


    public void savePdf(){
        Snackbar.make(parentLayout,"Download Completed.!",Snackbar.LENGTH_SHORT).show();
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/JobPool/Resume/" + "Name"+ Global.candidatelist.get(position).getName() + timeStamp + ".pdf";
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

        String name=Global.candidatelist.get(position).getName();

        //Font titleFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 50, Font.BOLD);
        Paragraph p = new Paragraph();
        p.add("RESUME –"+""+name+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n");
        p.setAlignment(Element.ALIGN_CENTER);
        p.setFont(titleFont);

        document.add(p);


        // Start New Paragraph
        Paragraph prHead1 = new Paragraph();
        // Set Font in this Paragraph
        prHead1.setFont(titleFont);

        // Add item into Paragraph
        prHead1.add("RESUME –"+Global.candidatelist.get(position).getName());

        // Create Table into Document with 1 Row
        PdfPTable myTable = new PdfPTable(1);
        // 100.0f mean width of table is same as Document size
        myTable.setWidthPercentage(100.0f);

        // Create New Cell into Table
        PdfPCell myCell = new PdfPCell(new Paragraph(""));
        //myCell.setBorder(Rectangle.BOTTOM);
        String city="";
        if(Global.candidatelist.get(position).getLocation().equals("null")){
            city = "";
        }else {
             city=Global.candidatelist.get(position).getLocation();
        }


        myTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        Phrase phrase = new Phrase();
        phrase.add(
                new Chunk("\n"+"City:  ",  new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD))
        );
        phrase.add(new Chunk(""+city, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));


        String Skills=" ";
        if(Global.candidatelist.get(position).getSkill().equals("null")){
            Skills = "";
        }else {
            Skills =Global.candidatelist.get(position).getSkill();
        }
        Phrase phrase1 = new Phrase();

        phrase1.add(
                new Chunk("\n"+"Skills:  ",  new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD))
        );
        phrase1.add(new Chunk(""+Skills, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));


        String Experience="";
        if(Global.candidatelist.get(position).getExperience().equals("null")){
            Experience =   "";
        }else {
            Experience = Global.candidatelist.get(position).getExperience();
        }
        Phrase phrase2 = new Phrase();

        phrase2.add(
                new Chunk("\n"+"Experience: ",  new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD))
        );
        phrase2.add(new Chunk(""+Experience, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));


        String Address="173 Mig colony ,Indore";
        if(Global.candidatelist.get(position).getAddress().toString().equals("null")){
            Address ="";
        }else {
            Address=Global.candidatelist.get(position).getAddress().toString();
        }

        Phrase phrase3 = new Phrase();
        phrase3.add(
                new Chunk("\n"+"Address: ",  new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD))
        );
        phrase3.add(new Chunk(""+Address, new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL)));



        myTable.addCell(phrase);
        myTable.addCell(phrase1);
        myTable.addCell(phrase2);
        myTable.addCell(phrase3);


        document.add(myTable);
        String about_detail="";
        if(!Global.candidatelist.get(position).getBrief_description().equals("null")){
           about_detail = Global.candidatelist.get(position).getBrief_description();
        }
        Paragraph prProfile = new Paragraph();
        prProfile.setFont(titleFont);
        prProfile.add(""+"\n"+"\n");
        prProfile.add("\n \n About us : \n ");
        prProfile.setLeading(30.0f);

        prProfile.setFont(normal1);
        prProfile
                .add("\n"+about_detail);

        prProfile.setFont(smallBold);
        document.add(prProfile);

        // Create new Page in PDF
        document.newPage();
    }

}
