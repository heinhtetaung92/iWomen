package org.undp_iwomen.iwomen.manager;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.PushService;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.parse.AppVersion;
import org.undp_iwomen.iwomen.ui.activity.DrawerMainActivity;


/**
 * Created by khinsandar on 4/12/15.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));
        PushService.setDefaultPushCallback(this, DrawerMainActivity.class);
        PushService.startServiceIfRequired(this);
        //channel '13fb3c43 com.questmyanmar.gobus/com.questmyanmar.gobus.ui.activity.SearchActivity (server)'
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        ParseObject.registerSubclass(AppVersion.class);
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }



}
