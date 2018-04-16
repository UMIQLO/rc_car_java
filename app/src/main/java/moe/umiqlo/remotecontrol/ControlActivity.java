package moe.umiqlo.remotecontrol;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import moe.umiqlo.remotecontrol.util.Config;
import moe.umiqlo.remotecontrol.util.SimpleSocketClient;


public class ControlActivity extends MainActivity implements View.OnTouchListener {

    Button btnUp, btnDown, btnLeft, btnRight, btnStop;
    SimpleSocketClient controlClient;
    Thread controlThread;
    Config config;
    SurfaceView surfaceView;
    int screenWidth, screenHeight;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            controlClient.send("MD_SD " + config.getLEFT_MOTOR_SPEED() + " " + config.getRIGHT_MOTOR_SPEED());
            switch (v.getId()) {
                case R.id.btnUp:
                    controlClient.send("MD_Qian");
                    break;
                case R.id.btnDown:
                    controlClient.send("MD_Hou");
                    break;
                case R.id.btnLeft:
                    controlClient.send("MD_Zuo");
                    break;
                case R.id.btnRight:
                    controlClient.send("MD_You");
                    break;
                case R.id.btnStop:
                    controlClient.send("MD_Ting");
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            controlClient.send("MD_Ting");
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        config = loadFromSharedPreferences();
        initComponent();
    }

    @Override
    protected void onDestroy() {
        System.out.println("-----onDestroy-----");
        super.onDestroy();
        controlClient.kill();
        System.out.println("-----controlClient End-----");
        System.out.println("-----cameraClient End-----");
        System.out.println("-----onDestroy End-----");
    }

    @Override
    public void onResume() {
        System.out.println("-----onResume-----");
        super.onResume();
        initControlConnection();
        initScreenSize();
        System.out.println("-----onResume End-----");
    }

    private void initScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private void initComponent() {
        btnUp = (Button) findViewById(R.id.btnUp);
        btnDown = (Button) findViewById(R.id.btnDown);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnStop = (Button) findViewById(R.id.btnStop);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);


        btnUp.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnStop.setOnTouchListener(this);
    }

    private void initControlConnection() {
        try {
            controlClient = new SimpleSocketClient(config.getCONTROL_HOST(), config.getCONTROL_PORT());
            controlThread = new Thread(controlClient);
            controlThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
