package com.chriswallace.leadme;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cwallace on 10/21/14.
 */
public class HTTPFunctions {

    public Context context;
    public RequestQueue queue;


    public HTTPFunctions(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }//ALWAYS PASS IN getActivity

    public void directionSearch(String Destination) { //WONT ALWAYS BE VOID, RETURN INFO FROM DATA
        final MainActivity activity = (MainActivity)context;
        LatLng latlong = App.app.location;
        String URL;
        Destination = Destination.replaceAll(" ", "+"); // for proper url functionality
        //URL = "https://maps.googleapis.com/maps/api/directions/json?origin=1000+Olin+Way+Needham+MA&destination=4+Providence+Rd+Sutton+MA&key=AIzaSyCWaaIJ91rA98zqVpN0GLpLdaNKcAl7HbY";
        URL = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latlong.latitude + "," + latlong.longitude + "&destination=" + Destination + "&key=AIzaSyCWaaIJ91rA98zqVpN0GLpLdaNKcAl7HbY";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TEst", response.toString());// display response
                        DirectionsJSONParser parser = new DirectionsJSONParser();
                        List<List<HashMap<String,String>>> results = parser.parse(response);
                        Log.d("REULTS",results.toString());
                        Fragment frag = activity.getFragmentManager().findFragmentByTag("Map");
                        InitialFragment Mapfrag = (InitialFragment)frag;
                        ArrayList<LatLng> waypoints= MapFunctions.drawRoute(Mapfrag.map, results);
                        App.app.WaypointList = waypoints;
                        App.app.mapInitialized = true;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        queue.add(getRequest);

    }
}


