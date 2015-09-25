package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.makeramen.RoundedImageView;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.parse.utils.Utils;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.adapter.DrawerListViewAdapter;
import org.undp_iwomen.iwomen.ui.fragment.BeTogetherFragment;
import org.undp_iwomen.iwomen.ui.fragment.GoogleMapFragment;
import org.undp_iwomen.iwomen.ui.fragment.MainMaterialTab;
import org.undp_iwomen.iwomen.ui.fragment.ResourcesFragment;
import org.undp_iwomen.iwomen.ui.fragment.SettingsFragment;
import org.undp_iwomen.iwomen.ui.fragment.SisterAppFragment;
import org.undp_iwomen.iwomen.ui.fragment.TLGUserStoriesRecentFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ProfilePictureView;
import org.undp_iwomen.iwomen.utils.SharePrefUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by khinsandar on 7/29/15.
 */


public class DrawerMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayoutt;
    private LinearLayout mDrawerLinearLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    private String[] DrawerListName;
    private int[] DrawerListIcon;
    private CharSequence mTitle;
    public CustomTextView textViewTitle;
    private TextView txt_user_name;
    private CustomTextView txt_sing_out;

    private static final int LOGIN_REQUEST = 0;
    private ParseUser currentUser;

    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private String user_name, user_obj_id, user_ph;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    Runnable run;
    DrawerListViewAdapter drawer_adapter;

    ProfilePictureView userProfilePicture;
    ProgressBar drawer_progressBar_profile_item;


    RoundedImageView drawer_profilePic_rounded;
    String userprofile_Image_path;

    String mstrUserfbId;
    SharePrefUtils sessionManager;

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        /*if (currentUser != null) {
            showProfileLoggedIn();
        } else {
            showProfileLoggedOut();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.

        org.undp_iwomen.iwomen.utils.Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.main_drawer_material);

        //Back from LoginActivity Control
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        //getFacebookHashKey();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.ic_action_myanmadeals_app_icon);
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        //textViewTitle.setText("Myanma\u0020Deals");//"MYANMARDEALS"
        textViewTitle.setText(R.string.app_name);
        //textViewTitle.setTypeface(faceBold);


        drawerLayoutt = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        userProfilePicture = (ProfilePictureView) findViewById(R.id.profilePicture);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_lv);
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        txt_sing_out = (CustomTextView) findViewById(R.id.menu_sing_out);

        drawer_profilePic_rounded = (RoundedImageView) findViewById(R.id.drawer_profilePic_rounded);
        drawer_progressBar_profile_item = (ProgressBar) findViewById(R.id.drawer_progressBar_profile_item);
        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayoutt.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutt, toolbar, R.string.app_name, R.string.app_name);
        drawerLayoutt.setDrawerListener(actionBarDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // set up the drawer's list view with items and click listener
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        sessionManager = new SharePrefUtils(getApplicationContext());
       /* if (user_obj_id == null) {

            if (savedInstanceState == null) {
                selectItem(0);
            }
        } else {
            // User clicked to log in.
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    DrawerMainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);

        }*/
        Bundle bundle = getIntent().getExtras();

        currentUser = ParseUser.getCurrentUser();


        if (currentUser != null) {
            // User clicked to log out.
            //showProfileLoggedOut();
            if (savedInstanceState == null) {


                if (!mSharedPreferencesUserInfo.getBoolean(CommonConfig.IS_LOGIN, false)) {


                    String objectId = bundle.getString(CommonConfig.USER_OBJ_ID);
                    String userName = bundle.getString(CommonConfig.USER_NAME);
                    String userPH = bundle.getString(CommonConfig.USER_PH);
                    String lang = bundle.getString(Utils.PREF_SETTING_LANG);
                    Boolean isFbInclude = bundle.getBoolean(CommonConfig.USER_FBID_INCLUDE);


                    if (objectId.length() != 0) {
                        mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                        mEditorUserInfo.putBoolean(CommonConfig.IS_LOGIN, true);
                        mEditorUserInfo.putString(CommonConfig.USER_OBJ_ID, objectId);
                        mEditorUserInfo.putString(CommonConfig.USER_NAME, userName);
                        if (isFbInclude) {
                            mstrUserfbId = bundle.getString(CommonConfig.USER_FBID);
                            mEditorUserInfo.putString(CommonConfig.USER_FBID, mstrUserfbId);
                        }

                        SharedPreferences.Editor editor = sharePrefLanguageUtil.edit();
                        editor.putString(Utils.PREF_SETTING_LANG, lang);

                /*if(userNrc != null) {
                    mEditorUserInfo.putString(CommonConfig.USER_NRC, userNrc);
                }else if(passport != null){
                    mEditorUserInfo.putString(CommonConfig.USER_NRC, passport); // For rare register with Passport case
                }*/
                        mEditorUserInfo.putString(CommonConfig.USER_PH, userPH);

                        mEditorUserInfo.commit();
                    }

                    user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
                    user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);

                    txt_user_name.setText(user_name);

                    if (mstrUserfbId != null) {

                        userProfilePicture.setProfileId(mstrUserfbId);
                    }
                    if (currentUser.get("user_profile_img") != null) {


                        Uri uri = Uri.parse(currentUser.getParseFile("user_profile_img").getUrl());
                        userprofile_Image_path = currentUser.getParseFile("user_profile_img").getUrl();
                        Log.e("1st userprofile_Image_path", "==>" + userprofile_Image_path);
                        //userProfilePicture.setD

                        if (userprofile_Image_path != null) {

                            mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                            mEditorUserInfo.putString(CommonConfig.USER_IMAGE_PATH, userprofile_Image_path);

                            mEditorUserInfo.commit();
                            try {
                                drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                                userProfilePicture.setVisibility(View.GONE);
                                Picasso.with(getApplicationContext())
                                        .load(userprofile_Image_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                        .placeholder(R.drawable.blank_profile)
                                        .error(R.drawable.blank_profile)
                                        .into(drawer_profilePic_rounded, new ImageLoadedCallback(drawer_progressBar_profile_item) {
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
                            drawer_progressBar_profile_item.setVisibility(View.GONE);
                            drawer_profilePic_rounded.setVisibility(View.GONE);
                            userProfilePicture.setVisibility(View.VISIBLE);
                        }

                    }

                    Log.e("1st Login==== > user Info parse", objectId + "Parse :" + userName + ":mstrUserfbId >>" + mstrUserfbId);

                } else {
                    user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
                    user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);
                    //Toast.makeText(this, "2ndTime(PaymentActivity) You are login as \n " + mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null) + "\n" + sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG), Toast.LENGTH_LONG).show();

                    mstrUserfbId = mSharedPreferencesUserInfo.getString(CommonConfig.USER_FBID, null);
                    //2nd Login==== > user Info parse﹕ GuzTg3T1aWParse :Su:mstrUserfbId >>null
                    Log.e("2nd Login==== > user Info parse", user_obj_id + "Parse :" + user_name + ":mstrUserfbId >>" + mstrUserfbId);

                    if (mstrUserfbId != null) {


                        userProfilePicture.setProfileId(mstrUserfbId);
                        drawer_progressBar_profile_item.setVisibility(View.GONE);

                    } else {
                        drawer_progressBar_profile_item.setVisibility(View.GONE);
                        drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                        userProfilePicture.setVisibility(View.GONE);
                    }

                    userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_IMAGE_PATH, null);


                    if (userprofile_Image_path != null) {

                        try {
                            drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                            userProfilePicture.setVisibility(View.GONE);
                            Picasso.with(getApplicationContext())
                                    .load(userprofile_Image_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                    .placeholder(R.drawable.blank_profile)
                                    .error(R.drawable.blank_profile)
                                    .into(drawer_profilePic_rounded, new ImageLoadedCallback(drawer_progressBar_profile_item) {
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
                        drawer_progressBar_profile_item.setVisibility(View.GONE);
                        drawer_profilePic_rounded.setVisibility(View.GONE);
                        userProfilePicture.setVisibility(View.VISIBLE);
                    }


                    txt_user_name.setText(user_name);

                }

                //TODO FONT DRAWERMAIN
                mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {

                    txt_sing_out.setText("Sign Out");
                    txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
                } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
                    txt_sing_out.setText("ထ\u103Cက္ရန္");
                    txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
                } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
                    txt_sing_out.setText("ထ\u103Cက္ရန္");
                    //txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
                } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
                    txt_sing_out.setText("ထ\u103Cက္ရန္");
                    //txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
                }

                LoadDrawerCustomData();
                selectItem(0);
                drawerLayoutt.openDrawer(mDrawerLinearLayout);

            }


        } else {

            // User clicked to log in.
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    DrawerMainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);


        }

        txt_sing_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    ParseUser.logOut();
                    sessionManager.ClearLogOut();
                    // User clicked to log in.
                    ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                            DrawerMainActivity.this);
                    startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);

                }
            }
        });


        run = new Runnable() {
            @Override
            public void run() {


                //queryToBooking();

                drawer_adapter.notifyDataSetChanged();
                mDrawerList.invalidateViews();
                mDrawerList.refreshDrawableState();

                //Log.e("Load Adapter===","==runRunnable=" );
            }
        };


    }

    public void setThemeToApp() {
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING_LANG, MODE_PRIVATE);
        int theme = sharePrefLanguageUtil.getInt(org.undp_iwomen.iwomen.utils.Utils.PREF_THEME, org.undp_iwomen.iwomen.utils.Utils.THEME_PINK);

        if (theme == org.undp_iwomen.iwomen.utils.Utils.THEME_BLUE) {
            setTheme(R.style.AppTheme_Blue);
        } else if (theme == org.undp_iwomen.iwomen.utils.Utils.THEME_PINK) {
            setTheme(R.style.AppTheme);
        } else if (theme == org.undp_iwomen.iwomen.utils.Utils.THEME_YELLOW) {
            setTheme(R.style.AppTheme_Yellow);
        }


    }

    public void LoadDrawerCustomData() {

        /*DrawerListName = new String[]
                {"Stories",  "Resources", "Setting","AboutUs"};*/

        //TODO FONT DRAWERMAIN
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            DrawerListName = new String[]
                    {"Be Inspired", "Be Knowledgeable", "Be Together", "Talk Together", "Setting", "AboutUs", "Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories, R.drawable.ic_resources, R.drawable.be_together, R.drawable.ic_talk_together, R.drawable.ic_setting, R.drawable.about_us, R.drawable.sister_app};

            // R.drawable.ic_community, R.drawable.ic_news

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            DrawerListName = new String[]
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories, R.drawable.ic_resources, R.drawable.be_together, R.drawable.ic_talk_together, R.drawable.ic_setting, R.drawable.about_us, R.drawable.sister_app};

            // R.drawable.ic_community, R.drawable.ic_news

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        }else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
            DrawerListName = new String[]
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories, R.drawable.ic_resources, R.drawable.be_together, R.drawable.ic_talk_together, R.drawable.ic_setting, R.drawable.about_us, R.drawable.sister_app};

            // R.drawable.ic_community, R.drawable.ic_news

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        }else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
            DrawerListName = new String[]
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories, R.drawable.ic_resources, R.drawable.be_together, R.drawable.ic_talk_together, R.drawable.ic_setting, R.drawable.about_us, R.drawable.sister_app};

            // R.drawable.ic_community, R.drawable.ic_news

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        }else {
            DrawerListName = new String[]
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories, R.drawable.ic_resources, R.drawable.be_together, R.drawable.ic_talk_together, R.drawable.ic_setting, R.drawable.about_us, R.drawable.sister_app};

            // R.drawable.ic_community, R.drawable.ic_news

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        }


    }

    /**
     * **************** ListView WIthin ScrollView Step1 ********************
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        //
        MainMaterialTab mainMaterialTab = new MainMaterialTab();
        /*StoriesFragment storiesFragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putInt(StoriesFragment.ARG_MENU_INDEX, position);
        storiesFragment.setArguments(args);*/


        ResourcesFragment resourcesFragment = new ResourcesFragment();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        SettingsFragment settingsFragment = new SettingsFragment();

        BeTogetherFragment beTogetherFragment = new BeTogetherFragment();

        GoogleMapFragment googleMapFragment = new GoogleMapFragment();

        SisterAppFragment sisterAppFragment = new SisterAppFragment();

        TLGUserStoriesRecentFragment tlgUserStoriesRecentFragment = new TLGUserStoriesRecentFragment();
        switch (position) {
            case 0://Categories 1
                fragmentManager.beginTransaction().replace(R.id.content_frame, mainMaterialTab).commit();
                setTitle(DrawerListName[position]);
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.content_frame, resourcesFragment).commit();
                setTitle(DrawerListName[position]);
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.content_frame, googleMapFragment).commit();
                setTitle(DrawerListName[position]);
                break;

            case 3:
                fragmentManager.beginTransaction().replace(R.id.content_frame, tlgUserStoriesRecentFragment).commit();
                setTitle(DrawerListName[position]);
                break;
            case 4:

                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                //fragmentManager.beginTransaction().replace(R.id.content_frame, settingsFragment).commit();


                setTitle(DrawerListName[position]);
                break;
            case 5:
                Intent intent2 = new Intent(this, AboutUsWebActivity.class);
                startActivity(intent2);
                break;
            case 6:
                fragmentManager.beginTransaction().replace(R.id.content_frame, sisterAppFragment).commit();
                setTitle(DrawerListName[position]);

                break;//Sister apps
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);

        //TODO close drawer
        drawerLayoutt.closeDrawer(mDrawerLinearLayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        //getSupportActionBar().setTitle(title); // Black Letter showing default
        mTitle = title;
        //toolbar.setTitle(mTitle);

        //getActionBar().setTitle(mTitle);
        textViewTitle.setText(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void getFacebookHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("org.undp_iwomen.iwomen", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException ex) {


        } catch (NoSuchAlgorithmException e) {

        }
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
