package moe.umiqlo.remotecontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import moe.umiqlo.remotecontrol.config.CmdListConfig;
import moe.umiqlo.remotecontrol.config.Config;
import moe.umiqlo.remotecontrol.util.CommonUtil;
import moe.umiqlo.remotecontrol.util.DebugMessage;

import static moe.umiqlo.remotecontrol.util.CommonUtil.isExternalStorageReadable;
import static moe.umiqlo.remotecontrol.util.CommonUtil.isExternalStorageWritable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnControl, btnPreview, btnSetting;
    static TextView txtDebugMsg;
    Config config;
    CmdListConfig cmd;
    Boolean cameraConn, controlConn;

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
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSetting();
        initDebugMessage();
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

        DebugMessage.getInstance().setMessage(CommonUtil.getConsoleStyleMsg("Config: " + config.getInstance().toString()));
        DebugMessage.getInstance().setMessage(CommonUtil.getConsoleStyleMsg("CmdConfig: " + cmd.getInstance().toString()));
        DebugMessage.getInstance().setMessage(CommonUtil.getConsoleStyleMsg("isExternalStorageReadable: " + isExternalStorageReadable()));
        DebugMessage.getInstance().setMessage(CommonUtil.getConsoleStyleMsg("isExternalStorageWritable: " + isExternalStorageWritable()));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cameraConn = CommonUtil.isURLConnectedToServer(Config.getInstance().getCameraUrl(), 100);
                controlConn = CommonUtil.isSocketConnectedToServer(
                        Config.getInstance().getControlHost(), Config.getInstance().getControlPort());
                DebugMessage.getInstance().setMessage(CommonUtil.getConsoleStyleMsg("Camera Connection: " + cameraConn + ""));
                DebugMessage.getInstance().setMessage(CommonUtil.getConsoleStyleMsg("Control Connection: " + controlConn + ""));
            }
        });
    }

    public static void initDebugMessage() {
        txtDebugMsg.setText(""); //Clear Console
        for (String msg : DebugMessage.getInstance().getMessage()) {
            String oldMessage = txtDebugMsg.getText().toString();
            txtDebugMsg.setText(msg + "\n" + oldMessage);
        }
    }
}

