package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.job.EditProfileActivity;
import com.example.dell.job.ProfileActivity;
import com.example.dell.job.R;

import java.util.ArrayList;

import dtos.CandidateDTO;
import dtos.CompanyDTO;
import utils.Global;

import static utils.Global.candidatelist;

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

    public CandidateSearchAdapter(Context context, Activity activity, ArrayList<CandidateDTO> candidateList){
        this.context = context;
        this.activity = activity;
        this.candidateList = candidateList;
        sharedPreferences = context.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

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
        if(convertView==null){
          holder = new ViewHolder();
          convertView = layoutInflater.inflate(R.layout.searchlist_infilater,null);
          holder.showInterestLayout = (LinearLayout)convertView.findViewById(R.id.showInterestLayout);
          holder.contactLayout = (LinearLayout)convertView.findViewById(R.id.contactLayout);
          holder.skillesTxt = (TextView)convertView.findViewById(R.id.skillesTxt);
          holder.locationTxt = (TextView)convertView.findViewById(R.id.locationTxt);
          holder.expTxt = (TextView)convertView.findViewById(R.id.expTxt);
          holder.candidateName = (TextView)convertView.findViewById(R.id.candidateName);
          convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.candidateName.setText(candidateList.get(position).getName());
        if(!candidateList.get(position).getSkill().equals("null")){
            holder.skillesTxt.setText(candidateList.get(position).getSkill());
        }else {
            holder.skillesTxt.setText("");
        }
        if(candidateList.get(position).getExperience().equals("null")){
            holder.expTxt.setText("");
        }else {
            holder.expTxt.setText(candidateList.get(position).getExperience()+"");
        }
        if(!candidateList.get(position).getLocation().equals("null")){
            holder.locationTxt.setText(candidateList.get(position).getLocation());
        }else {
            holder.locationTxt.setText("");
        }

        holder.showInterestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getString("status","").equals("1")){
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("position",""+position);
                    activity.startActivity(intent);
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


        holder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getString("status","").equals("1")){
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.share_dialog);
                    TextView mailTxt = (TextView)dialog.findViewById(R.id.mailTxt);
                    TextView messageTxt = (TextView)dialog.findViewById(R.id.messageTxt);

                    mailTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /*Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(candidateList.get(position).getEmail()
                              ,"", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite Friend");
                            emailIntent.putExtra(Intent.EXTRA_TEXT,  "");
                            activity.startActivity(Intent.createChooser(emailIntent, "Send Email..."));*/
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{candidateList.get(position).getEmail().toString()});
                            i.putExtra(Intent.EXTRA_SUBJECT, "");
                            i.putExtra(Intent.EXTRA_TEXT   , "");
                            try {
                                activity.startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });

                    messageTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                    + candidatelist.get(position).getPhone())));
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
        TextView skillesTxt, locationTxt, expTxt,candidateName;
        LinearLayout showInterestLayout, contactLayout;
    }
}
