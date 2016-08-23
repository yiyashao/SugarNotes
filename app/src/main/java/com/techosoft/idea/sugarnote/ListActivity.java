package com.techosoft.idea.sugarnote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.techosoft.idea.sugarnote.helper.MyHelper;
import com.techosoft.idea.sugarnote.model.AdapterRecrodList;
import com.techosoft.idea.sugarnote.model.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // UI reference
    private ListView lvRecordList;
    private ProgressDialog progress;

    // variables
    ArrayList<SugarRecord> recordList; // to store the want items
    MyHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init helpers
        mHelper = new MyHelper(this);
        //AVOSCloud.initialize(this, myHelper.mConst.CLOUD_KEY_01, myHelper.mConst.CLOUD_KEY_02); //initilize the cloud service
        lvRecordList = (ListView) findViewById(R.id.lvRecordList);
        recordList = new ArrayList<SugarRecord>();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                goToActivity(AddRecordActivity.class);
            }
        });

        //initialize some records
        SugarRecord item = new SugarRecord();
        for(int i = 0; i < 40; i ++){
            recordList.add(item);
        }


        //start doing business
        loadDataFromCloud();

        mHelper.displayToast("load the list once");
    }

    private void loadDataFromCloud() {
        //handle the progress loading image
        progress=new ProgressDialog(this);
        progress.setMessage("loading data");
        progress.show();

        //load data from cloud
        AVQuery<AVObject> query = new AVQuery<>("want_item");//query.whereEqualTo("user_id", 1); add this line to get records only for this user
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> resultList, AVException e) {
                //loop through the result if returned value
                if(resultList.size() > 0){
                    for(int i = 0; i < resultList.size(); i ++){
                        AVObject cloudItem = resultList.get(i);
                        WantItem tempWantItem = new WantItem();
                        tempWantItem.title = cloudItem.getString(myHelper.mConst.WANT_ITEM_TITLE);
                        tempWantItem.detail = cloudItem.getString(myHelper.mConst.WANT_ITEM_DETAIL);
                        tempWantItem.expireDate = cloudItem.getDate(myHelper.mConst.WANT_ITEM_DATE);
                        tempWantItem.itemCloudId = cloudItem.getObjectId();
                        itemList.add(tempWantItem);
                    }
                    Log.d(myHelper.mConst.LOG_TAG, "record found: " + resultList.size());
                }else{
                    Log.d(myHelper.mConst.LOG_TAG, "no record found for userId: " + 1); //current user have no record
                }

                //mySleep(500); used to make sure the loading diagram shows
                whenResultReturned(itemList);

                progress.hide();
            }
        });

        whenResultReturned(recordList);
    }

    // called when cloud data loaded, then build the cell list
    public void whenResultReturned(final ArrayList<SugarRecord> itemList){
        AdapterRecrodList adapter = new AdapterRecrodList(this, itemList);
        lvRecordList.setAdapter(adapter);
        //use customized adapter to show the cell in a better way
        lvRecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SugarRecord selectedItem = itemList.get(position); //find selected item
                //goToDetailActivity(selectedItem);
                //do nothing for now, since no need to click thru
            }
        });
    }


    //inner helpers
    private void goToActivity(Class destinationClass) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }
}
