package com.parkshare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
*/
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.parkshare.dao.MyResponseObj;
import com.parkshare.constant.Constant;
import com.parkshare.pojo.ItemInfo;
import com.parkshare.pojo.ResultObject;


//public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, LocationListener {
public class MainActivity extends AppCompatActivity {
    final ArrayList<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
    ProgressDialog mDialog;

    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;
    ListView mainListView;
    //JSONAdapter mJSONAdapter;
    ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    SharedPreferences mSharedPreferences;
    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";
    //ProgressDialog mDialog;
    //final ArrayList<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    Location lastKnownLocation;

    ProgressDialog prgDialog;
    TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActionOverflowMenuShown();
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_directions_car_white_24dp);

        /*
        ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.ic_directions_car_white_24dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        String strAddress = "1 newman street, east brunswick, nj 08816";
        Address location = getLatiLong(strAddress);
        */
        prgDialog = new ProgressDialog(this);
        errorMsg = (TextView)findViewById(R.id.login_error);

        Address location = new Address(Locale.US);
        listItems(this);

        RequestParams params = new RequestParams();
        params.put("useremail", "shoponh@yahoo.com");
        params.put("isall", true);
        params.put("distance", 3.0);
        //invokeWS(params);
    }

    /**
      * Method that performs RESTful webservice invocations
      *
      * @param params
      */
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.0.2:8080/useraction/item/getitems",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        // Set Default Values for Edit View controls
                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
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

    public void setDefaultValues(){
        //nameET.setText("");
        //emailET.setText("");
        //pwdET.setText("");
    }

    /**
     * Method gets triggered when Login button is clicked
     *
     * @param --userEmail
     * @param --isAll
     * @param --distance
     */
    //public void listItems(final Activity context, String userEmail, double latitude, double longitude, boolean isAll, double distance) {
    public void listItems(final Activity context) {
        final MyResponseObj MyResponseObj = new MyResponseObj();
        String userEmail = "shoponh@yahoo.com";

        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(userEmail)) {
            // Put Http parameter username with value of Email Edit View control
            params.put("useremail", "shoponh@yahoo.com");
            params.put("isall", true);
            params.put("distance", 3.0);

            AsyncHttpClient client = new AsyncHttpClient();
            //client.get("http://10.0.0.4:8080/useraction/login/dologin",params ,new AsyncHttpResponseHandler() {
            //RequestHandle sfs = client.get("http://"+ IpPortActivity.getIpPort() +"/useraction/item/getitems",params ,new AsyncHttpResponseHandler());

            //client.get("http://" + IpPortActivity.getIpPort() + "/useraction/item/getitems", params, new AsyncHttpResponseHandler() {
            client.get("http://10.0.0.2:8080/useraction/item/getitems", params, new AsyncHttpResponseHandler() {
                // When the response returned by REST has Http response code '200'
                @Override
                public void onSuccess(String response) {
                    final String[] itemname = {
                            "Safari",
                            "Camera",
                            "Global",
                            "FireFox",
                            "UC Browser",
                            "Android Folder",
                            "VLC Player",
                            "Cold War"
                    };

                    final Integer[] imgid = {
                            R.drawable.pic1,
                            R.drawable.pic2,
                            R.drawable.pic3,
                            R.drawable.pic4,
                            R.drawable.pic5,
                            R.drawable.pic6,
                            R.drawable.pic7,
                            R.drawable.pic8,
                    };

                    try {
                        // JSON Object
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        System.out.println(status);

                        if (Utility.isNotNull(status) && status.equalsIgnoreCase(Constant.SUCCESS)) {
                            JSONArray jsonArr = (JSONArray) obj.get(Constant.ITEM_LIST);

                            jsonArr.length();
                            String[] itemNames = new String[jsonArr.length()];


                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject currItem = (JSONObject) jsonArr.get(i);
                                //String itemId = (String)currItem.get(Constant.ITEM_ID);
                                //String specInstruction = (String)currItem.get(Constant.ITEM_SPECIAL_INSTRUNCTION);
                                //String itemAddressId = (String)currItem.get(Constant.ITEM_ADDRESS_ID);


                                String specInstruction = "";
                                int itemAddressId = -1;
                                int itemId = -1;
                                try {
                                    itemId = Integer.parseInt((String) currItem.get(Constant.ITEM_ID));
                                    itemAddressId = Integer.parseInt((String) currItem.get(Constant.ITEM_ADDRESS_ID));
                                    specInstruction = currItem.get(Constant.ITEM_SPECIAL_INSTRUNCTION) == null ? "" : (String) currItem.get(Constant.ITEM_SPECIAL_INSTRUNCTION);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }

                                ItemInfo itemInfo = new ItemInfo();
                                itemInfo.setId(itemId);
                                itemInfo.setName("ABC");
                                itemInfo.setAddressId(itemAddressId);
                                itemInfo.setSpecialInstruction(specInstruction);
                                itemInfo.setImage(imgid[itemId]);

                                itemNames[i] = "Item-" + itemId;
                                itemInfoList.add(itemInfo);
                            }

                            CustomListAdapter adapter = new CustomListAdapter(context, itemNames, itemInfoList);

                            //CustomListAdapter adapter = new CustomListAdapter(context, itemname, imgid);
                            ListView list = (ListView) findViewById(R.id.list);
                            list.setAdapter(adapter);
                            mDialog.hide();

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    // TODO Auto-generated method stub
                                    String Slecteditem = itemname[+position];
                                    Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    // When Http response code is '404'
                    if (statusCode == 404) {
                        //Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {
                        //Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404, 500
                    else {
                        //Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    //Hack to show three dots
    private void makeActionOverflowMenuShown()
    {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) { menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception e)
        {
            //Log.d(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



}