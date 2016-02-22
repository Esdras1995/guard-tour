package com.innovhaitian.guardtour;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hollyn on 1/9/16.
 */
public class LocalTimeStore {
    public final String SP_NAME = "userDetails";
    SharedPreferences timeStore;

    public LocalTimeStore(Context context){
        timeStore = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeTime(){
        SharedPreferences.Editor spEditor = timeStore.edit();
        spEditor.putLong("storedMillis", System.currentTimeMillis());
        spEditor.commit();
    }

    public long getStoreTime(){
        return timeStore.getLong("storedMillis", -1);
    }

    public void setTimeStored(boolean timeStored){
        SharedPreferences.Editor spEditor = timeStore.edit();
        spEditor.putBoolean("timeStored", timeStored);
        spEditor.commit();
    }

    public boolean getTimeStored(){
        if (timeStore.getBoolean("timeStored", false))
            return true;
        return false;
    }

    public void clearTimeStored(){
        SharedPreferences.Editor spEditor = timeStore.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public void setRemainingTime(long time){
        SharedPreferences.Editor spEditor = timeStore.edit();
        spEditor.putLong("remainingTime", time);
        spEditor.commit();
    }


    public long getRemainingTime(){
        return timeStore.getLong("remainingTime", -1);
    }
}
