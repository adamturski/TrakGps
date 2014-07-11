package pl.com.turski.gps.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.com.turski.gps.settings.SettingKey;
import pl.com.turski.trak.gps.R;

public class SettingActivity extends Activity {

    TextView serverUrlText;
    TextView vehicleIdText;
    TextView localizationFrequencyText;
    Button saveButton;
    Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initView();
    }

    private void initView() {
        SharedPreferences settings = this.getSharedPreferences("pl.com.turski.trak.gps", Context.MODE_PRIVATE);
        serverUrlText = (TextView) findViewById(R.id.serverUrlText);
        serverUrlText.setText(settings.getString(SettingKey.SERVER_URL.getKey(), SettingKey.SERVER_URL.getDefValue()));
        vehicleIdText = (TextView) findViewById(R.id.vehicleIdText);
        vehicleIdText.setText(settings.getString(SettingKey.VEHICLE_ID.getKey(), SettingKey.VEHICLE_ID.getDefValue()));
        localizationFrequencyText = (TextView) findViewById(R.id.localizationFrequencyText);
        localizationFrequencyText.setText(settings.getString(SettingKey.LOCALIZATION_FREQUENCY.getKey(), SettingKey.LOCALIZATION_FREQUENCY.getDefValue()));
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveSettings();
                finish();
            }
        });
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveSettings() {
        SharedPreferences settings = this.getSharedPreferences("pl.com.turski.trak.gps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingKey.SERVER_URL.getKey(), serverUrlText.getText().toString());
        editor.putString(SettingKey.VEHICLE_ID.getKey(), vehicleIdText.getText().toString());
        editor.putString(SettingKey.LOCALIZATION_FREQUENCY.getKey(), localizationFrequencyText.getText().toString());
        editor.commit();
    }
}
