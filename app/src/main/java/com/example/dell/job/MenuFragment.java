package com.example.dell.job;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.TagLostException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import utils.Constant;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by SURAJ SHAKYA.
 * shakyasuraj08@gmail.com
 */

public class MenuFragment extends Fragment implements RequestReceiver{

    private View rootView;
    LinearLayout profileLayout, searchLayout, notificationLayout, aboutusLayout,
            change_passwordLayout, privacyPolicyLayout, termsLayout, logoutLayout, paymentLayout;
    TextView  searchCandidate, userTxt;
    static TextView userNameTxt;
    RequestReceiver receiver;
    static  SharedPreferences sharedPreferences;
    SearchActivity searchActivity;
    static ImageView userImage;
    ScrollView parentLayout;

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

        searchActivity = new SearchActivity();
        Constant.EMAIL = sharedPreferences.getString("email","");

        if(sharedPreferences.getString("user_type","").equalsIgnoreCase("candidate")){
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
        }
*/
        return rootView;
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

    private void clicklistener(){

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
                final EditText new_passwordEdit = (EditText)dialog.findViewById(R.id.new_passwordEdit);
                final EditText confirm_passwordEdit = (EditText)dialog.findViewById(R.id.confirm_passwordEdit);

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
                                    Toast.makeText(getActivity(),"Check confirm password.!",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(),"Enter new password.!",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),"Enter old password.!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getSimpleName().equals("EditProfileActivity")){
                    Intent intent = new Intent(getActivity(),EditProfileActivity.class);
                    startActivity(intent);
                    getActivity().finish();
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
            sharedPreferences =  getActivity().getSharedPreferences("loginstatus",Context.MODE_PRIVATE);
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