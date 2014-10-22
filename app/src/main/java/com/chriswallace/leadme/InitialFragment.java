package com.chriswallace.leadme;

import android.app.Fragment;
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
        //MapView mMap = (MapView) rootView.findViewById(R.id.map);
        //mMap.onCreate(savedInstanceState);
        //GoogleMap map = mMap.getMap();
        MapFragment frag = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
        map = frag.getMap();
        Log.d("MAP",map.toString());
           // MapsInitializer.initialize(this.getActivity());





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

        if (points.size() == 1) {
            Log.d("ONLY","ONE LONG");
            for (i = 0; i < points.get(0).size(); i++) {
                String lat = points.get(0).get(i).get("lat");
                String lng = points.get(0).get(i).get("lng");
                double lati = Double.parseDouble(lat);
                double longi = Double.parseDouble(lng) ;
                Log.d(lat, lng);
                route.add(new LatLng(lati,longi));
            }
            map.addPolyline(route);
        }
        else {
            for (i = 0; i < points.size(); i++) {
                int j;
                for (j = 0; j < points.get(i).size(); i++) {
                    String lat = points.get(i).get(j).get("lat");
                    String lng = points.get(i).get(j).get("lng");
                    Log.d(lat, lng);
                    //route.add(new LatLng());
                }
            }
        }

    }
}
