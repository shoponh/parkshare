package com.parkshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

/**
 *
 * Register Activity Class
 */
public class IpPortActivity extends Activity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // set new IP Port
    EditText ipPortET;
    static public String ipPort = "10.0.0.2:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_port);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.ipport_error);
        // Find Name Edit View control by ID
        ipPortET = (EditText)findViewById(R.id.ip_port);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void setIpPort(View view){
        // Get IP and Port
        String newIpPort = ipPortET.getText().toString();

         RequestParams params = new RequestParams();
        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if( Utility.isNotNull(newIpPort) ) {
            ipPort = newIpPort;

            navigatetoRegistrationActivity();
        }else{
            Toast.makeText(getApplicationContext(), "Please provide a valid IP address and Port.", Toast.LENGTH_LONG).show();
        }
    }

    public void navigatetoRegistrationActivity(){
        Intent registerIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        // Clears History of Activity
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(registerIntent);
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoIpPortActivity(){
        Intent ipPortIntent = new Intent(getApplicationContext(),IpPortActivity.class);
        // Clears History of Activity
        ipPortIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ipPortIntent);
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoIpPortActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),IpPortActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public static String getIpPort(){
        return ipPort;
    }
}