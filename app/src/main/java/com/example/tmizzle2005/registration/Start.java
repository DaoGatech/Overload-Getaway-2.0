package com.example.tmizzle2005.registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class Start extends Activity {
	public static String[] type;
	private PendingIntent pintent;
	public static String classes="";
	private AlarmManager alarm;
	 private Handler mHandler;
	 private int time = 0;
	 public static final String PREFS_COUNT = "MyPrefsFile";
	private static ArrayList<ReviewItem> obj = new ArrayList<ReviewItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final EditText c = (EditText) findViewById(R.id.input1);
		final EditText d = (EditText) findViewById(R.id.input2);
		mHandler = new Handler();
		Button a = (Button) findViewById(R.id.start);
        Button e = (Button) findViewById(R.id.notify);
        Button b = (Button) findViewById(R.id.stop);
        e.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               Intent next = new Intent(Start.this,sms.class);
               startActivity(next);
            }
        });
		a.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE); 

inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                           InputMethodManager.HIDE_NOT_ALWAYS);
            	if (d.getText().toString().equals("")) {
            		Toast.makeText(Start.this, "Please input Time",Toast.LENGTH_LONG).show();
            	} else {
            		time = Integer.parseInt(d.getText().toString()) * 1000;
            		String s = c.getText().toString();       		
            		type = s.split("\\s+");
            	
            			Calendar cal = Calendar.getInstance();
            			Intent intent = new Intent(Start.this, Check.class);
            			pintent = PendingIntent
            					.getService(Start.this, 0, intent, 0);
            			
            			alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            			
            			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
            					time, pintent);
            		
            		startRepeatingTask();
       
            	}
            }
        });
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					
					stopRepeatingTask();
					alarm.cancel(pintent);
					c.setText("");
					d.setText("");
				} catch (Exception e) {
					Toast.makeText(Start.this, "Encounter Problems", Toast.LENGTH_LONG).show();
				}
			}
		});
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

	private class getClass extends AsyncTask<String, String, Void> {
		
	    InputStream inputStream = null;
	    String result = ""; 

	    protected void onPreExecute() {
	    	Toast.makeText(Start.this, "Loading Class...", Toast.LENGTH_LONG).show();
	    }
	    @Override
	    protected Void doInBackground(String... params) {
	        String url_select = "http://sinhvienboston.org/thong/anodtest/oscar.php";
	        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	        for (int i = 0; i < type.length; i++) {
	        	
	             param.add(new BasicNameValuePair("type"+i,type[i]));

	        }
	        param.add(new BasicNameValuePair("total",String.valueOf(type.length)));
	                
	                
	        try {
	            // Set up HTTP post

	            // HttpClient is more then less deprecated. Need to change to URLConnection
	            HttpClient httpClient = new DefaultHttpClient();

	            HttpPost httpPost = new HttpPost(url_select);
	            httpPost.setEntity(new UrlEncodedFormEntity(param));
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();

	            // Read content & Log
	            inputStream = httpEntity.getContent();
	        } catch (UnsupportedEncodingException e1) {
	            Log.e("UnsupportedEncodingException", e1.toString());
	            e1.printStackTrace();
	        } catch (ClientProtocolException e2) {
	            Log.e("ClientProtocolException", e2.toString());
	            e2.printStackTrace();
	        } catch (IllegalStateException e3) {
	            Log.e("IllegalStateException", e3.toString());
	            e3.printStackTrace();
	        } catch (IOException e4) {
	            Log.e("IOException", e4.toString());
	            e4.printStackTrace();
	        }
	        // Convert response to string using String Builder
	        try {
	            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
	            StringBuilder sBuilder = new StringBuilder();

	            String line = null;
	            while ((line = bReader.readLine()) != null) {
	                sBuilder.append(line + "\n");
	            }

	            inputStream.close();
	            result = sBuilder.toString();

	        } catch (Exception e) {
	            Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
	        }
			return null;
	    }
	    protected void onPostExecute(Void v) {
	        //parse JSON data
	        try {
	        	obj.clear();
	        	JSONArray json = new JSONArray(result); 
	        	for (int i = 0; i < json.length(); i++) {
	        		JSONArray item = json.getJSONArray(i);
	        		int t = 0;
	        		try {
	        		    t = Integer.parseInt(item.getString(0));
	        		  } catch (NumberFormatException e) {
	        			  t= 0;
	        			  Toast.makeText(Start.this, "One of the classes is not found. Hit \"Stop\"", Toast.LENGTH_LONG).show();
	        		  }
	        		if (t > 0) {
	        			obj.add(new ReviewItem(item.getString(0),item.getString(1),item.getString(2)));
	        		}
	        		
	        	}
	        	ReviewList adapter = new ReviewList(obj,Start.this,obj.size());
	            ListView list = (ListView) findViewById(R.id.listView);
	    	    list.setAdapter(adapter);
	    	    
	        	
	        } catch (JSONException e) {
	            Log.e("JSONException", "Error: " + e.toString());
	            
	         
	        } // catch (JSONException e)
	    } // protected void onPostExecute(Void v)
	}
	private getClass mTask;
	Runnable mStatusChecker = new Runnable() {
	    @Override 
	    public void run() {
	      mTask = (Start.getClass) new getClass().execute();
	      //startService(intent);
	      mHandler.postDelayed(this,time);
	    
	    }
	  };

	  void startRepeatingTask() {
	    mStatusChecker.run(); 
	  }

	  void stopRepeatingTask() {
	    mHandler.removeCallbacks(mStatusChecker);
	    mTask.cancel(true);
	  }

	  
	  protected void onResume(){
		    super.onResume();
		    SharedPreferences settings = getSharedPreferences(PREFS_COUNT, 0);
		    int size = settings.getInt("array_size", 0);
		    type = new String[size];
		    time = settings.getInt("time",0);
		    for(int i=0; i<size; i++) {
		    	type[i] = settings.getString("array_" + i, null);
	  		}
		    String back = "";
		    for(int i=0; i<size; i++) {
		    	back = back + type[i] + " ";
	  		}
		    if (settings.getBoolean("yes", false)) {
		    	EditText e = (EditText) findViewById(R.id.input1);
		    	e.setText(back);
		    	EditText f = (EditText) findViewById(R.id.input2);
		    	f.setText(String.valueOf(time/1000));
		    	startRepeatingTask();
		    	Calendar cal = Calendar.getInstance();
    			Intent intent = new Intent(Start.this, Check.class);
    			pintent = PendingIntent
    					.getService(Start.this, 0, intent, 0);
    			
    			alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    			
    			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
    					time, pintent);
		    }
		}
		protected void onPause(){
		   super.onPause();


		  SharedPreferences settings = getSharedPreferences(PREFS_COUNT, 0);
		  SharedPreferences.Editor editor = settings.edit();
		  editor.putInt("array_size", type.length);
		  for(int i=0;i<type.length; i++)
		      editor.putString("array_" + i, type[i]);
		  editor.putInt("time", time);
		  editor.putBoolean("yes", true);
		  stopRepeatingTask();
		  editor.commit();
		}
		
		protected void onStop() {
			super.onStop();
		}
}
