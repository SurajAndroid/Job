package com.startupsoch.jobpool;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.startupsoch.jobpool.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import adapter.SkillesAdapter;
import app.Config;
import dtos.SkillesDTO;
import utils.Constant;
import utils.Global;
import utils.MarshMallowPermission;
import utils.NotificationUtils;
import utils.RequestReceiver;
import utils.WebserviceHelper;

public class HomeActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, RequestReceiver {
    //Signing Options
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;
    RequestReceiver receiver;
    MarshMallowPermission marshMallowPermission;
    /*Facebook*/
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    LoginButton login_button;
    SharedPreferences sharedPreferences;
    RelativeLayout parentLayout;
    EditText skillesediTxt, locationEdit;
    Spinner spinner;
    String[] citys = {"Select City", "Bidar", "Delhi", "Kalaburagi", "Hyderabad", "Indore","Coimbatore","Pune","Bengaluru"};
    String [] skilles = {"Android","PHP","Xamarin",".Net","iOS","Ionic","Angular JS","Node JS","ASP .Net","MVC","React Native"};
    SkillesDTO skillesDTO;
    ArrayList<SkillesDTO> list = new ArrayList<>();
    LinearLayout facebookLayout, googleLayout, registerLayout, loginLayout, findContactLayout;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    AutoCompleteTextView lookingJob;
    Spinner spinnerJobroll;
    String UserName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(HomeActivity.this);
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.home_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        showAccessTokens();
        getKeyHash();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.e("Access Token", "Tracker");
            }
        };
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.e("Access Token", "request data");
        }
        accessTokenTracker.startTracking();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
        displayFirebaseRegId();
        init();
        clickListener();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Constant.TOKEN = regId;
        Log.e(TAG, "Firebase reg id: " + regId);
    }


    public void callSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, HomeActivity.this);
        employer.setAction(Constant.GET_FILTER_DATA);
        employer.execute();
    }

    public void GetJobRollSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, HomeActivity.this);
        employer.setAction(Constant.GET_JOB_LIST);
        employer.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    public  void init(){

        Constant.FB_ID ="";
        Constant.GOOGLE_ID = "";
        receiver = this;
        if(Constant.checkInternetConnection(getApplicationContext())){
            callSerivice();
        }else {
            Snackbar.make(parentLayout,"Check your Internet.!",Snackbar.LENGTH_SHORT).show();
        }


//      GetJobRollSerivice();
        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("status","").equals("1")){

            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /*Facebook Button*/
        login_button = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        login_button.setReadPermissions("email");

        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);

        facebookLayout = (LinearLayout)findViewById(R.id.facebookLayout);
        googleLayout = (LinearLayout)findViewById(R.id.googleLayout);
        registerLayout = (LinearLayout)findViewById(R.id.registerLayout);
        loginLayout = (LinearLayout)findViewById(R.id.loginLayout);
        findContactLayout = (LinearLayout)findViewById(R.id.findContactLayout);
        marshMallowPermission = new MarshMallowPermission(HomeActivity.this);
        parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);
        skillesediTxt = (EditText)findViewById(R.id.skillesediTxt);
        locationEdit  = (EditText)findViewById(R.id.locationEdit);
        lookingJob = (AutoCompleteTextView)findViewById(R.id.lookingJob) ;
        spinner = (Spinner)findViewById(R.id.spinner);

        spinnerJobroll = (Spinner)findViewById(R.id.spinnerJobroll);


        for(int i=0;i<skilles.length;i++){
            skillesDTO = new SkillesDTO( skilles[i],false);
            list.add(skillesDTO);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,R.layout.spinner_txt,skilles);
        lookingJob.setAdapter(adapter);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_txt, citys);
        spinner.setAdapter(dataAdapter);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
                if (!marshMallowPermission.checkPermissionForLocation()) {
                    marshMallowPermission.requestPermissionForLocation();
                }
            }
        }
    }



    private void showAccessTokens() {
        Log.e("showAccessToken", "accessToken");
        AccessToken access_token = AccessToken.getCurrentAccessToken();
    }

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Constant.GOOGLE_ID = acct.getId();
            Constant.EMAIL = acct.getEmail();
            Constant.USER_NAME = acct.getDisplayName();
            UserName = acct.getDisplayName();
            try {
                Constant.USER_IMAGE = acct.getPhotoUrl().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Login faild ", Toast.LENGTH_LONG).show();
        }
    }

     private void signIn() {
             Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
             startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Constant.PASSWORD ="";
            logincallSerivice();
            Constant.FB_ID = "";
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            Log.e("onActivityResult", "data" + requestCode);
            RequestData();

        }
    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("request data", "oncomplete" + response);
                JSONObject json = response.getJSONObject();
                System.out.println("Json data :" + json);
                try {
                    if (json != null) {

                        Log.e("","Email : "+json.getString("email"));
                        try {
                            Constant.USER_NAME = json.getString("name");
                            UserName = json.getString("name");
                            Constant.EMAIL = json.getString("email");
                        }catch (Exception e){
                             Constant.EMAIL = json.getString("name")+"@gmail.com";
                        }
//                        String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> ";
//                        Toast.makeText(getApplicationContext(),"User Name :"+ json.getString("name")+"\nEmail :"+json.getString("email") ,Toast.LENGTH_LONG).show();
                        Constant.PASSWORD ="";
                        logincallSerivice();
                        Constant.GOOGLE_ID = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        //parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    protected void onStop() {
        super.onStop();
        Log.e("onstop", "accesstokentracker");
        //Facebook login
        accessTokenTracker.stopTracking();
    }

    public void showDialogPopup(){
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);
        LinearLayout candiadte = (LinearLayout) dialog.findViewById(R.id.candidate_registration);
        LinearLayout employer = (LinearLayout) dialog.findViewById(R.id.employer_registration);

        candiadte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CondidateRegisterActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        employer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EmployerActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public void clickListener(){

        spinnerJobroll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.SKILLES = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(),""+  Constant.SKILLES ,Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

       /* lookingJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constant.SKILLES = adapterView.getItemAtPosition(i).toString();
            }
        });*/

        skillesediTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog1 = new Dialog(HomeActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.skilles_dialog);
                dialog1.show();
                ListView myListView = (ListView)dialog1.findViewById(R.id.myListView);
                LinearLayout SubmiTLayout= (LinearLayout)dialog1.findViewById(R.id.SubmiTLayout);
                final EditText editText2 = (EditText)dialog1.findViewById(R.id.editText2);
                final SkillesAdapter skillesAdapter  = new SkillesAdapter(HomeActivity.this,list);
                myListView.setAdapter(skillesAdapter);

                Constant.setListViewHeightBasedOnChildren(myListView);


                editText2.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                        // TODO Auto-generated method stub
                        skillesAdapter.getFilter().filter(arg0);
                    }
                });

                SubmiTLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder builder = new StringBuilder();
                        dialog1.dismiss();
//                        Toast.makeText(getApplicationContext(),"Liat Size "+ Global.skilleslist.size(),Toast.LENGTH_SHORT).show();
                        for(int i=0;i<Global.skilleslist.size();i++){
                            builder.append(Global.skilleslist.get(i));
                            if(i!=Global.skilleslist.size()-1){
                                builder.append(", ");
                            }
                        }
                        skillesediTxt.setText(builder.toString());
                    }
                });

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.LOCATION = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        findContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//              InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//              inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if(!Constant.SKILLES.equals("Select JobRoll")){
                    if(!Constant.LOCATION.equals("Select City")){
                        if(sharedPreferences.getString("user_type","").equals("candidate")){
                            Toast.makeText(getApplicationContext(),"Company Searching Comming Soon.", Toast.LENGTH_LONG).show();
                        }else {

                            if(Constant.checkInternetConnection(getApplicationContext())){
                                searchApiSerivice();
                            }else {
                                Snackbar.make(parentLayout,"Check your Internet.!",Snackbar.LENGTH_SHORT).show();
                            }

                        }
                    }else {
                        Snackbar.make(parentLayout,"Please select location",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentLayout,"Please select Jobroll",Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("registercallback", "onSuccess" + loginResult);
                Log.e("loginResult", "else" + loginResult);

                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.e("AccessToken", "onSuccessif" + loginResult);
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        /*get all profile data*/
                        Constant.FB_ID = profile.getId();
                        Constant.USER_NAME = profile.getFirstName()+" "+profile.getLastName();
                        UserName = profile.getFirstName()+" "+profile.getLastName();

                        try {
                            Constant.USER_IMAGE = profile.getProfilePictureUri(200,200).toString();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }

        });

        facebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_button.performClick();
            }
        });

        googleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(in);
            }
        });


        registerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPopup();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

/*    public void socialcallSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, HomeActivity.this);
        employer.setAction(Constant.SOCIALLOGIN);
        employer.execute();
    }*/

    public void logincallSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, HomeActivity.this);
        employer.setAction(Constant.LOGIN);
        employer.execute();
    }

    public void searchApiSerivice() {
        WebserviceHelper searchAPI = new WebserviceHelper(receiver, HomeActivity.this);
        searchAPI.setAction(Constant.CANDIDATE_SEARCH_API);
        searchAPI.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
           }
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if(result[0].equals("1")){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "1");
            editor.putString("user_id", "" + Constant.USER_ID);
            editor.putString("email", "" + Constant.EMAIL);
            if(Constant.GOOGLE_ID.equals("") && Constant.FB_ID.equals("") ){
                editor.putString("user_name", "" + Constant.USER_NAME);
            }else {
                editor.putString("user_name", "" + UserName);
            }

            editor.putString("phone", "" + Constant.PHONE_NUMBER);
            editor.putString("location", "" + Constant.LOCATION);
            editor.putString("user_Image", "" + Constant.USER_IMAGE);
            editor.putString("user_type", "" + "candidate");
            Log.e("",""+ Constant.EMAIL+"  "+ Constant.USER_IMAGE);
            editor.commit();

            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }if(result[0].equals("0001")){
            Intent intent = new Intent(HomeActivity.this, AllListActivity.class);
            startActivity(intent);
        }else if(result[0].equals("01")){
            GetJobRollSerivice();
        }else if(result[0].equals("701")) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_txt, Global.jobroll_List);
            spinnerJobroll.setAdapter(dataAdapter);
        }else {
            Snackbar.make(parentLayout,""+result[1],Snackbar.LENGTH_SHORT).show();
        }
    }
}