package com.example.thispc.database_sms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TabHost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NetworkChangeReceiver receiver;
    private List<sms> smslist = new ArrayList<>();
    private List<contact> contactlist = new ArrayList<>();
    RecyclerView mRecyclerViewSMS;
    MyAdapter mRcvAdapterSMS;

    RecyclerView mRecyclerViewCONTACT;
    MyContactAdapter mRcvAdapterCONTACT;

    Thread mthread;
    MyDatabaseHelper db = new MyDatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //db.insert();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 2);
        }else {
            receiver = new NetworkChangeReceiver();
            final IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(receiver, filter);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 2);
        }else {

        }
        loadTabs();
        //Tin nhan
        mRecyclerViewSMS = (RecyclerView) findViewById(R.id.recycler_view);
        mRcvAdapterSMS = new MyAdapter(smslist);

        LinearLayoutManager layoutManagerSMS = new LinearLayoutManager(getApplicationContext());
        layoutManagerSMS.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewSMS.setLayoutManager(layoutManagerSMS);
        mRecyclerViewSMS.setAdapter(mRcvAdapterSMS);

        //Danh ba
        mRecyclerViewCONTACT = (RecyclerView) findViewById(R.id.recycler_viewcontact);
        mRcvAdapterCONTACT = new MyContactAdapter(contactlist);

        LinearLayoutManager layoutManagerCONTACT = new LinearLayoutManager(getApplicationContext());
        layoutManagerCONTACT.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewCONTACT.setLayoutManager(layoutManagerCONTACT);
        mRecyclerViewCONTACT.setAdapter(mRcvAdapterCONTACT);
        showAllContacts();

        final Cursor list = db.readMessage();

        if (list != null && list.moveToFirst()) {
            do {


                    final String phone = list.getString(list.getColumnIndex("phone"));
                    String name = db.getContact(phone);
                    String message = list.getString(list.getColumnIndex("message"));
                    new Thread( new Add_dataRunnable(name,phone,message)).start();
                    mRcvAdapterSMS.notifyDataSetChanged();
                    mRcvAdapterCONTACT.notifyDataSetChanged();

                   // add_data(name,phone, message);




            } while (list.moveToNext());
        }





    }
    //Cấu hình tab
    public void loadTabs() {
        //Lấy Tabhost id ra trước (cái này của built - in android
        final TabHost tab = (TabHost) findViewById
                (android.R.id.tabhost);
        //gọi lệnh setup
        tab.setup();
        TabHost.TabSpec spec;
        //Tạo tab1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("SMS");
        tab.addTab(spec);
        //Tạo tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Contact");
        tab.addTab(spec);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab.setCurrentTab(0);
        //xử lý khi chuyển tab
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String arg0) {
            }
        });
    }
    public void showAllContacts()
    {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.valueOf(id));

                InputStream bitmap = ContactsContract.Contacts.openContactPhotoInputStream(cr,uri);

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phoneNo = phoneNo.replaceAll("[^\\d]", "");
                        contact c = new contact(name,phoneNo,bitmap);
                        contactlist.add(c);
                        mRcvAdapterSMS.notifyDataSetChanged();
                        mRcvAdapterCONTACT.notifyDataSetChanged();
                    }
                    pCur.close();
                }

            }
        }

    }
//  public void add_data(String name,String number,String body) {
//        sms sms = new sms( name,number,body );
//        smslist.add(sms);
//       mRcvAdapter.notifyDataSetChanged();
//
//   }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String b = intent.getStringExtra("number");
        String c = intent.getStringExtra("body");
        String name = db.getContact(b);

        for(int i=0; i<smslist.size(); i++)
        {
            if(smslist.get(i).getName().equals(name)) {
                smslist.remove(i);
            }
        }
        new Thread( new InsertRunable(b,c)).start();//chạy Thread
        //db.insert_message(b,c);
        //add_data(name,b,c);
        new Thread(new Add_dataRunnable(name,b,c)).start();
        mRcvAdapterSMS.notifyDataSetChanged();
        mRcvAdapterCONTACT.notifyDataSetChanged();
    }

    public class InsertRunable implements Runnable{
        public String number;
        public String messager;
        public InsertRunable(String a,String b){
            number = a;
            messager = b;
        }
        @Override
        public void run() {
            Log.i("NHAN_IN","Đã chạy InsertRunable");
            db.insert_message(number,messager);
        }
    }
    public class Add_dataRunnable implements Runnable {

        public String Name;
        public String Number;
        public String body;
        public Add_dataRunnable(String strName,String strNumber,String strbody){
            Name = strName;
            Number = strNumber;
            body = strbody;
        }
        @Override
        public void run() {
            Log.i("NHAN_ADD","Đã chạy Add_dataRunnable");
            sms sms = new sms( Name,Number,body );
             smslist.add(sms);

        }
    }

}
