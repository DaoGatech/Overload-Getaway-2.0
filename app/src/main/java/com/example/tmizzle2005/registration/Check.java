package com.example.tmizzle2005.registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class Check extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() {
    }

    @Override
    public void onStart(Intent intent, int startId) {

        new check().execute();
    }
 
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
    private class check extends AsyncTask<String, String, Void> {

	    InputStream inputStream = null;
	    String result = ""; 

	    protected void onPreExecute() {
	    	
	    }
	    @Override
	    protected Void doInBackground(String... params) {
	        String url_select = "http://sinhvienboston.org/thong/anodtest/oscar.php";
	        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	        for (int i = 0; i < Start.type.length; i++) {
	             param.add(new BasicNameValuePair("type"+i,Start.type[i]));
	        }
	        param.add(new BasicNameValuePair("total",String.valueOf(Start.type.length)));
	                
	                
	        try {
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
	        	JSONArray json = new JSONArray(result); 
	        	int count = 0;
	        	String res = "";
	        	for (int i = 0; i < json.length(); i++) {
	        		JSONArray item = json.getJSONArray(i);
	        		int t = 0;
	        		try {
	        		    t = Integer.parseInt(item.getString(0));
	        		  } catch (NumberFormatException e) {
	        			  t= 0;
	        		  }
	        			if (t > 0) {
	        				res += item.getString(1) +  "-" + item.getString(2) + "\n";
	        				count += 1;
	        			}
	        		
	        	}
	        	if (count > 0) {
	        		NotificationCompat.Builder mBuilder =
	            	        new NotificationCompat.Builder(Check.this)
	            	        .setSmallIcon(R.drawable.ic_launcher)
	            	        .setContentTitle(count + " classes are available");
	        		
	        		NotificationCompat.BigTextStyle bigPicStyle = new NotificationCompat.BigTextStyle();  
	                bigPicStyle.bigText(res);  
	                mBuilder.setStyle(bigPicStyle);  
	            	mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
	            	mBuilder.setAutoCancel(true);
	            	Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	            	mBuilder.setSound(uri);
	            	// Creates an explicit intent for an Activity in your app
	            	Intent resultIntent = new Intent(Check.this, MainActivity.class);
	            	resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            	// The stack builder object will contain an artificial back stack for the
	            	// started Activity.
	            	// This ensures that navigating backward from the Activity leads out of
	            	// your application to the Home screen.
	            	PendingIntent pendingIntent = PendingIntent.getActivity(Check.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	            	mBuilder.setContentIntent(pendingIntent);
	            	NotificationManager mNotificationManager =
	            	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	            	// mId allows you to update the notification later on.
	            	mNotificationManager.notify(0, mBuilder.build());
	        	}
	        } catch (JSONException e) {
	            Log.e("JSONException", "Error: " + e.toString());
	            Toast.makeText(Check.this, "Please input appropriate class", Toast.LENGTH_LONG).show();
	        } // catch (JSONException e)
	    } // protected void onPostExecute(Void v)
	}

}