package org.undp_iwomen.iwomen.ui.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algo.hha.emojiicon.EmojiconEditText;
import com.algo.hha.emojiicon.EmojiconGridView;
import com.algo.hha.emojiicon.EmojiconsPopup;
import com.algo.hha.emojiicon.emoji.Emojicon;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.makeramen.RoundedImageView;
import com.parse.FunctionCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.CommentItem;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.TextWatcherAdapter;
import org.undp_iwomen.iwomen.model.parse.Comment;
import org.undp_iwomen.iwomen.model.retrofit_api.CommentAPI;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.adapter.CommentAdapter;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener{

    TextView mPostTile;

    TextView post_content;
    TextView post_like;
    TextView post_img_path;
    TextView post_content_type;
    TextView post_content_user_id;
    TextView post_content_user_name;
    TextView post_content_user_img_path;
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
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    //private Toolbar toolbar;
    private LinearLayout ly_likes_button;
    private ImageView img_like;
    private TextView txt_like_count;
    private String postId, like_status;
    //private TextView txt_simile_emoji_icon;
    private EmojiconEditText et_comment;
    //FrameLayout et_comment_frame;
    private TextView txt_comment_submit;

    private ImageView img_viber_share;

    private LinearLayout ly_postdetail_share_button;
    ListView listView;
    List<CommentItem> listComment;

    Comment commentParse;
    String share_data;
    String share_img_url_data;
    //FOR FB SHARE
    private ShareButton shareButton;
    private CallbackManager callbackManager;
    private final String PENDING_ACTION_BUNDLE_KEY =
            "org.undp_iwomen.iwomen.app:PendingAction";
    private PendingAction pendingAction = PendingAction.NONE;
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }


    //Emoji Keyboard
    public ImageView emojiIconToggle;


    private ShareDialog shareDialog;
    private static final String PERMISSION = "publish_actions";
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;

    public static final int MENU_Search = Menu.FIRST;
    public static final int MENU_Share = Menu.FIRST + 1;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(PostDetailActivity.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FB SHARE
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        //FB SHARE
        setContentView(R.layout.activity_post_detail);
        sharePrefLanguageUtil = getSharedPreferences(com.parse.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        postId = bundle.getString("post_id");
        init();
        initEmojiIcon();



    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setTitle("Detail");*/
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        if (!mSharedPreferencesUserInfo.getBoolean(CommonConfig.IS_LOGIN, false)) {

        } else {
            user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
            user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);

        }

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

        listView = (ListView) findViewById(R.id.postdetail_comment_listview);
        ly_likes_button = (LinearLayout) findViewById(R.id.postdetail_like_button);
        img_like = (ImageView) findViewById(R.id.postdetail_like_img);
        txt_like_count = (TextView) findViewById(R.id.postdetail_like_count);

        //txt_simile_emoji_icon = (TextView) findViewById(R.id.postdetail_smile_img_upload);
        et_comment = (EmojiconEditText) findViewById(R.id.postdetail_et_comment_upload);
        //et_comment_frame = (FrameLayout) findViewById(R.id.emojicons);
        txt_comment_submit = (TextView) findViewById(R.id.postdetail_submit_comment);

        img_viber_share = (ImageView)findViewById(R.id.postdetail_viber_img);
        ly_postdetail_share_button = (LinearLayout)findViewById(R.id.postdetail_share_button);

        shareButton = (ShareButton)findViewById(R.id.postdetail_fb_share_button);

        emojiIconToggle = (ImageView) findViewById(R.id.toggleEmojiIcon);




        if (postId != null) {
            getLocalPostDetail(postId);

        } else {
            Utils.doToastEng(this, "Nothing to show!");
        }

        shareButton.setShareContent(getLinkContent());

        ly_likes_button.setOnClickListener(this);
        //txt_simile_emoji_icon.setOnClickListener(this);
        txt_comment_submit.setOnClickListener(this);
        img_viber_share.setOnClickListener(this);
        ly_postdetail_share_button.setOnClickListener(this);



        et_comment.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //mTxtEmojicon.setText(s);
                //et_comment_frame.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                //et_comment_frame.setVisibility(View.GONE);

            }
        });



        getTestCommentList();

    }

    public void initEmojiIcon(){
        boolean isLargerLollipop = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);

        int reduceheight = 0;
        if(isLargerLollipop){
            reduceheight = (int) getResources().getDimension(R.dimen.reduceeheight);
        }
        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(getWindow().getDecorView().getRootView(), this, reduceheight);


        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();


        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {


            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiIconToggle, R.drawable.smiley);
            }
        });


        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {


            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });


        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {


            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (et_comment == null || emojicon == null) {
                    return;
                }


                int start = et_comment.getSelectionStart();
                int end = et_comment.getSelectionEnd();
                if (start < 0) {
                    et_comment.append(emojicon.getEmoji());
                } else {
                    et_comment.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });


        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {


            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                et_comment.dispatchKeyEvent(event);
            }
        });



        emojiIconToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiIconToggle, R.drawable.ic_action_keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        et_comment.setFocusableInTouchMode(true);
                        et_comment.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(et_comment, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiIconToggle, R.drawable.ic_action_keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();

                }
            }
        });
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    public void getTestCommentList() {


        listComment = new ArrayList<>();


        //TODO data from server
        CommentAPI.getInstance().getService().getCommentByPostId("{\"postId\":{\"__type\":\"Pointer\",\"className\":\"Post\",\"objectId\":\"" + postId + "\"}}", new Callback<String>() {
            @Override
            public void success(String s, Response response) {

                Log.e("Comments>>>", ">>>>" + s);
                String comment;
                String comment_created_time;
                String comment_user_name;

                try {
                    JSONObject whole_body = new JSONObject(s);
                    JSONArray result = whole_body.getJSONArray("results");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject each_object = result.getJSONObject(i);


                        if (each_object.isNull("comment_contents")) {
                            comment = "null";
                        } else {
                            comment = each_object.getString("comment_contents");
                        }
                        Log.e("Comments>>>", ">>>>" + comment);

                       /* if (each_object.isNull("comment_created_time")) {
                            comment_created_time = "null";
                        } else {
                            comment_created_time = each_object.getString("comment_created_time");
                        }*/
                        if (each_object.isNull("user_name")) {
                            comment_user_name = "null";
                        } else {
                            comment_user_name = each_object.getString("user_name");
                        }
                        listComment.add(new CommentItem("", comment_user_name, comment, "30 min ago"));
                    }


                    CommentAdapter adapter = new CommentAdapter(PostDetailActivity.this, listComment);
                    listView.setAdapter(adapter);


                    setListViewHeightBasedOnChildren(listView);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Comments>>>", ">>>>JSONERR" + e.toString());

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Comments>>>", ">>>>ERR" + error);
            }
        });

        //list.add(new CommentItem("", "Khin Khin", "Thanks you for your post", "30 min ago"));
        //listComment.add(new CommentItem("", "Phyu Hnin", "Nice place to visit", "30 min ago"));


    }

    private void getLocalPostDetail(String postId) {
        String selections = TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?";
        String[] selectionargs = {postId};

        Cursor cursor = getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, selections, selectionargs, BaseColumns._ID + " DESC LIMIT 1 ");


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

                    this.like_status = like_status;
                    Log.e("Db like status", "==>" + like_status);


                    i++;


                } while (cursor.moveToNext());
            }

            cursor.close();


            FeedItem item = getPostById(postId);
            Log.i("Item", item.getPost_content());
            setItem(item);

            //lost_data_list, lost_data_id_list, lost_data_obj_id_list ,lost_data_img_url_list
            //storageUtil.SaveArrayListToSD("lost_data_list", lost_data_list);


        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

    }

    private void setItem(FeedItem item) {
        profile.setAdjustViewBounds(true);
        profile.setScaleType(ImageView.ScaleType.CENTER_CROP);


        strLang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);


        if (item.getPost_content_suggest_text() != null && !item.getPost_content_suggest_text().isEmpty()) {
            post_suggest_text.setText(item.getPost_content_suggest_text());

        }
        post_content_user_name.setText(item.getPost_content_user_name());


        if (strLang.equals(com.parse.utils.Utils.ENG_LANG)) {
            postdetail_username.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));


            if (item.getPost_content_type().equalsIgnoreCase("Letter")) {
                postdetail_username.setText("Dear " + user_name);
            } else {
                postdetail_username.setText("");
            }


            //post_timestamp.setText(item.getCreated_at());
            mPostTile.setText(item.getPost_title());
            post_content.setText(item.getPost_content());
            mPostTile.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            post_content.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            post_suggest_text.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));

        } else {
            postdetail_username.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));

            if (item.getPost_content_type().equalsIgnoreCase("Letter")) {
                postdetail_username.setText(" ခ်စ္လွစြာေသာ " + user_name);

            } else {
                postdetail_username.setText("");
            }

            //post_timestamp.setText(item.getCreated_at());
            mPostTile.setText(item.getPost_title_mm());
            post_content.setText(item.getPost_content_mm());
            mPostTile.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            post_content.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            post_suggest_text.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
        }

        if (like_status.equalsIgnoreCase("0")) {
            img_like.setImageResource(R.drawable.like_stroke);
        } else {

            img_like.setImageResource(R.drawable.like_fill);
        }
        txt_like_count.setText(item.getPost_like() + " likes");

        //viewHolder.mCatNameTextView.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        //viewHolder.profilePictureView.setProfileId(item.get());
        if (item.getPost_content_user_img_path() != null && !item.getPost_content_user_img_path().isEmpty()) {
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
        } else {
            profilePictureView.setBackgroundResource(R.drawable.blank_profile);
            profile_item_progressBar.setVisibility(View.GONE);
        }

        // Feed image
        if (item.getPost_img_path() != null && !item.getPost_img_path().isEmpty()) {
            try {
                postIMg.setVisibility(View.VISIBLE);
                feed_item_progressBar.setVisibility(View.VISIBLE);
                share_img_url_data = item.getPost_img_path();
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

            if (cursor != null && cursor.moveToFirst()) {

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
                    like_status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.LIKE_STATUS));
                    status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.STATUS));
                    created_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE));
                    updated_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.UPDATED_DATE));

                    video_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID));
                    post_content_suggest_text = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT));
                    post_content_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_MM));
                    post_content_title_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM));

                    item.setPost_obj_id(post_obj_id);
                    item.setPost_title(post_title);
                    item.setPost_content(post_content);
                    item.setPost_like(post_like);
                    item.setPost_img_path(post_img_path);
                    item.setPost_content_type(post_content_type);
                    item.setPost_content_user_id(post_content_user_id);
                    item.setPost_content_user_name(post_content_user_name);
                    item.setPost_content_user_img_path(post_content_user_img_path);

                    item.setPost_content_video_id(video_id);
                    item.setPost_content_suggest_text(post_content_suggest_text);
                    item.setPost_content_mm(post_content_mm);
                    item.setPost_title_mm(post_content_title_mm);

                    item.setPost_like_status(like_status);

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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.postdetail_like_button:
                if (Connection.isOnline(getApplicationContext())) {

                    if (like_status != null) {


                        if (like_status.equalsIgnoreCase("0")) {
                            /*********************************************************
                             **************Calling Serial No Cloud Code***************/
                            HashMap<String, String> _param = new HashMap<>();
                            _param.put("objectId", postId);

                            ParseCloud.callFunctionInBackground("Likes_increment", _param, new FunctionCallback<Integer>() {
                                @Override
                                public void done(Integer like_count, ParseException e) {

                                    if (e == null) {
                                        //Log.e("Cloud Increment", "===>" + like_count);
                                        txt_like_count.setText(like_count + " likes");
                                        //TODO call updatePost
                                        updatePostLikeStatus(postId, like_count);
                                        like_status = "1";

                                    } else {

                                        Log.e("Cloud Increment", "ERRr" + like_count + e.toString());
                                    }

                                }
                            });
                            img_like.setImageResource(R.drawable.like_fill);
                        } else {
                            //Utils.doToastEng(getApplicationContext(), "unLike click");
                        }
                    }
                } else {
                    if (strLang.equals(Utils.ENG_LANG)) {
                        Utils.doToastEng(getApplicationContext(), "Internet Connection need!");
                    } else {

                        Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
                    }
                }

                break;

            /*case R.id.postdetail_smile_img_upload:
                //Utils.doToastEng(getApplicationContext(),"Smile");
                //et_comment_frame.setVisibility(View.VISIBLE);

                break;*/
            case R.id.postdetail_viber_img:
                /*Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);*/


                /*String sphone = "12345678";
                Uri uri = Uri.parse("tel:" + Uri.encode(sphone));*/
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //FLAG_ACTIVITY_NEW_TASK
                //intent.setData(uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, post_content.getText().toString().substring(0,12)+"...");//Title Of The Post
                intent.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL);
                intent.setType("text/plain");
                getApplicationContext().startActivity(intent);
                break;

            case R.id.postdetail_share_button:
                shareTextUrl();
                break;
            case R.id.postdetail_submit_comment:


                if (Connection.isOnline(getApplicationContext())) {
                    Utils.doToastEng(getApplicationContext(), "Comment");
                    commentParse = new Comment();

                    commentParse.setcomment_contents(et_comment.getText().toString());
                    commentParse.setUserId(user_obj_id);
                    commentParse.setUserName(user_name);
                    commentParse.setpostId(postId);
                    commentParse.setcomment_created_time(new Date());
                    /**Very Important */
                    ParseACL groupACL = new ParseACL();
                    // userList is an Iterable<ParseUser> with the users we are sending this message to.
                    /*for (ParseUser user : userList) {
                    groupACL.setReadAccess(user, true);
                    //groupACL.setWriteAccess(user, true);
                    }*/
                    //groupACL.setRoleReadAccess("");

                    //groupACL.setRoleWriteAccess("");


                    groupACL.setPublicReadAccess(true);

                    commentParse.setACL(groupACL);
                    commentParse.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                et_comment.setText("");


                                //TODO comment adapter notrifieddatasetchange


                            } else {


                                Utils.doToastEng(getApplicationContext(), "Error saving: \" + e.getMessage()");
                                //progressbackground.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                } else {
                    if (strLang.equals(Utils.ENG_LANG)) {
                        Utils.doToastEng(getApplicationContext(), "Internet Connection need!");
                    } else {

                        Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
                    }
                }
                break;

        }

    }

    private void updatePostLikeStatus(String postId, int like_count) {

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, like_count);
        cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "1");
        String selections = TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?";
        String[] selectionargs = {postId};
        getContentResolver().update(IwomenProviderData.PostProvider.CONTETN_URI, cv, selections, selectionargs);

        //Log.e("Update Post like status", "====>" + postId);

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


    /**
     * * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView  ***
     */
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

    /*private void setEmojiconFragment(boolean useSystemDefault) {


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }*/

    /*@Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(et_comment, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(et_comment);
    }*/

    //Share URL
    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);


        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, mPostTile.getText().toString());//Title Of The Post
        if(post_content.getText().length()>20) {
            share_data = post_content.getText().toString().substring(0, 12) + " ...";
        }else{
            share_data = post_content.getText().toString();
        }

        share.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL );

        share.putExtra(Intent.EXTRA_HTML_TEXT, share_data);


        startActivity(Intent.createChooser(share, "I Women Share link!"));
    }

    //TODO FB SHARE

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //profileTracker.stopTracking();
    }
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                //postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }
    private void showError(int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailActivity.this);
        builder.setTitle(R.string.error_dialog_title).
                setMessage(messageId).
                setPositiveButton(R.string.ok, null);
        builder.show();
    }
    public void shareUsingNativeDialog() {

        ShareContent content = getLinkContent();

        // share the app
        if (shareDialog.canShow(content, ShareDialog.Mode.NATIVE)) {
            shareDialog.show(content, ShareDialog.Mode.NATIVE);
        } else {
            showError(R.string.native_share_error);
        }
    }
    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }
    private ShareLinkContent getLinkContent() {

        if(post_content.getText().length() > 20) {
            share_data = post_content.getText().toString().substring(0, 20) + " ...";
        }else{
            share_data = post_content.getText().toString();
        }

        return new ShareLinkContent.Builder()

                .setContentTitle(mPostTile.getText().toString())
                .setContentUrl(Uri.parse(CommonConfig.SHARE_URL))
                .setImageUrl(Uri.parse(share_img_url_data))
                .setContentDescription(share_data)
                .build();
    }
    //It is not work well in all device
    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();

        if(post_content.getText().length() > 20) {
            share_data = post_content.getText().toString().substring(0, 12) + " ...";
        }else{
            share_data = post_content.getText().toString();
        }

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Lost And Found")
                .setContentDescription(share_data
                )
                .setContentUrl(Uri.parse(CommonConfig.SHARE_URL))
                .build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }
    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else {
                // We need to get new permissions, then complete the action when we get called back.
                LoginManager.getInstance().logInWithPublishPermissions(
                        this,
                        Arrays.asList(PERMISSION));
                return;
            }
        }

        if (allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }



}
