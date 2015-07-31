package org.undp_iwomen.iwomen;

/**
 * Created by khinsandar on 3/23/15.
 */
public class CommonConfig {
    public static final String SHARE_PREFERENCE_NAME = "com.questmyanmar.myticketmanager";


    //https://api.parse.com/1/classes/City?X-Parse-Application-Id=OUN2VvuU6SN6DcRIDq3bT0ovJPXAk630qHVNJ9Gk&X-Parse-REST-API-Key=wxZB1WZBLzHEwfkUMToL0ykTLaiWY7Z1NuzfADLO
    public static final String BASE_URL = "https://api.parse.com/1";

    public static final String LOST_URL = "/classes/LostReport";
    public static final String FOUND_URL = "/classes/FoundReport";

    public int getPostMaxCharacterCount () {
        int value = 140;
        return value;
    }


}
