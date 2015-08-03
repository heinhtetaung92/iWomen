package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.parse.Post;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.adapter.PostListRecyclerViewAdapter;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by khinsandar on 7/29/15.
 */
public class StoriesFragment extends Fragment {
    public static final String ARG_MENU_INDEX = "index";

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostListRecyclerViewAdapter mPostListRecyclerViewAdapter;
    ProgressWheel progress;
    private List<FeedItem> feedItems;

    private ProgressDialog mProgressDialog;
    public StoriesFragment() {
        // Empty constructor required for fragment subclasses
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
    }
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stories, container, false);
        mContext = getActivity().getApplicationContext();

        //int index = getArguments().getInt(ARG_MENU_INDEX);

        //SetUserData();
        //SetPostData();
        init(rootView);

        return rootView;
    }

    private void init(View rootView) {
        feedItems = new ArrayList<FeedItem>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.activity_main_recyclerview);
        final Activity parentActivity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mRecyclerView.setHasFixedSize(false);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //mRecyclerView.setLayoutManager(layoutManager);
        progress = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);

        progress.setVisibility(View.VISIBLE);

        if (Connection.isOnline(getActivity())) {

            setupAdapter();

        } else {
            //scrollview.setVisibility(View.INVISIBLE);
            //connectionerrorview.setVisibility(View.VISIBLE);
            //            product_arrayList = (ArrayList<ProductsModel>) storageUtil.ReadArrayListFromSD("HomeProductsList");

            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity().getApplicationContext(),
                    "Please Open Internet Connection!",
                    Toast.LENGTH_LONG).show();
        }

        //Avoid SwipeReresh Loading when it is not at the top item
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ?
                                0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(dx == 0 && topRowVerticalPosition >= 0);


            }
        });


        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupAdapter();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });


    }

    private void setupAdapter() {

        if (getActivity().getApplicationContext() != null) {



            /*String selections = TableAndColumnsName.ParseUtil.PARSE_ID + "!=?";
            String[] selectionargs = {cityID};*/
            Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");


            ArrayList<FeedItem> feedItemArrayList = new ArrayList<>();
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
            feedItems.clear();
            if (cursor != null && cursor.moveToFirst()) {
                int i = 0;
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


                    FeedItem item = new FeedItem();

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



                    /*String image = feedObj.isNull("image") ? null : feedObj
                            .getString("image");*/

                    feedItems.add(item);

                    i++;


                } while (cursor.moveToNext());
            }

            //lost_data_list, lost_data_id_list, lost_data_obj_id_list ,lost_data_img_url_list
            //storageUtil.SaveArrayListToSD("lost_data_list", lost_data_list);
            mPostListRecyclerViewAdapter = new PostListRecyclerViewAdapter(getActivity().getApplicationContext(), feedItems);
            mRecyclerView.setAdapter(mPostListRecyclerViewAdapter);
            mProgressDialog.dismiss();
            progress.setVisibility(View.INVISIBLE);
        } else {
            Log.e("LostListFragment", "Activity Null Case");
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {


            case R.id.action_refresh:
                //Utils.doToast(getActivity(), "do refresh");
                //mProgressDialog.show();


                //SetPostData();
                getPostDataOrderByLikesDate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void SetUserData() {

        Utils.doToast(mContext, "User Save");

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.UserUtil.USER_OBJ_ID, "U001");
        cv.put(TableAndColumnsName.UserUtil.USER_ROLE, "2");
        cv.put(TableAndColumnsName.UserUtil.USER_NAME, "Khin");
        cv.put(TableAndColumnsName.UserUtil.USER_PH, "123456789");

        cv.put(TableAndColumnsName.UserUtil.STATUS, "0");
        cv.put(TableAndColumnsName.UserUtil.CREATED_DATE, "01-06-2015");
        cv.put(TableAndColumnsName.UserUtil.UPDATED_DATE, "01-06-2015");
        Log.e("saveUserLocal : ", "= = = = = = = : " + cv.toString());

        getActivity().getContentResolver().insert(IwomenProviderData.UserProvider.CONTENT_URI, cv);

    }

    private void SetPostData() {

        Utils.doToast(mContext, "Post Save");

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, "POO4");
        cv.put(TableAndColumnsName.PostUtil.POST_TITLE, "TITLE1");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, "POST CONTENT");
        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, "1");
        cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "http://i.imgur.com/ad7uSGY.png");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, "Letter");//

        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "U001");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, "Khin");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "http://www.windowsegis.com/wp-content/uploads/2013/08/100-superb-windows-phone-wallpapers-to-mark-you-niftier-01.jpg");

        cv.put(TableAndColumnsName.UserUtil.STATUS, "0");
        cv.put(TableAndColumnsName.UserUtil.CREATED_DATE, "Sun Aug 02 18:07:00 GMT+06:30 2015");
        cv.put(TableAndColumnsName.UserUtil.UPDATED_DATE, "Sun Aug 02 18:07:00 GMT+06:30 2015");

        Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());

        getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);

    }

    private void getPostDataOrderByLikesDate(){
        if(Connection.isOnline(mContext)){

            mProgressDialog.show();
            Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");

            if(cursor.getCount() > 0){

                Date date =  null;
                if (cursor != null && cursor.moveToFirst()) {

                    date = new Date(cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE)));
                    /*First Row Id﹕ ==>5
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/Date﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/First Row Data﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015*/
                    Log.e("First Row Id","==>" + cursor.getString(cursor.getColumnIndex("_id")));
                    Log.e("Date","==>" + date.toString());
                    Log.e("First Row Data","==>" + cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE)));




                }



            }else{

                ParseQuery<Post> query = Post.getQuery();
                query.orderByDescending("createdAt"); //Latest date is first
                query.orderByDescending("likes");

                query.findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> postList, ParseException e) {

                        if (e == null) {
                            Log.e("Post sizes", "==>" + postList.size() + "/" + postList.get(0).toString());

                            for (Post post : postList) {

                                final ContentValues cv = new ContentValues();
                                cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, post.getObjectId());
                                cv.put(TableAndColumnsName.PostUtil.POST_TITLE, post.getString("title"));
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, post.getString("content"));
                                cv.put(TableAndColumnsName.PostUtil.POST_LIKES, post.getNumber("likes").toString());
                                cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, post.getParseFile("image").getUrl());
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, post.getString("contentType"));//

                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, post.getParseObject("userId").getObjectId());


                                Log.e("Post Date ", "1==>" + post.getParseObject("userId").getObjectId() + "/==>" + post.getParseFile("image").getUrl());
                                // getting the user  who created the post
                               /* post.getParseObject("userId").fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseUserObject, ParseException e) {
                                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, parseUserObject.getString("username"));
                                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, parseUserObject.getParseFile("profileimage").getUrl());


                                    }
                                });
                            */

                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, "Ms.Artrid Tuminez");
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-be397891-4633-401d-b974-4bb608efd705-kentucky-derby-hats-10.jpg");




                                cv.put(TableAndColumnsName.UserUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.UserUtil.CREATED_DATE, post.get("postUploadedDate").toString());
                                cv.put(TableAndColumnsName.UserUtil.UPDATED_DATE, post.get("postUploadedDate").toString());

                                Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);



                            }
                            setupAdapter();



                        } else {
                            Log.e("Post Get Err", "===>" + e.toString());
                        }
                    }
                });
            }

        }else{
            Utils.doToast(mContext, "Internet Connection need!");
        }
    }
}

