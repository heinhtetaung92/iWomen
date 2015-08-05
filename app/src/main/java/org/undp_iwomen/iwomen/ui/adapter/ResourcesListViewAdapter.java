package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.ResourceItem;
import org.undp_iwomen.iwomen.model.MyTypeFace;

import java.util.List;

/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourcesListViewAdapter extends BaseAdapter
{


    private List<ResourceItem> ResourceItems;
    Context mContext;
    LayoutInflater inflater;
    public ResourcesListViewAdapter(Context context, List<ResourceItem> resourceItems) { //
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.ResourceItems = resourceItems;
        //Log.e("BrowseGridviewAdapter Constructor", "" + listCountry.size() +listCountry.toString());

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return ResourceItems.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return ResourceItems.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {

        public TextView txtName;
        public TextView txtBodyText;
        public RoundedImageView imgIcon;
        //public TextView txtViewTitle;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        //LayoutInflater inflator = activity.getLayoutInflater();

        if(view ==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.resources_list_item_view, null);//gridview_row //fra_browse_gridview_item
            holder.txtName= (TextView)view.findViewById(R.id.resource_item_name);
            holder.txtBodyText= (TextView)view.findViewById(R.id.resource_item_text);
            holder.imgIcon = (RoundedImageView) view.findViewById(R.id.resouce_list_item_img);


            view.setTag(holder);
        }
        else
        {
            //holder = (ViewHolder) view.getTag();
            holder = (ViewHolder) view.getTag();
        }

        holder.imgIcon.setAdjustViewBounds(true);
        holder.imgIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.txtName.setText(ResourceItems.get(position).getResourceName());
        holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());

        holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        holder.txtBodyText.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        //holder.txtName.setTypeface(DrawerMainActivity.faceNormal);

        holder.imgIcon.setImageResource(ResourceItems.get(position).getResourceImg());

        //For transparent bg alpha 51=20% , 127=50% , 191=75% , 204 = 80%  ,229=90% , 242=95%
        //holder.txtName.setBackgroundColor (Color.argb(229, 175, 42, 43));// Color.argb(0, 175, 42, 43)); // background transparency
        //holder.txtName.setTextColor (Color.argb (255, 255, 255, 255));//Color.argb (0, 255, 255, 255)); // transparency of the text


        return view;
    }

}
