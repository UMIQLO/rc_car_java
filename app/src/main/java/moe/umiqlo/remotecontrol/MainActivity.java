package moe.umiqlo.remotecontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import moe.umiqlo.remotecontrol.util.Config;

import static moe.umiqlo.remotecontrol.util.CommonUtil.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    Config config;
    Button btnControl, btnPreview, btnSetting;
    TextView txtDebugMsg;
    Gson gson;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnControl:
                startActivity(new Intent(this, ControlActivity.class));
                break;
            case R.id.btnPreview:
                startActivity(new Intent(this, PreviewActivity.class));
                break;
            case R.id.btnSetting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println(sharedPreferences.getString("setting_json", "err_no_data"));
        initComponent();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromSharedPreferences();
        initDebugMsg();
    }

    public Config loadFromSharedPreferences() {
        gson = new Gson();
        Config config = new Config();
        // load Shared Preferences from "setting_json"
        String setting_json = sharedPreferences.getString("setting_json", "err_no_data");
        if (!setting_json.equals("err_no_data")) {
            // convert setting_json to Config.class
            this.config = gson.fromJson(setting_json, Config.class);
        } else {
            // if setting_json not existed, create new setting here
            this.config.setVIDEO_URL("http://192.168.8.1:8083/?action=snapshot");
            this.config.setCONTROL_HOST("192.168.8.1");
            this.config.setCONTROL_PORT(8081);
            this.config.setLEFT_MOTOR_SPEED(255);
            this.config.setRIGHT_MOTOR_SPEED(255);
            saveToSharedPreferences(config);
        }
        return this.config;
    }

    public void saveToSharedPreferences(Config config) {
        // save config to Shared Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("setting_json", config.toString());
        editor.apply();
    }

    private void initComponent() {
        btnControl = (Button) findViewById(R.id.btnControl);
        btnPreview = (Button) findViewById(R.id.btnPreview);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnControl.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        txtDebugMsg = (TextView) findViewById(R.id.txtDebugMsg);
    }

    private void initDebugMsg() {
        String debugMsg = txtDebugMsg.getText().toString();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> configHashMap = gson.fromJson(gson.toJson(config), type);
        for (Object key : configHashMap.keySet()) {
            debugMsg += getConsoleStyleMsg(key + ": " + configHashMap.get(key));
        }
        txtDebugMsg.setText(debugMsg);
    }
}

