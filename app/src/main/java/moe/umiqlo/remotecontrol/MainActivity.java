package moe.umiqlo.remotecontrol;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnControl = (Button) findViewById(R.id.btnControl);
        Button btnPreview = (Button) findViewById(R.id.btnPreview);
        Button btnSetting = (Button) findViewById(R.id.btnSetting);
        btnControl.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println(sharedPreferences.getString("setting_json", "NO DATA"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnControl:
            case R.id.btnPreview:
                break;
            case R.id.btnSetting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
