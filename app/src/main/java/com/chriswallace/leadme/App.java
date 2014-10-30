package com.chriswallace.leadme;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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

    @Override
    public void onCreate() {
        super.onCreate();
        this.location = null;
        this.WaypointList = null;
        this.started = false;
        this.mapInitialized = false;
        app = this;
    }



    public void createConnectThread(BluetoothDevice device){
        mConnectThread = new ConnectThread(device);
        mConnectThread.run();

    }
}
