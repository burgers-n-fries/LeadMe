package com.chriswallace.leadme;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cwallace on 10/21/14.
 */
public class InitialFragment extends Fragment {
    GoogleMap map;
    Button start;
    Button cancel;
    ListView results;
    Boolean open;
    SearchView searchView;
    TextView directionView;


    String searchedText;
    public InitialFragment() {
        this.map = null;
        this.open = false;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedFromList = (results.getItemAtPosition(position)).toString();
                searchView.setQuery(selectedFromList,true);
                results.setVisibility(View.INVISIBLE);
                App.app.destinations.clear();

            }
        });


        inflater.inflate(R.menu.main, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered

                HTTPFunctions http = new HTTPFunctions(getActivity());
                if (newText != null) {
                    if (open) {
                        Log.d("TEST", newText);
                        http.autocompleteSearch(newText);
                        results.setVisibility(View.VISIBLE);
                        results.setAdapter(new AutocompleteAdapter(getActivity(), R.layout.results_layout,
                                App.app.destinations));

                    }
                    open = true;
                }
                return true;
            }



            public boolean onQueryTextSubmit(String query) {

                    //SHOULD I CLEAR TEXT??
                    App.app.destination = query;
                    HTTPFunctions http = new HTTPFunctions(getActivity());
                    http.directionSearch(query, false);
                    searchedText = query;
                    start.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                    App.app.destinations.clear();
                    results.setVisibility(View.INVISIBLE);
                     if (query.equals("test")) {
                         String writeString = "test!"; //TEST FUNCTIONALITY
                         byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                         Log.d("NULL", b.toString());
                         if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                             App.app.mConnectThread.mConnectedThread.write(b);
                         }
                     }
                    InputMethodManager imm = (InputMethodManager) App.app.getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    return true;
                }

        };
        searchView.setOnQueryTextListener(queryTextListener);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        start = (Button) rootView.findViewById(R.id.Start);
        cancel = (Button) rootView.findViewById(R.id.Cancel);
        results = (ListView) rootView.findViewById(R.id.autocomplete);
        directionView = (TextView) rootView.findViewById(R.id.directionDisplay);
        setHasOptionsMenu(true);
        //MapView mMap = (MapView) rootView.findViewById(R.id.map);
        //mMap.onCreate(savedInstanceState);
        //GoogleMap map = mMap.getMap();
        MapFragment frag = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
        this.map =frag.getMap();
        Log.d("MAP",map.toString());
        LocationManager mLocationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Boolean enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        Location lastLocal = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocal != null) {
            Log.d("LOCATION",lastLocal.toString());
        }
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.291022, -71.265235), 12.0f));
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 4, mLocationListener); //ADJUST TO BALANCE PERFORMANCE WITH ABTTERY LIFE
       // mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,3000,4,coarseLocationListener);
           // MapsInitializer.initialize(this.getActivity());



        start.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                //cancel.s
                Log.d("BEGIN", "STARTING DIRECTIONS");
                MapFunctions.zoomMap(map,App.app.location, 16.0f);
                MapFunctions.clearMapRedraw(map, App.app.location ,App.app.WaypointList);
                start.setVisibility(View.INVISIBLE);
                directionView.setVisibility(View.VISIBLE);
                directionView.setText(Html.fromHtml(App.app.directionList.get(0).second));
                //directionView.setText("THIS IS A TEST OF EVERYTHING");
                activity.getActionBar().hide();
                //ORIENT BASED ON COMPASS, ALSO GET LAST LOCATION, 

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity) getActivity();

            @Override
            public void onClick(View v) {
                App.app.WaypointList = null;

                MapFunctions.zoomMap(map, App.app.location, 12.0f);
                MapFunctions.clearMapRedraw(map, App.app.location, null);
                cancel.setVisibility(View.INVISIBLE);
                directionView.setVisibility(View.INVISIBLE);
                searchView.setQuery("",false);
                App.app.directionList.clear();
                activity.getActionBar().show();
                //ORIENT BASED ON COMPASS, ALSO GET LAST LOCATION,

            }
        });






        return rootView;




    }




    private final LocationListener mLocationListener = new LocationListener() { //FINE LOCATION LISTERENER,
        @Override
        public void onLocationChanged(final Location location) {
            Log.d("LOCATION",location.toString());
            if (App.app.previousWaypoint != null){

                double recalcCheck = MapFunctions.recalculateCheck(App.app.location,App.app.previousWaypoint,App.app.WaypointList.get(0));
                if (recalcCheck > 50){
                    HTTPFunctions http = new HTTPFunctions(getActivity());
                    http.directionSearch(App.app.destination,true);


                }
            }

            //GET DISTANCE FROM LINE, THEN RECALCULATE;
            if (App.app.started == true) {
                MapFunctions.zoomMap(map, new LatLng(location.getLatitude(), location.getLongitude()), 16.0f);
            }
            if (App.app.mapInitialized == false){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
                App.app.mapInitialized = true;
            }
            App.app.location = new LatLng(location.getLatitude(), location.getLongitude());
            if (App.app.WaypointList != null) {

                //REMOVE WAYPOINT IF YOU REACHED IT
                Double checkDistance = MapFunctions.calculateDistance(App.app.location,App.app.WaypointList.get(0));
                Double newInstruction = MapFunctions.calculateDistance(App.app.location,App.app.directionList.get(0).first);
                if (newInstruction < 20){
                    App.app.directionList.remove(0);
                    directionView.setText(Html.fromHtml(App.app.directionList.get(0).second));
                }
                if (checkDistance < 20) { //MAYBRE CHANGE TO A CLSOER DISTANCE, 40 IS PRETTY FAR.
                    App.app.previousWaypoint = App.app.WaypointList.get(0);
                    App.app.WaypointList.remove(0);
                }


                MapFunctions.clearMapRedraw(map, App.app.location, App.app.WaypointList);

                Double NorthAngle = MapFunctions.determineAngle(App.app.WaypointList,App.app.location,checkDistance);
                Log.d("ANGLE", NorthAngle.toString());

                //WRITE ANGLE TO BLUETOOTH
                String writeString = "angle@" + String.valueOf(NorthAngle.intValue()) + "!";
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL",b.toString());
                if (App.app.mConnectThread != null &&  App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }
                else {
                    Log.d("DIDN't WRITE","THIS DIDNT WRITE< BLUETOOTH IS NOT CONNECTED");
                }
            }
            //MapFunctions.drawCircle(map,activity.location); //CHANGE COLOR, MAYBE ADD SCALING FACOTR BASED ON ZOOM LEVEL, ALSO DELETE IT, SO CLEAR THE MAP, THEN REDRAW IT WITH NEW PATH AS WELL
            //your code here
        }

        public void onProviderEnabled(String Provider){
            Log.d("YO","PROVIDER ENABLED");
        }

        public void onProviderDisabled(String Provider){
            Log.d("YO","PROVIDER DISABLED");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
        //THIS SHOULD PROMPT THE USER TO TURN BACK ON
        public void onStatusChanged(String LocationServices, int status, Bundle extras){
            Log.d("ERROR","THE GPS WENT AWAY");
            //CREATE A MESSAGE TELLING SUER TO TURN GPS BACK ON
        }
    };

    private final LocationListener coarseLocationListener = new LocationListener() { //COARSE LISTENER SHOULD NOT DO THESE THINGS ON REAL APP, ASEFUL FOR TESTING
        @Override
        public void onLocationChanged(final Location location) {
            Log.d("COARSELOCATION",location.toString());


            MainActivity activity = (MainActivity)getActivity();
            if (App.app.started == true) {
                MapFunctions.zoomMap(map, new LatLng(location.getLatitude(), location.getLongitude()), 16.0f);
            }
            if (App.app.mapInitialized == false){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
                App.app.mapInitialized = true;
            }
            App.app.location = new LatLng(location.getLatitude(), location.getLongitude());
            if (App.app.WaypointList != null) {

                Double checkDistance = MapFunctions.calculateDistance(App.app.location,App.app.WaypointList.get(0));
                if (checkDistance < 20) {
                    App.app.WaypointList.remove(0);
                }

                //clear map and redraw with current location and waypoints.
                MapFunctions.clearMapRedraw(map, App.app.location, App.app.WaypointList);
                // Determine angle to vibrate
                Double NorthAngle = MapFunctions.determineAngle(App.app.WaypointList,App.app.location, checkDistance);
                Log.d("ANGLE", NorthAngle.toString());

                //WRITE THE ANGEL TO THE BLUETOOTH
                String writeString = "angle@" + String.valueOf(NorthAngle.intValue()) + "!";
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL",b.toString());
                if (App.app.mConnectThread != null &&  App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }
                else {
                    Log.d("DIDN't WRITE","THIS DIDNT WRITE< BLUETOOTH IS NOT CONNECTED");
                }

            }
        }

        public void onProviderEnabled(String Provider){
            Log.d("YO","PROVIDER ENABLED");
        }

        public void onProviderDisabled(String Provider){
            Log.d("YO","PROVIDER DISABLED");
        }

        public void onStatusChanged(String LocationServices, int status, Bundle extras){
            Log.d("ERROR","THE GPS WENT AWAY");
            //CREATE A MESSAGE TELLING SUER TO TURN GPS BACK ON
        }
    };


}
