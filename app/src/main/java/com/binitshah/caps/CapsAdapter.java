package com.binitshah.caps;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by bshah on 5/22/2016.
 */
public class CapsAdapter {
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewAge;

        public ViewHolder(View view) {
            super(view);
            //textViewName = (TextView) view.findViewById(R.id.textview_name);
            //textViewAge = (TextView) view.findViewById(R.id.textview_age);
        }
    }

        public CapsAdapter(Query query, Class<Cap> itemClass, @Nullable ArrayList<Cap> items,
                         @Nullable ArrayList<String> keys) {
            //super(query, itemClass, items, keys);
        }
}
