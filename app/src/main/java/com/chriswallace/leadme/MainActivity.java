package com.chriswallace.leadme;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
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


public class MainActivity extends FragmentActivity {



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




    /**
     * A placeholder fragment containing a simple view.
     */










}
