package moe.umiqlo.remotecontrol;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import moe.umiqlo.remotecontrol.util.Config;

public class SettingActivity extends MainActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    Config config = null;
    EditText txtVideoUrl, txtControlHost, txtControlPort;
    SeekBar seekBarLeftMotor, seekBarRightMotor;
    TextView lbLeftSpeed, lbRightSpeed;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Configuration");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        config = loadFromSharedPreferences();
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
        txtVideoUrl.setText(config.getVIDEO_URL());
        txtControlHost.setText(config.getCONTROL_HOST());
        txtControlPort.setText(String.valueOf(config.getCONTROL_PORT()));
        seekBarLeftMotor.setProgress(config.getLEFT_MOTOR_SPEED());
        seekBarRightMotor.setProgress(config.getRIGHT_MOTOR_SPEED());
    }

    private void save() {
        // Create new config
        Config newConfig = new Config();
        newConfig.setVIDEO_URL(txtVideoUrl.getText().toString());
        newConfig.setCONTROL_HOST(txtControlHost.getText().toString());
        newConfig.setCONTROL_PORT(Integer.valueOf(txtControlPort.getText().toString()));
        newConfig.setLEFT_MOTOR_SPEED(seekBarLeftMotor.getProgress());
        newConfig.setRIGHT_MOTOR_SPEED(seekBarRightMotor.getProgress());
        super.saveToSharedPreferences(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                save();
                new MaterialDialog.Builder(this)
                        .title("OK")
                        .content("Save OK")
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
