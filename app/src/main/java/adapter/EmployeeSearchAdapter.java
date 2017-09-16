package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suraj.jobpool.R;

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

    public EmployeeSearchAdapter(Context context, Activity activity, ArrayList<CompanyDTO> companylist){
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
          holder.itiNonIti = (TextView)convertView.findViewById(R.id.itiNonIti);
          holder.specialization = (TextView)convertView.findViewById(R.id.specialization);
          holder.locationTxt = (TextView)convertView.findViewById(R.id.locationTxt);
          holder.expTxt = (TextView)convertView.findViewById(R.id.expTxt);
          holder.applyTxt = (TextView)convertView.findViewById(R.id.applyTxt);
          holder.candidateName = (TextView)convertView.findViewById(R.id.candidateName);
          convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.applyTxt.setText("Apply");
        holder.showInterestLayout.setVisibility(View.GONE);
        holder.candidateName.setText(companylist.get(position).getCompany_name());
        if(!companylist.get(position).getCurrent_requirment().equals("null")){
            holder.itiNonIti.setText(companylist.get(position).getCurrent_requirment());
        }else {
            holder.itiNonIti.setText("");
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
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{companylist.get(position).getEmail().toString()});
                        i.putExtra(Intent.EXTRA_SUBJECT, "");
                        i.putExtra(Intent.EXTRA_TEXT   , "");
                        try {
                            activity.startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        dialog.dismiss();
                    }
                });

                messageTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                )));
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView itiNonIti,specialization, locationTxt, expTxt,candidateName, applyTxt;
        LinearLayout showInterestLayout, contactLayout;
    }
}
