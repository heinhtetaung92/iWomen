package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.utils.Utils;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;


public class TlgProfileActivity extends AppCompatActivity {

    public TextView textViewTitle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tlg_profile);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        textViewTitle = (TextView) toolbar.findViewById(R.id.title_action2);
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


        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        if(strLang.equals(Utils.ENG_LANG)) {
            setEnglishFont();
        }else if(strLang.equals(Utils.MM_LANG)){
            setMyanmarFont();
        }

    }

    public void setEnglishFont(){
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

        textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
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
