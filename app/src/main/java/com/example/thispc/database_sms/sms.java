package com.example.thispc.database_sms;

/**
 * Created by This PC on 9/30/2017.
 */

public class sms {
    private String mName,mInfo,mPhone;

    public sms(String name, String info,String phone) {
        this.mName = name;
        this.mInfo = info;
        this.mPhone = phone;
    }



    public String getName() {
        return mName;
    }
    public void setName(String name) {
        this.mName = name;
    }

    public String getInfo() {
        return mInfo;
    }
    public void setInfo(String info) {
        this.mInfo = info;
    }

    public String getmPhone(){return  mPhone;}

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }
}
