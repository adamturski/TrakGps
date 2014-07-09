package pl.com.turski.gps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.com.turski.trak.gps.R;

public class MainActivity extends Activity {

    TextView gpsStatusLabel;
    TextView connectionStatusLabel;
    TextView serviceStatusLabel;
    Button gpsStatusRefreshButton;
    Button connectionStatusRefreshButton;
    Button serviceStatusRefreshButton;
    Button settingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        checkServiceStatus();
        checkGpsStatus();
        checkConnectionStatus();
    }

    private void initView() {
        final Context context = this;
        gpsStatusLabel = (TextView) findViewById(R.id.gpsStatusLabel);
        gpsStatusLabel.setText("");
        connectionStatusLabel = (TextView) findViewById(R.id.appStatusLabel);
        connectionStatusLabel.setText("");
        serviceStatusLabel = (TextView) findViewById(R.id.serviceStatusLabel);
        serviceStatusLabel.setText("");
        settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
            }
        });
        gpsStatusRefreshButton = (Button) findViewById(R.id.gpsStatusRefreshButton);
        gpsStatusRefreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkGpsStatus();
            }
        });
        connectionStatusRefreshButton = (Button) findViewById(R.id.appStatusRefreshButton);
        connectionStatusRefreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkConnectionStatus();
            }
        });
        serviceStatusRefreshButton = (Button) findViewById(R.id.serviceStatusRefreshButton);
        serviceStatusRefreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkServiceStatus();
            }
        });
    }

    private void checkGpsStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsStatusLabel.setText("OK");
        } else {
            gpsStatusLabel.setText("Wyłączony");
        }
    }

    private void checkConnectionStatus() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            connectionStatusLabel.setText("OK");
        } else {
            connectionStatusLabel.setText("Brak");
        }
    }

    private void checkServiceStatus() {
        SharedPreferences settings = this.getSharedPreferences("pl.com.turski.trak.gps", Context.MODE_PRIVATE);

        if (settings.getBoolean("locationService", false)) {
            serviceStatusLabel.setText("OK");
            return;
        }

        serviceStatusLabel.setText("Uruchamianie...");

        Intent mServiceIntent = new Intent(this, LocationService.class);
        startService(mServiceIntent);

        serviceStatusLabel.setText("OK");
    }

    public static void showAlert(String message) {
                AlertDialog ad = new AlertDialog.Builder(App.getAppContext()).create();
                ad.setCancelable(false); // This blocks the 'BACK' button
                ad.setMessage(message);
                ad.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, LocationService.class));
        super.onDestroy();
    }
}
