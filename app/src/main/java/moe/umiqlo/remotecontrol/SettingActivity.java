package moe.umiqlo.remotecontrol;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import moe.umiqlo.remotecontrol.config.Config;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtVideoUrl, txtControlHost, txtControlPort, txtCaptureFolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // change title
        setTitle(R.string.configuration);
        // lock device SCREEN_ORIENTATION
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComponent();
        loadSettingToView();
    }

    private void initComponent() {
        // init Component
        txtVideoUrl = (EditText) findViewById(R.id.txtVideoUrl);
        txtControlHost = (EditText) findViewById(R.id.txtControlHost);
        txtControlPort = (EditText) findViewById(R.id.txtControlPort);
        txtCaptureFolder = (EditText) findViewById(R.id.txtCaptureFolder);
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
            default:
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.btnMenuSave:
                save();
                new MaterialDialog.Builder(this)
                        .title(R.string.success)
                        .content(R.string.config_saved)
                        .positiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                finish();
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
