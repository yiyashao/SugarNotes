package com.techosoft.idea.sugarnote.model;

import com.techosoft.idea.sugarnote.helper.MyConst;

import java.util.Date;

/**
 * Created by david on 2016/8/23.
 */
public class Reading {
    //public properties
    public int reading;
    public int unit; //set in constants in "mmol/L" and "mg/dl"
    public String note;
    public Date timeStamp;
    public int userId;


    public Reading(int reading, String note, Date currentTime){
        unit = MyConst.UNIT_MG_DL;  //by default
        this.reading = reading;
        this.note = note;
        this.timeStamp = currentTime;
    }
}
