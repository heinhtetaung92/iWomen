package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;


public class ResourceDetailActivity extends AppCompatActivity {


    private TextView textViewTitle;
    private TextView txtName;
    private TextView txtBody;
    private RoundedImageView profileImg;
    private ProgressBar profileProgressbar;
    private TextView profileName;

    private String title;
    private String bodyText;
    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sub_resource);
        sharePrefLanguageUtil = getSharedPreferences(com.parse.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");


        textViewTitle = (TextView) toolbar.findViewById(R.id.title_action2);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i  =getIntent();
        bodyText = i.getStringExtra("Text");
        title = i.getStringExtra("Name");

        txtName = (TextView)findViewById(R.id.tipdetail_title_name);
        txtBody = (TextView)findViewById(R.id.tipdetail_body);
        profileImg = (RoundedImageView)findViewById(R.id.tipdetail_profilePic_rounded);
        profileName = (TextView)findViewById(R.id.tipdetail_content_username);
        profileProgressbar = (ProgressBar)findViewById(R.id.tipdetail_progressBar_profile_item);


        txtBody.setText(bodyText);
        txtName.setText(title);

        strLang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);
        if(strLang.equals(com.parse.utils.Utils.ENG_LANG)){

            textViewTitle.setText(R.string.leadership_eng);

            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));


            txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else{
            textViewTitle.setText(R.string.leadership_mm);

            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));


            txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }



        profileProgressbar.setVisibility(View.GONE);

        profileImg.setAdjustViewBounds(true);
        profileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profileImg.setImageResource(R.drawable.astrid);
        profileName.setText("Dr Astrid Tuminez ");


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
