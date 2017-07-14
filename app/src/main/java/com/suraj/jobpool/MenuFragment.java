package com.suraj.jobpool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.Constant;
import utils.MarshMallowPermission;
import utils.RequestReceiver;
import utils.WebserviceHelper;

import static android.app.Activity.RESULT_OK;

/**
 * Created by SURAJ SHAKYA.
 * shakyasuraj08@gmail.com
 */

public class MenuFragment extends Fragment implements RequestReceiver {

    private View rootView;
    LinearLayout profileLayout, searchLayout, notificationLayout, aboutusLayout,
            change_passwordLayout, privacyPolicyLayout, termsLayout, logoutLayout, paymentLayout;
    TextView searchCandidate, userTxt,profileViewTxt,resumeDownloadTxt;
    static TextView userNameTxt;
    RequestReceiver receiver;
    static SharedPreferences sharedPreferences;
    SearchActivity searchActivity;
    static ImageView userImage;
    ScrollView parentLayout;
    MarshMallowPermission marshMallowPermission;

    public static Fragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sliding_menu_items, container, false);
        sharedPreferences = getActivity().getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        receiver = this;
        marshMallowPermission = new MarshMallowPermission(getActivity());
        try{
            getActivity().getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }catch (Exception e){
            e.printStackTrace();
        }

        parentLayout = (ScrollView)rootView.findViewById(R.id.parentLayout);
        paymentLayout = (LinearLayout)rootView.findViewById(R.id.paymentLayout);
        profileLayout = (LinearLayout)rootView.findViewById(R.id.profileLayout);
        searchLayout = (LinearLayout)rootView.findViewById(R.id.searchLayout);
        notificationLayout = (LinearLayout)rootView.findViewById(R.id.notificationLayout);
        aboutusLayout = (LinearLayout)rootView.findViewById(R.id.aboutusLayout);
        change_passwordLayout = (LinearLayout)rootView.findViewById(R.id.change_passwordLayout);
        privacyPolicyLayout = (LinearLayout)rootView.findViewById(R.id.privacyPolicyLayout);
        termsLayout = (LinearLayout)rootView.findViewById(R.id.termsLayout);
        logoutLayout = (LinearLayout)rootView.findViewById(R.id.logoutLayout);
        userNameTxt = (TextView)rootView.findViewById(R.id.userNameTxt);
        searchCandidate = (TextView)rootView.findViewById(R.id.searchCandidate);
        userTxt = (TextView)rootView.findViewById(R.id.userTxt) ;
        userImage = (ImageView)rootView.findViewById(R.id.userImage);

        profileViewTxt = (TextView)rootView.findViewById(R.id.profileViewTxt);
        resumeDownloadTxt = (TextView)rootView.findViewById(R.id.resumeDownloadTxt);

        searchActivity = new SearchActivity();
        Constant.EMAIL = sharedPreferences.getString("email","");

        if(sharedPreferences.getString("user_type","").equalsIgnoreCase("candidate")){

            profileViewTxt.setText("My Profile Views");
            resumeDownloadTxt.setText("My Resume Download");
            searchCandidate.setText("Search Company");
            userNameTxt.setText(sharedPreferences.getString("user_name",""));
            try {
                Picasso.with(getActivity()).load(sharedPreferences.getString("user_Image","")).placeholder(R.drawable.placeholder).into(userImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            searchCandidate.setText("Search Candidates");
            userTxt.setVisibility(View.GONE);
            userNameTxt.setText(sharedPreferences.getString("company_name",""));
            try {
                Picasso.with(getActivity()).load(sharedPreferences.getString("user_Image","")).placeholder(R.drawable.placeholder).into(userImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

      /*  String str = Build.BRAND;
        Log.e("",""+str);
        if(str.equalsIgnoreCase("motorola")){
            ScrollView.LayoutParams relativeParams = (ScrollView.LayoutParams)parentLayout.getLayoutParams();
            relativeParams.setMargins(0, 0, 0, 40);  // left, top, right, bottom
            parentLayout.setLayoutParams(relativeParams);
        }*/


        return rootView;
    }

    public void candidateSerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, getActivity());
        getprofile.setAction(Constant.UPDATE_CADIDATE_PIC);
        getprofile.execute();
    }

    public void employeeSerivice() {
        WebserviceHelper getprofile = new WebserviceHelper(receiver, getActivity());
        getprofile.setAction(Constant.UPDATE_EMPLOYER_PIC);
        getprofile.execute();
    }

    public static void updateName (Context context){
        if(sharedPreferences.getString("user_type","").equalsIgnoreCase("candidate")){
            userNameTxt.setText(sharedPreferences.getString("user_name",""));
            try {
                Picasso.with(context).load(sharedPreferences.getString("user_Image","")).placeholder(R.drawable.placeholder).into(userImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            userNameTxt.setText(sharedPreferences.getString("company_name",""));
            try {
                Picasso.with(context).load(sharedPreferences.getString("user_Image","")).placeholder(R.drawable.placeholder).into(userImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clicklistener();
    }

    public void logoutcallSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, getActivity());
        employer.setAction(Constant.LOGOUT);
        employer.execute();
    }

    public void changecallSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, getActivity());
        employer.setAction(Constant.CHANGEPASSWORD);
        employer.execute();
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


    public void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String myfilePath = picturePath;
            File imgFile = new File(myfilePath);

            final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            userImage.setImageBitmap(myBitmap);
            Constant.USER_IMAGE = getRealPathFromUri(getActivity(),selectedImage);


            final Dialog dialog = new Dialog(getActivity());
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
                Uri tempUri = getImageUri(getActivity(), thumbnail);
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

                userImage.setImageBitmap(thumbnail);
                Constant.USER_IMAGE = getRealPathFromUri(getActivity(),tempUri);
                final Dialog dialog = new Dialog(getActivity());
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

    private void clicklistener(){

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForCameraPermission();
            }
        });


        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getString("user_type","").equalsIgnoreCase("candidate")){
                    Intent intent = new Intent(getActivity(),CandidatepackageActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(),SelectPackageActivity.class);
                    startActivity(intent);
                }

            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getSimpleName().equals("SearchActivity")){
                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        termsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getSimpleName().equals("TermsCondtionActivity")){
                    Intent intent = new Intent(getActivity(),TermsCondtionActivity.class);
                    startActivity(intent);
                }

            }
        });

        privacyPolicyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getSimpleName().equals("PrivacyPolicyActivity")){
                    Intent intent = new Intent(getActivity(),PrivacyPolicyActivity.class);
                    startActivity(intent);
                }

            }
        });

        aboutusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getSimpleName().equals("AboutUsActivity")){
                    Intent intent = new Intent(getActivity(),AboutUsActivity.class);
                    startActivity(intent);
                }

            }
        });

        change_passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.change_password);
                LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
                final EditText old_passwordEdit = (EditText)dialog.findViewById(R.id.old_passwordEdit);
                old_passwordEdit.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                final EditText new_passwordEdit = (EditText)dialog.findViewById(R.id.new_passwordEdit);
                new_passwordEdit.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                final EditText confirm_passwordEdit = (EditText)dialog.findViewById(R.id.confirm_passwordEdit);
                confirm_passwordEdit.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                submitLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(old_passwordEdit.getText().length()!=0){
                            if(new_passwordEdit.getText().length()!=0){
                                if(new_passwordEdit.getText().toString().equals(confirm_passwordEdit.getText().toString())) {
                                    Constant.OLDPASSWORD = old_passwordEdit.getText().toString();
                                    Constant.PASSWORD = new_passwordEdit.getText().toString();
                                    changecallSerivice();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(getActivity(),"Check confirm password.!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(),"Enter new password.!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),"Enter old password.!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if(!getActivity().getClass().getSimpleName().equals("EditProfileActivity")){
                    Intent intent = new Intent(getActivity(),EditProfileActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }*/

                if(sharedPreferences.getString("user_type","").equals("candidate")){
                    Intent intent = new Intent(getActivity(), EditCandidateActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), EditemployeActivity.class);
                    startActivity(intent);
                }

            }
        });

        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getSimpleName().equals("NotificationActivity")){
                    Intent intent = new Intent(getActivity(),NotificationActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.EMAIL = sharedPreferences.getString("email","");
                logoutcallSerivice();
            }
        });
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if(result[0].equals("1") || result[0].equals("0")){
            sharedPreferences =  getActivity().getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            try {
                LoginManager.getInstance().logOut();
            }catch (Exception e){
                e.printStackTrace();
            }

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
//          Toast.makeText(getActivity(),""+result[1], Toast.LENGTH_SHORT).show();
        }else if(result[0].equals("101")){
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertpopup);
            TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
            massageTxtView.setText(result[1]);
            LinearLayout submitLayout = (LinearLayout)dialog.findViewById(R.id.submitLayout);
            submitLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}