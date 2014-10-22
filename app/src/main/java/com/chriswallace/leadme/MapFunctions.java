package com.chriswallace.leadme;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cwallace on 10/22/14.
 */
public class MapFunctions {
    public static void reboundMap(GoogleMap map, LatLngBounds bounds){
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
    }

    public static void drawRoute(GoogleMap map, List<List<HashMap<String,String>>> points){
        List yo = points.get(0);
        Log.d("TEST", points.get(0).get(0).get("lng"));
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
            reboundMap(map,bounds);

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

    public static void zoomMap(GoogleMap map, LatLng center, float zoom){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
    }
}
