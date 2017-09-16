package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suraj.jobpool.R;

import java.util.ArrayList;

import dtos.GetFillterDTO;
import dtos.JobRollDTO;


/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class JobRollAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<JobRollDTO> list;

    public JobRollAdapter(Context context, ArrayList<JobRollDTO> list){
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
          convertView = layoutInflater.inflate(R.layout.branch_list,null);
          holder.branchNameTxt = (TextView)convertView.findViewById(R.id.branchNameTxt);
          convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.branchNameTxt.setText(list.get(position).getTitle());
        return convertView;
    }

    class ViewHolder{
        TextView branchNameTxt;
    }
}
