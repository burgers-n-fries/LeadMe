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

import java.util.HashMap;
import java.util.List;

/**
 * Created by cwallace on 10/21/14.
 */
public class InitialFragment extends Fragment {
    GoogleMap map;
    public InitialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final Button searchDirections = (Button) rootView.findViewById(R.id.directions);
        final Button zoom = (Button) rootView.findViewById(R.id.zoom);
        //MapView mMap = (MapView) rootView.findViewById(R.id.map);
        //mMap.onCreate(savedInstanceState);
        //GoogleMap map = mMap.getMap();
        MapFragment frag = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
        map = frag.getMap();
        Log.d("MAP",map.toString());
        LocationManager mLocationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Location lastLocal = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocal != null) {
            Log.d("LOCATION",lastLocal.toString());
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.291022, -71.265235), 12.0f));
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, mLocationListener);
           // MapsInitializer.initialize(this.getActivity());



        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        searchDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPFunctions http = new HTTPFunctions(getActivity());
                http.directionSearch("THIS IS A TEST");
            }
        });

        return rootView;




    }
    public void drawRoute(List<List<HashMap<String,String>>> points){
        List yo = points.get(0);
        Log.d("TEST",points.get(0).get(0).get("lng"));
        PolylineOptions route = new PolylineOptions();


        int i;
        Log.d("THIS",points.get(0).toString());
        double minLat = 100000;
        double minLong = 100000;
        double maxLong = -10000;
        double maxLat = -10000;
        if (points.size() == 1) {
            Log.d("ONLY","ONE LONG");
            for (i = 0; i < points.get(0).size(); i++) {
                String lat = points.get(0).get(i).get("lat");
                String lng = points.get(0).get(i).get("lng");
                double lati = Double.parseDouble(lat);
                double longi = Double.parseDouble(lng) ;
                Log.d(lat, lng);
                route.add(new LatLng(lati,longi));
                // VALUES TO SET AS BOUNDS
                if (minLat > lati) {
                    minLat = lati;
                }
                else if (maxLat < lati) {
                    maxLat = lati;
                }

                if (minLong > longi) {
                    minLong = longi;
                }
                else if (maxLong < longi) {
                    maxLong = longi;
                }
            }
            LatLngBounds bounds = new LatLngBounds(new LatLng(minLat,minLong),new LatLng(maxLat,maxLong));
            map.addPolyline(route);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,30));
        }
        else {
            for (i = 0; i < points.size(); i++) {
                int j;
                for (j = 0; j < points.get(i).size(); i++) {
                    String lat = points.get(i).get(j).get("lat");
                    String lng = points.get(i).get(j).get("lng");
                    Log.d(lat, lng);
                    //route.add(new LatLng());
                    //UPDATE THIS TO DO SAME AS ABOVE
                }
            }
        }


    }

    public void zoomMap(){

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d("LOCATION",location.toString());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
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
}
