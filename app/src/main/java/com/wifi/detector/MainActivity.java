package com.wifi.detector;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


public class MainActivity extends AppCompatActivity
{


    ArrayList<WifiDevice>mList=new ArrayList<>();
    ListView mListview;
    WifiListAdapter adapter;
    TextView tvDistance,tvDeviceID,tvDeviceType;
    List<ScanResult> wifiScanResults;
    String currentID="";
    int unit=0;
    SharedPreferences sharedpreferences;
    ColorArcProgressBar mainBar;
    public final String MyPREFERENCES = "WifiDetector";
    SwitchCompat filterSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mListview=findViewById(R.id.wifi_list);
        mainBar=findViewById(R.id.main_bar);
        tvDistance=findViewById(R.id.tv_distance);
        tvDeviceID=findViewById(R.id.tv_device_id);
        tvDeviceType=findViewById(R.id.tv_device_type);
        filterSwitch=findViewById(R.id.filter_switch);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        unit=sharedpreferences.getInt("unitKey",0);
        adapter=new WifiListAdapter(this,mList);
        mListview.setAdapter(adapter);

        filterSwitch.setChecked(sharedpreferences.getBoolean("isFiltered",false));


        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                sharedpreferences.edit().putBoolean("isFiltered",isChecked).apply();
            }
        });


        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                if(filterSwitch.isChecked())
                {

                    currentID=mList.get(position).getSsID();
                    WifiConfiguration conf = new WifiConfiguration();
                    conf.SSID = "\"" + mList.get(position).getSsID() + "\"";
                    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.addNetwork(conf);


                    List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                    for( WifiConfiguration i : list )
                    {
                        if(i.SSID != null && i.SSID.equals("\"" + mList.get(position).getSsID() + "\""))
                        {
                            wifiManager.disconnect();
                            wifiManager.enableNetwork(i.networkId, true);
                            wifiManager.reconnect();
                            break;
                        }
                    }

                }


            }
        });


        ImageView settingsImage=findViewById(R.id.img_settings);

        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    showAlertDialog();
            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                int strength=0;

                WifiManager wifiManager=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                currentID=wifiManager.getConnectionInfo().getSSID().replaceAll("^\"|\"$", "");

                WifiInfo wifiInfo=wifiManager.getConnectionInfo();
                int signalelvel=wifiManager.getConnectionInfo().getRssi()*-1;

                if(signalelvel<30)
                {
                    strength=100;
                }

                else

                {
                    strength=130-signalelvel;
                }

                tvDeviceID.setText(wifiInfo.getSSID());
                mainBar.setCurrentValues(strength);

                if(currentID.contains("TP"))
                {
                    tvDeviceType.setText("Trac Pac");
                }
                else if(currentID.contains("AT"))
                {
                    tvDeviceType.setText("Asset Tracker");
                }

                else if(currentID.contains("BT"))
                {
                    tvDeviceType.setText("Bicycle Tracker");
                }

                else if(currentID.contains("VL"))
                {
                    tvDeviceType.setText("Vehicle Tracker");
                }

                else if(currentID.contains("PT"))
                {
                    tvDeviceType.setText("Pharma Tracker");
                }

                else if(currentID.contains("JT"))
                {
                    tvDeviceType.setText("Jewelery Tracker");
                }

                else if(currentID.contains("GT"))
                {
                    tvDeviceType.setText("Gun Tracker");
                }

                else
                {
                    tvDeviceType.setText("Unknown Tracker");
                }


                String distance=calculateDistance(wifiInfo.getRssi(),wifiInfo.getFrequency());
                tvDistance.setText("Distance: "+distance+"\n"+wifiInfo.getRssi()+" dB");
                arrangeList();
                handler.postDelayed(this,800);
            }
        }, 100);





















    }




    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void arrangeList()
    {

        WifiManager wifiManager=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //wifiManager.startScan();
        wifiScanResults = wifiManager.getScanResults();

        mList.clear();
        for (ScanResult item : wifiScanResults)
        {

            WifiDevice wifiDevice=new WifiDevice();
            wifiDevice.setSsID(item.SSID);

            wifiDevice.setDbm(String.valueOf(item.level));
            int signalelvel=item.level*-1;

            if(signalelvel<30)
            {
                wifiDevice.setSignalsStrength(100);
            }

            else

            {
                wifiDevice.setSignalsStrength(130-signalelvel);
            }


            if(!item.SSID.equalsIgnoreCase(currentID))
            {

                if(filterSwitch.isChecked())
                {

                        if (item.SSID.contains("BT-"))
                    {
                        mList.add(wifiDevice);
                    }

                }

                else
                {
                    mList.add(wifiDevice);
                }




            }

            adapter.notifyDataSetChanged();

        }


    }





    public void showAlertDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose distance unit");

        // add a list
        String[] animals = {"Feet", "Meter"};
        builder.setItems(animals, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                unit=which;
                sharedpreferences.edit().putInt("unitKey",which).commit();
                arrangeList();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }





    public String calculateDistance(double signalLevelInDb, double freqInMHz)
    {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        double distance= Math.pow(10.0, exp);

        if(unit==0)
        {
            distance = distance*3.2808;
            return String.format("%.2f", distance)+" feet";
        }

        else
        {
            return String.format("%.2f", distance)+" m";
        }

    }


}





