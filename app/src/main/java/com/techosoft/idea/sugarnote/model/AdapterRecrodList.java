package com.techosoft.idea.sugarnote.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techosoft.idea.sugarnote.ListActivity;
import com.techosoft.idea.sugarnote.R;
import com.techosoft.idea.sugarnote.helper.MyConst;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by davidsss on 16-08-22.
 */
public class AdapterRecrodList extends BaseAdapter {

    //necessary variables
    private Context myContext;
    private LayoutInflater myInflater;
    private ArrayList<Reading> dataSource;

    public AdapterRecrodList(Context context, ArrayList<Reading> items) {

        myContext = context;
        dataSource = items;
        myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDataSource(ArrayList<Reading> data){
        dataSource = data;
    }

    @Override
    public int getCount() {
        return dataSource.size();

    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get view for row item
        View rowView = myInflater.inflate(R.layout.cell_record, parent, false);
        // Get title element
        TextView record = (TextView) rowView.findViewById(R.id.tvRecordTime);
        TextView count = (TextView) rowView.findViewById(R.id.tvRecordNum);
        TextView note = (TextView) rowView.findViewById(R.id.tvRecordNote);
        // for each item, get the data into each cell
        Reading cellItem = (Reading) getItem(position);
        String recordTime = getDayTimeStr(cellItem.timeStamp); //format the time
        count.setText(String.valueOf(cellItem.reading));
        note.setText(cellItem.note);
        record.setText( recordTime);
        return rowView;
    }

    private String getDayTimeStr(Date date){
        Calendar calander;
        calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat;
        simpledateformat = new SimpleDateFormat(MyConst.DAY_TIME_FORMAT);
        return simpledateformat.format(date);
    }

}
