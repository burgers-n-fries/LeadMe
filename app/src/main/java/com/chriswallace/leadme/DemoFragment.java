package com.chriswallace.leadme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by cwallace on 12/9/14.
 */
public class DemoFragment extends Fragment {
    GoogleMap map;
    Button forward;
    Button left;
    Button right;
    Button back;
    Button stop;
    Button one;
    Button two;
    Button three;
    Button four;
    Button five;




    String searchedText;
    public DemoFragment() {
        ;


    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        final View rootView = inflater.inflate(R.layout.demo_layout, container, false);





        forward = (Button) rootView.findViewById(R.id.forward);
        back = (Button) rootView.findViewById(R.id.back);
        left = (Button) rootView.findViewById(R.id.left);
        right = (Button) rootView.findViewById(R.id.right);
        stop = (Button) rootView.findViewById(R.id.stop);
        one = (Button) rootView.findViewById(R.id.one);
        two = (Button) rootView.findViewById(R.id.two);
        three = (Button) rootView.findViewById(R.id.three);
        four = (Button) rootView.findViewById(R.id.Four);
        five = (Button) rootView.findViewById(R.id.five);




        one.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@0!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@72!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@144!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@216!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@288!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@-50!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });




        forward.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@144!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@322!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@232!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity)getActivity();
            @Override
            public void onClick(View v) {
                String writeString = "angle@54!"; //TEST FUNCTIONALITY
                byte[] b = writeString.getBytes(Charset.forName("ASCII"));
                Log.d("NULL", b.toString());
                if (App.app.mConnectThread != null && App.app.mConnectThread.mConnectedThread != null) {
                    App.app.mConnectThread.mConnectedThread.write(b);
                }


            }
        });




        return rootView;




    }




}
