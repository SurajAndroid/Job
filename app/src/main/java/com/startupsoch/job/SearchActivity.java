package com.startupsoch.job;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;

import adapter.CandidateSearchAdapter;
import adapter.EmployeeSearchAdapter;
import adapter.SkillesAdapter;
import dtos.SkillesDTO;
import utils.Constant;
import utils.Global;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by chauhan on 5/13/2017.
 */

public class SearchActivity extends SlidingFragmentActivity implements RequestReceiver {

    ListView searchListView;
    EditText skillesediTxt, locationEdit;
    LinearLayout searchLayout, slidMenuLayout;
    EmployeeSearchAdapter adapter;
    public static SlidingMenu sm;
    RelativeLayout layout;
    RelativeLayout filterLayout, parentLayout;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences;
    RequestReceiver receiver;
    EmployeeSearchAdapter employeeSearchAdapter;
    CandidateSearchAdapter candidateSearchAdapter;
    Spinner spinner, spinnerJobroll;
    String TAG;
    AutoCompleteTextView lookingJob;
    String[] citys = {"Select City", "Bidar", "Delhi", "Kalaburagi", "Hyderabad", "Indore","Coimbatore","Pune","Bengaluru"};
    String[] skilles = {"Android", "PHP", "Xamarin", ".Net", "iOS", "Ionic", "Angular JS", "Node JS", "ASP .Net", "MVC", "React Native"};
    SkillesDTO skillesDTO;
    ArrayList<SkillesDTO> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        init();
//      showPopup();
        clickListener();

        for (int i = 0; i < skilles.length; i++) {
            skillesDTO = new SkillesDTO(skilles[i], false);
            list.add(skillesDTO);
        }

        setBehindView();
        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setSlidingEnabled(true);
    }

    public void init() {

        TAG = SearchActivity.class.getSimpleName();
        try {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sharedPreferences = this.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        receiver = this;
        layout = (RelativeLayout) findViewById(R.id.layout);


        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        slidMenuLayout = (LinearLayout) findViewById(R.id.slidMenuLayout);
        skillesediTxt = (EditText) findViewById(R.id.skillesediTxt);
        locationEdit = (EditText) findViewById(R.id.locationEdit);
        searchListView = (ListView) findViewById(R.id.searchListView);

        filterLayout = (RelativeLayout) findViewById(R.id.filterLayout);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        lookingJob = (AutoCompleteTextView) findViewById(R.id.lookingJob);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerJobroll = (Spinner) findViewById(R.id.spinnerJobroll);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, skilles);
        lookingJob.setAdapter(adapter);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_txt, citys);
        spinner.setAdapter(dataAdapter);
        Constant.EMAIL = sharedPreferences.getString("email", "");
        if (sharedPreferences.getString("user_type", "").equals("candidate")) {
            getEmployeeTopTenSerivice();
        } else {

            getCandidatetopTenSerivice();
        }

    }


    public static void closeMenu() {
        sm.toggle();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Snackbar.make(parentLayout, "Please click BACK again to exit.!", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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

    public void GetJobRollSerivice() {
        WebserviceHelper employer = new WebserviceHelper(receiver, SearchActivity.this);
        employer.setAction(Constant.GET_JOB_LIST);
        employer.execute();
    }

    public void clickListener() {

        lookingJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constant.SKILLES = adapterView.getItemAtPosition(i).toString();
            }
        });

        skillesediTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(SearchActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.skilles_dialog);
                dialog1.show();
                ListView myListView = (ListView) dialog1.findViewById(R.id.myListView);
                LinearLayout SubmiTLayout = (LinearLayout) dialog1.findViewById(R.id.SubmiTLayout);
                final EditText editText2 = (EditText) dialog1.findViewById(R.id.editText2);
                final SkillesAdapter skillesAdapter = new SkillesAdapter(SearchActivity.this, list);
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
                        for (int i = 0; i < Global.skilleslist.size(); i++) {
                            builder.append(Global.skilleslist.get(i));
                            if (i != Global.skilleslist.size() - 1) {
                                builder.append(", ");
                            }
                        }
                        skillesediTxt.setText(builder.toString());
                    }
                });
            }
        });

        spinnerJobroll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.SKILLES = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constant.LOCATION = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, FilterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.toggle();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
                if (!Constant.SKILLES.equals("Select JobRoll")) {
                    if (!Constant.LOCATION.equalsIgnoreCase("Location")) {
                        if (sharedPreferences.getString("user_type", "").equals("candidate")) {
                            companySearchApiSerivice();
                        } else {
                            candidateSearchApiSerivice();
                        }
                    } else {
                        Snackbar.make(parentLayout, "Please select location", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(parentLayout, "Please select  Job Role", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getCandidatetopTenSerivice() {
        WebserviceHelper candidateTop = new WebserviceHelper(receiver, SearchActivity.this);
        candidateTop.setAction(Constant.CADIDATE_TOP_TEN);
        candidateTop.execute();
    }

    public void getEmployeeTopTenSerivice() {
        WebserviceHelper employeeTop = new WebserviceHelper(receiver, SearchActivity.this);
        employeeTop.setAction(Constant.EMPLOYEETOP_TEN);
        employeeTop.execute();
    }

    public void candidateSearchApiSerivice(){
        WebserviceHelper searchAPI = new WebserviceHelper(receiver, SearchActivity.this);
        searchAPI.setAction(Constant.CANDIDATE_SEARCH_API);
        searchAPI.execute();
    }

    public void companySearchApiSerivice() {
        WebserviceHelper Companysearch = new WebserviceHelper(receiver, SearchActivity.this);
        Companysearch.setAction(Constant.COMPANY_SEARCH_API);
        Companysearch.execute();
    }

    public void showPopup() {
        final Dialog dialog = new Dialog(SearchActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.membership_renavel_popup);
        dialog.show();
        LinearLayout cancelLayout = (LinearLayout) dialog.findViewById(R.id.cancelLayout);
        LinearLayout okayLayout = (LinearLayout) dialog.findViewById(R.id.okayLayout);
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        okayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, SelectPackageActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void requestFinished(String[] result) throws Exception {

        if (result[0].equals("01")) {
            if (sharedPreferences.getString("user_type", "").equals("candidate")) {
                employeeSearchAdapter = new EmployeeSearchAdapter(SearchActivity.this, SearchActivity.this, Global.companylist, receiver);
                searchListView.setAdapter(employeeSearchAdapter);
                GetJobRollSerivice();
            } else {
                candidateSearchAdapter = new CandidateSearchAdapter(SearchActivity.this, SearchActivity.this, Global.candidatelist, TAG);
                searchListView.setAdapter(candidateSearchAdapter);
                GetJobRollSerivice();
            }
        } else if (result[0].equals("0001")) {
            Intent intent = new Intent(SearchActivity.this, AllListActivity.class);
            startActivity(intent);
        } else if (result[0].equals("701")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, R.layout.spinner_txt, Global.jobroll_List);
            spinnerJobroll.setAdapter(adapter);
        }else if(result[0].equals("0101")){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("no_of_applicant", "" + Constant.NO_OF_APPLIED);
            editor.putString("out_of_apply", "" + Constant.OUT_OFF_APPLY);
            editor.commit();
            MenuFragment.SetInterestvalue();

            final Dialog dialog = new Dialog(SearchActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertpopup);
            TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
            massageTxtView.setText(result[1]);
            LinearLayout submitLayout = (LinearLayout) dialog.findViewById(R.id.submitLayout);
            submitLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else {
            Snackbar.make(parentLayout,""+result[1], Snackbar.LENGTH_SHORT).show();
        }
    }
}