package moe.umiqlo.remotecontrol;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.TextView;

import java.net.Socket;

import moe.umiqlo.remotecontrol.util.Config;

public class PreviewActivity extends SettingActivity implements SurfaceHolder.Callback2 {

    Config config;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        setTitle("Preview");
        config = super.loadFromSharedPreferences();
        TextView txtPreview = (TextView) findViewById(R.id.txtPreview);
        txtPreview.setText(config.toString());
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void screenSize() {

    }
}
