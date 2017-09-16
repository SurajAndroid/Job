package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suraj.jobpool.R;

import java.util.ArrayList;

import dtos.PostDTO;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class PostAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    public ArrayList<PostDTO> candidateList;
    SharedPreferences sharedPreferences;

    public PostAdapter(Context context, Activity activity, ArrayList<PostDTO> candidateList){
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
          convertView = layoutInflater.inflate(R.layout.post_infilater,null);
          holder.candidateName = (TextView)convertView.findViewById(R.id.candidateName);
          holder.iit_Txt = (TextView)convertView.findViewById(R.id.iit_Txt);
          holder.branchTxt = (TextView)convertView.findViewById(R.id.branchTxt);
          holder.jobrollTxt = (TextView)convertView.findViewById(R.id.jobrollTxt);

          convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.candidateName.setText(candidateList.get(position).getEmployer_name());
        holder.iit_Txt.setText(candidateList.get(position).getIndustry_type());
        holder.branchTxt.setText(candidateList.get(position).getFunctional_area());
        holder.jobrollTxt.setText(candidateList.get(position).getJob_role());

        return convertView;
    }

    class ViewHolder{

        TextView candidateName, iit_Txt, branchTxt, jobrollTxt;

    }
}
