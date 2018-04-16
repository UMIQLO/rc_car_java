package moe.umiqlo.remotecontrol;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import moe.umiqlo.remotecontrol.util.Config;

public class SettingActivity extends MainActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    Config config;
    EditText txtVideoUrl, txtControlHost, txtControlPort;
    SeekBar seekBarLeftMotor, seekBarRightMotor;
    TextView lbLeftSpeed, lbRightSpeed;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Configuration");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        config = super.loadFromSharedPreferences();
        System.out.println("Setting:" + new Gson().toJson(config));
        initComponent();
        loadSettingToView();
    }

    private void initComponent() {
        // init Component
        txtVideoUrl = (EditText) findViewById(R.id.txtVideoUrl);
        txtControlHost = (EditText) findViewById(R.id.txtControlHost);
        txtControlPort = (EditText) findViewById(R.id.txtControlPort);
        lbLeftSpeed = (TextView) findViewById(R.id.lbLeftSpeed);
        lbRightSpeed = (TextView) findViewById(R.id.lbRightSpeed);

        // init onClickListener to Button(s)
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        // init SeekBar
        seekBarLeftMotor = (SeekBar) findViewById(R.id.seekBarLeftMotor);
        seekBarRightMotor = (SeekBar) findViewById(R.id.seekBarRightMotor);

        seekBarLeftMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lbLeftSpeed.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }
        });
        seekBarRightMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lbRightSpeed.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }
        });

    }

    private void loadSettingToView() {
        txtVideoUrl.setText(config.getCAMERA_URL());
        txtControlHost.setText(config.getCONTROL_HOST());
        txtControlPort.setText(String.valueOf(config.getCONTROL_PORT()));
        seekBarLeftMotor.setProgress(config.getLEFT_MOTOR_SPEED());
        seekBarRightMotor.setProgress(config.getRIGHT_MOTOR_SPEED());
    }

    private void save() {
        // Create new config
        //Config newConfig = Config.getInstance();
        config.setCAMERA_URL(txtVideoUrl.getText().toString());
        config.setCONTROL_HOST(txtControlHost.getText().toString());
        config.setCONTROL_PORT(Integer.valueOf(txtControlPort.getText().toString()));
        config.setLEFT_MOTOR_SPEED(seekBarLeftMotor.getProgress());
        config.setRIGHT_MOTOR_SPEED(seekBarRightMotor.getProgress());
        super.saveToSharedPreferences(config);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                save();
                new MaterialDialog.Builder(this)
                        .title("Success")
                        .content("Configuration Saved!")
                        .positiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                finish();
                            }
                        })
                        .show();
                break;
        }
    }
}
