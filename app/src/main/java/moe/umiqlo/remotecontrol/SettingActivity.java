package moe.umiqlo.remotecontrol;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import moe.umiqlo.remotecontrol.config.Config;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtVideoUrl, txtControlHost, txtControlPort, txtCaptureFolder;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Configuration");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initComponent();
        loadSettingToView();
    }

    private void initComponent() {
        // init Component
        txtVideoUrl = (EditText) findViewById(R.id.txtVideoUrl);
        txtControlHost = (EditText) findViewById(R.id.txtControlHost);
        txtControlPort = (EditText) findViewById(R.id.txtControlPort);
        txtCaptureFolder = (EditText) findViewById(R.id.txtCaptureFolder);

        // init onClickListener to Button(s)
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    private void loadSettingToView() {
        txtVideoUrl.setText(Config.getInstance().getCameraUrl());
        txtControlHost.setText(Config.getInstance().getControlHost());
        txtControlPort.setText(String.valueOf(Config.getInstance().getControlPort()));
        txtCaptureFolder.setText(Config.getInstance().getCaptureFolderName());
    }

    private void save() {
        // Create new config
        Config.getInstance().setCameraUrl(txtVideoUrl.getText().toString());
        Config.getInstance().setControlHost(txtControlHost.getText().toString());
        Config.getInstance().setControlPort(Integer.valueOf(txtControlPort.getText().toString()));
        Config.getInstance().setCaptureFolderName(txtCaptureFolder.getText().toString());
        Config.getInstance().setLastAccess("Setting - save()");
        Config.getInstance().save(this);
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
