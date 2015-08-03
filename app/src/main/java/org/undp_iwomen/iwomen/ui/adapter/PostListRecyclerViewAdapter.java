package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.ui.activity.MainActivity;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;

import java.util.List;

public class PostListRecyclerViewAdapter extends RecyclerView.Adapter<PostListRecyclerViewAdapter.NamesViewHolder>  {

    private Context mContext;
    private List<FeedItem> feedItems;

    public PostListRecyclerViewAdapter(Context context, List<FeedItem> feedItems) {
        mContext = context;
        this.feedItems = feedItems;
        //randomizeCatNames();
    }

    /*public void randomizeCatNames() {
        mCatNames = Arrays.asList(getCatNamesResource());
        Collections.shuffle(mCatNames);
    }
    */

    public class NamesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mPostTile;

        TextView post_content;
        TextView post_like ;
        TextView post_img_path ;
        TextView post_content_type;
        TextView post_content_user_id ;
        TextView post_content_user_name;
        TextView post_content_user_img_path ;
        TextView post_timestamp;
        private ProgressBar feed_item_progressBar;
        private ProgressBar profile_item_progressBar;
        private org.undp_iwomen.iwomen.ui.widget.ProfilePictureView profilePictureView;
        private RoundedImageView profile;
        private org.undp_iwomen.iwomen.ui.widget.ResizableImageView postIMg;

        public NamesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mPostTile = (TextView) itemView.findViewById(R.id.txtPostTitle);
            post_content = (TextView) itemView.findViewById(R.id.txtContent);

            post_content_user_name = (TextView) itemView.findViewById(R.id.name);

            post_timestamp = (TextView)itemView.findViewById(R.id.timestamp);

            profile = (RoundedImageView) itemView.findViewById(R.id.profilePic_rounded);
            feed_item_progressBar =(ProgressBar)itemView.findViewById(R.id.feed_item_progressBar);
            profile_item_progressBar =(ProgressBar)itemView.findViewById(R.id.progressBar_profile_item);

            postIMg = (ResizableImageView) itemView.findViewById(R.id.postImg);
            profilePictureView = (org.undp_iwomen.iwomen.ui.widget.ProfilePictureView) itemView.findViewById(R.id.profilePic);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, MainActivity.class);


            //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();

        }
    }

    /*private String[] getCatNamesResource() {
        return mContext.getResources().getStringArray(R.array.lostlist);
    }*/

    @Override
    public NamesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.stories_list_item, viewGroup, false);
        return new NamesViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(NamesViewHolder viewHolder, int i) {

        FeedItem item = feedItems.get(i);

        viewHolder.profile.setAdjustViewBounds(true);
        viewHolder.profile.setScaleType(ImageView.ScaleType.CENTER_CROP);

        viewHolder.mPostTile.setText(item.getPost_title());
        viewHolder.post_content.setText(item.getPost_content());
        viewHolder.post_content_user_name.setText(item.getPost_content_user_name());
        viewHolder.post_timestamp.setText(item.getCreated_at());

        //viewHolder.mCatNameTextView.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        //viewHolder.profilePictureView.setProfileId(item.get());
        try {
            viewHolder.profilePictureView.setVisibility(View.GONE);
            viewHolder.profile.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(item.getPost_content_user_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                    .placeholder(R.drawable.blank_profile)
                    .error(R.drawable.blank_profile)
                    .into(viewHolder.profile, new ImageLoadedCallback(viewHolder.profile_item_progressBar) {
                        @Override
                        public void onSuccess() {
                            if (this.progressBar != null) {
                                this.progressBar.setVisibility(View.GONE);
                            } else {
                                this.progressBar.setVisibility(View.VISIBLE);
                            }
                        }

                    });
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }

        // Feed image
        if (item.getPost_img_path() != null) {
            try {
                viewHolder.postIMg.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(item.getPost_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(viewHolder.postIMg, new ImageLoadedCallback(viewHolder.feed_item_progressBar) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        } else {
            viewHolder.postIMg.setVisibility(View.GONE);
        }


    }

    public Object getItem(int location) {
        return feedItems.get(location);
    }


    @Override
    public int getItemCount() {
        return feedItems.size();
    }
    private class ImageLoadedCallback implements com.squareup.picasso.Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

}