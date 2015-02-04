package com.example.tmizzle2005.registration;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;


public class sms extends Activity   {
    Button sendBtn;
    EditText txtphoneNo;
    EditText txtMessage;
    Button choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);
        sendBtn = (Button) findViewById(R.id.send);
        choose = (Button) findViewById(R.id.contact);
        txtphoneNo = (EditText) findViewById(R.id.num);
        txtMessage = (EditText) findViewById(R.id.content);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage();
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });

    }

    protected void sendSMSMessage() {
        String phoneNo = txtphoneNo.getText().toString();
        String message = txtMessage.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        super.onStart();

    }

    protected void onStop() {
        super.onStop();

    }


    protected void onResume(){
        super.onResume();

    }
    protected void onPause(){
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.TYPE },
                            null, null, null);

                    if (c != null && c.moveToFirst()) {
                        txtphoneNo.setText(c.getString(0));
                        int type = c.getInt(1);

                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }


}
