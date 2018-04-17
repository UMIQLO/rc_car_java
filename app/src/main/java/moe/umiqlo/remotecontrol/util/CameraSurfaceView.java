package moe.umiqlo.remotecontrol.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import moe.umiqlo.remotecontrol.config.Config;

public class CameraSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback2 {

    private int screenWidth;
    private int screenHeight;

    private boolean runFlag = false;
    private static SurfaceHolder holder;
    private HttpURLConnection conn;
    private Thread thread;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenValue();
        holder = this.getHolder();
        holder.addCallback(this);
    }

    private void screenValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
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

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (holder) {
            runFlag = false;
        }
    }

    private Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int imageWidth = bm.getWidth();
        int imageHeight = bm.getHeight();

        int newHeight = 0;
        int newWidth = 0;

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
    public void run() {
        Canvas canvas;
        Bitmap image;
        InputStream inputStream;
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
                    image = BitmapFactory.decodeStream(inputStream, null, imageOption);
                    //image = Bitmap.createBitmap(image);
                    //image = Bitmap.createScaledBitmap(image, screenWidth, screenWidth * 480 / 640, true);
                    image = scaleBitmap(image, screenWidth, screenHeight);
                    if (canvas != null) { // Prevent null pointer exception
                        canvas.drawBitmap(image, 0, 0, null);
                        holder.unlockCanvasAndPost(canvas);
                        conn.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
