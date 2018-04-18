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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import moe.umiqlo.remotecontrol.config.CmdListConfig;


public class CmdConfigActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtCmdForward, txtCmdBackward, txtCmdLeft, txtCmdRight, txtCmdStop,
            txtCmdSpeed, txtCmdServoL, txtCmdServoR, txtCmdLED, txtCmdFPS;
    CmdListConfig cmd;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmd_config);
        // change title
        setTitle(getString(R.string.debug_config));
        // lock device SCREEN_ORIENTATION
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComponent();
        loadSettingToView();
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

    private void loadSettingToView() {
        txtCmdForward.setText(CmdListConfig.getInstance().getCmdForward());
        txtCmdBackward.setText(CmdListConfig.getInstance().getCmdBackward());
        txtCmdLeft.setText(CmdListConfig.getInstance().getCmdLeft());
        txtCmdRight.setText(CmdListConfig.getInstance().getCmdRight());
        txtCmdStop.setText(CmdListConfig.getInstance().getCmdStop());
        txtCmdSpeed.setText(CmdListConfig.getInstance().getCmdSpeed());
        txtCmdServoL.setText(CmdListConfig.getInstance().getCmdServoLeft());
        txtCmdServoR.setText(CmdListConfig.getInstance().getCmdServoRight());
        txtCmdLED.setText(CmdListConfig.getInstance().getCmdLED());
        txtCmdFPS.setText(String.valueOf(CmdListConfig.getInstance().getCmdFPS()));
    }

    private void initComponent() {
        txtCmdForward = findViewById(R.id.txtCmdForward);
        txtCmdBackward = findViewById(R.id.txtCmdBackward);
        txtCmdLeft = findViewById(R.id.txtCmdLeft);
        txtCmdRight = findViewById(R.id.txtCmdRight);
        txtCmdStop = findViewById(R.id.txtCmdStop);
        txtCmdSpeed = findViewById(R.id.txtCmdSpeed);
        txtCmdServoL = findViewById(R.id.txtCmdServoL);
        txtCmdServoR = findViewById(R.id.txtCmdServoR);
        txtCmdLED = findViewById(R.id.txtCmdLED);
        txtCmdFPS = findViewById(R.id.txtCmdFPS);
    }

    private void save() {
        cmd = CmdListConfig.getInstance();
        cmd.setCmdForward(txtCmdForward.getText().toString());
        cmd.setCmdBackward(txtCmdBackward.getText().toString());
        cmd.setCmdLeft(txtCmdLeft.getText().toString());
        cmd.setCmdRight(txtCmdRight.getText().toString());
        cmd.setCmdStop(txtCmdStop.getText().toString());
        cmd.setCmdSpeed(txtCmdSpeed.getText().toString());
        cmd.setCmdServoLeft(txtCmdServoL.getText().toString());
        cmd.setCmdServoRight(txtCmdServoR.getText().toString());
        cmd.setCmdLED(txtCmdLED.getText().toString());
        cmd.setCmdFPS(Integer.valueOf(txtCmdFPS.getText().toString()));
        cmd.save(this);
    }
}
