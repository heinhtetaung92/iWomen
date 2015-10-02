package org.undp_iwomen.iwomen.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.ResourceItem;
import org.undp_iwomen.iwomen.model.retrofit_api.ResourceAPI;
import org.undp_iwomen.iwomen.ui.activity.SubResourceListActivity;
import org.undp_iwomen.iwomen.ui.adapter.ResourcesListViewAdapter;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourcesFragment extends Fragment {
    public static final String ARG_MENU_INDEX = "index";

    private Context mContext;
    private ListView lvResouces;
    private ArrayList<ResourceItem> ResourceItems;

    private ResourcesListViewAdapter adapter;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    String name[];

    int[] randomImgMain = new int[]{R.drawable.idea
            , R.drawable.donors, R.drawable.law};

    private ProgressDialog mProgressDialog;

    private StorageUtil storageUtil;


    public ResourcesFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //storageUtil = StorageUtil.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resources, container, false);
        mContext = getActivity().getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);

        init(rootView);


        return rootView;
    }

    private void init(View rootView) {
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        ResourceItems = new ArrayList<ResourceItem>();
        lvResouces = (ListView) rootView.findViewById(R.id.resource_list);


        mstr_lang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);

        ResourceItems = (ArrayList<ResourceItem>) storageUtil.ReadArrayListFromSD("ResourceArrayList");

        Log.e("ResourceItems size", "===>" + ResourceItems.size());

        if (ResourceItems.size() > 0) {
            adapter = new ResourcesListViewAdapter(getActivity().getApplicationContext(), ResourceItems, mstr_lang);
            lvResouces.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            getResourceDataFromSever();
        }





        lvResouces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /*if (i == 0) {

                } else {
                    Utils.doToastEng(mContext, "Coming soon!");
                }*/
                Intent intent = new Intent(mContext, SubResourceListActivity.class);

                intent.putExtra("ResourceId", ResourceItems.get(i).getResourceId());
                intent.putExtra("TitleEng", ResourceItems.get(i).getResourceName());
                intent.putExtra("TitleMM", ResourceItems.get(i).getResourceNameMM());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });


    }

    private void getResourceDataFromSever() {
        if (Connection.isOnline(mContext)) {

            mProgressDialog.show();
            ResourceAPI.getInstance().getService().getResourceList("created", new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                    try {

                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");

                        String id;
                        String resouceEng;
                        String resouceMM;
                        String iconPath;
                        ResourceItems.clear();
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject each_object = result.getJSONObject(i);

                            if (!each_object.isNull("objectId")) {

                                id = each_object.getString("objectId");

                            } else {
                                id = "";
                            }

                            if (!each_object.isNull("resource_title_eng")) {

                                resouceEng = each_object.getString("resource_title_eng");

                            } else {
                                resouceEng = "";
                            }
                            if (!each_object.isNull("resource_title_mm")) {

                                resouceMM = each_object.getString("resource_title_mm");

                            } else {
                                resouceMM = "";
                            }

                            if (!each_object.isNull("resource_icon_img")) {

                                JSONObject imgjsonObject = each_object.getJSONObject("resource_icon_img");
                                if (!imgjsonObject.isNull("url")) {

                                    iconPath = imgjsonObject.getString("url");
                                } else {
                                    iconPath = "";
                                }


                            } else {
                                iconPath = "";
                            }


                            ResourceItem rI = new ResourceItem(id, resouceEng, resouceMM, iconPath);
                            ResourceItems.add(rI);
                        }


                        storageUtil.SaveArrayListToSD("ResourceArrayList", ResourceItems);
                        adapter = new ResourcesListViewAdapter(getActivity().getApplicationContext(), ResourceItems, mstr_lang);
                        lvResouces.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSON err", "==>" + e.toString());
                    }
                    mProgressDialog.dismiss();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("error", "==" + error);
                    mProgressDialog.dismiss();
                }
            });

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, "Internet Connection need!");
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.refresh_menu, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_refresh:
                getResourceDataFromSever();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Background Async Task to Load all tracks under one album
     */
    class LoadData extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;


        int[] randomImg = new int[]{R.drawable.idea
                , R.drawable.donors, R.drawable.law};

        private Random randomGenerator;

        //public  DatabaseManager dbmanager;
        ArrayList<ResourceItem> arraylist_BuildingInfo = new ArrayList<ResourceItem>();

        /**
         * Before starting background thread Show Progress Dialog
         */

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting tracks json and parsing
         */
        protected String doInBackground(String... args) {
            // Building Parameters


            try {


            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fiexed Data Categories
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all tracks
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {


                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    //DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());
                    randomGenerator = new Random();
                    try {

                        String name[] = new String[]{
                                "ေရေဘးအတ\u103Cက္\u1080ကိ\u1033တင္\u103Bပင္ဆင္\u103Bခင္း\n", "Important Flood Clean-Up Tips",
                                "DIY Home Decoration Tip", " Building-D"};
                        String text[] = new String[]{"ေရေဘးအတ\u103Cက္\u1080ကိ\u1033တင္\u103Bပင္ဆင္\u103Bခင္း\n" +
                                "(၁)ေရမ\u1080ကီးမီ ေဆာင္ရ\u103Cက္ရမည့္အခ\u103Aက္မ\u103Aား။\n" +
                                "    (က) မိုုးေလ၀သ သတင္းကိုုဂရုု\u103Bပ\u1033နားေထာင္ပ\u102B။\t\n" +
                                "(ခ)ေရေဘးမ\u103D လ\u103Cတ္ကင္းနိုုင္သည့္အခိုုင္အမာအေဆာက္အအံုုမ\u103Aား၊ကုုန္း\u103Bမင့္မ\u103Aား \u1080ကိ\u1033တင္သတ္မ\u103Dတ္ထားပ\u102B၊\n" +
                                "(ဂ)သတ္မ\u103Dတ္ေနရာမ\u103Aားသိုု\u1095 ေရာက္ရ\u103Dိမည့္ ေ\u103Bပးလမ္းေ\u1080ကာင္းမ\u103Aား \u1080ကိ\u1033တင္ေရ\u103Cးခ\u103Aယ္ထားပ\u102B။\n" +
                                "(ဃ)ေရတ\u103Cင္ေပ\u105Aနိုုင္သည့္ င\u103Dက္ေပ\u103Aာပင္ကဲ့သိုု\u1095အပင္မ\u103Aား စိုုက္ပ\u103Aိ\u1034းထားပ\u102B၊\n" +
                                "(င)အေရး\u1080ကီးစာရ\u103Cက္စာတမ္းမ\u103Aား ေရမစိုုနိုုင္ေအာင္ ပလက္စတစ္\u103Bဖင့္ ေသခ\u103Aာစ\u103Cာထုုပ္ပိုုးထားပ\u102B။\n" +
                                "(စ)အ၀တ္အစား၊ အစားအေသာက္၊ ေဆး၀\u102Bး၊ ဓာတ္မီး၊ မီး\u103Bခစ္၊ ဖေယာင္းတိုုင္ စသည့္ ပစည္းမ\u103Aား အသင့္ေဆာင္ထားပ\u102B။\n" +
                                "(ဆ)သယ္ယူပိုု\u1095ေဆာင္သ\u103Cားလာေရးအတ\u103Cက္ ေလ\u103D၊ ပဲ့ေထာင္၊ ေမာ္ေတာ္၊ စက္သံုုးဆီ စသည္မ\u103Aား \u1080ကိ\u1033တင္စီစဥ္ထားပ\u102B၊\n" +
                                "(ဇ) ေဘးအ\u108Fရာယ္ \u1080ကိ\u1033တင္\u103Bပင္ဆင္မ\u1088မ\u103Aားတ\u103Cင္ ပူးေပ\u102Bင္းပ\u102B၀င္ ေဆာင္ရ\u103Cက္ပ\u102B။\n" +
                                "\n" +
                                "(၂)ေရ\u1080ကီး\u103Bခင္း သတိေပးခ\u103Aက္ ရရ\u103Dိပ\u102Bက ေဆာင္ရ\u103Cက္ရန္အခ\u103Aက္မ\u103Aား\n" +
                                "\n" +
                                "(က)အစားအစာ၊ ေဆး၀\u102Bး၊ ေသာက္ေရတိုု\u1095န\u103Dင့္အတူ လံုု\u103Bခံ\u1033စိတ္ခ\u103Aရေသာ အေဆာက္အအံုုန\u103Dင့္ ကုုန္းေ\u103Bမ\u103Bပင္ေပ\u105Aသိုု\u1095 အခ\u103Aိန္မီေရ\u103C\u1095ေ\u103Bပာင္းပ\u102B။\n" +
                                "(ခ)သစ္တံုုး၊ ၀\u102Bးေဖာင္ငယ္၊ ကားတာယာက\u107D\u103Cတ္၊ ေဗာက\u103Cင္း၊ လုူတစ္ဦးဖက္တ\u103Cယ္နိုုင္သည့္ ၀\u102Bးပိုုး၊ ၀\u102Bးအဆစ္ပိတ္မ\u103Aား ေဆာင္ထားပ\u102B။\n" +
                                "(ဂ) ကေလးငယ္မ\u103Aားအား ေရစီးတ\u103Cင္ေမ\u103Aာပ\u102Bမည္စိုုးသ\u103Bဖင့္ \u1080ကိ\u1033း\u103Bဖင့္ခ\u103Aည္ေ\u108F\u103Dာင္ထား\u103Bခင္းမ\u103Aိ\u1033း မိသားစုုလိုုက္ ခ\u103Aည္ေန\u103Dာင္ထား\u103Bခင္းမ\u103Aိ\u1033းေရ\u103Dာင္ရ\u103Dားပ\u102B။\n" +
                                "( ဃ)ေရေဘးအ\u108Fရာယ္သတိေပးခ\u103Aက္ ရရ\u103Dိပ\u102Bက ပင္လယ္ကမ္းေ\u103Bခေဒသမ\u103Aား၊ အနိမ့္ပိုုင္းေဒသမ\u103Aား၊ လယ္က\u103Cင္း\u103Bပင္မ\u103Aားတ\u103Cင္ ေနထုုိင္\u103Bခင္းမ\u103D ေရ\u103Dာင္ရ\u103Dားပ\u102B။\n" +
                                "(င)\u103Bမစ္ေခ\u103Aာင္း၊ ကမ္းပ\u102Bး ေ\u103Bမ\u103Bပိ\u1033နိုုင္သည့္ေဒသမ\u103Aားသိုု\u1095 သ\u103Cားလာ\u103Bခင္းမ\u103D ေရ\u103Dာင္ရ\u103Dားပ\u102B၊", " If you have experienced any water accumulation in your home, it is important to remember that not all water damage is visible. Since flood water may contain bacteria that can cause serious illnesses, it is vital to clean and disinfect everything that may have been contaminated."
                                , "Address C", " Address D"
                        };

                        int index = randomGenerator.nextInt(randomImg.length);
                        arraylist_BuildingInfo.clear();
                        /*for(int i = 0 ; i < randomImg.length ; i ++){
                            ResourceItem rI = new ResourceItem(name[i], text[i],  randomImg[i] );
                            arraylist_BuildingInfo.add(rI);
                        }
                        adapter = new ResourcesListViewAdapter(getActivity().getApplicationContext(), arraylist_BuildingInfo);
                        lvResouces.setAdapter(adapter);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

        }

    }


}

