package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telemedicine.myclinic.models.dashboardModels.ApptMinisModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.views.RegularTextView;

import java.util.ArrayList;

public class SpinnerCustomAdapter extends ArrayAdapter<String> {

    ArrayList<String> objects;
    Activity mc;

    public SpinnerCustomAdapter(Context context, int textViewResourceId,
                                ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        mc = (Activity) context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = mc.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_item_list, parent, false);
        RegularTextView label = row.findViewById(R.id.item);
        label.setText(objects.get(position));

        return row;
    }
}