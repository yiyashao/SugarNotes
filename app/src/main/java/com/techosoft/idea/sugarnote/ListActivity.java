package com.techosoft.idea.sugarnote;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.techosoft.idea.sugarnote.helper.MyConst;
import com.techosoft.idea.sugarnote.helper.MyHelper;
import com.techosoft.idea.sugarnote.model.AdapterRecrodList;
import com.techosoft.idea.sugarnote.model.Reading;
import com.techosoft.idea.sugarnote.model.SugarRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // UI reference
    private ListView lvRecordList;
    private ProgressDialog progress;

    // variables
    ArrayList<SugarRecord> recordList; // to store the want items
    MyHelper mHelper;
    ArrayList<Reading> itemList; // to store the want items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init helpers & variables
        mHelper = new MyHelper(this);
        AVOSCloud.initialize(this, MyConst.CLOUD_KEY_01, MyConst.CLOUD_KEY_02); //initilize the cloud service
        lvRecordList = (ListView) findViewById(R.id.lvRecordList);
        itemList = new ArrayList<>();

        //setup toolbar title
        String username = mHelper.getSettingsStr(MyConst.KEY_USER_NAME);
        getSupportActionBar().setTitle(username + "'s Record");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                goToActivity(AddRecordActivity.class);
                finish();
            }
        });

        //start doing business
        loadDataFromCloud();
    }

    /***
     * setup the option menus
     * @param menu
     * @return
     */
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
        switch(item.getItemId()){
            case R.id.actBtnSettings:
                mHelper.logInfo("settings button clicked");
                return true;
            case R.id.actBtnSignOut:
                mHelper.logInfo(("sign out button clicked"));
                confirmDialogSignout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * confirmation dialog FOR signout
     */
    private void confirmDialogSignout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Sign Out?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mHelper.logInfo(("and user said yes to logout"));
                        userSignout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    /**
     * signout user
     */
    private void userSignout() {
        //empty username and user ID, signout user
        mHelper.setSettingsStr(MyConst.KEY_USER_ID, "");
        mHelper.setSettingsStr(MyConst.KEY_USER_NAME, "");
        goToActivity(LoginActivity.class);
        finish();
    }

    private void loadDataFromCloud() {
        //handle the progress loading image
        progress=new ProgressDialog(this);
        progress.setMessage("loading data");
        progress.show();

        //load data from cloud
        AVQuery<AVObject> query = new AVQuery<>(MyConst.TABLE_BLOOD_RECORD);
        query.whereEqualTo(MyConst.BLOOD_RECORD_USERID, mHelper.getSettingsStr(MyConst .KEY_USER_ID)); //add this line to get records only for this user
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> resultList, AVException e) {
                //loop through the result if returned value
                if(resultList.size() > 0){
                    for(int i = 0; i < resultList.size(); i ++){
                        AVObject cloudItem = resultList.get(i);
                        int readingValue = cloudItem.getInt(MyConst.BLOOD_RECORD_READING);
                        String readingNote = cloudItem.getString(MyConst.BLOOD_RECORD_NOTE);
                        String readingUserId = cloudItem.getString(MyConst.BLOOD_RECORD_USERID);
                        Date readingDate = cloudItem.getDate(MyConst.BLOOD_RECORD_TIME);
                        int readingUnit = cloudItem.getInt(MyConst.BLOOD_RECORD_UNIT);
                        Reading bloodReading = new Reading(readingValue, readingNote, readingDate);
                        bloodReading.unit = readingUnit;
                        bloodReading.userId = readingUserId;
                        itemList.add(bloodReading);
                    }
                    Log.d(MyConst.LOG_TAG, "record found: " + resultList.size());
                }else{
                    Log.d(MyConst.LOG_TAG, "no record found for userId: " + mHelper.getSettingsStr(MyConst .KEY_USER_ID)); //current user have no record
                }

                //mySleep(500); used to make sure the loading diagram shows
                whenResultReturned(itemList);

                progress.hide();
                progress.dismiss();
            }
        });
    }

    // called when cloud data loaded, then build the cell list
    public void whenResultReturned(final ArrayList<Reading> itemList){
        AdapterRecrodList adapter = new AdapterRecrodList(this, itemList);
        lvRecordList.setAdapter(adapter);
        //use customized adapter to show the cell in a better way
        lvRecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Reading selectedItem = itemList.get(position); //find selected item
                //TODO actions
            }
        });
    }



    //inner helpers
    private void goToActivity(Class destinationClass) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }
}
