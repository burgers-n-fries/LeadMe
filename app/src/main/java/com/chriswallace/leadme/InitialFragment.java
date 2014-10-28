package com.chriswallace.leadme;

import android.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cwallace on 10/21/14.
 */
public class InitialFragment extends Fragment {
    GoogleMap map;
    public InitialFragment() {
        this.map = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final Button searchDirections = (Button) rootView.findViewById(R.id.directions);
        final Button start = (Button) rootView.findViewById(R.id.Start);
        final Button write = (Button) rootView.findViewById(R.id.write);
        //MapView mMap = (MapView) rootView.findViewById(R.id.map);
        //mMap.onCreate(savedInstanceState);
        //GoogleMap map = mMap.getMap();
        MapFragment frag = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
        this.map =frag.getMap();
        Log.d("MAP",map.toString());
        LocationManager mLocationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Location lastLocal = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocal != null) {
            Log.d("LOCATION",lastLocal.toString());
        }
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.291022, -71.265235), 12.0f));
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,200,1,coarseLocationListener);
           // MapsInitializer.initialize(this.getActivity());



        start.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                Log.d("BEGIN", "STARTING DIRECTIONS");
                MapFunctions.zoomMap(map,activity.location, 16.0f);
                MapFunctions.clearMapRedraw(map, activity.location ,activity.WaypointList);
                //ORIENT BASED ON COMPASS, ALSO GET LAST LOCATION, 

            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                byte one = 1;
                byte two = 2;

                byte[] b = "angle@45!".getBytes(Charset.forName("ASCII"));



                activity.mConnectThread.mConnectedThread.write(b);
                //ORIENT BASED ON COMPASS, ALSO GET LAST LOCATION,

            }
        });


        searchDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addressText = (EditText) rootView.findViewById(R.id.DestinationText);
                String address = addressText.getText().toString();
                //SHOULD I CLEAR TEXT??
                HTTPFunctions http = new HTTPFunctions(getActivity());
                http.directionSearch(address);
            }
        });

        return rootView;




    }




    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d("LOCATION",location.toString());


            MainActivity activity = (MainActivity)getActivity();
            if (activity.started == true) {
                MapFunctions.zoomMap(map, new LatLng(location.getLatitude(), location.getLongitude()), 16.0f);
            }
            if (activity.mapInitialized == false){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
                activity.mapInitialized = true;
            }
            activity.location = new LatLng(location.getLatitude(), location.getLongitude());
            if (activity.WaypointList != null) {

                //REMOVE WAYPOINT IF YOU REACHED IT
                Double checkDistance = MapFunctions.calculateDistance(activity.location,activity.WaypointList.get(0));
                if (checkDistance < 40) { //MAYBRE CHANGE TO A CLSOER DISTANCE, 40 IS PRETTY FAR.
                    activity.WaypointList.remove(0);
                }


                MapFunctions.clearMapRedraw(map, activity.location, activity.WaypointList);

                Double NorthAngle = MapFunctions.determineAngle(activity.WaypointList,activity.location);
                Log.d("ANGLE", NorthAngle.toString());

                //WRITE ANGLE TO BLUETOOTH
                String writeString = "angle@" + String.valueOf(NorthAngle.intValue()) + "!";
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL",b.toString());
                if (activity.mConnectThread != null &&  activity.mConnectThread.mConnectedThread != null) {
                    activity.mConnectThread.mConnectedThread.write(b);
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
        }

        public void onStatusChanged(String LocationServices, int status, Bundle extras){
            Log.d("ERROR","THE GPS WENT AWAY");
            //CREATE A MESSAGE TELLING SUER TO TURN GPS BACK ON
        }
    };

    private final LocationListener coarseLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d("COARSELOCATION",location.toString());


            MainActivity activity = (MainActivity)getActivity();
            if (activity.started == true) {
                MapFunctions.zoomMap(map, new LatLng(location.getLatitude(), location.getLongitude()), 16.0f);
            }
            if (activity.mapInitialized == false){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
                activity.mapInitialized = true;
            }
            activity.location = new LatLng(location.getLatitude(), location.getLongitude());
            if (activity.WaypointList != null) {

                Double checkDistance = MapFunctions.calculateDistance(activity.location,activity.WaypointList.get(0));
                if (checkDistance < 40) {
                    activity.WaypointList.remove(0);
                }

                //clear map and redraw with current location and waypoints.
                MapFunctions.clearMapRedraw(map, activity.location, activity.WaypointList);
                // Determine angle to vibrate
                Double NorthAngle = MapFunctions.determineAngle(activity.WaypointList,activity.location);
                Log.d("ANGLE", NorthAngle.toString());

                //WRITE THE ANGEL TO THE BLUETOOTH
                String writeString = "angle@" + String.valueOf(NorthAngle.intValue()) + "!";
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL",b.toString());
                if (activity.mConnectThread != null &&  activity.mConnectThread.mConnectedThread != null) {
                    activity.mConnectThread.mConnectedThread.write(b);
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
