package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.utils.Utils;

/**
 * Created by khinsandar on 8/7/15.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private RadioGroup radioLanguageGroup;
    private RadioButton radioLanguageButton;

    RadioButton rd_lang_en, rd_lang_mm;
    SharedPreferences sharePref;

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
        sharePref = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        radioLanguageGroup = (RadioGroup) rootView.findViewById(R.id.settings_language);
        rd_lang_en = (RadioButton) rootView.findViewById(R.id.settings_english_language);
        rd_lang_mm = (RadioButton) rootView.findViewById(R.id.settings_myanmar_language);


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

    }
    public void setMyanmarFont(){

    }
}
