package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.SisterAppItem;
import org.undp_iwomen.iwomen.ui.adapter.SisterAppListAdapter;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khinsandar on 8/28/15.
 */
public class SisterAppFragment extends Fragment {

    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    private Context mContext;

    private ListView lv_sister;
    private List<SisterAppItem> sisterAppItemList;
    private SisterAppListAdapter sisterAppListAdapter;
    private TextView txt_gen_link;
    private TextView txt_undp_link;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_sister, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {


        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);

        lv_sister = (ListView)rootView.findViewById(R.id.sister_app_listview);

        set_up_adapter();

        txt_gen_link = (TextView)rootView.findViewById(R.id.sister_app_gen_txt);
        txt_undp_link = (TextView)rootView.findViewById(R.id.sister_app_undp_link_txt);

        txt_gen_link.setClickable(true);
        txt_gen_link.setMovementMethod(LinkMovementMethod.getInstance());

        txt_undp_link.setClickable(true);
        txt_undp_link.setMovementMethod(LinkMovementMethod.getInstance());

        lv_sister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + sisterAppItemList.get(i).get_app_down_link())));
            }
        });


    }

    private  void set_up_adapter(){

        sisterAppItemList = new ArrayList<>() ;


        sisterAppItemList.add(new SisterAppItem("May May", "com.koekoe.mayonline.myan.app", R.drawable.may_may_logo));
        sisterAppItemList.add(new SisterAppItem("Bindez","com.bindez.news",R.drawable.bindez));
        sisterAppItemList.add(new SisterAppItem("TED","com.ted.android",R.drawable.ted_logo));
        //https://play.google.com/store/apps/details?id=com.koekoe.mayonline.myan.app
        ////https://play.google.com/store/apps/details?id=com.bindez.news
        //https://play.google.com/store/apps/details?id=com.ted.android




        sisterAppListAdapter = new SisterAppListAdapter(mContext, sisterAppItemList);
        lv_sister.setAdapter(sisterAppListAdapter);
        View padding = new View(getActivity().getApplicationContext());
        padding.setMinimumHeight(20);
        lv_sister.addFooterView(padding);

        setListViewHeightBasedOnChildren(lv_sister);
    }
    public void setEnglishFont(){

    }
    public void setMyanmarFont(){

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.menu_post_news, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            return false;

        }

        return super.onOptionsItemSelected(item);
    }
}
