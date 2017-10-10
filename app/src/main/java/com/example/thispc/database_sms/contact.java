package com.example.thispc.database_sms;

import java.io.InputStream;

/**
 * Created by This PC on 10/9/2017.
 */

public class contact {
    private String mName,mPhone;
    private InputStream mPic;

    public contact(String name,String phone, InputStream pic) {
        this.mName = name;
        this.mPhone = phone;
        this.mPic = pic;
    }

    public String getPhone() {
        return mPhone;
    }
    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        this.mName = name;
    }

    public InputStream getPic() {
        return mPic;
    }
    public void setPic(InputStream info) {
        this.mPic = info;
    }
}
