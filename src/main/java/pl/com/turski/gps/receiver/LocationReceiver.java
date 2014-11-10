package pl.com.turski.gps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.appspot.modern_radius_498.location.Location;
import pl.com.turski.gps.App;
import pl.com.turski.gps.activity.MainActivity;
import pl.com.turski.gps.model.LocationSubmitModel;
import pl.com.turski.gps.settings.SettingKey;

import java.io.IOException;

/**
 * User: Adam
 */
public class LocationReceiver extends BroadcastReceiver {

    double oldLatitude, oldLongitude;
    double latitude, longitude;

    @Override
    public void onReceive(final Context context, final Intent calledIntent) {

        latitude = calledIntent.getDoubleExtra("latitude", -1);
        longitude = calledIntent.getDoubleExtra("longitude", -1);


        if (oldLatitude != latitude || oldLongitude != longitude) {
            oldLatitude = latitude;
            oldLongitude = longitude;
            updateRemote(latitude, longitude);
        }
    }

    private void updateRemote(final double latitude, final double longitude) {
        Log.d("TRAK_GPS", "Sending location to server: [latitude='" + latitude + "', longitude='" + longitude + "']");
        MainActivity.showToast("Aktualizacja lokalizacji [" + latitude + ":" + longitude + "]");
        SharedPreferences settings = App.getAppContext().getSharedPreferences("pl.com.turski.trak.gps", Context.MODE_PRIVATE);
        String locatorId = settings.getString(SettingKey.GPS_LOCATOR_IDENTIFIER.getKey(), SettingKey.GPS_LOCATOR_IDENTIFIER.getDefValue());
        LocationSubmitModel submitModel = new LocationSubmitModel(locatorId, latitude, longitude);
        SubmitLocationTask locationTask = new SubmitLocationTask();
        locationTask.execute(submitModel);
    }

    private class SubmitLocationTask extends AsyncTask<LocationSubmitModel, Void, Void> {

        @Override
        protected Void doInBackground(LocationSubmitModel... submitModels) {
            try {
                Location location = App.getLocationService();
                LocationSubmitModel submitModel = submitModels[0];
                location.addLocation(submitModel.getLocatorId(), submitModel.getLatitude(), submitModel.getLongitude()).execute();
            } catch (IOException e) {
                Log.e("TRAK_GPS", "IOException occured during sending location", e);
                MainActivity.showToast("Wystąpił błąd podczas wysyłania lokalizacji na serwer");
            }
            return null;
        }
    }
}
