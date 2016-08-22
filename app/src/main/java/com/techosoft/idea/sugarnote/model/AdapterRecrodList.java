package com.techosoft.idea.sugarnote.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techosoft.idea.sugarnote.ListActivity;
import com.techosoft.idea.sugarnote.R;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by davidsss on 16-08-22.
 */
public class AdapterRecrodList extends BaseAdapter {

    //necessary variables
    private Context myContext;
    private LayoutInflater myInflater;
    private ArrayList<SugarRecord> dataSource;

    public AdapterRecrodList(Context context, ArrayList<SugarRecord> items) {

        myContext = context;
        dataSource = items;
        myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        TextView record = (TextView) rowView.findViewById(R.id.tvRecord);
        // for each item, get the data into each cell
        SugarRecord cellItem = (SugarRecord) getItem(position);

        //String expDate = DateFormat.getDateInstance().format(record.expireDate);
        record.setText("this is a test");
        //following used to add photo into cell, keep for future use
        //Picasso.with(mContext).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return rowView;
    }
}
