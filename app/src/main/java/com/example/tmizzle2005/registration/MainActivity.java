package com.example.tmizzle2005.registration;


import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;


public class MainActivity extends Activity implements OnClickListener,ConnectionCallbacks, OnConnectionFailedListener  {
	private static final int RC_SIGN_IN = 0;
	public String name = "";
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	private SignInButton btnSignIn;
	private int track = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        final EditText name = (EditText) findViewById(R.id.n);
        final EditText phone = (EditText) findViewById(R.id.p);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this,Start.class);
                String[] arr = new String[] {name.getText().toString(),phone.getText().toString()};
                a.putExtra("input",arr);
                startActivity(a);
            }
        });
	    btnSignIn.setOnClickListener(this);
	    
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
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
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
		      mGoogleApiClient.disconnect();
		}
	}

	  
	  protected void onResume(){
		    super.onResume();
		    
	  }
	  protected void onPause(){
		   super.onPause();
	  }
	  private void resolveSignInError() {
			if (mConnectionResult.hasResolution()) {
				try {
					mIntentInProgress = true;
					mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
				} catch (SendIntentException e) {
					mIntentInProgress = false;
					mGoogleApiClient.connect();
				}
			}
		}
		
		@Override
		public void onConnectionFailed(ConnectionResult result) {
			if (!result.hasResolution()) {
				GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
						0).show();
				return;
			}
			if (!mIntentInProgress) {
				mConnectionResult = result;
				if (mSignInClicked) {
					resolveSignInError();
				}
			}
		}
		@Override
		public void onConnected(Bundle arg0) {
			mSignInClicked = false;
			Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
			Intent a = new Intent(MainActivity.this,Start.class);
			startActivity(a);
		}
		
		@Override
		public void onConnectionSuspended(int arg0) {
			mGoogleApiClient.connect();
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_sign_in:
				signInWithGplus();
				break;
			}
		}
		
		private void signInWithGplus() {
			if (!mGoogleApiClient.isConnecting()) {
				mSignInClicked = true;
				resolveSignInError();
			}
		}
		@Override
		protected void onActivityResult(int requestCode, int responseCode,
		        Intent intent) {
		    if (requestCode == RC_SIGN_IN) {
		        if (responseCode != RESULT_OK) {
		            mSignInClicked = false;
		        }
		 
		        mIntentInProgress = false;
		 
		        if (!mGoogleApiClient.isConnecting()) {
		            mGoogleApiClient.connect();
		        }
		    }
		}
}
