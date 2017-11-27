package adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.startupsoch.jobpool.LoginActivity;
import com.startupsoch.jobpool.R;

import java.util.ArrayList;

import dtos.CompanyDTO;
import utils.Constant;
import utils.MarshMallowPermission;
import utils.RequestReceiver;
import utils.WebserviceHelper;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class EmployeeSearchAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    RequestReceiver receiver;
    Activity activity;
    public ArrayList<CompanyDTO> companylist;
    MarshMallowPermission marshMallowPermission;
    SharedPreferences sharedPreferences;

    public EmployeeSearchAdapter(Context context, Activity activity, ArrayList<CompanyDTO> companylist, RequestReceiver receiver){
        this.context = context;
        this.activity = activity;
        this.companylist = companylist;
        this.receiver = receiver;
        marshMallowPermission = new MarshMallowPermission(activity);
        sharedPreferences = context.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.USER_ID = sharedPreferences.getString("user_id","");
    }

 /*   @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }*/

    @Override
    public int getCount() {
        return companylist.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if(convertView==null){
          holder = new ViewHolder();
          convertView = layoutInflater.inflate(R.layout.searchlist_infilater,null);
          holder.showInterestLayout = (LinearLayout)convertView.findViewById(R.id.showInterestLayout);
          holder.contactLayout = (LinearLayout)convertView.findViewById(R.id.contactLayout);
          holder.itiNonIti = (TextView)convertView.findViewById(R.id.itiNonIti);
          holder.specialization = (TextView)convertView.findViewById(R.id.specialization);
          holder.postedJobTxt = (TextView)convertView.findViewById(R.id.postedJobTxt);
          holder.locationTxt = (TextView)convertView.findViewById(R.id.locationTxt);
          holder.expTxt = (TextView)convertView.findViewById(R.id.expTxt);
          holder.applyTxt = (TextView)convertView.findViewById(R.id.applyTxt);
          holder.candidateName = (TextView)convertView.findViewById(R.id.candidateName);
          holder.view = (LinearLayout)convertView.findViewById(R.id.view);
          convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.applyTxt.setText("Apply");
        holder.showInterestLayout.setVisibility(View.GONE);
        holder.candidateName.setText(companylist.get(position).getCompany_name().toUpperCase());

        if(!companylist.get(position).getJobe_type().equals("null")){
            holder.itiNonIti.setText(companylist.get(position).getJobe_type());
        }else {
            holder.itiNonIti.setText("");
        }
        if(!companylist.get(position).getExperience().equals("null")){
            holder.expTxt.setText(companylist.get(position).getExperience()+" Year Experience");
        }else {
            holder.expTxt.setText("");
        }
        if(!companylist.get(position).getLocation().equals("null")){
            holder.locationTxt.setText(companylist.get(position).getLocation());
        }else {
            holder.locationTxt.setText("");
        }

        if(!companylist.get(position).getSpecilization().equals("null")){
            holder.specialization.setText(companylist.get(position).getSpecilization());
        }else {
            holder.specialization.setText("");
        }

        try{
            String[] date = companylist.get(position).getPosted_job().split(" ");
            holder.postedJobTxt.setText("Posted Job on : "+date[0]);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.showInterestLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra("position",""+position);
                activity.startActivity(intent);*/

            }
        });

        holder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_applicantSerivice();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.share_dialog);
                TextView mailTxt = (TextView) dialog.findViewById(R.id.mailTxt);
                TextView messageTxt = (TextView) dialog.findViewById(R.id.messageTxt);


                mailTxt.setText(companylist.get(position).getEmail());
                messageTxt.setText(companylist.get(position).getPhone());

                mailTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", companylist.get(position).getEmail(), null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        activity.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                        dialog.dismiss();
                    }
                });

                messageTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + companylist.get(position).getPhone()));
                            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                if(!marshMallowPermission.checkPermissionForCall()){
                                    marshMallowPermission.requestPermissionForCall();
                                }

                                return;
                            }
                            activity.startActivity(callIntent);
                        } catch (ActivityNotFoundException activityException) {
                            Log.e("Calling a Phone Number", "Call failed", activityException);
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    public void Update_applicantSerivice() {
        WebserviceHelper Update_applicant = new WebserviceHelper(receiver, activity);
        Update_applicant.setAction(Constant.UPDATE_APPLICANT);
        Update_applicant.execute();
    }

    class ViewHolder{
        TextView itiNonIti,specialization,postedJobTxt, locationTxt, expTxt,candidateName, applyTxt;
        LinearLayout showInterestLayout, contactLayout, view;
    }
}
