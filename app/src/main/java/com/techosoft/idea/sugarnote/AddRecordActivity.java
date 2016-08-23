package com.techosoft.idea.sugarnote;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.techosoft.idea.sugarnote.helper.CloudAgent;
import com.techosoft.idea.sugarnote.helper.MyConst;
import com.techosoft.idea.sugarnote.helper.MyHelper;
import com.techosoft.idea.sugarnote.model.Reading;

import java.util.Date;

public class AddRecordActivity extends AppCompatActivity {

    //UI components
    Button btnAdd;
    EditText etReading;
    EditText etNote;
    TextView tvReadingTime;

    //helpers
    MyHelper mHelper;

    //variables
    Reading mReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //initialize helpers
        mHelper = new MyHelper(this);

        //initialize UI components
        tvReadingTime = (TextView)findViewById(R.id.tvReadingTime);
        tvReadingTime.setText(mHelper.getCurrentTime());
        final Date now = mHelper.getCurrentDate();
        etReading = (EditText)findViewById(R.id.etReadingNum);
        etNote = (EditText)findViewById(R.id.etReadingNote);
        btnAdd = (Button)findViewById(R.id.btnAddRecord);

        //add listeners
        etReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etReading.setHint(null);
            }
        }); //clear the textfields
        etReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etReading.setHint(null);
            }
        }); //clear the textfields
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reading;
                String readingStr = etReading.getText().toString();
                if(!readingStr.isEmpty()){
                    reading  = Integer.parseInt(readingStr); //input type is number, so it has to be an int
                }else{
                    reading = 0;
                }
                mReading = new Reading(reading,  etNote.getText().toString(), now);
                if(isValidData(mReading)){
                    processAndSaveData(mReading);
                    goToActivity(ListActivity.class);
                    finish();
                }else{
                    askForReEntry();
                }
            }
        });

    }


    private boolean isValidData(Reading mReading) {
        return mReading.reading != 0;
    }
    private void askForReEntry() {
        mHelper.displayToast("Invalid data entry, please re-enter. Thanks.");
    }
    private void processAndSaveData(Reading reading){
        CloudAgent cloudConn = new CloudAgent(this);
        reading.userId = mHelper.getSettingsStr(MyConst .KEY_USER_ID);
        cloudConn.saveBloodReading(reading);
        mHelper.displayToast("OK, saving data");
    }


    //inner helpers
    private void goToActivity(Class destinationClass) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }
}
