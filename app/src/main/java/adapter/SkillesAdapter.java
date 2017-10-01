package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import com.startupsoch.jobpool.R;

import java.util.ArrayList;


import dtos.SkillesDTO;
import utils.Global;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class SkillesAdapter extends BaseAdapter implements Filterable {

    LayoutInflater layoutInflater;
    Context context;
    String [] skilles = {"Android","PHP","Xamarin",".Net","iOS","Ionic","Angular JS","Node JS","ASP .Net","MVC","React Native","PHP","Xamarin",".Net","iOS","Ionic","Angular JS","Node JS","ASP .Net","MVC","React Native"};
    ArrayList<SkillesDTO> arraylist;
    ArrayList<SkillesDTO> dupliatearraylist;

    public SkillesAdapter(Context context, ArrayList<SkillesDTO> arraylist){
        this.context = context;
        this.arraylist = arraylist;
        this.dupliatearraylist = arraylist;
    }


    @Override
    public int getCount() {
        return dupliatearraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return  position;
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
          convertView = layoutInflater.inflate(R.layout.skilles_listview,null);
          holder.skillesTxt = (CheckBox)convertView.findViewById(R.id.skillesTxt);
          convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.skillesTxt.setText(dupliatearraylist.get(position).getSkillesName());

        if(dupliatearraylist.get(position).isSkillesflage()){
            holder.skillesTxt.setChecked(true);
        }else {
            holder.skillesTxt.setChecked(false);
        }

        holder.skillesTxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    dupliatearraylist.get(position).setSkillesflage(true);
                    if(!Global.skilleslist.contains(dupliatearraylist.get(position).getSkillesName())){
                        Global.skilleslist.add(dupliatearraylist.get(position).getSkillesName());
                    }
                }else {
                    dupliatearraylist.get(position).setSkillesflage(false);
                    if(Global.skilleslist.contains(dupliatearraylist.get(position).getSkillesName())){
                        Global.skilleslist.remove(dupliatearraylist.get(position).getSkillesName());
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                dupliatearraylist = (ArrayList<SkillesDTO>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<SkillesDTO> FilteredArrList = new ArrayList<SkillesDTO>();

                if (arraylist == null) {
                    arraylist = new ArrayList<SkillesDTO>(dupliatearraylist); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = arraylist.size();
                    results.values = arraylist;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < arraylist.size(); i++) {
                        String data = arraylist.get(i).getSkillesName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new SkillesDTO(arraylist.get(i).getSkillesName(),arraylist.get(i).isSkillesflage()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    class ViewHolder{
        CheckBox skillesTxt;
    }


}
