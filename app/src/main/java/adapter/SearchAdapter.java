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

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */
public class SearchAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;

    public SearchAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
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
        return 10;
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

          convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.showInterestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                activity.startActivity(intent);
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

        LinearLayout showInterestLayout, contactLayout;
    }
}
