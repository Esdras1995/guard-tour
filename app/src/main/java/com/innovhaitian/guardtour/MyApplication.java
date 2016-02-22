package com.innovhaitian.guardtour;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;


/**
 * Created by Castro Alhdo on 10/18/15.
 * Code writed by Castro Alhdo on 10/18/15
 * Don't use my code without my approuve
 */
public class MyApplication extends Application{
    private static MyApplication sInstancee;
    private static final String IP_ADD="67.205.29.204";

    public static final String GUARD_TOUR="http://"+IP_ADD+"/Guardtour/tours.php";


    @Override
    public void onCreate() {
        super.onCreate();
        sInstancee=this;
        Log.i("UID",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public static MyApplication getInstance(){
        return sInstancee;
    }
    public static Context getAppContext(){
        return sInstancee.getApplicationContext();
    }


}
