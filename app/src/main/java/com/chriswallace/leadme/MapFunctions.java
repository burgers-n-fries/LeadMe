package com.chriswallace.leadme;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cwallace on 10/22/14.
 */
public class MapFunctions {

    public static void reboundMap(GoogleMap map, LatLngBounds bounds){
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
    }

    public static ArrayList<LatLng> drawRoute(GoogleMap map, List<List<HashMap<String,String>>> points){
        ArrayList<LatLng> waypoints = new ArrayList<LatLng>();
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

                route.add(new LatLng(lati,longi));
                waypoints.add(new LatLng(lati,longi));
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

        return waypoints;
    }

    public static void zoomMap(GoogleMap map, LatLng center, float zoom){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
    }

    public static void drawCircle(GoogleMap map, LatLng center){
        CircleOptions circle = new CircleOptions().center(center).radius(2);
        map.addCircle(circle);
    }

    public static void clearMapRedraw(GoogleMap map, LatLng center ,ArrayList<LatLng> waypoints){
        map.clear();
        int i;
        PolylineOptions route = new PolylineOptions();
        for (i = 0; i < waypoints.size();i++){

            route.add(waypoints.get(i));
        }
        map.addPolyline(route);
        drawCircle(map,center);
    }
}
