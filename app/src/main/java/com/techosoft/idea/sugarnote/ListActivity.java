package com.techosoft.idea.sugarnote;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVOSCloud;
import com.techosoft.idea.sugarnote.model.AdapterRecrodList;
import com.techosoft.idea.sugarnote.model.SugarRecord;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    // UI reference
    private ListView lvRecordList;
    private ProgressDialog progress;

    // variables
    ArrayList<SugarRecord> recordList; // to store the want items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init helpers
        //AVOSCloud.initialize(this, myHelper.mConst.CLOUD_KEY_01, myHelper.mConst.CLOUD_KEY_02); //initilize the cloud service
        lvRecordList = (ListView) findViewById(R.id.lvRecordList);
        recordList = new ArrayList<SugarRecord>();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //initialize some records
        SugarRecord item = new SugarRecord();
        for(int i = 0; i < 40; i ++){
            recordList.add(item);
        }


        //start doing business
        loadDataFromCloud();
    }

    private void loadDataFromCloud() {


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
}
