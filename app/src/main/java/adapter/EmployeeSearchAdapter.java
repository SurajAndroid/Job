package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.job.ProfileActivity;
import com.example.dell.job.R;

import java.util.ArrayList;

import dtos.CompanyDTO;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class EmployeeSearchAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    public ArrayList<CompanyDTO> companylist;

    public EmployeeSearchAdapter(Context context, Activity activity,ArrayList<CompanyDTO> companylist){
        this.context = context;
        this.activity = activity;
        this.companylist = companylist;
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
          holder.skillesTxt = (TextView)convertView.findViewById(R.id.skillesTxt);
          holder.locationTxt = (TextView)convertView.findViewById(R.id.locationTxt);
          holder.expTxt = (TextView)convertView.findViewById(R.id.expTxt);
          holder.candidateName = (TextView)convertView.findViewById(R.id.candidateName);
          convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.candidateName.setText(companylist.get(position).getCompany_name());
        if(!companylist.get(position).getSkill().equals("null")){
            holder.skillesTxt.setText(companylist.get(position).getSkill());
        }else {
            holder.skillesTxt.setText("");
        }
        if(!companylist.get(position).getExperience().equals("null")){
            holder.expTxt.setText(companylist.get(position).getExperience()+" of year");
        }else {
            holder.expTxt.setText("");
        }
        if(!companylist.get(position).getLocation().equals("null")){
            holder.locationTxt.setText(companylist.get(position).getLocation());
        }else {
            holder.locationTxt.setText("");
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
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.share_dialog);
                TextView mailTxt = (TextView)dialog.findViewById(R.id.mailTxt);
                TextView messageTxt = (TextView)dialog.findViewById(R.id.messageTxt);

                mailTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite Friend");
                        emailIntent.putExtra(Intent.EXTRA_TEXT,  "");
                        activity.startActivity(Intent.createChooser(emailIntent, "Send Email..."));
                        dialog.dismiss();
                    }
                });

                messageTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String smsBody= "";
                        Uri uri = Uri.parse("smsto:0800000123");
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.putExtra("sms_body", smsBody);
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        activity.startActivity(sendIntent);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView skillesTxt, locationTxt, expTxt,candidateName;
        LinearLayout showInterestLayout, contactLayout;
    }
}
