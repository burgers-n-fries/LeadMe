package com.chriswallace.leadme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by cwallace on 10/21/14.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    public ConnectedThread mConnectedThread;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        mBluetoothAdapter.cancelDiscovery();
        Log.d("HEYEYEYEYFYEYGYREYUGUYERGUYERGYUERYUGREYUG", "RUNNING");
        try {
            mmSocket.connect();
            Log.d("HEYEYEYEYFYEYGYREYUGUYERGUYERGYUERYUGREYUG", "CONNECTED");
        } catch (IOException connectException) {
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        Log.d(mmSocket.toString(),"HOPEFULLY THSI aINT NULL");
        Log.d("RUNNING RUN FUNCTION", "CONNECTED THREAD IS ABOUT TO EXIST");
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();

    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
