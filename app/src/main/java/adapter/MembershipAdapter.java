package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.startupsoch.jobpool.R;
import java.util.ArrayList;

import dtos.MembershipDTO;
import listners.CustomButtonListener;
import utils.Constant;
import utils.TextviewSemiBold;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class MembershipAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    public ArrayList<MembershipDTO> candidateList;
    SharedPreferences sharedPreferences;

    private CustomButtonListener customButtonListener = null;

    public MembershipAdapter(Context context, Activity activity, ArrayList<MembershipDTO> candidateList) {
        this.context = context;
        this.activity = activity;
        this.candidateList = candidateList;
        sharedPreferences = context.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        ;
    }

    public void setCustomListener(CustomButtonListener customButtonListener) {
        this.customButtonListener = customButtonListener;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.membership_infilater, null);
            holder.packName = (TextView) convertView.findViewById(R.id.packName);
            holder.CandidatesCount = (TextView) convertView.findViewById(R.id.CandidatesCount);
            holder.postJobCount = (TextView) convertView.findViewById(R.id.postJobCount);
            holder.packPrice = (TextView) convertView.findViewById(R.id.packPrice);
            holder.packDesc = (TextView) convertView.findViewById(R.id.packDesc);
            holder.validFor = (TextView) convertView.findViewById(R.id.validFor);

            holder.selectedPackBtn = (TextviewSemiBold) convertView.findViewById(R.id.selectedPackBtn);

            holder.selectLayout = (LinearLayout) convertView.findViewById(R.id.selectLayout);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.packName.setText(candidateList.get(position).getPackage_name());
        holder.CandidatesCount.setText(candidateList.get(position).getCandidate_count());
        holder.postJobCount.setText(candidateList.get(position).getPost_job_count());
        holder.packPrice.setText("Price " + candidateList.get(position).getPackage_price());

        holder.validFor.setText(candidateList.get(position).getValidFor());
//        holder.validFor.setText("1 month");

        if (candidateList.get(position).getDiscription().equals("")) {
            holder.packDesc.setVisibility(View.GONE);
        } else {
            holder.packDesc.setVisibility(View.VISIBLE);
            holder.packDesc.setText(candidateList.get(position).getDiscription());
        }

        sharedPreferences = context.getSharedPreferences("loginstatus", Context.MODE_PRIVATE);
        Constant.SELECTED_PACK = sharedPreferences.getString("selected_pack", "");

        holder.selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "btn_click");
                }
            }
        });

        return convertView;
    }

    class ViewHolder {

        TextView packName, CandidatesCount, postJobCount, packPrice, packDesc, validFor;
        LinearLayout selectLayout;
        TextviewSemiBold selectedPackBtn;
    }
}
