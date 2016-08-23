package com.techosoft.idea.sugarnote.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

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

    public String getSettingsStr(String key){
        return settings.getString(key, "");
    }
    public int getSettingsInt(String key){
        return settings.getInt(key, 0);
    }

    public boolean isLogin(){
        return settings.getBoolean(mConst.KEY_LOGIN, false);
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

    public Date getCurrentDate(){
        Date currentDate = new Date();
/*        String currentDateTimeString = DateFormat.getDateInstance().format(currentDate);
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hhmmss");
        dateFormatter.setLenient(false);
        String s = dateFormatter.format(currentDate);*/
        return currentDate;
    }

}




