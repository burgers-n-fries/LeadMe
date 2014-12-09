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
        //CALLED TO BOUND THE MAP WHEN NEW DIRECTIONS ARE SEARCHED
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
    }

    public static ArrayList<LatLng> drawRoute(GoogleMap map, List<List<HashMap<String,String>>> points,Boolean recalculate){
        //DRAWS THE ROUTE ON THE ACTIVE MAP, CALLED WHEN YOU HIT DIRECTIONS
        ArrayList<LatLng> waypoints = new ArrayList<LatLng>();


        PolylineOptions route = new PolylineOptions();
        route.color(0xFF0000FF);

        int i;
        Log.d("THIS",points.get(0).toString());
        double minLat = 100000; //ARBITRARY MIN MAXES
        double minLong = 100000;
        double maxLong = -10000;
        double maxLat = -10000;
        if (points.size() == 1) { //FOR SOME REASON THIS ONCE ORGANIZED AS a 2D POINTS ARRAY, ADDED THIS CHECK STATEMENT TO HANDLE BOTH CASES, GNEREALLY THIS IS TRUE
            Log.d("ONLY","ONE LONG");
            for (i = 0; i < points.get(0).size(); i++) {
                String lat = points.get(0).get(i).get("lat");
                String lng = points.get(0).get(i).get("lng");
                double lati = Double.parseDouble(lat);
                double longi = Double.parseDouble(lng) ;

                route.add(new LatLng(lati,longi));
                waypoints.add(new LatLng(lati,longi)); //ADD TO THE WAYPOINT LIST
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
            if (recalculate == false){
                reboundMap(map,bounds);
            }



        }
        else {
            for (i = 0; i < points.size(); i++) {
                int j;
                for (j = 0; j < points.get(i).size(); i++) {
                    String lat = points.get(i).get(j).get("lat");
                    String lng = points.get(i).get(j).get("lng");
                    Log.d(lat, lng);
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
            }


        return waypoints;
    }

    public static void zoomMap(GoogleMap map, LatLng center, float zoom){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
    }

    public static void drawCircle(GoogleMap map, LatLng center){
        CircleOptions circle = new CircleOptions().center(center).radius(8).fillColor(0x110000FF).strokeColor(0xFF0000FF);
        map.addCircle(circle);
    }

    public static void clearMapRedraw(GoogleMap map, LatLng center ,ArrayList<LatLng> waypoints) {
        map.clear();
        if (waypoints == null) {
            Log.d("Logging", "CLEARING MAP");
        } else {
            int i;
            PolylineOptions route = new PolylineOptions();
            route.color(0xFF0000FF);
            for (i = 0; i < waypoints.size(); i++) {

                route.add(waypoints.get(i));
            }
            map.addPolyline(route);
            drawCircle(map, center);
        }
    }

    public static double calculateDistance(LatLng c1, LatLng c2){
        //HAVERSINE FORMULA, TRUST IN THE STACK OVERFLOW THAT THIS WORKS, SEEMS TO
        int R = 6371;
        double lat1 = Math.toRadians(c1.latitude);
        double lat2 = Math.toRadians(c2.latitude);
        double long1 = Math.toRadians(c1.latitude);
        double long2 = Math.toRadians(c2.latitude);
        double latDif = lat2-lat1;
        double longDif = long2-long1;
        double a = Math.pow(Math.sin(latDif/2),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(longDif/2),2);
        double c = 2* Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        Double test = R*c;

        return R*c*3280; //FEET PER KILOMETER, CONVERTING UNITS

    }

    public static double determineAngle(LatLng waypoints, LatLng location, double distance){

        //double distance = calculateDistance(location,waypoints.get(0));

        if (distance < 100) {
            // SLOWLY TAPER TOWARDS SECONDARY WAYPOINT AS YOU APROACH THE FIRST
            double weight0 = distance/100;
            double weight1 = (100 - distance)/100;

            //Code to change angle here... but for now
            double LatDif0 = location.latitude - waypoints.latitude;
            double LongDif0 = location.longitude - waypoints.longitude;
            Double angle0 = (Math.toDegrees(Math.atan2(LongDif0, LatDif0))+90) % 360;

            double LatDif1 = location.latitude - waypoints.latitude;
            double LongDif1 = location.longitude - waypoints.longitude;
            Double angle1 = (Math.toDegrees(Math.atan2(LongDif1, LatDif1))+90) % 360;

            return angle0*weight0+weight1*angle1;

        }
        else{
            double LatDif = location.latitude - waypoints.latitude;
            double LongDif = location.longitude - waypoints.longitude;
            //double hypotenuse = Math.pow(Math.pow(LatDif,2) + Math.pow(LongDif,2),.5);
            Double angle = Math.toDegrees(Math.atan2(LongDif, LatDif));
            //REPALCE THOSE WITH FUNCTION TO DETERMINE ANGLE IT SHOULD VIBRATE< THAT INCLUDES MEASURED FOR WHEN YOU NEXT HAVE TO TURN
            Log.d("WEIRD STUFF",angle.toString());
            angle = (angle +360)%360;
            Double NorthAngle = (angle - 90 ) % 360;
            return NorthAngle;
        }
    }

    public static double recalculateCheck(LatLng location, LatLng pt1, LatLng pt2){

        //CHECKS BY HOW MUCH YOU ARE OFF THE ROUTE BY.
        double lineSlope = (pt1.longitude - pt2.longitude)/(pt1.latitude-pt2.latitude);
        double invertedSlope =  -(pt1.latitude-pt2.latitude)/(pt1.longitude - pt2.longitude);
        double intercept1 = pt1.longitude-lineSlope*pt1.latitude;
        double intercept2 = location.longitude-invertedSlope*location.latitude;
        double latpoi = (intercept1-intercept2)/(invertedSlope-lineSlope);
        double longpoi = latpoi*lineSlope+intercept1;
        return calculateDistance(new LatLng(latpoi,longpoi),location);
    }

    public static Double totalDistance(ArrayList<LatLng> waypointList){
        int i;
        Double distance = 0.0;
        for (i = 0;i < waypointList.size()-1;i++){
            distance = distance + calculateDistance(waypointList.get(i),waypointList.get(i+1));
        }
        return distance;
    }


    public static ArrayList<LatLng> determineOffsets(LatLng point, Double ANGLE) {
        ArrayList<LatLng> returnList = new ArrayList<LatLng>();
        Double lat = Math.asin(Math.sin(point.latitude)*Math.cos(15)+Math.cos(point.latitude)*Math.sin(15)*Math.cos((ANGLE+90)%360));
        Double lon = ((point.longitude-Math.asin(Math.sin((ANGLE+90)%360)*Math.sin(15)/Math.cos(15))+3.14) % (2*3.14)) - 3.14;
        LatLng pt1 = new LatLng(lat,lon);
        returnList.add(pt1);

        Double lat2 = Math.asin(Math.sin(point.latitude)*Math.cos(15)+Math.cos(point.latitude)*Math.sin(15)*Math.cos((ANGLE+360-90)%360));
        Double lon2 = ((point.longitude-Math.asin(Math.sin((ANGLE+360-90)%360)*Math.sin(15)/Math.cos(15))+3.14) % (2*3.14)) - 3.14;
        LatLng pt2 = new LatLng(lat2,lon2);
        returnList.add(pt2);

        return returnList;
    }

}



