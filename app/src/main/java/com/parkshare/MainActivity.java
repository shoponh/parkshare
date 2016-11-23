package com.parkshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
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

import android.location.Geocoder;
//import com.google.android.gms.GeoPoint;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, LocationListener {
public class MainActivity extends AppCompatActivity implements LocationListener{
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

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActionOverflowMenuShown();
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_directions_car_white_24dp);

        displayWelcome();

        Address location = new Address(Locale.US);
        lastKnownLocation = getCurrentLocation();

        /*
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        }catch(Exception e){
            System.out.println(e.toString());
        }
        */

        listItems(this);
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
                                String itemType = "";
                                float latitude = 0;
                                float longitude = 0;
                                try {
                                    itemId = Integer.parseInt((String) currItem.get(Constant.ITEM_ID));
                                    itemAddressId = Integer.parseInt((String) currItem.get(Constant.ITEM_ADDRESS_ID));
                                    specInstruction = currItem.get(Constant.ITEM_SPECIAL_INSTRUNCTION) == null ? "" : (String) currItem.get(Constant.ITEM_SPECIAL_INSTRUNCTION);
                                    itemType = (String) currItem.get(Constant.ITEM_TYPE);
                                    latitude = Float.valueOf( (String) currItem.get(Constant.LATITUDE));
                                    longitude = Float.valueOf( (String) currItem.get(Constant.LONGITUDE));
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }

                                ItemInfo itemInfo = new ItemInfo();
                                itemInfo.setId(itemId);
                                itemInfo.setName("ABC");
                                itemInfo.setAddressId(itemAddressId);
                                itemInfo.setSpecialInstruction(specInstruction);
                                itemInfo.setImage(imgid[itemId]);
                                itemInfo.setItemType(itemType);
                                itemInfo.setLatitude(latitude);
                                itemInfo.setLongitude(longitude);

                                Location locationA = new Location("point A");
                                locationA.setLatitude(lastKnownLocation.getLatitude());
                                locationA.setLongitude(lastKnownLocation.getLongitude());
                                Location locationB = new Location("point B");
                                locationB.setLatitude(latitude);
                                locationB.setLongitude(longitude);
                                double distance1 = locationA.distanceTo(locationB) ;
                                double distanceInMiles = distance1 == 0 ? 0 : distance1 / 1609.344;

                                double distance = Utility.getDistance(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), latitude, longitude);
                                itemInfo.setDistance(distance);

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
                                    String slecteditem = itemname[+position];
                                    Toast.makeText(getApplicationContext(), slecteditem, Toast.LENGTH_SHORT).show();

                                    detailActivity(slecteditem);

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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.user_profile:
                //displayUserProfile()
                Toast.makeText(getApplicationContext(), "User Profile", Toast.LENGTH_LONG).show();
                profileActivity();
                return true;
            case R.id.paypal_info:
                Toast.makeText(getApplicationContext(), "Paypal Info", Toast.LENGTH_LONG).show();
                return true;
            case R.id.transaction_history:
                Toast.makeText(getApplicationContext(), "Transaction History", Toast.LENGTH_LONG).show();
                return true;
            case R.id.rent_paking_space:
                Toast.makeText(getApplicationContext(), "Rent Paking Space", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    public void displayWelcome() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void navigatetoRegisterActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),ProfileActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public void profileActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),ProfileActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public void detailActivity(String slecteditem){
        Intent detailIntent = new Intent(getApplicationContext(),ItemDetailActivity.class);
        detailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        detailIntent.putExtra("item_name", slecteditem);
        startActivity(detailIntent);
    }

    public float getDistance(float currLat, float currLong, float itemLatitude, float itemLongitude) {
        float distance = 0;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(currLat-itemLatitude, currLong-itemLongitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            System.out.println("=======================================================================================");

        }catch (Exception e){
            System.out.println(e.toString());
        }



        /*
        GeoPoint p1 = null;
        //p1 = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
        p1 = new GeoPoint((int)((currLat-itemLatitude) * 1E6), (int)((currLong-itemLongitude) * 1E6));

        System.out.println("=======================================================================================");
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        System.out.println("=======================================================================================");
        */

        return 1;
    }

    public Address getLatiLong(String strAddress) {
        Address address = new Address(null);
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = new ArrayList<Address>();
        double latitude = 0.0;
        double longitude = 0.0;

        try {
            System.out.println("=======================================================================================");
            addresses = geocoder.getFromLocationName(strAddress, 1);
            if (addresses==null) {
                System.out.println("Address is null!");
            }
            address = addresses.get(0);
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            System.out.println("Latitude: "+ latitude);
            System.out.println("Longitude: "+ longitude);
            return address;
        }catch(Exception e) {
            System.out.println(e.toString());
        }

        return address;
    }
    public Location getCurrentLocation(){
        Location currentLocation = null;

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  currentLocation;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        System.out.println("=======================================================================================");
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = new ArrayList<Address>();
            addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        }catch(Exception e){
            System.out.println(e.toString());
        }
        System.out.println("=======================================================================================");

        return currentLocation;
    }

    /****
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.shopon.androidrestfulwstest/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }
    ***/

    /***
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.shopon.androidrestfulwstest/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }
    **/

    @Override
    public void onLocationChanged(Location location) {
        if( locationManager == null || location == null ){
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lastKnownLocation = getCurrentLocation();

        }else{
            //txtLat = (TextView) findViewById(R.id.textview1);
            //txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
            System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
            lastKnownLocation = location;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("Latitude - disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("Latitude - enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("Latitude - status");
    }
}