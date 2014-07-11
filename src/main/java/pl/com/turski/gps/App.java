package pl.com.turski.gps;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.appspot.trak.location.Location;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import pl.com.turski.gps.settings.SettingKey;

/**
 * User: Adam
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }

    public static Location getLocationService() {
        SharedPreferences settings = App.getAppContext().getSharedPreferences("pl.com.turski.trak.gps", Context.MODE_PRIVATE);
        String serverUrl = settings.getString(SettingKey.SERVER_URL.getKey(), SettingKey.SERVER_URL.getDefValue());
        Location.Builder builder = new Location.Builder(
                new ApacheHttpTransport(), new GsonFactory(), null).setRootUrl(serverUrl);
        return builder.build();
    }
}