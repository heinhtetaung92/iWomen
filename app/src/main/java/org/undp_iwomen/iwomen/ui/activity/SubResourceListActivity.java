package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.adapter.SubResourceListViewAdapter;


public class SubResourceListActivity extends AppCompatActivity {



    private TextView textViewTitle;
    private ListView lv;
    private Context mContext;

    private String[] ListName;
    private String[] ListDate;
    private String[] ListDataText;
    private int[] ListIcon;

    SubResourceListViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_resource_list);


        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.title_action2);

        textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        textViewTitle.setText(R.string.leadership_mm);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        lv = (ListView)findViewById(R.id.sub_resource_list);

        ListName = new String[]
                {"အ\u107Eကံ\u1033\u107Fပ\u1033ခ\u103Aက္ (၁)",  "အ\u107Eကံ\u1033\u107Fပ\u1033ခ\u103Aက္ (၂)"
                        , "အ\u107Eကံ\u1033\u107Fပ\u1033ခ\u103Aက္ (၃)","အ\u107Eကံ\u1033\u107Fပ\u1033ခ\u103Aက္ (၄)"
                        , "အ\u107Eကံ\u1033\u107Fပ\u1033ခ\u103Aက္ (၅)","အ\u107Eကံ\u1033\u107Fပ\u1033ခ\u103Aက္ (၆)"};
        ListDate = new String[]
                {"22 July, 2014",  "22 July, 2014", "22 July, 2014","22 July, 2014" ,"22 July, 2014", "22 July, 2014"};

        ListDataText = new String[]
                {"သင့္ရဲ\u1095ကုိယ္ပုိင္စရုိက္လက\u1061ဏာကုိ ဂုဏ္ယူပ\u102B - သင္သည္ ကမ\u107Bာေပ\u105Aမ\u103Dာရ\u103Dိတဲ့ ဘယ္ေနရာ ကုိ သ\u103Cားသည္\u103Bဖစ္ေစ၊ စီးပ\u103Cားေရးနယ္ပယ္ \u108F\u103Dင့္ \u108Fုိင္ငံေရးေလာကတ\u103Cင္ ဘယ္ေလာက္အထိ  ေအာင္\u103Bမင္ တုိးတက္\u103Bမင့္မားသည္\u103Bဖစ္ေစ၊ သင္သည္ ဘယ္သူ\u103Bဖစ္သည္ဆုိသည္ \u108F\u103Dင့္ သင္ ဘယ္ကလာသည္ဆုိသည္ကုိ မေမ့ပ\u102B\u108F\u103Dင့္။ သင္၏ဘ၀အစပုိင္းမ\u103Aားသည္ ဘယ္ေလာက္ပင္ နိမ့္က\u103Aေနသည္\u103Bဖစ္ေစ ယင္းတုိ\u1094ကုိ ရ\u103Dက္စရာမလုိပ\u102B။",
                        "အစပထမတ\u103Cင္ လူေတ\u103Cက မသိနားမလည္မ\u1088\u108F\u103Dင့္ဘက္လုိက္မ\u1088ေတ\u103Cေ\u107Eကာင့္ သင့္ကုိ သာမန္ ထူး\u103Bခားမ\u1088မရ\u103Dိေသာလူတစ္ဦးအေန\u103Bဖင့္ သတ္မ\u103Dတ္ထား\u107Eကမည္ကုိ သတိ\u103Bပ\u1033ပ\u102B - သုိ\u1094ေသာ္ အဆုိပ\u102B ခ\u107D\u103Cတ္ယ\u103Cင္းခ\u103Aက္မ\u103Aားကို ရင္ဆုိင္စိန္ေခ\u105A၍ သင့္ယဥ္ေက\u103Aးမ\u1088၏ က\u103Aယ္ေ\u103Bပာမ\u1088\u108F\u103Dင့္နက္ ရိ\u1088င္းမ\u1088တုိ\u1094ကုိ \u103Bပသပ\u102B။ လာဘ္ေပးလာဘ္ယူမ\u1088\u108F\u103Dင့္ ဆင္းရဲ\u108F\u103Cမ္းပ\u102Bးမ\u1088တုိ\u1094ေ\u107Eကာင့္ လက္ရ\u103Dိ\u103Bဖစ္ေပ\u105A ေနေသာ ဖိလစ္ပုိင္\u108Fုိင္ငံအေပ\u105A ထင္\u103Bမင္ယူဆခ\u103Aက္မ\u103Aားေ\u107Eကာင့္ \u107Fပိ\u1033လဲအရံ\u1088းေပးစရာမလုိပ\u102B၊ မိ မိ\u108Fုိင္ငံမ\u103Dာရ\u103Dိသည့္  ထက္\u103Bမက္မ\u1088မ\u103Aား\u108F\u103Dင့္ရုိးသားမ\u1088မ\u103Aားအတ\u103Cက္ ဂုဏ္ယူလုိက္ပ\u102B။"
                        , "သင္ကုိယ္တုိင္\u108F\u103Dင့္သင္၏\u103Bပည္သူမ\u103Aားအေ\u107Eကာင္းကုိေလ့လာမ\u1088သည္ တစ္သက္တာလးံု လုပ္ ေဆာင္ရမည့္အလုပ္\u103Bဖစ္သည္။ ဖိလစ္ပုိင္\u108Fုိင္ငံ၏ ဒုက\u1061မ\u103Aား\u1080ကံ\u1033ေတ\u103C\u1095ခဲ့ရေသာ သမုိင္းေ\u107Eကာင္း ကို စူးစမ္းေလ့လာရန္ \u108F\u103Dင့္ \u103Bပန္လည္ရင္\u107Eကားေစ့ရန္ မ\u103Aားစ\u103Cာလုိအပ္ေနေသး\u107Fပီး \u108Fုိင္ငံေတာင္ ပုိင္းမ\u103D ပဋိပက\u1061မ\u103Aားသည္ အဓိကေ\u103Bဖရ\u103Dင္းရမည့္ကစ\u1065ရပ္တစ္ခု\u103Bဖစ္ေနပ\u102Bသည္။ ယင္းသုိ\u1094ေဆာင္ ရ\u103Cက္လုိအပ္ရသည့္အေ\u107Eကာင္းအရင္းမ\u103Dာ သိရ\u103Dိနားလည္မ\u1088မ\u103Dေန၍ \u107Fငိမ္းခ\u103Aမ္းေရး ေပ\u105Aထ\u103Cက္လာ \u108Fုိင္ေသာေ\u107Eကာင့္ \u103Bဖစ္ပ\u102Bသည္။"
                        ,"သင္၏ ေလာကီေရးရာ\u108F\u103Dင့္ေလာကုတ\u1071ရာေရးရာ အရင္းအ\u103Bမစ္မ\u103Aားကုိ ဂုဏ္ယူပ\u102B။ က\u107D\u103Cန္မတုိ\u1094 အမ\u103Aားစုမ\u103Dာ ဘာသာေရးသ\u103Cန္သင္ဆံုးမ\u1088မ\u103Aား\u107Eကားမ\u103D\u1080ကီး\u103Bပင္းလာသူမ\u103Aား\u103Bဖစ္သလုိ၊ ေလာကီ ေရးရာကိစ\u1065ရပ္မ\u103Aားကုိလည္း လုပ္ကုိင္ေဆာင္ရ\u103Cက္\u107Eကရပ\u102Bသည္။ ယင္းလုပ္ငန္း ၂ ခုကုိ မ\u103D\u103Aမ\u103D\u103A တတ ေဆာင္ရ\u103Cက္ပ\u102B၊ ေ၀ဖန္ပုိင္း\u103Bခား စဥ္းစားဆင္\u103Bခင္ပ\u102B၊ အဆုိပ\u102Bက\u1091 ၂ ခုလံုးက သင္၏ ကုိယ္ရည္ကုိယ္ေသ\u103Cးဖ\u103Cံ\u1095\u107Fဖိ\u1033းတုိးတက္မ\u1088နဲ\u1094 အ\u103Bခားသူမ\u103Aား အက\u103A\u1033ိးသယ္ပုိးေဆာင္ရ\u103Cက္မ\u1088ကုိ \u103Bဖစ္ ေပ\u105Aေစ\u108Fုိင္သည့္ အေကာင္းဆံုးအရာမ\u103Aားကုိ အသံုး\u103Bပ\u1033ပ\u102B။"
                        , "ရည္မ\u103Dန္းခ\u103Aက္ \u1080ကီး\u1080ကီးထားပ\u102B။ အလုပ္\u1080ကိ\u1033းစားမ\u1088၊ မိတ္ေဆ\u103Cအေပ\u102Bင္အသင္းမ\u103Aားမ\u1088 \u108F\u103Dင့္ ဘုရား သခင္၏ ဘုန္းက\u103Aက္သေရတုိ\u1094က သင့္ကုိ ေစာင့္ေရ\u103Dာက္ပ\u102Bလိမ့္မယ္။ သင္၏ဘ၀အစပုိင္းမ\u103Aား သည္ မည္မ\u103D\u103Aပင္နိမ့္က\u103Aေနပ\u102Bေစ သင္\u103Bဖစ္\u108Fုိင္ေသာအရာမ\u103Aားကုိ သင္၏စိတ္က လ\u103Cတ္လပ္စ\u103Cာ စိတ္ကူးယဥ္\u108Fုိင္ပ\u102Bသည္။ အ\u103Bခားလူတုိင္းလူတုိင္းထက္ ပုိ၍\u1080ကီ\u1033းစားပ\u102B။ သင္\u103Bဖစ္ခ\u103Aင္တာ အမ\u103Dန္တကယ္\u103Bဖစ္လာခဲ့လ\u103D\u103Aင္ ေပ\u103Aာ္ရ\u108Aင္\u108Fုိင္ပ\u102Bေစ။"
                        ,"ပညာေရးက သင့္ကုိ လ\u103Cတ္ေ\u103Bမာက္ေအာင္ \u103Bပ\u1033လုပ္ေပးပ\u102Bလိမ့္မည္။ ဆင္းရဲ\u108F\u103Cံနစ္ေနမ\u1088 \u108F\u103Dင့္ ခက္ခဲ\u107Eကမ္းတမ္းမ\u1088 တုိ\u1094မ\u103D လ\u103Cတ္ေ\u103Bမာက္ရာနည္းလမ္းသည္ ေလ့လာသင္ယူမ\u1088၊ ဗဟုသုတရ\u103Dိမ\u1088 \u108F\u103Dင့္ အလုပ္\u1080ကိ\u1033းစားမ\u1088 တုိ\u1094\u103Bဖစ္သည္။ ေလ့လာသင္ယူရန္ အခ\u103Cင့္အေရးတစ္ခုခု\u103Bဖစ္ေစ၊ အားလံုးကုိ\u103Bဖစ္ေစ အက\u103A\u1033ိးရ\u103Dိေအာင္ အသံုးခ\u103Aပ\u102B။ ေရ\u103Dးယခင္ကာလတံုးကဆုိလ\u103D\u103Aင္ ေကာင္းမ\u103Cန္ ေသာ ပညာေရးတစ္ခုရရ\u103Dိရန္အတ\u103Cက္ ေင\u103Cေ\u107Eကးအေ\u103Bမာက္အမ\u103Aားအသံုး\u103Bပ\u1033ရန္ လုိအပ္သည္။ ယေန\u1094ေခတ္တ\u103Cင္ နည္းပညာအသံုး\u103Bပ\u1033\u103Bခင္း\u103Bဖင့္ ပညာဗဟုသုတမ\u103Aားစ\u103Cာ ရရ\u103D\u108Fုိင္ပ\u102Bသည္။ ယင္း အေ\u103Bခအေနကုိ အက\u103A\u1033ိးရ\u103Dိေအာင္ အသံုးခ\u103Aပ\u102B။"};
        ListIcon = new int[]
                {R.drawable.tip, R.drawable.tip, R.drawable.tip,R.drawable.tip
                ,R.drawable.tip,R.drawable.tip};


        mAdapter = new SubResourceListViewAdapter(mContext,ListName,ListDate,ListIcon);
        lv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, ResourceDetailActivity.class);
                intent.putExtra("Name", ListName[i]);
                intent.putExtra("Text", ListDataText[i]);//mCatNames.get((Integer)view.getTag()).toString()

                //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
