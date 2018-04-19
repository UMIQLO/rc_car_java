package moe.umiqlo.remotecontrol;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import moe.umiqlo.remotecontrol.config.CmdListConfig;
import moe.umiqlo.remotecontrol.config.Config;
import moe.umiqlo.remotecontrol.util.CommonUtil;
import moe.umiqlo.remotecontrol.util.DebugMessage;

import static moe.umiqlo.remotecontrol.util.CommonUtil.isExternalStorageReadable;
import static moe.umiqlo.remotecontrol.util.CommonUtil.isExternalStorageWritable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnControl, btnPreview, btnSetting;
    static TextView lbDebugMsg, lbAlert;
    Config config;
    CmdListConfig cmd;
    private int count;

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
            case R.id.lbDebugMsg:
                count++;
                if (count > 7) {
                    lbDebugMsg.setTextColor(Color.parseColor("#FF000000"));
                }
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
        checkConnection();
    }

    private void initComponent() {
        btnControl = (Button) findViewById(R.id.btnControl);
        btnPreview = (Button) findViewById(R.id.btnPreview);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnControl.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        lbDebugMsg = (TextView) findViewById(R.id.lbDebugMsg);
        lbDebugMsg.setOnClickListener(this);
        lbDebugMsg.setTextColor(Color.parseColor("#00000000")); // hidden debug message
        lbAlert = (TextView) findViewById(R.id.lbAlert);

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
    }

    public static void initDebugMessage() {
        lbDebugMsg.setText(""); //Clear Console
        for (String msg : DebugMessage.getInstance().getMessage()) {
            String oldMessage = lbDebugMsg.getText().toString();
            lbDebugMsg.setText(msg + "\n" + oldMessage);
        }
    }

    private void checkConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean controlConn = CommonUtil.isSocketConnectedToServer(Config.getInstance().getControlHost(), Config.getInstance().getControlPort());
                Boolean cameraConn = CommonUtil.isURLConnectedToServer(Config.getInstance().getCameraUrl(), 3000);
                Map<String, Boolean> connection = new HashMap<>();
                connection.put("control", controlConn);
                connection.put("camera", cameraConn);
                Message msg = Message.obtain();
                msg.obj = connection;
                mConnectionHandler.sendMessage(msg);
            }
        }).start();
    }


    private Handler mConnectionHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            Map<String, Boolean> map = (HashMap) msg.obj;
            Boolean controlConn = false, cameraConn = false;
            for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                if (entry.getKey().equals("control")) {
                    controlConn = entry.getValue();
                }
                if (entry.getKey().equals("camera")) {
                    cameraConn = entry.getValue();
                }
            }
            if (cameraConn && controlConn) {
                btnControl.setEnabled(true);
                btnControl.setClickable(true);
                lbAlert.setText(R.string.status_all_ok);
            } else {
                btnControl.setEnabled(false);
                btnControl.setClickable(false);
                try {
                    new MaterialDialog.Builder(MainActivity.this)
                            .title(R.string.conn_failed)
                            .content(R.string.update_config)
                            .positiveText(R.string.go)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    lbAlert.setText(R.string.update_config);
                                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                }
                            })
                            .show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            initDebugMessage(); // Update Debug Message
        }
    };
}

