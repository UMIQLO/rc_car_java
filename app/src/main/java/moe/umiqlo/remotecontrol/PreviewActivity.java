package moe.umiqlo.remotecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import moe.umiqlo.remotecontrol.util.Config;

public class PreviewActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Config config = super.loadFromSharedPreferences();
        setTitle(config.toString());
    }
}
