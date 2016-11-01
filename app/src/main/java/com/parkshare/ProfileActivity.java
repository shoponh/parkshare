package com.parkshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parkshare.constant.Constant;
import com.parkshare.pojo.ItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Register Activity Class
 */
public class ProfileActivity extends Activity {
    UserInfo userInfo = UserInfo.getInstance();

    // Progress Dialog Object
    ProgressDialog prgDialog;
    TextView errorMsg;

    EditText person_id;
    EditText address_id;
    EditText profile_name;
    EditText email;
    EditText password;
    EditText address1;
    EditText address2;
    EditText city;
    EditText state;
    EditText zip;
    TextView country;
    EditText latitude;
    EditText longitude;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        setDefaultValues();

        /*
        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.register_error);
        nameET = (EditText)findViewById(R.id.registerName);
        emailET = (EditText)findViewById(R.id.registerEmail);
        pwdET = (EditText)findViewById(R.id.registerPassword);
        nameET = (EditText)findViewById(R.id.registerName);
        */

        getUserProfile("producer@gmail.com");

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param email
     */
    public void getUserProfile(String email){
        RequestParams params = new RequestParams();
        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if( Utility.isNotNull(email) ){
            if(Utility.validate(email)){
                params.put("useremail", email);
                invokeWS(params);
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        //prgDialog = new ProgressDialog(this);
        //prgDialog.setMessage("Please wait...");
        //prgDialog.setCancelable(false);
        //prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.2.2:9999/useraccount/profile/getprofile",params ,new AsyncHttpResponseHandler() {
        client.get("http://"+ IpPortActivity.getIpPort() +"/useraction/profile/getprofile",params ,new AsyncHttpResponseHandler() {

                        // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    if(obj == null || (Constant.FAIL.equalsIgnoreCase(obj.getString("status"))) ){
                        Toast.makeText(getApplicationContext(), "ERRRRORRRRR!!!!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String statusStr = obj.getString("status");
                    System.out.println(statusStr);

                    if (Utility.isNotNull(statusStr) && statusStr.equalsIgnoreCase(Constant.SUCCESS)) {
                        JSONArray jsonArr = (JSONArray) obj.get(Constant.PERSONAL_PROFILE);

                        jsonArr.length();
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject currPerson = (JSONObject) jsonArr.get(i);
                            //String itemId = (String)currItem.get(Constant.ITEM_ID);
                            //String specInstruction = (String)currItem.get(Constant.ITEM_SPECIAL_INSTRUNCTION);
                            //String itemAddressId = (String)currItem.get(Constant.ITEM_ADDRESS_ID);

                            person_id.setText(currPerson.getString("id"));
                            address_id.setText(currPerson.getString("address_id"));
                            profile_name.setText(currPerson.getString("name"));
                            email.setText(currPerson.getString("email"));
                            password.setText(currPerson.getString("password"));
                            address_id.setText(currPerson.getString("address1"));
                            address2.setText(currPerson.getString("address2"));
                            city.setText(currPerson.getString("city"));
                            state.setText(currPerson.getString("state"));
                            zip.setText(currPerson.getString("zip"));
                            country.setText(currPerson.getString("country"));
                            phone.setText(currPerson.getString("phone"));
                            latitude.setText(currPerson.getString("latitude"));
                            longitude.setText(currPerson.getString("longitude"));
                        }

                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();

                        //navigatetoLoginActivity();
                    }
                    // Else display error message
                    else{
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /****
    public void navigatetoLoginActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

     public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public void navigatetoIpPortActivity(View view){
        Intent ipPortIntent = new Intent(getApplicationContext(),IpPortActivity.class);
        // Clears History of Activity
        ipPortIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ipPortIntent);
    }

    public void navigatetoShowIpPortActivity(View view){
        Toast.makeText(getApplicationContext(), "Current IP and Port: "+ IpPortActivity.getIpPort() , Toast.LENGTH_LONG).show();
    }
    ***/


    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        person_id = (EditText)findViewById(R.id.person_id);
        address_id = (EditText)findViewById(R.id.address_id);
        profile_name = (EditText)findViewById(R.id.profile_name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        address_id = (EditText)findViewById(R.id.address_id);
        address2 = (EditText)findViewById(R.id.address2);
        city = (EditText)findViewById(R.id.city);
        state = (EditText)findViewById(R.id.state);
        zip = (EditText)findViewById(R.id.zip);
        country = (TextView)findViewById(R.id.country);
        latitude = (EditText)findViewById(R.id.latitude);
        longitude = (EditText)findViewById(R.id.longitude);
        phone = (EditText)findViewById(R.id.phone);

        person_id.setText("");
        address_id.setText("");
        profile_name.setText("");
        email.setText("");
        password.setText("");
        address_id.setText("");
        address2.setText("");
        city.setText("");
        state.setText("");
        zip.setText("");
        country.setText("");
        latitude.setText("");
        longitude.setText("");
        phone.setText("");
    }

}