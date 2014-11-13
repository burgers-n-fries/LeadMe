package com.chriswallace.leadme;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by cwallace on 11/5/14.
 */
public class AutocompleteAdapter extends ArrayAdapter<String> {

    public AutocompleteAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);


    }


}
