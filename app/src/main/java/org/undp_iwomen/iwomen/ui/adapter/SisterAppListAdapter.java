package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.SisterAppItem;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.List;

/**
 * Created by khinsandar on 8/13/15.
 */
public class SisterAppListAdapter extends BaseAdapter {

    List<SisterAppItem> datalist;
    private Context mContext;

    public SisterAppListAdapter(Context context, List<SisterAppItem> dl){
        this.datalist = dl;
        mContext = context;
    }

    @Override
    public int getCount() {
        return this.datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return this.datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh;
        if(view == null){
            view= LayoutInflater.from(mContext).inflate(R.layout.sister_apps_list_item, viewGroup, false);

            //LayoutInflater inflater = mActivity.getLayoutInflater();
            //view = inflater.inflate(R.layout.sister_apps_list_item, viewGroup, false);

            vh = new ViewHolder();
            vh.imgApp = (ResizableImageView) view.findViewById(R.id.sister_app_logo_img);
            vh.tv_name = (TextView) view.findViewById(R.id.sister_app_txt_app_name);
            vh.tv_download = (TextView) view.findViewById(R.id.sister_app_txt_download);

            view.setTag(vh);

        }else{
            vh = (ViewHolder) view.getTag();
        }

        vh.tv_name.setText(datalist.get(i).get_app_name());//

        vh.imgApp.setImageResource(datalist.get(i).get_app_img());//


        //vh.tv_download.setText(datalist.get(i).get_timestamp());
        vh.tv_download.setClickable(true);
        vh.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.doToastEng(mContext,"OnCLickDownload");
            }
        });

        return view;
    }

    public class ViewHolder{
        ResizableImageView imgApp;
        TextView tv_name , tv_download;

    }

}
