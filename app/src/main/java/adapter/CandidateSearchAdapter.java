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


import com.startupsoch.jobpool.ProfileActivity;
import com.startupsoch.jobpool.R;

import java.util.ArrayList;

import dtos.CandidateDTO;
import utils.Constant;
import utils.MarshMallowPermission;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class CandidateSearchAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    public ArrayList<CandidateDTO> candidateList;
    SharedPreferences sharedPreferences;
    MarshMallowPermission marshMallowPermission;
    String TAG;

    public CandidateSearchAdapter(Context context, Activity activity, ArrayList<CandidateDTO> candidateList, String TAG) {
        this.context = context;
        this.activity = activity;
        this.candidateList = candidateList;
        this.TAG = TAG;
        sharedPreferences = context.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        marshMallowPermission =new MarshMallowPermission(activity);
    }

/*    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }*/

    @Override
    public int getCount() {
        return candidateList.size();
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.searchlist_infilater, null);
            holder.showInterestLayout = (LinearLayout) convertView.findViewById(R.id.showInterestLayout);
            holder.contactLayout = (LinearLayout) convertView.findViewById(R.id.contactLayout);
            holder.itiNonIti = (TextView) convertView.findViewById(R.id.itiNonIti);
            holder.specialization = (TextView) convertView.findViewById(R.id.specialization);
            holder.locationTxt = (TextView) convertView.findViewById(R.id.locationTxt);
            holder.expTxt = (TextView) convertView.findViewById(R.id.expTxt);
            holder.candidateName = (TextView) convertView.findViewById(R.id.candidateName);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.candidateName.setText(candidateList.get(position).getName().toUpperCase());
        if (!candidateList.get(position).getJobType().equals("null")) {
            holder.itiNonIti.setText(candidateList.get(position).getJobType());
        } else {
            holder.itiNonIti.setText("");
        }
      /*  holder.itiNonIti.setText("ITI");
        holder.specialization.setText("Android Developer");
        holder.expTxt.setText("2 year");*/
        holder.expTxt.setText(candidateList.get(position).getSpecialization());
        if (!candidateList.get(position).getLocation().equals("null")) {
            holder.locationTxt.setText(candidateList.get(position).getLocation());
        } else {
            holder.locationTxt.setText("");
        }

        if (!candidateList.get(position).getLocation().equals("null")) {
            holder.specialization.setText(candidateList.get(position).getJobRole());
        } else {
            holder.specialization.setText("");
        }

        holder.showInterestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPreferences.getString("status", "").equals("1")) {
                    Constant.USER_ID = candidateList.get(position).getUserId();
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("position", "" + position);
                    intent.putExtra("Tag", "" + TAG);
                    activity.startActivity(intent);
                } else {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.login_alert);
                    dialog.show();

                    TextView cancelTxt = (TextView) dialog.findViewById(R.id.cancelTxt);
                    cancelTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });


        holder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPreferences.getString("status", "").equals("1")) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.share_dialog);
                    TextView mailTxt = (TextView) dialog.findViewById(R.id.mailTxt);
                    TextView messageTxt = (TextView) dialog.findViewById(R.id.messageTxt);

                    mailTxt.setText(candidateList.get(position).getEmail());
                    messageTxt.setText(candidateList.get(position).getPhone());


                    mailTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", candidateList.get(position).getEmail(), null));
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
                                callIntent.setData(Uri.parse("tel:" + candidateList.get(position).getPhone()));
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
                }else {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.login_alert);
                    dialog.show();

                    TextView cancelTxt = (TextView) dialog.findViewById(R.id.cancelTxt);
                    cancelTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }

            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView itiNonIti,specialization, locationTxt, expTxt,candidateName;
        LinearLayout showInterestLayout, contactLayout;
    }
}
