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

import org.json.JSONObject;

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

    public void directionSearch(String URL) { //WONT ALWAYS BE VOID, RETURN INFO FROM DATA
        URL = URL.replaceAll(" ", "+"); // for proper url functionality
        URL = "https://maps.googleapis.com/maps/api/directions/json?origin=1000+Olin+Way+Needham+MA&destination=4+Providence+Rd+Sutton+MA&key=AIzaSyCWaaIJ91rA98zqVpN0GLpLdaNKcAl7HbY";
        final MainActivity activity = ((MainActivity) HTTPFunctions.this.context);
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
                        Mapfrag.drawRoute(results);

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


