package pl.com.turski.gps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.appspot.trak.location.Location;
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
        SharedPreferences settings = App.getAppContext().getSharedPreferences("pl.com.turski.trak.gps", Context.MODE_PRIVATE);
        String vehicleId = settings.getString(SettingKey.VEHICLE_ID.getKey(), SettingKey.VEHICLE_ID.getDefValue());
        LocationSubmitModel submitModel = new LocationSubmitModel(Long.parseLong(vehicleId), latitude, longitude);
        new SubmitLocationTask().execute(submitModel);
    }

    private class SubmitLocationTask extends AsyncTask<LocationSubmitModel, Void, Void> {

        @Override
        protected Void doInBackground(LocationSubmitModel... submitModels) {
            try {
                Location location = App.getLocationService();
                LocationSubmitModel submitModel = submitModels[0];
                location.addVehicleLocation(submitModel.getVehicleId(), submitModel.getLatitude(), submitModel.getLongitude()).execute();
            } catch (IOException e) {
                Log.e("TRAK_GPS", "IOException occured during adding vehicle localization", e);
                MainActivity.showToast("Wystąpił błąd podczas wysyłania lokalizacji na serwer");
            }
            return null;
        }
    }
}
