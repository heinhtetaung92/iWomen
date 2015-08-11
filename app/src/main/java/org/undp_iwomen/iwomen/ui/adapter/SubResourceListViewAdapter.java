package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;

/**
 * Created by khinsandar on 7/29/15.
 */
public class SubResourceListViewAdapter extends BaseAdapter
{
    private String[] listName = null;
    private String[] listDate = null;
    private String[] listDataText = null;
    private int[] listicon = null;

    //private Activity activity;

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    public SubResourceListViewAdapter(Context context, String[] listName, String[] listDate,int[] listic) { //
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        //Log.e("BrowseGridviewAdapter Constructor", "" + listCountry.size() +listCountry.toString());
        this.listName = listName;
        this.listicon = listic;
        this.listDate = listDate;
        //Log.e("BrowseGridviewAdapter Constructor", "" + listShopName.size() +listShopName.toString());
        //this.activity = activity;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return listName.length;
    }


    public String getItem(int position) {
        // TODO Auto-generated method stub
        return listName[position];
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {

        public TextView txtName;
        public TextView txtTime;
        public ImageView imgIcon;
        //public TextView txtViewTitle;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        //LayoutInflater inflator = activity.getLayoutInflater();

        if(view ==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.sub_resource_list_item, null);//gridview_row //fra_browse_gridview_item
            holder.txtName= (TextView)view.findViewById(R.id.txt_sub_resource_item_name);
            holder.txtTime= (TextView)view.findViewById(R.id.txt_sub_resource_item_time);
            holder.imgIcon = (ImageView) view.findViewById(R.id.sub_resource_icon);


            view.setTag(holder);
        }
        else
        {
            //holder = (ViewHolder) view.getTag();
            holder = (ViewHolder) view.getTag();
        }

        holder.txtName.setText(listName[position]);
        holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        holder.txtTime.setText(listDate[position]);


        holder.imgIcon.setImageResource(listicon[position]);

        //For transparent bg alpha 51=20% , 127=50% , 191=75% , 204 = 80%  ,229=90% , 242=95%
        //holder.txtName.setBackgroundColor (Color.argb(229, 175, 42, 43));// Color.argb(0, 175, 42, 43)); // background transparency
        //holder.txtName.setTextColor (Color.argb (255, 255, 255, 255));//Color.argb (0, 255, 255, 255)); // transparency of the text


        return view;
    }

}
