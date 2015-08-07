package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.adapter.DrawerListViewAdapter;
import org.undp_iwomen.iwomen.ui.fragment.ResourcesFragment;
import org.undp_iwomen.iwomen.ui.fragment.StoriesFragment;

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
    private TextView textViewTitle;
    private TextView txt_user_name;

    private static final int LOGIN_REQUEST = 0;
    private ParseUser currentUser;

    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private String user_name, user_obj_id, user_ph;

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

        setContentView(R.layout.main_drawer_material);


        //getFacebookHashKey();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.ic_action_myanmadeals_app_icon);
        textViewTitle = (TextView) toolbar.findViewById(R.id.title_action2);

        //textViewTitle.setText("Myanma\u0020Deals");//"MYANMARDEALS"
        textViewTitle.setText(R.string.app_name);
        //textViewTitle.setTypeface(faceBold);


        drawerLayoutt = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);

        mDrawerList = (ListView) findViewById(R.id.left_drawer_lv);
        txt_user_name = (TextView)findViewById(R.id.txt_user_name);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayoutt.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        LoadDrawerCustomData();


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutt, toolbar, R.string.app_name, R.string.app_name);
        drawerLayoutt.setDrawerListener(actionBarDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);



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

         currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            // User clicked to log out.
            //showProfileLoggedOut();
            if (savedInstanceState == null) {
                selectItem(0);

                if (!mSharedPreferencesUserInfo.getBoolean(CommonConfig.IS_LOGIN, false)) {

                    Bundle bundle = getIntent().getExtras();
                    String objectId = bundle.getString(CommonConfig.USER_OBJ_ID);
                    String userName = bundle.getString(CommonConfig.USER_NAME);
                    String userPH = bundle.getString(CommonConfig.USER_PH);


                    if (objectId.length() != 0) {
                        mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                        mEditorUserInfo.putBoolean(CommonConfig.IS_LOGIN, true);
                        mEditorUserInfo.putString(CommonConfig.USER_OBJ_ID, objectId);
                        mEditorUserInfo.putString(CommonConfig.USER_NAME, userName);

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
                    //Toast.makeText(this, "You are login as at Payment \n " + objectId + "\n" + userName + userEmail, Toast.LENGTH_LONG).show();
                    Log.e("==== > user Info parse", objectId + "Parse :" + userName + ":session >>" + user_obj_id);
                } else {
                    user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
                    user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);
                    //Toast.makeText(this, "2ndTime(PaymentActivity) You are login as \n " + mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null) + "\n" + mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null), Toast.LENGTH_LONG).show();


                    txt_user_name.setText(user_name);
                }

            }
        } else {

            // User clicked to log in.
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    DrawerMainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);


        }


    }

    public void LoadDrawerCustomData() {

        DrawerListName = new String[]
                {"Stories", "Community", "Resources", "News", "Setting"};
        DrawerListIcon = new int[]
                {R.drawable.ic_stories, R.drawable.ic_community, R.drawable.ic_resources, R.drawable.ic_news, R.drawable.ic_setting};


        DrawerListViewAdapter drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
        drawer_adapter.notifyDataSetChanged();
        mDrawerList.setAdapter(drawer_adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        setListViewHeightBasedOnChildren(mDrawerList);

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
        StoriesFragment storiesFragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putInt(StoriesFragment.ARG_MENU_INDEX, position);
        storiesFragment.setArguments(args);


        ResourcesFragment resourcesFragment = new ResourcesFragment();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();


        switch (position) {
            case 0://Categories 1
                fragmentManager.beginTransaction().replace(R.id.content_frame, storiesFragment).commit();
                setTitle(DrawerListName[position]);
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.content_frame, resourcesFragment).commit();
                setTitle(DrawerListName[position]);
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.content_frame, resourcesFragment).commit();

                //materialTab.setRetainInstance(true);

                setTitle(DrawerListName[position]);
                break;
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);

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


}
