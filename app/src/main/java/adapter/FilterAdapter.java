package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.startupsoch.jobpool.R;

import java.util.ArrayList;

import dtos.FilterDTO;


/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class FilterAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    LinearLayout genderLayout;

    Context context;
    ArrayList<FilterDTO> list;

    public FilterAdapter(Context context,ArrayList<FilterDTO> list){
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
          convertView = layoutInflater.inflate(R.layout.filter_check_list,null);
          holder.textView14 = (TextView)convertView.findViewById(R.id.textView14);
          holder.checkBox2 = (CheckBox)convertView.findViewById(R.id.checkBox2);

          convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView14.setText(list.get(position).getFilterString());

        if(list.get(position).isFlage()){
            holder.checkBox2.setChecked(true);
        }

        holder.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    list.get(position).setFlage(true);
                }else {
                    list.get(position).setFlage(false);
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView textView14;
        CheckBox checkBox2;

    }
}
