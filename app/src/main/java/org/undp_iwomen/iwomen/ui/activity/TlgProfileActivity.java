package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.parse.utils.Utils;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.TlgProfileAPI;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TlgProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public CustomTextView textViewTitle;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;

    private TextView txt_tlg_group_name;
    private TextView txt_tlg_group_address;

    private TextView txt_tlg_leader_name;
    private TextView txt_tlg_leader_role_lbl;

    private TextView txt_tlg_info_who_we_are_lbl;
    private TextView txt_tlg_member_lbl;
    private TextView txt_tlg_member_txt;

    private TextView txt_tlg_srg_member_lbl;
    private TextView txt_tlg_srg_member_txt;

    private TextView txt_tlg_key_activity_lbl;
    private TextView txt_tlg_key_activity_txt;

    private TextView txt_tlg_key_skill_lbl;
    private TextView txt_tlg_key_skill_txt;

    private ImageView img_tlg_ph_no;
    private ImageView img_tlg_viber_no;
    private ImageView img_tlg_fb_link;
    private RoundedImageView leaderProfileImg;
    private RoundedImageView tlgLogoImg;

    String tlgName;
    String tlgAddress;
    String tlgId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tlg_profile);

        Bundle bundle = getIntent().getExtras();
        tlgName = bundle.getString("TLGName");
        tlgId = bundle.getString("TLGID");
        tlgAddress = bundle.getString("TLGAddress");
        mContext = getApplicationContext();
        Log.e("tlgId","===>" +tlgId +"///" + tlgName);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        //textViewTitle.setText("Myanma\u0020Deals");//"MYANMARDEALS"
        textViewTitle.setText(R.string.app_name);
        //textViewTitle.setTypeface();


        txt_tlg_group_name = (TextView)findViewById(R.id.tlg_group_name);
        txt_tlg_group_address = (TextView)findViewById(R.id.tlg_group_address);

        txt_tlg_leader_name = (TextView)findViewById(R.id.tlg_leader_profile_username);
        txt_tlg_leader_role_lbl = (TextView)findViewById(R.id.tlg_leader_lbl);

        txt_tlg_info_who_we_are_lbl = (TextView)findViewById(R.id.tlg_who_are_u);
        txt_tlg_member_lbl = (TextView)findViewById(R.id.tlg_member_txt_lbl);
        txt_tlg_member_txt = (TextView)findViewById(R.id.tlg_member_txt);

        txt_tlg_srg_member_lbl = (TextView)findViewById(R.id.tlg_srg_lbl);
        txt_tlg_srg_member_txt = (TextView)findViewById(R.id.tlg_srg_txt);

        txt_tlg_key_activity_lbl = (TextView)findViewById(R.id.tgl_key_activity_lbl);
        txt_tlg_key_activity_txt = (TextView)findViewById(R.id.tlg_key_acitvity_txt);

        txt_tlg_key_skill_lbl = (TextView)findViewById(R.id.tlg_key_kill_lbl);
        txt_tlg_key_skill_txt = (TextView)findViewById(R.id.tlg_key_skill_txt);

        img_tlg_ph_no = (ImageView)findViewById(R.id.tlg_ph_img);
        img_tlg_viber_no = (ImageView)findViewById(R.id.tlg_viber_img);
        img_tlg_fb_link = (ImageView)findViewById(R.id.tlg_fb_img);

        leaderProfileImg = (RoundedImageView)findViewById(R.id.tlg_leader_profilePic_rounded);
        tlgLogoImg = (RoundedImageView)findViewById(R.id.tlg_group_logo_img);


        img_tlg_ph_no.setOnClickListener(this);
        img_tlg_viber_no.setOnClickListener(this);
        img_tlg_viber_no.setOnClickListener(this);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        if(strLang.equals(Utils.ENG_LANG)) {
            setEnglishFont();
        }else if(strLang.equals(Utils.MM_LANG)){
            setMyanmarFont();
        }


        getTLGDetailByIdFromSever();

    }

    public void setEnglishFont(){



        if( tlgName.equalsIgnoreCase("May Tot Ah Man")){
            textViewTitle.setText(R.string.tlg_group_name_eng);

            txt_tlg_group_name.setText(R.string.tlg_group_name_eng);
            txt_tlg_group_address.setText(R.string.tlg_group_address_eng);
            txt_tlg_leader_name.setText(R.string.tlg_leader_name_eng);
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_eng);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_eng);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_eng);
            txt_tlg_member_txt.setText(R.string.tlg_member_txt_eng);

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_eng);
            txt_tlg_srg_member_txt.setText(R.string.tlg_srg_member_txt_eng);

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_eng);
            txt_tlg_key_activity_txt.setText(R.string.tlg_key_activity_txt_eng);

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_eng);
            txt_tlg_key_skill_txt.setText(R.string.tlg_key_skill_txt_eng);

            tlgLogoImg.setImageResource(R.drawable.maydoearrman);
            leaderProfileImg.setImageResource(R.drawable.tlg_leader);
        }else {

            textViewTitle.setText(tlgName);

            txt_tlg_group_name.setText(tlgName);


            txt_tlg_group_address.setText(tlgAddress);


            tlgLogoImg.setImageResource(R.drawable.place_holder);
            leaderProfileImg.setImageResource(R.drawable.blank_profile);

            txt_tlg_leader_name.setText("");
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_eng);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_eng);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_eng);
            txt_tlg_member_txt.setText("");

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_eng);
            txt_tlg_srg_member_txt.setText("....");

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_eng);
            txt_tlg_key_activity_txt.setText("...");

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_eng);
            txt_tlg_key_skill_txt.setText("....");

        }
        textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_group_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_group_address.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_leader_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_leader_role_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_info_who_we_are_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_srg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_srg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_activity_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_activity_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_skill_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_skill_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));






    }
    public void setMyanmarFont(){


        if( tlgName.equalsIgnoreCase("May Tot Ah Man")){
            textViewTitle.setText(R.string.tlg_group_name_mm);

            txt_tlg_group_name.setText(R.string.tlg_group_name_mm);
            txt_tlg_group_address.setText(R.string.tlg_group_address_mm);
            txt_tlg_leader_name.setText(R.string.tlg_leader_name_mm);
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_mm);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_mm);
            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_mm);
            txt_tlg_member_txt.setText(R.string.tlg_member_txt_mm);
            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_mm);
            txt_tlg_srg_member_txt.setText(R.string.tlg_srg_member_txt_mm);
            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_mm);
            txt_tlg_key_activity_txt.setText(R.string.tlg_key_activity_txt_mm);
            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_mm);
            txt_tlg_key_skill_txt.setText(R.string.tlg_key_skill_txt_mm);
        }else{

            textViewTitle.setText(tlgName);

            txt_tlg_group_name.setText(tlgName);


            txt_tlg_group_address.setText(tlgAddress);

            tlgLogoImg.setImageResource(R.drawable.place_holder);
            leaderProfileImg.setImageResource(R.drawable.blank_profile);

            txt_tlg_leader_name.setText("");
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_mm);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_mm);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_mm);
            txt_tlg_member_txt.setText("");

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_mm);
            txt_tlg_srg_member_txt.setText("....");

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_mm);
            txt_tlg_key_activity_txt.setText("...");

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_mm);
            txt_tlg_key_skill_txt.setText("....");

        }


        //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_group_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_group_address.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_leader_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_leader_role_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_info_who_we_are_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_srg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_srg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_activity_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_activity_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_skill_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_skill_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));


    }

    private void getTLGDetailByIdFromSever() {
        if (Connection.isOnline(mContext)) {
            TlgProfileAPI.getInstance().getService().getTlgProfileDetailById("", new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }else {

            if (strLang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                org.undp_iwomen.iwomen.utils.Utils.doToastEng(mContext, "Internet Connection need!");
            } else {

                org.undp_iwomen.iwomen.utils.Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }
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
        if (id == android.R.id.home) {

            finish();
            return true;


        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tlg_ph_img:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                startActivity(callIntent);

                break;
            case R.id.tlg_viber_img:
                String sphone = "12345678";
                Uri uri = Uri.parse("tel:" + Uri.encode(sphone));
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //FLAG_ACTIVITY_NEW_TASK
                intent.setData(uri);
                //intent.putExtra(Intent.EXTRA_SUBJECT, post_content.getText().toString().substring(0, 12) + "...");//Title Of The Post
                //intent.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL);
                //intent.setType("text/plain");
                getApplicationContext().startActivity(intent);
                break;
            case R.id.tlg_fb_img:

                startActivity(getOpenFacebookIntent(getApplicationContext()));




                break;
        }
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/testingmobileapp"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/1457723087870355"));
        }
    }

}
