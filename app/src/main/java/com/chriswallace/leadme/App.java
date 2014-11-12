package com.chriswallace.leadme;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cwallace on 10/30/14.
 */
public class App extends Application {

    public static App app;


    String username;
    LatLng location;
    ArrayList<LatLng> WaypointList;
    Boolean started;
    Boolean mapInitialized;
    ConnectThread mConnectThread;
    Boolean onRoute;
    LatLng previousWaypoint;
    String destination;
    LatLng previousLocation;
    ArrayList<String> destinations; //LOCATION TO DISPALY FOR AUTOCOMPLETE

    ArrayList<Pair<LatLng,String>> directionList;
    @Override
    public void onCreate() {
        super.onCreate();
        this.location = null;
        this.WaypointList = null;
        this.started = false;
        this.mapInitialized = false;
        this.onRoute = false;
        this.destination = null;
        this.previousWaypoint = null;
        this.destinations = new ArrayList<String>();
        this.directionList = new ArrayList<Pair<LatLng, String>>();
        app = this;
    }



    public void createConnectThread(BluetoothDevice device){
        mConnectThread = new ConnectThread(device);
        mConnectThread.run();

    }
}
