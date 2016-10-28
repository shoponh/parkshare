package com.parkshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Register Activity Class
 */
public class ItemDetailActivity extends Activity {
    UserInfo userInfo = UserInfo.getInstance();

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    TextView itemName;
    TextView itemDesc;
    TextView itemDistance;
    TextView itemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        //setTitle("Item Detail");

        Intent intent = getIntent();
        String intemName = intent.getStringExtra("item_name") == null ? "" : intent.getStringExtra("item_name");


        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.register_error);

        itemName = (TextView)findViewById(R.id.itemName);
        itemDesc = (TextView)findViewById(R.id.itemDesc);
        itemDistance = (TextView)findViewById(R.id.itemDistance);
        itemPrice = (TextView)findViewById(R.id.itemPrice);

        itemName.setText(intemName);
        itemDesc.setText("Single car parking.");
        itemDistance.setText("1.4 miles");
        itemPrice.setText("$2 per hour");

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }


}