package org.undp_iwomen.iwomen.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.makeramen.RoundedImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.retrofit_api.UserPostAPI;
import org.undp_iwomen.iwomen.ui.adapter.EditProfileGridviewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ExpandableHeightGridView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileEditActivity extends AppCompatActivity {


    private CustomTextView textViewTitle;


    private RoundedImageView profileImg;
    private ProgressBar profileProgressbar;

    private EditProfileGridviewAdapter mAdapter;
    private ArrayList<String> listShopName;
    private ArrayList<String> listShopImg;
    private ExpandableHeightGridView gridView;


    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    private ProgressDialog mProgressDialog;

    String mstrUserId, mstrTitleMm, mstrContentEng;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;

    private String userprofile_Image_path;

    String[] cameraList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_main);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(ProfileEditActivity.this);
        mProgressDialog.setCancelable(false);

        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();

        mstrUserId = i.getStringExtra("UserId");

        userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_IMAGE_PATH, null);


        profileImg = (RoundedImageView) findViewById(R.id.edit_profile_profilePic_rounded);
        profileProgressbar = (ProgressBar) findViewById(R.id.edit_profile_progressBar_profile_item);
        profileImg.setAdjustViewBounds(true);
        profileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);


        strLang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);


        if (strLang.equals(Utils.ENG_LANG)) {

            textViewTitle.setText(R.string.edit_profile_eng);

            cameraList = new String[]{getResources().getString(R.string.new_post_take_photo_eng),getResources().getString(R.string.new_post_upload_photo_eng)};

        } else //FOR ALl MM FONT
        {
            textViewTitle.setText(R.string.edit_profile_mm);

            cameraList = new String[]{getResources().getString(R.string.new_post_take_photo_mm),getResources().getString(R.string.new_post_upload_photo_mm)};


        }

        userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_IMAGE_PATH, null);


        if (userprofile_Image_path != null) {

            try {

                Picasso.with(getApplicationContext())
                        .load(userprofile_Image_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(profileImg, new ImageLoadedCallback(profileProgressbar) {
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
            profileProgressbar.setVisibility(View.GONE);
        }

        gridView = (ExpandableHeightGridView) findViewById(R.id.edit_profile_gv);
        /**********Ajust Layout Image size depend on screen at Explore ************/
        prepareList();



        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (strLang.equals(Utils.ENG_LANG)) {
                    new MaterialDialog.Builder(ProfileEditActivity.this)
                            .title(R.string.choose_photo_eng)
                            .items(cameraList)
                            .typeface("roboto-medium.ttf", "roboto-medium.ttf")
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                            Utils.doToastEng(mContext, "Choose Comming Soon" + which + text.toString());
                                /*String phno = "tel:" + text.toString();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phno));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);*/

                                        }
                                    })
                            .show();
                }else{
                    new MaterialDialog.Builder(ProfileEditActivity.this)
                            .title(R.string.choose_photo_mm)
                            .items(cameraList)
                            .typeface("zawgyi.ttf", "zawgyi.ttf")

                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                    Utils.doToastEng(mContext, "Choose Comming Soon" + which + text.toString());
                                /*String phno = "tel:" + text.toString();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phno));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);*/

                                }
                            })
                            .show();
                }

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //Toast.makeText(mContext, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                //profileImg.setImageResource(listShopImg.get(position));
                profileProgressbar.setVisibility(View.GONE);

                mEditorUserInfo = mSharedPreferencesUserInfo.edit();

                if (listShopImg.get(position) != null && listShopImg.get(position) != "") {
                    try {

                        Picasso.with(getApplicationContext())
                                .load(listShopImg.get(position)) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                .placeholder(R.drawable.blank_profile)
                                .error(R.drawable.blank_profile)
                                .into(profileImg, new ImageLoadedCallback(profileProgressbar) {
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

                }

                //TODO selected Image path in main
                //Log.e("==","");
                userprofile_Image_path = listShopImg.get(position);
                mEditorUserInfo.putString(CommonConfig.USER_IMAGE_PATH, listShopImg.get(position));
                mEditorUserInfo.commit();


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            //finish();

            mProgressDialog.show();
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.getInBackground(mstrUserId, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if(e == null){
                        parseUser.put("userImgPath",userprofile_Image_path);
                        parseUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) {
                                    mProgressDialog.dismiss();
                                    startDrawerMainActivity();
                                }
                            }
                        });
                    }
                }
            });



            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void startDrawerMainActivity(){
        Intent intent = new Intent(this, DrawerMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void prepareList() {

        if (Connection.isOnline(mContext)) {
            listShopName = new ArrayList<String>();
            listShopImg = new ArrayList<String>();

            mProgressDialog.show();

            UserPostAPI.getInstance().getService().getAllStickers(new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                    try {
                        listShopName.clear();
                        listShopImg.clear();
                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject each_object = result.getJSONObject(i);

                            if (!each_object.isNull("objectId")) {


                                listShopName.add(each_object.getString("objectId"));

                            } else {
                                listShopName.add("");
                            }

                            if (!each_object.isNull("stickerImg")) {


                                JSONObject ObjjsonObject = each_object.getJSONObject("stickerImg");
                                if (!ObjjsonObject.isNull("url")) {


                                    listShopImg.add(ObjjsonObject.getString("url"));
                                } else {
                                    listShopImg.add("");
                                }


                            } else {
                                listShopImg.add("");
                            }


                        }

                        mProgressDialog.dismiss();
                        mAdapter = new EditProfileGridviewAdapter(mContext, listShopName, listShopImg);
                        // Set custom adapter to gridview

                        gridView.setExpanded(true);
                        gridView.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mProgressDialog.dismiss();
                }
            });
        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (strLang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }



        /*listShopName.add("sticker_bird");
        listShopName.add("sticker_black_cat");
        listShopName.add("sticker_chicken");
        listShopName.add("sticker_chihuahua");

        listShopName.add("sticker_cow");
        listShopName.add("sticker_frog");
        listShopName.add("sticker_hamster");
        listShopName.add("sticker_horse");
        listShopName.add("sticker_mouse");

        listShopName.add("sticker_pig");
        listShopName.add("sticker_pony");
        listShopName.add("sticker_puppy");*/




        /*listShopImg.add(R.drawable.sticker_bird);
        listShopImg.add(R.drawable.sticker_black_cat);
        listShopImg.add(R.drawable.sticker_chicken);
        listShopImg.add(R.drawable.sticker_chihuahua);

        listShopImg.add(R.drawable.sticker_cow);
        listShopImg.add(R.drawable.sticker_frog);
        listShopImg.add(R.drawable.sticker_hamster);
        listShopImg.add(R.drawable.sticker_horse);

        listShopImg.add(R.drawable.sticker_mouse);
        listShopImg.add(R.drawable.sticker_pig);
        listShopImg.add(R.drawable.sticker_pony);
        listShopImg.add(R.drawable.sticker_puppy);*/


        //1.ic_westernfood  2. ic_streetfoods 3. ic_tea
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
