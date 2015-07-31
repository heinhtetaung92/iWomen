package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;

/**
 * Created by khinsandar on 7/29/15.
 */
public class DrawerListViewAdapter extends BaseAdapter
{
    private String[] listName = null;
    private int[] listicon = null;

    //private Activity activity;

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    public DrawerListViewAdapter(Context context, String[] listName , int[] listic) { //
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        //Log.e("BrowseGridviewAdapter Constructor", "" + listCountry.size() +listCountry.toString());
        this.listName = listName;
        this.listicon = listic;
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
            view = inflater.inflate(R.layout.drawer_list_item, null);//gridview_row //fra_browse_gridview_item
            holder.txtName= (TextView)view.findViewById(R.id.txt_item_name);
            holder.imgIcon = (ImageView) view.findViewById(R.id.icon);


            view.setTag(holder);
        }
        else
        {
            //holder = (ViewHolder) view.getTag();
            holder = (ViewHolder) view.getTag();
        }

        holder.txtName.setText(listName[position]);
        //holder.txtName.setTypeface(DrawerMainActivity.faceNormal);

        holder.imgIcon.setImageResource(listicon[position]);

        //For transparent bg alpha 51=20% , 127=50% , 191=75% , 204 = 80%  ,229=90% , 242=95%
        //holder.txtName.setBackgroundColor (Color.argb(229, 175, 42, 43));// Color.argb(0, 175, 42, 43)); // background transparency
        //holder.txtName.setTextColor (Color.argb (255, 255, 255, 255));//Color.argb (0, 255, 255, 255)); // transparency of the text


        return view;
    }

}
