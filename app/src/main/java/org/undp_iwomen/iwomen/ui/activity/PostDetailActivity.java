package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.utils.Utils;

public class PostDetailActivity extends AppCompatActivity {

    TextView mPostTile;

    TextView post_content;
    TextView post_like ;
    TextView post_img_path ;
    TextView post_content_type;
    TextView post_content_user_id ;
    TextView post_content_user_name;
    TextView post_content_user_img_path ;
    TextView post_timestamp;
    TextView postdetail_username;
    TextView post_suggest_text;
    private ProgressBar feed_item_progressBar;
    private ProgressBar profile_item_progressBar;
    private org.undp_iwomen.iwomen.ui.widget.ProfilePictureView profilePictureView;
    private RoundedImageView profile;
    private org.undp_iwomen.iwomen.ui.widget.ResizableImageView postIMg;
    private SharedPreferences mSharedPreferencesUserInfo;
    private String user_name, user_obj_id, user_ph;
    //private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        init();
        Bundle bundle = getIntent().getExtras();
        String postId = bundle.getString("post_id");

        if(postId != null) {
            FeedItem item = getPostById(postId);
            Log.i("Item", item.getPost_content());
            setItem(item);
        }
        else{
            Utils.doToastEng(this, "Nothing to show!");
        }


    }

    private void init(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setTitle("Detail");*/
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        profile = (RoundedImageView) findViewById(R.id.postdetail_profilePic_rounded);
        mPostTile = (TextView) findViewById(R.id.postdetail_title);
        post_content = (TextView) findViewById(R.id.postdetail_content);
        post_content_user_name = (TextView) findViewById(R.id.postdetail_content_username);
        postdetail_username = (TextView) findViewById(R.id.postdetail_username);
        post_suggest_text = (TextView) findViewById(R.id.postdetail_suggest_title);

        profilePictureView = (org.undp_iwomen.iwomen.ui.widget.ProfilePictureView) findViewById(R.id.postdetail_profilePic);
        postIMg = (org.undp_iwomen.iwomen.ui.widget.ResizableImageView) findViewById(R.id.postdetail_content_img);
        feed_item_progressBar = (ProgressBar) findViewById(R.id.postdetail_feed_item_progressBar);
        profile_item_progressBar = (ProgressBar) findViewById(R.id.postdetail_progressBar_profile_item);
    }

    private void setItem(FeedItem item){
        profile.setAdjustViewBounds(true);
        profile.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mPostTile.setText(item.getPost_title_mm());
        post_content.setText(item.getPost_content());

        if(item.getPost_content_suggest_text() != null && !item.getPost_content_suggest_text().isEmpty()){
            post_suggest_text.setText(item.getPost_content_suggest_text());

        }
        post_content_user_name.setText(item.getPost_content_user_name());

        user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);

        postdetail_username.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        postdetail_username.setText(" ခ်စ္လွစြာေသာ " + user_name);
        //post_timestamp.setText(item.getCreated_at());

        post_content.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
        post_suggest_text.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));

        //viewHolder.mCatNameTextView.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        //viewHolder.profilePictureView.setProfileId(item.get());
        try {
            profilePictureView.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(item.getPost_content_user_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                    .placeholder(R.drawable.blank_profile)
                    .error(R.drawable.blank_profile)
                    .into(profile, new ImageLoadedCallback(profile_item_progressBar) {
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
        if (item.getPost_img_path() != null  && !item.getPost_img_path().isEmpty() ) {
            try {
                postIMg.setVisibility(View.VISIBLE);
                feed_item_progressBar.setVisibility(View.VISIBLE);

                Picasso.with(this)
                        .load(item.getPost_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(postIMg, new ImageLoadedCallback(feed_item_progressBar) {
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
            postIMg.setVisibility(View.GONE);
            feed_item_progressBar.setVisibility(View.GONE);
        }
    }

    private FeedItem getPostById(String postId) {


        FeedItem item = new FeedItem();
        if (this != null) {

            /*String selections = TableAndColumnsName.ParseUtil.PARSE_ID + "!=?";
            String[] selectionargs = {cityID};*/
            Cursor cursor = getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?", new String[]{postId}, null);


            String post_obj_id = "";
            String post_title = "";
            String post_content = "";
            String post_like = "";
            String post_img_path = "";
            String post_content_type = "";
            String post_content_user_id = "";
            String post_content_user_name = "";
            String post_content_user_img_path = "";

            String status = "";
            String created_at = "";
            String updated_at = "";

            if (cursor != null && cursor.moveToFirst()) {

                do {

                    post_obj_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_OBJ_ID));
                    post_title = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_TITLE));
                    post_content = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT));
                    post_like = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_LIKES));
                    post_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_IMG_PATH));
                    post_content_type = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES));
                    post_content_user_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID));
                    post_content_user_name = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME));
                    post_content_user_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH));
                    status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.STATUS));
                    created_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE));
                    updated_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.UPDATED_DATE));


                    item.setPost_obj_id(post_obj_id);
                    item.setPost_title(post_title);
                    item.setPost_content(post_content);
                    item.setPost_like(post_like);
                    item.setPost_img_path(post_img_path);
                    item.setPost_content_type(post_content_type);
                    item.setPost_content_user_id(post_content_user_id);
                    item.setPost_content_user_name(post_content_user_name);
                    item.setPost_content_user_img_path(post_content_user_img_path);
                    item.setStatus(status);
                    item.setCreated_at(created_at);
                    item.setUpdated_at(updated_at);

                } while (cursor.moveToNext());
            }

        } else {
            Log.e("LostListFragment", "Activity Null Case");
        }

        return item;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
