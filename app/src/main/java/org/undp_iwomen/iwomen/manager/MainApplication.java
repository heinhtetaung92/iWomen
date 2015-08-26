package org.undp_iwomen.iwomen.manager;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.parse.AppVersion;
import org.undp_iwomen.iwomen.model.parse.Comment;
import org.undp_iwomen.iwomen.model.parse.Post;
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

        //You must register this ParseObject subclass before instantiating it.
        ParseObject.registerSubclass(AppVersion.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);




        // Associate the device with a user
        /*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();*/


        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }



}
