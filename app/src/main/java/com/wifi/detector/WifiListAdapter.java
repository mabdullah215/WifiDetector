package com.wifi.detector;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WifiListAdapter extends ArrayAdapter<WifiDevice>
{

    private static class ViewHolder
    {
        TextView details;
        ColorArcProgressBarSmall barItem;
    }

    public WifiListAdapter(Context context, ArrayList<WifiDevice> list)
    {
        super(context, R.layout.list_item, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WifiDevice item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.details = convertView.findViewById(R.id.tv_ssid_name);
            viewHolder.barItem = convertView.findViewById(R.id.item_bar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.details.setText("SSID: "+item.getSsID());
        float value=item.getSignalsStrength();
        viewHolder.barItem.setCurrentValues(value);

        return convertView;
    }
}