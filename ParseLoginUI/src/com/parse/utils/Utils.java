package com.parse.utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.model.MyTypeFace;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    //public static String HOCKEY_APPID = "ab8e61deeab46d679d4f6d14757e8c27";
    public static String PREF_SETTING = "settings";
    public static String PREF_SETTING_LANG = "language";
    public static String ENG_LANG = "english";
    public static String MM_LANG = "myanmar";

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
        	
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              //Read byte from input stream
            	
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              
              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    public static void doToastEng(Context context, String toast){
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }
    public static void doToastMM(Context context, String toast){
        //Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        Toast toast_mm = Toast.makeText(context, toast, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast_mm.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(15);
        toastTV.setTypeface(MyTypeFace.get(context, MyTypeFace.ZAWGYI));
        toast_mm.show();
    }

}