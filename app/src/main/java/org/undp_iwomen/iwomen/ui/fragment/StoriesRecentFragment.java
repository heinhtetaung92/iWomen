package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.parse.Post;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.activity.MainPhotoPostActivity;
import org.undp_iwomen.iwomen.ui.activity.PostDetailActivity;
import org.undp_iwomen.iwomen.ui.adapter.PostListRecyclerViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.RecyclerOnItemClickListener;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by khinsandar on 7/29/15.
 */
public class StoriesRecentFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    public static final String ARG_MENU_INDEX = "index";

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostListRecyclerViewAdapter mPostListRecyclerViewAdapter;
    ProgressWheel progress;
    private List<FeedItem> feedItems;

    private ProgressDialog mProgressDialog;

    FloatingActionButton fab;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;

    public StoriesRecentFragment() {
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
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        feedItems = new ArrayList<FeedItem>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.activity_main_recyclerview);
        final Activity parentActivity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mRecyclerView.setHasFixedSize(false);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //mRecyclerView.setLayoutManager(layoutManager);
        progress = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        fab = (FloatingActionButton) rootView.findViewById(R.id.post_news);
        fab.setOnClickListener(this);

        progress.setVisibility(View.VISIBLE);

        mstr_lang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);

        //When very start this fragment open , need to check db data
        Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");

        if (cursorMain.getCount() > 0) {
            setupAdapter();


        } else {

            if (Connection.isOnline(getActivity())) {
                getPostDataOrderByLikesDate();
            } else {
                //scrollview.setVisibility(View.INVISIBLE);
                //connectionerrorview.setVisibility(View.VISIBLE);
                //            product_arrayList = (ArrayList<ProductsModel>) storageUtil.ReadArrayListFromSD("HomeProductsList");

                progress.setVisibility(View.INVISIBLE);
                    /* Toast.makeText(getActivity().getApplicationContext(),
                    "Please Open Internet Connection!",
                    Toast.LENGTH_LONG).show();*/
                if (mstr_lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, "Internet Connection need!");
                } else {

                    Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
                }


            }
        }


        //setupAdapter();


        mRecyclerView.addOnItemTouchListener(new RecyclerOnItemClickListener(getActivity(), new RecyclerOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);

                intent.putExtra("post_id", feedItems.get(position).getPost_obj_id());

                //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }));

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
            int post_like = 0;
            String post_img_path = "";
            String post_content_type = "";
            String post_content_user_id = "";
            String post_content_user_name = "";
            String post_content_user_img_path = "";

            String video_id = "";
            String post_content_suggest_text = "";
            String post_content_mm = "";
            String post_content_title_mm = "";

            String like_status = "";
            String status = "";
            String created_at = "";
            String updated_at = "";
            feedItems.clear();
            try {

                if (cursor != null && cursor.moveToFirst()) {
                    int i = 0;
                    do {

                        post_obj_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_OBJ_ID));
                        post_title = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_TITLE));
                        post_content = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT));
                        post_like = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_LIKES));
                        post_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_IMG_PATH));
                        post_content_type = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES));
                        post_content_user_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID));
                        post_content_user_name = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME));
                        post_content_user_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH));

                        video_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID));
                        post_content_suggest_text = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT));
                        post_content_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_MM));
                        post_content_title_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM));


                        like_status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.LIKE_STATUS));
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

                        item.setPost_content_mm(post_content_mm);
                        item.setPost_content_video_id(video_id);
                        item.setPost_content_suggest_text(post_content_suggest_text);
                        item.setPost_title_mm(post_content_title_mm);

                        item.setPost_like_status(like_status);
                        item.setStatus(status);
                        item.setCreated_at(created_at);
                        item.setUpdated_at(updated_at);



                    /*String image = feedObj.isNull("image") ? null : feedObj
                            .getString("image");*/

                        feedItems.add(item);

                        i++;


                    } while (cursor.moveToNext());
                }

                cursor.close();

                //lost_data_list, lost_data_id_list, lost_data_obj_id_list ,lost_data_img_url_list
                //storageUtil.SaveArrayListToSD("lost_data_list", lost_data_list);


                if (mstr_lang.equals(com.parse.utils.Utils.ENG_LANG)) {
                    mPostListRecyclerViewAdapter = new PostListRecyclerViewAdapter(getActivity().getApplicationContext(), feedItems, mstr_lang);
                    mRecyclerView.setAdapter(mPostListRecyclerViewAdapter);
                    mProgressDialog.dismiss();
                    progress.setVisibility(View.INVISIBLE);
                } else {
                    mPostListRecyclerViewAdapter = new PostListRecyclerViewAdapter(getActivity().getApplicationContext(), feedItems, mstr_lang);
                    mRecyclerView.setAdapter(mPostListRecyclerViewAdapter);
                    mProgressDialog.dismiss();
                    progress.setVisibility(View.INVISIBLE);
                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }


            //Utils.doToast(getActivity(), String.valueOf(feedItems.size()));
        } else {
            Log.e("LostListFragment", "Activity Null Case");
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.refresh_menu, menu);

        final MenuItem item = menu.add(0, 12, 0, "Search");
        //menu.removeItem(12);
        item.setIcon(R.drawable.ic_action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                | MenuItem.SHOW_AS_ACTION_ALWAYS);


        final SearchView sv = new SearchView(getActivity());//.getActionBar().getThemedContext()

        item.setActionView(sv);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) sv.findViewById(android.support.v7.appcompat.R.id.search_src_text);


        //int searchPlateId = sv.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        // Getting the 'search_plate' LinearLayout.
        View searchPlate = sv.findViewById(android.support.v7.appcompat.R.id.search_plate);
        // Setting background of 'search_plate' to earlier defined drawable.
        searchPlate.setBackgroundResource(R.drawable.searchviewbackground);


        sv.setSubmitButtonEnabled(false);
        sv.setOnQueryTextListener(this);
        /*EditText et_search = (EditText) sv.findViewById(R.id.search_src_text);
        et_search.setTextColor(Color.WHITE);*/

        /*ImageView clearBtn = (ImageView) sv.findViewById(R.id.search_close_btn);
        clearBtn.setImageResource(R.drawable.ic_action_cancel);*/

        ImageView close = (ImageView) sv.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        close.setImageResource(R.drawable.ic_action_cancel);
        close.setAlpha(192);

        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        ImageView search_btn = (ImageView) sv.findViewById(R.id.search_button);
        search_btn.setBackgroundResource(R.drawable.ic_action_search);

        SpannableStringBuilder ssb = new SpannableStringBuilder("   ");
        ssb.append("Search");
        Drawable searchIcon = getActivity().getResources().getDrawable(R.drawable.ic_action_search);
        int textSize = (int) (searchAutoComplete.getTextSize() * 1.25);
        searchIcon.setBounds(0, 0, textSize, textSize);
        ssb.setSpan(new ImageSpan(searchIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        searchAutoComplete.setHint(ssb);

        /*sv.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search_hint) + "</font>"));*/


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //index = 1;
                setupAdapter();
                sv.setIconified(true);
                sv.setQuery("", false);
            }
        });

        sv.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    item.collapseActionView();
                }
            }
        });
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

        Utils.doToastEng(mContext, "User Save");

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

        Utils.doToastEng(mContext, "Post Save");

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

        cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
        cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, "Sun Aug 02 18:07:00 GMT+06:30 2015");
        cv.put(TableAndColumnsName.PostUtil.UPDATED_DATE, "Sun Aug 02 18:07:00 GMT+06:30 2015");

        Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());

        getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);

    }

    private void getPostDataOrderByLikesDate() {
        if (Connection.isOnline(mContext)) {

            Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");


            if (cursorMain.getCount() > 0) {


                cursorMain.close();


                Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC LIMIT 1");


                if (cursor.getCount() > 0) {


                    Date date = null;
                    if (cursor != null && cursor.moveToFirst()) {

                        date = new Date(cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE)));
                    /*First Row Id﹕ ==>5
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/Date﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/First Row Data﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015*/
                        Log.e("First Row Id", "==>" + cursor.getString(cursor.getColumnIndex("_id")));
                        Log.e("Date", "==>" + date.toString());
                        Log.e("First Row Data", "==>" + cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE)));


                        mProgressDialog.show();

                        ParseQuery<Post> query = Post.getQuery();


                        query.whereGreaterThan("postUploadedDate", date);
                        //query.whereLessThan("likes",)
                        query.setLimit(3);

                        query.orderByDescending("postUploadedDate"); //Latest date is first
                        //query.orderByDescending("likes");
                        query.findInBackground(new FindCallback<Post>() {
                            @Override
                            public void done(List<Post> postList, ParseException e) {
                                if (e == null) {

                                    for (Post post : postList) {
                                        Log.e("Second time Data", "==>" + post.getObjectId());
                                        final ContentValues cv = new ContentValues();
                                        cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, post.getObjectId());
                                        cv.put(TableAndColumnsName.PostUtil.POST_TITLE, post.getString("title"));
                                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, post.getString("content"));
                                        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, post.getNumber("likes").intValue());


                                        if (post.get("image") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, post.getParseFile("image").getUrl());
                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "");

                                        }
                                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, post.getString("contentType"));//

                                        if (post.get("image") != null) {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, post.getParseObject("userId").getObjectId());
                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "");
                                        }

                                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, post.getString("postUploadName"));

                                        if (post.get("postUploadPersonImg") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, post.getParseFile("postUploadPersonImg").getUrl());

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                        }
                                        if (post.get("videoId") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, "");

                                        }
                                        if (post.get("suggest_section") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, post.getString("suggest_section"));

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                        }
                                        if (post.get("titleMm") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, post.getString("titleMm"));

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, "");

                                        }
                                        if (post.get("content_mm") != null) {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, post.getString("content_mm"));
                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, "");

                                        }

                                        cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "0");

                                        cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
                                        cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, post.get("postUploadedDate").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                        cv.put(TableAndColumnsName.PostUtil.UPDATED_DATE, post.get("postUploadedDate").toString());

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
                }


            } else {
                mProgressDialog.show();


                ParseQuery<Post> query = Post.getQuery();
                //query.orderByDescending("createdAt"); //Latest date is first
                query.orderByAscending("postUploadedDate");//Very first row Date is first
                //query.orderByDescending("likes");//Most likes is first

                query.setLimit(3);
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
                                cv.put(TableAndColumnsName.PostUtil.POST_LIKES, post.getNumber("likes").intValue());

                                if (post.get("image") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, post.getParseFile("image").getUrl());
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "");

                                }
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, post.getString("contentType"));//

                                if (post.get("image") != null) {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, post.getParseObject("userId").getObjectId());
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "");
                                }

                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, post.getString("postUploadName"));

                                if (post.get("postUploadPersonImg") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, post.getParseFile("postUploadPersonImg").getUrl());

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                }

                                if (post.get("videoId") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, "");

                                }
                                if (post.get("videoId") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, "");

                                }
                                if (post.get("suggest_section") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, post.getString("suggest_section"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                }
                                if (post.get("titleMm") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, post.getString("titleMm"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, "");

                                }
                                if (post.get("content_mm") != null) {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, post.getString("content_mm"));
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, "");

                                }

                                cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "0");

                                cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, post.get("postUploadedDate").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.PostUtil.UPDATED_DATE, post.get("postUploadedDate").toString());


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

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, "Internet Connection need!");
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.post_news:

                startActivity(new Intent(getActivity(), MainPhotoPostActivity.class));
                //Utils.doToastEng(mContext, "Coming Soon!");

                break;
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        mPostListRecyclerViewAdapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.equals("")) {
            //index = 1;
            setupAdapter();

        }
        return false;
    }
}
