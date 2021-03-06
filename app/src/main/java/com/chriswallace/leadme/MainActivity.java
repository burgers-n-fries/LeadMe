package com.chriswallace.leadme;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;


import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends FragmentActivity implements SensorEventListener {


    public SensorManager mSensorManager;
    public Sensor magnetometer;
    public Sensor accelerometer;
    public MainActivity(){


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .add(R.id.container, new InitialFragment(),"Map")
                    .commit();
        }

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
// Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        //mDevice = mBluetoothAdapter.getBondedDevices()
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() == 0) {
            Log.d("NO DEVICE","NO DEVICE FOUND");
        }

        Log.d("TEST","WITHIN ONCREATE");
        if (pairedDevices.size() > 0) {
            Log.d("SIZE","A PAIRED DEVICE EXISTS");
            for (BluetoothDevice device : pairedDevices) {
                //MIGHT NEED TO HANDLE THIS DIFFERENT SINCE THIS IS A FOR LOOP
                BluetoothDevice mDevice = device;
                Log.d("CREATEDACONNECTTHREAD","CREATEDACONNECTTHREAD");
               App.app.createConnectThread(mDevice);
                break;
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }



    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }
    Float azimut;
    float[] mGravity;
    float[] mGeomagnetic;


    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) //NOT USING ACCELEROMETER, BUT SHIT IS THERE
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll
                App.app.heading = azimut;
            }
        }

    }




    /**
     * A placeholder fragment containing a simple view.
     */










}
