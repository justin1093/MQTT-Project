package com.example.viji.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;


public class SilentBeaconService extends Service implements BeaconConsumer {
    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundMode(true);

        // need to understand what this line means really
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d("SilentBeacon", "I just saw an beacon for the first time!");
                Log.d("SilentBeacon", "Region id - " + region.getUniqueId());

                try {
                    String server = "tcp://m11.cloudmqtt.com:11091";
                    MQTTUtils.connect(server);
                    String topic = "Blinds";
                    String status ="Near Beacon";
                    String payload =String.format("{switch: %s}", status);
                    MQTTUtils.pub(topic, payload);


                }catch(Exception ex){

                    String test ="msg  not posted";
                    ex.printStackTrace();

                }


            }

            @Override
            public void didExitRegion(Region region) {
                Log.d("SilentBeacon", "I no longer see a beacon");
                Log.d("SilentBeacon", "Region id - " + region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
            }
        });

        try {
            // can you check this line also what it means???
            Identifier identifier = Identifier.parse("f7826da6-4fa2-4e98-8024-bc5b71e0893e"); //kontakt
            beaconManager.startMonitoringBeaconsInRegion(new Region("Region", identifier, null, null));
        } catch (RemoteException e) {
            Log.e("SilentBeacon", e.getMessage(), e);
        }
    }
}