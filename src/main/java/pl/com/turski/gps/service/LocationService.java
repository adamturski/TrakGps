package pl.com.turski.gps.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import pl.com.turski.gps.listener.LocationListenerImpl;
import pl.com.turski.gps.settings.SettingKey;

public class LocationService extends Service {

    private Thread locationThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        addLocationListener();
        return ret;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void addLocationListener() {
        locationThread = new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences settings = getSharedPreferences("pl.com.turski.trak.gps", Context.MODE_PRIVATE);
                    String localizationFrequency = settings.getString(SettingKey.LOCALIZATION_FREQUENCY.getKey(), SettingKey.LOCALIZATION_FREQUENCY.getDefValue());
                    Looper.prepare();
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    Criteria c = new Criteria();
                    c.setAccuracy(Criteria.ACCURACY_COARSE);

                    LocationListener locationListener = new LocationListenerImpl();
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,Long.parseLong(localizationFrequency) , 0, locationListener);
                    Looper.loop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, "LocationThread");
        locationThread.start();
    }

    @Override
    public void onDestroy() {
        locationThread.interrupt();
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
