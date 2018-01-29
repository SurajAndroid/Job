package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.startupsoch.job.R;

import java.util.ArrayList;

import dtos.NotificationDTO;


/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class NotifictionAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<NotificationDTO> list;

    public NotifictionAdapter(Context context, ArrayList<NotificationDTO> list){
        this.context = context;
        this.list = list;
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
          convertView = layoutInflater.inflate(R.layout.notification_list,null);
          holder.email = (TextView)convertView.findViewById(R.id.textView9);
          holder.dateTime = (TextView)convertView.findViewById(R.id.textView11);
          holder.message = (TextView)convertView.findViewById(R.id.textView12);

          convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        String[] data = list.get(position).getDatetime().split(" ");
        holder.email.setText(""+list.get(position).getEmail());
        holder.dateTime.setText(data[0]);
        holder.message.setText(list.get(position).getMessage());

        return convertView;
    }

    class ViewHolder{

            TextView email, dateTime,message;
    }
}
