package moe.umiqlo.remotecontrol;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import moe.umiqlo.remotecontrol.config.CmdListConfig;
import moe.umiqlo.remotecontrol.config.Config;
import moe.umiqlo.remotecontrol.util.CommonUtil;
import moe.umiqlo.remotecontrol.util.SimpleSocketClient;


public class ControlActivity extends MainActivity implements View.OnTouchListener, SurfaceHolder.Callback2, Runnable {

    Button btnUp, btnDown, btnLeft, btnRight, btnCapture, btnServoLeft, btnServoRight;
    Switch switchLED, switchSyncMotor;
    SimpleSocketClient controlClient;
    Thread controlThread;
    SurfaceView cameraSurfaceView;
    SeekBar seekBarLeftMotor, seekBarRightMotor;
    TextView lbLeftSpeed, lbRightSpeed;

    SurfaceHolder holder;
    int screenWidth, screenHeight;
    int newHeight, newWidth;
    private boolean runFlag = false;
    private HttpURLConnection conn;
    private Thread thread;
    private Bitmap captureImage;
    private boolean syncMotor;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            controlClient.send(CmdListConfig.getInstance().getCmdSpeed() + " " + Config.getInstance().getLeftMotorSpeed() + " " + Config.getInstance().getRightMotorSpeed());
            switch (v.getId()) {
                case R.id.btnUp:
                    controlClient.send(CmdListConfig.getInstance().getCmdForward());
                    break;
                case R.id.btnDown:
                    controlClient.send(CmdListConfig.getInstance().getCmdBackward());
                    break;
                case R.id.btnLeft:
                    controlClient.send(CmdListConfig.getInstance().getCmdLeft());
                    break;
                case R.id.btnRight:
                    controlClient.send(CmdListConfig.getInstance().getCmdRight());
                    break;
                case R.id.btnCapture:
                case R.id.surfaceView:
                    capture();
                    break;
                case R.id.btnServoLeft:
                    controlClient.send(CmdListConfig.getInstance().getCmdServoLeft());
                    break;
                case R.id.btnServoRight:
                    controlClient.send(CmdListConfig.getInstance().getCmdServoRight());
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            controlClient.send(CmdListConfig.getInstance().getCmdStop());
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        // change title
        setTitle(getString(R.string.car_remote_control));
        // lock device SCREEN_ORIENTATION
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComponent();
        initScreenValue();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        System.out.println("-----onDestroy-----");
        super.onDestroy();
        controlClient.kill();
        System.out.println("-----controlClient End-----");
        System.out.println("-----onDestroy End-----");
    }

    @Override
    public void onResume() {
        System.out.println("-----onResume-----");
        super.onResume();
        initControlConnection();
        System.out.println("-----onResume End-----");
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    private void initScreenValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private void initComponent() {
        btnUp = (Button) findViewById(R.id.btnUp);
        btnDown = (Button) findViewById(R.id.btnDown);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnServoLeft = (Button) findViewById(R.id.btnServoLeft);
        btnServoRight = (Button) findViewById(R.id.btnServoRight);
        switchLED = (Switch) findViewById(R.id.switchLED);
        switchSyncMotor = (Switch) findViewById(R.id.switchSyncMotor);

        btnUp.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnCapture.setOnTouchListener(this);
        btnServoLeft.setOnTouchListener(this);
        btnServoRight.setOnTouchListener(this);


        screenValue();
        cameraSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        cameraSurfaceView.setOnTouchListener(this);
        holder = cameraSurfaceView.getHolder();
        holder.addCallback(this);

        // setting emoji as capture button text
        btnCapture.setText("\uD83D\uDCF7");

        // init switch
        switchLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(ControlActivity.this, "LED On", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ControlActivity.this, "LED Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchSyncMotor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                syncMotor = isChecked;
                if (seekBarLeftMotor.getProgress() > seekBarRightMotor.getProgress()) {
                    seekBarRightMotor.setProgress(seekBarLeftMotor.getProgress());
                } else {
                    seekBarLeftMotor.setProgress(seekBarRightMotor.getProgress());
                }
            }
        });

        // init SeekBar (Motor)
        lbLeftSpeed = (TextView) findViewById(R.id.lbLeftSpeed);
        lbRightSpeed = (TextView) findViewById(R.id.lbRightSpeed);
        seekBarLeftMotor = (SeekBar) findViewById(R.id.seekBarLeftMotor);
        seekBarRightMotor = (SeekBar) findViewById(R.id.seekBarRightMotor);
        seekBarLeftMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lbLeftSpeed.setText(String.valueOf(progress));
                Config.getInstance().setLeftMotorSpeed(progress);
                Config.getInstance().save(ControlActivity.this);
                if (syncMotor) {
                    seekBarRightMotor.setProgress(progress);
                }
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
                Config.getInstance().setRightMotorSpeed(progress);
                Config.getInstance().save(ControlActivity.this);
                if (syncMotor) {
                    seekBarLeftMotor.setProgress(progress);
                }
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

        seekBarLeftMotor.setProgress(Config.getInstance().getLeftMotorSpeed());
        seekBarRightMotor.setProgress(Config.getInstance().getRightMotorSpeed());

        // hidden capture button
        btnCapture.setVisibility(View.INVISIBLE);
    }

    private void initControlConnection() {
        try {
            controlClient = new SimpleSocketClient(Config.getInstance().getControlHost(), Config.getInstance().getControlPort());
            controlThread = new Thread(controlClient);
            controlThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void capture() {
        Bitmap currentImage = captureImage;
        if (currentImage == null) {
            new MaterialDialog.Builder(this)
                    .title(R.string.system_error)
                    .content(R.string.screen_capture + R.string.error)
                    .positiveText(android.R.string.yes)
                    .onPositive(null)
                    .show();
        }
        if (CommonUtil.isExternalStorageReadable() && CommonUtil.isExternalStorageWritable()) {
            // Create folder
            File folderPath = CommonUtil.getAlbumStorageDir(Config.getInstance().getCaptureFolderName());
            CommonUtil.saveBitmap(currentImage, folderPath);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            new MaterialDialog.Builder(this)
                    .title(R.string.success)
                    .content(R.string.screen_captured)
                    .positiveText(android.R.string.yes)
                    .onPositive(null)
                    .show();
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.system_error)
                    .content(R.string.storage_permission_problem)
                    .positiveText(android.R.string.yes)
                    .onPositive(null)
                    .show();
        }
    }

    private void screenValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int imageWidth = bm.getWidth();
        int imageHeight = bm.getHeight();

        if (maxHeight > maxWidth) {
            //Log.v("SCREEN_ORIENTATION", "PORTRAIT");
            float ratio = (float) maxWidth / imageWidth;
            newHeight = (int) (ratio * imageHeight);
            newWidth = maxWidth;
        } else if (maxWidth > maxHeight) {
            //Log.v("SCREEN_ORIENTATION", "LANDSCAPE");
            float ratio = (float) maxHeight / imageHeight;
            newWidth = (int) (ratio * imageWidth);
            newHeight = maxHeight;
        }
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        runFlag = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setFixedSize(newWidth, newHeight);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (holder) {
            runFlag = false;
        }
    }

    @Override
    public void run() {
        Canvas canvas;
        InputStream inputStream;
        Bitmap image;
        URL url = null;
        String cameraUrl = Config.getInstance().getCameraUrl();
        try {
            url = new URL(cameraUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BitmapFactory.Options imageOption = new BitmapFactory.Options();
        imageOption.inPreferredConfig = Bitmap.Config.ARGB_8888;
        while (runFlag) {
            try {
                synchronized (holder) {
                    canvas = holder.lockCanvas();
                    conn = (HttpURLConnection) url.openConnection();
                    inputStream = conn.getInputStream();
                    captureImage = BitmapFactory.decodeStream(inputStream, null, imageOption);
                    //image = Bitmap.createBitmap(image);
                    image = scaleBitmap(captureImage, screenWidth, screenHeight);
                    if (canvas != null) { // Prevent null pointer exception
                        canvas.drawBitmap(image, 0, 0, null);
                        holder.unlockCanvasAndPost(canvas);
                        conn.disconnect();
                    }
                    Thread.sleep(CmdListConfig.getInstance().getCmdFPS());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
