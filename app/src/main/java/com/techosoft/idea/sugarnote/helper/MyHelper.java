package com.techosoft.idea.sugarnote.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.techosoft.idea.sugarnote.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by david on 2016/8/23.
 */
public class MyHelper extends ContextWrapper {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    public MyConst mConst;
    Context context;

    public MyHelper(Context base){
        super(base);
        //initialize the editor
        context = this;
        settings = context.getSharedPreferences(mConst.PERF_SETTING, 0);
        editor = settings.edit();
    }


    public void logInfo(String message){
        if(MyConst.LOG_LEVEL >  MyConst.LOG_LEVEL_NO_LOG){
            Log.d(MyConst.LOG_TAG, message);
        }
    }


    public void displayToast(String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
        /* some good way to display, research later
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
         */
    }

    public void setLogin(boolean loginFlag){
        editor.putBoolean(mConst.KEY_LOGIN, loginFlag);
        editor.commit();
    }

    /**
     * universal method to set a string value to shared preference
     * @param key
     * @param value
     */
    public void setSettingsStr(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void setSettingsInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void setSettingsBool(String key, boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getSettingsStr(String key){
        return settings.getString(key, "");
    }
    public int getSettingsInt(String key){
        return settings.getInt(key, 0);
    }


    /**
     * the token used to determine isLogin is are username and userid empty as ""
     * @return
     */
    public boolean isLogin(){
        if(getSettingsStr(MyConst.KEY_USER_NAME) != "" && getSettingsStr(MyConst.KEY_USER_ID) != ""){
            return true;
        }else{
            return false;
        }
    }

    public String getCurrentTime() {
        Calendar calander;
        SimpleDateFormat simpledateformat;
        String date;
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = simpledateformat.format(calander.getTime());
        return date;
    }

    public String getDayTimeStr(Date date){
        Calendar calander;
        calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat;
        simpledateformat = new SimpleDateFormat(MyConst.DAY_TIME_FORMAT);
        return simpledateformat.format(calander.getTime());
    }
    public Date getCurrentDate(){
        Date currentDate = new Date();
        return currentDate;
    }

    /**
     * take current user and save the user's objId + username in cache, these 2 info are the token for login
     * @param currentUser
     */
    public void processLoginFor(User currentUser) {
        logInfo("username password match for " + currentUser.username);
        setSettingsStr(MyConst.KEY_USER_ID, currentUser.objId);  //assume only one result returned
        setSettingsStr(MyConst.KEY_USER_NAME, currentUser.username);
    }


    /**
     *
     */
    public void processSignout() {
        //empty username and user ID, signout user
        setSettingsStr(MyConst.KEY_USER_ID, "");
        setSettingsStr(MyConst.KEY_USER_NAME, "");
    }
}




