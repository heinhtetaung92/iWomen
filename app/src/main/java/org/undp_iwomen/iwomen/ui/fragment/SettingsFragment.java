package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.activity.SettingActivity;
import org.undp_iwomen.iwomen.utils.Utils;

/**
 * Created by khinsandar on 8/7/15.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private RadioGroup radioLanguageGroup;
    private RadioButton radioLanguageButton;

    RadioButton rd_lang_en, rd_lang_mm;
    SharedPreferences sharePref;

    TextView settings_language_setting_title;
    private Context mContext;
    private CheckBox chk_settings_getnotification;
    private TextView settings_changeTheme;

    public void SettingsFragment(Context context){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {


        mContext = getActivity().getApplicationContext();


        sharePref = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        radioLanguageGroup = (RadioGroup) rootView.findViewById(R.id.settings_language);
        rd_lang_en = (RadioButton) rootView.findViewById(R.id.settings_english_language);
        rd_lang_mm = (RadioButton) rootView.findViewById(R.id.settings_myanmar_language);
        settings_language_setting_title = (TextView)rootView.findViewById(R.id.settings_language_setting_title);
        settings_changeTheme = (TextView)rootView.findViewById(R.id.settings_changeTheme);
        chk_settings_getnotification = (CheckBox)rootView.findViewById(R.id.settings_getnotification);

        String lang = sharePref.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        rd_lang_en.setOnCheckedChangeListener(this);
        rd_lang_mm.setOnCheckedChangeListener(this);
        if(lang.equals(Utils.ENG_LANG)){
            rd_lang_en.setChecked(true);
            setEnglishFont();
        }
        else if(lang.equals(Utils.MM_LANG)){
            rd_lang_mm.setChecked(true);
            setMyanmarFont();
        }



    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sharePref.edit();


        if(isChecked) {
            if (buttonView.getId() == R.id.settings_english_language) {

                editor.putString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

                setEnglishFont();


            } else if (buttonView.getId() == R.id.settings_myanmar_language) {
                editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG);

                setMyanmarFont();

            }

        }
        editor.commit();
    }

    public void setEnglishFont(){

        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings);
        settings_language_setting_title.setText(R.string.title_action_settings_eng);
        chk_settings_getnotification.setText(R.string.title_notrification_eng);
        settings_changeTheme.setText(R.string.title_change_theme_eng);

        ((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

    }
    public void setMyanmarFont(){
        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings_mm);
        settings_language_setting_title.setText(R.string.title_action_settings_mm);
        chk_settings_getnotification.setText(R.string.title_notrification_mm);
        settings_changeTheme.setText(R.string.title_change_theme_mm);

        ((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));


    }
}
