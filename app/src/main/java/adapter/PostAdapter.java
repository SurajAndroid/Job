package adapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.startupsoch.job.PostNewJobActivity;
import com.startupsoch.job.R;

import java.util.ArrayList;

import dtos.PostDTO;
import utils.Constant;
import utils.RequestReceiver;
import utils.WebserviceHelper;
/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */

public class PostAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    LinearLayout reactiveLayout;
    public ArrayList<PostDTO> candidateList;
    SharedPreferences sharedPreferences;
    RequestReceiver receiver;

    public PostAdapter(Context context, Activity activity, RequestReceiver receiver, ArrayList<PostDTO> candidateList){
        this.context = context;
        this.activity = activity;
        this.candidateList = candidateList;
        this.receiver = receiver;
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
          convertView = layoutInflater.inflate(R.layout.post_infilater,null);
          holder.candidateName = (TextView)convertView.findViewById(R.id.candidateName);
          holder.iit_Txt = (TextView)convertView.findViewById(R.id.iit_Txt);
          holder.branchTxt = (TextView)convertView.findViewById(R.id.branchTxt);
          holder.jobrollTxt = (TextView)convertView.findViewById(R.id.jobrollTxt);
          holder.DeleteLayout = (LinearLayout) convertView.findViewById(R.id.DeleteLayout);
            holder.expTxt = (TextView) convertView.findViewById(R.id.expTxt);

          holder.reactiveLayout = (LinearLayout)convertView.findViewById(R.id.reactiveLayout);

          convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.candidateName.setText(candidateList.get(position).getEmployer_name());
        holder.iit_Txt.setText(candidateList.get(position).getIndustry_type());
        holder.branchTxt.setText(candidateList.get(position).getFunctional_area());
        holder.jobrollTxt.setText(candidateList.get(position).getJob_role());
        holder.expTxt.setText(candidateList.get(position).getExperience()+" Year Experience");

        holder.DeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_delete_popup);
                TextView massageTxtView = (TextView) dialog.findViewById(R.id.massageTxtView);
                TextView okayTxt = (TextView)dialog.findViewById(R.id.okayTxt);
                TextView cancelTxt = (TextView)dialog.findViewById(R.id.cancelTxt);
                massageTxtView.setText("Are you sure want to delete this post.");
                okayTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Constant.JOB_ID = candidateList.get(position).getId();
                        Constant.COMPANY_ID = candidateList.get(position).getEmp_id();
                        deletePOSTSerivice();
                        dialog.dismiss();
                    }
                });

                cancelTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });

        holder.reactiveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, PostNewJobActivity.class);
                intent.putExtra("position",""+position);
                activity.startActivity(intent);
                Constant.JobFlage = true;

            }
        });

        return convertView;
    }


    public void deletePOSTSerivice() {
        WebserviceHelper delete = new WebserviceHelper(receiver, activity);
        delete.setAction(Constant.DELETE_POST);
        delete.execute();
    }


    class ViewHolder{
        TextView candidateName, iit_Txt, branchTxt, jobrollTxt, expTxt;
        LinearLayout reactiveLayout, DeleteLayout;


    }
}
