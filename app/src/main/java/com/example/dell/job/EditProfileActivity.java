package com.example.dell.job;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.Constant;
import utils.Global;
import utils.MarshMallowPermission;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by Suraj Shakya on 5/28/2017.
 */

public class EditProfileActivity extends SlidingFragmentActivity implements RequestReceiver{

    RequestReceiver receiver;
    SlidingMenu sm;
    LinearLayout slidMenuLayout;
    RelativeLayout parentLayout;
    TextView editProfileTxtView;

    static ImageView UserProfileImage;
    static  TextView nameTxt,cityTxt, skillesTxt, experiencetTxt, addressTxt, emailTxt, contactTxt, aboutUsTxt;

    LinearLayout bottomLayout, expLayout, skillesLayout;
    static  SharedPreferences sharedPreferences;
    MarshMallowPermission marshMallowPermission;
    MenuFragment menuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setBehindView();
        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setSlidingEnabled(false);

        init();
        clickListener();

    }

    public void init(){

        try{
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }catch (Exception e){
            e.printStackTrace();
        }

        menuFragment = new MenuFragment();
        receiver = this;
        marshMallowPermission = new MarshMallowPermission(EditProfileActivity.this);
        sharedPreferences = getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        editProfileTxtView = (TextView)findViewById(R.id.editProfileTxtView);
        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);

        expLayout = (LinearLayout)findViewById(R.id. expLayout);
        skillesLayout = (LinearLayout)findViewById(R.id. skillesLayout);

        UserProfileImage = (ImageView)findViewById(R.id.UserProfileImage);
        nameTxt = (TextView)findViewById(R.id.nameTxt);
        cityTxt = (TextView)findViewById(R.id.cityTxt);
        skillesTxt = (TextView)findViewById(R.id.skillesTxt);
        experiencetTxt = (TextView)findViewById(R.id.experiencetTxt);
        addressTxt = (TextView)findViewById(R.id.addressTxt);
        emailTxt = (TextView)findViewById(R.id.emailTxt);
        contactTxt = (TextView)findViewById(R.id.contactTxt);
        aboutUsTxt = (TextView)findViewById(R.id.aboutUsTxt);

        bottomLayout = (LinearLayout)findViewById(R.id.bottomLayout);
        Constant.EMAIL = sharedPreferences.getString("email","");


 /*       String str = Build.BRAND;
        Log.e("",""+str);

        if(str.equalsIgnoreCase("motorola")){
            LinearLayout.LayoutParams relativeParams = (LinearLayout.LayoutParams)bottomLayout.getLayoutParams();
            relativeParams.setMargins(0, 0, 0, 40);  // left, top, right, bottom
            bottomLayout.setLayoutParams(relativeParams);
        }*/

        if(sharedPreferences.getString("user_type","").equals("candidate")){
            getCandidateSerivice();
        }else {
            getCompanySerivice();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

            super.onBackPressed();
            Intent intent = new Intent(EditProfileActivity.this, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

    }

    public void getCandidateSerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditProfileActivity.this);
        getprofile.setAction(Constant.GET_CANDIDATE_PROFILE);
        getprofile.execute();
    }

    public void getCompanySerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditProfileActivity.this);
        getprofile.setAction(Constant.GET_COMPANY_PROFILE);
        getprofile.execute();
    }

    public void candidateSerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditProfileActivity.this);
        getprofile.setAction(Constant.UPDATE_CADIDATE_PIC);
        getprofile.execute();
    }

    public void employeeSerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, EditProfileActivity.this);
        getprofile.setAction(Constant.UPDATE_EMPLOYER_PIC);
        getprofile.execute();
    }

    public void setcompanyData(Context context){

        try {
            try{
                expLayout.setVisibility(View.GONE);
                skillesLayout.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                Picasso.with(context).load(sharedPreferences.getString("user_Image","")).placeholder(R.drawable.placeholder).into(UserProfileImage);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(!Global.companylist.get(0).getCompany_name().equals("null")){
                nameTxt.setText(Global.companylist.get(0).getCompany_name());
            }else {
                nameTxt.setText("");
            }
            if(!Global.companylist.get(0).getLocation().equals("null")){
                cityTxt.setText(Global.companylist.get(0).getLocation());
            }else {
                cityTxt.setText("");
            }
            if(!Global.companylist.get(0).getEmail().equals("null")){
                emailTxt.setText(Global.companylist.get(0).getEmail());
            }else {
                emailTxt.setText("");
            }
            if(!Global.companylist.get(0).getPhone().equals("null")){
                contactTxt.setText("+91 "+Global.companylist.get(0).getPhone());
            }else {
                contactTxt.setText("+91 ");
            }
            if(!Global.companylist.get(0).getAddress().equals("null") ){
                addressTxt.setText(Global.companylist.get(0).getAddress());
            }else {
                addressTxt.setText("");
            }
            aboutUsTxt.setText("");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void setcandidateData(Context  context){

        try{

            try {
                Picasso.with(context).load(sharedPreferences.getString("user_Image","")).placeholder(R.drawable.placeholder).into(UserProfileImage);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(!Global.candidatelist.get(0).getName().equals("null")){
                nameTxt.setText(Global.candidatelist.get(0).getName());
            }else {
                nameTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getLocation().equals("null")){
                cityTxt.setText(Global.candidatelist.get(0).getLocation());
            }else {
                cityTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getExperience().equals("null") ){
                experiencetTxt.setText(Global.candidatelist.get(0).getExperience());
            }else {
                experiencetTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getAddress().equals("null")){
                addressTxt.setText(Global.candidatelist.get(0).getAddress());
            }else {
                addressTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getEmail().equals("null")){
                emailTxt.setText(Global.candidatelist.get(0).getEmail());
            }else {
                emailTxt.setText("");
            }
            if(!Global.candidatelist.get(0).getPhone().equals("null")){
                contactTxt.setText("+91 "+Global.candidatelist.get(0).getPhone());
            }else {
                contactTxt.setText("+91 "+"");
            }
            if(!Global.candidatelist.get(0).getSkill().equals("null")){
                skillesTxt.setText(Global.candidatelist.get(0).getSkill());
            }else {
                skillesTxt.setText("");
            }

            aboutUsTxt.setText("");

        }catch ( Exception e){
            e.printStackTrace();
        }

    }

    public void clickListener(){


        UserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForCameraPermission();
            }
        });

        editProfileTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getString("user_type","").equals("candidate")){
                    Intent intent = new Intent(EditProfileActivity.this, EditCandidateActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(EditProfileActivity.this, EditemployeActivity.class);
                    startActivity(intent);
                }
            }
        });

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.toggle();
            }
        });
    }

    private void setBehindView() {
        setBehindContentView(R.layout.menu_slide);
        //transaction fragment to sliding menu
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
            if(result[0].equals("01")){

                setcompanyData(EditProfileActivity.this);
            }else if(result[0].equals("001")){
                setcandidateData(EditProfileActivity.this);

            }else if(result[0].equals("101")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_Image", "" + Constant.USER_IMAGE);
                editor.commit();

                if(sharedPreferences.getString("user_type","").equals("candidate")){
                    MenuFragment.updateName(EditProfileActivity.this);
//                    Toast.makeText(getApplicationContext(),""+sharedPreferences.getString("user_Image",""),Toast.LENGTH_SHORT).show();
                    Log.e("",""+sharedPreferences.getString("user_Image",""));
                    try {
                        Picasso.with(getApplicationContext()).load(Constant.USER_IMAGE).placeholder(R.drawable.placeholder).into(UserProfileImage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    MenuFragment.updateName(EditProfileActivity.this);
//                    Toast.makeText(getApplicationContext(),""+sharedPreferences.getString("user_Image",""),Toast.LENGTH_SHORT).show();
                    Log.e("",""+sharedPreferences.getString("user_Image",""));
                    try {
                        Picasso.with(getApplicationContext()).load(Constant.USER_IMAGE).placeholder(R.drawable.placeholder).into(UserProfileImage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
    }


    public void showDialog() {

        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.camera_popup_layut);
        dialog.show();

        TextView cancelTxt = (TextView) dialog.findViewById(R.id.cancelTxt);
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView takePhotoTxt = (TextView) dialog.findViewById(R.id.takePhotoTxt);
        takePhotoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
                dialog.dismiss();
            }
        });

        TextView galleryTxt = (TextView) dialog.findViewById(R.id.galleryTxt);
        galleryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String myfilePath = picturePath;
            File imgFile = new File(myfilePath);

            final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            UserProfileImage.setImageBitmap(myBitmap);
            Constant.USER_IMAGE = getRealPathFromUri(EditProfileActivity.this,selectedImage);


            final Dialog dialog = new Dialog(EditProfileActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.upload_images);
            dialog.show();

            TextView okayTxt = (TextView)dialog.findViewById(R.id.okayTxt);
            TextView cancelTxt = (TextView) dialog.findViewById(R.id.cancelTxt);
            cancelTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            okayTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if(sharedPreferences.getString("user_type","").equals("candidate")){
                        candidateSerivice();
                    }else {
                        employeeSerivice();
                    }
                }
            });
       } else if (requestCode == 1) try {
            if (data != null) {

                final Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UserProfileImage.setImageBitmap(thumbnail);
                Constant.USER_IMAGE = getRealPathFromUri(EditProfileActivity.this,tempUri);
                final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.upload_images);
                dialog.show();

                TextView okayTxt = (TextView)dialog.findViewById(R.id.okayTxt);
                TextView cancelTxt = (TextView) dialog.findViewById(R.id.cancelTxt);
                cancelTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                okayTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        if(sharedPreferences.getString("user_type","").equals("candidate")){
                            candidateSerivice();
                        }else {
                            employeeSerivice();
                        }
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getImageString(String imagepath) {

        Bitmap bm = BitmapFactory.decodeFile(imagepath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();

        String ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);
        Log.e("","Image path : "+ba1);
        return ba1;
    }

    public String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void askForCameraPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            } else {
                showDialog();
            }
        } else {
            showDialog();
        }
    }
}
