package moe.umiqlo.remotecontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import moe.umiqlo.remotecontrol.util.Config;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    Config config = null;
    EditText txtVideoUrl;
    EditText txtControlHost;
    EditText txtControlPort;
    EditText txtLeftMotorSpeed;
    EditText txtRightMotorSpeed;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        init();
        loadFromSharedPreferences();
        loadSettingToView();
    }

    private void init() {
        // init view to variable
        txtVideoUrl = (EditText) findViewById(R.id.txtVideoUrl);
        txtControlHost = (EditText) findViewById(R.id.txtControlHost);
        txtControlPort = (EditText) findViewById(R.id.txtControlPort);
        txtLeftMotorSpeed = (EditText) findViewById(R.id.txtLeftMotorSpeed);
        txtRightMotorSpeed = (EditText) findViewById(R.id.txtRightMotorSpeed);

        // init onClickListener to Button(s)
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    private void loadSettingToView() {
        txtVideoUrl.setText(config.getVIDEO_URL());
        txtControlHost.setText(config.getCONTROL_HOST());
        txtControlPort.setText(String.valueOf(config.getCONTROL_PORT()));
        txtLeftMotorSpeed.setText(String.valueOf(config.getLEFT_MOTOR_SPEED()));
        txtRightMotorSpeed.setText(String.valueOf(config.getRIGHT_MOTOR_SPEED()));
    }

    private void loadFromSharedPreferences() {
        Gson gson = new Gson();
        // load Shared Preferences from "setting_json"
        String setting_json = sharedPreferences.getString("setting_json", "err_no_data");
        if (!setting_json.equals("err_no_data")) {
            // convert setting_json to Config.class
            config = gson.fromJson(setting_json, Config.class);
        } else {
            // if setting_json not existed, create new setting here
            config.setVIDEO_URL("http://192.168.8.1:8083/?action=snapshot");
            config.setCONTROL_HOST("192.168.8.1");
            config.setCONTROL_PORT(8081);
            config.setLEFT_MOTOR_SPEED(255);
            config.setRIGHT_MOTOR_SPEED(255);
            saveToSharedPreferences(config);
        }
    }

    private void saveToSharedPreferences(Config config) {
        // save config to Shared Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("setting_json", config.toString());
        editor.apply();
    }

    private void save() {
        // Create new config
        Config newConfig = new Config();
        newConfig.setVIDEO_URL(txtVideoUrl.getText().toString());
        newConfig.setCONTROL_HOST(txtControlHost.getText().toString());
        newConfig.setCONTROL_PORT(Integer.valueOf(txtControlPort.getText().toString()));
        newConfig.setLEFT_MOTOR_SPEED(Integer.valueOf(txtLeftMotorSpeed.getText().toString()));
        newConfig.setRIGHT_MOTOR_SPEED(Integer.valueOf(txtRightMotorSpeed.getText().toString()));
        saveToSharedPreferences(newConfig);
        // use new config to replace current config
        config = newConfig;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                save();
                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
