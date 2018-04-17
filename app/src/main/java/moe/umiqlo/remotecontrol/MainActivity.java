package moe.umiqlo.remotecontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import moe.umiqlo.remotecontrol.config.CmdListConfig;
import moe.umiqlo.remotecontrol.config.Config;

import static moe.umiqlo.remotecontrol.util.CommonUtil.isExternalStorageReadable;
import static moe.umiqlo.remotecontrol.util.CommonUtil.isExternalStorageWritable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnControl, btnPreview, btnSetting;
    TextView txtDebugMsg;
    Config config;
    CmdListConfig cmd;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnControl:
                startActivity(new Intent(MainActivity.this, ControlActivity.class));
                break;
            case R.id.btnPreview:
                startActivity(new Intent(MainActivity.this, CmdConfigActivity.class));
                break;
            case R.id.btnSetting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initSetting();
        initComponent();
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

    private void initSetting() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cmdJSON = sharedPreferences.getString("cmd_json", "no_cmd_json");
        String settingJSON = sharedPreferences.getString("setting_json", "no_setting_json");

        config = Config.getInstance();
        cmd = CmdListConfig.getInstance();

        if (!cmdJSON.equals("no_cmd_json")) {
            cmd = new Gson().fromJson(cmdJSON, CmdListConfig.class);
            cmd.setLastAccess("Main - initSetting() From SP");
            cmd.setInstance(); // use gson deserialized class to replace
        } else {
            // Create new setting
            cmd.save(this);
        }
        if (!settingJSON.equals("no_setting_json")) {
            config = new Gson().fromJson(settingJSON, Config.class);
            config.setLastAccess("Main - initSetting() From SP");
            config.setInstance(); // use gson deserialized class to replace
        } else {
            // Create new setting
            config.save(this);
        }
        Log.v("Config", config.getInstance().toString());
        Log.v("CmdConfig", cmd.getInstance().toString());

        Log.v("Storage Read", isExternalStorageReadable() + "");
        Log.v("Storage Write", isExternalStorageWritable() + "");
    }
}

