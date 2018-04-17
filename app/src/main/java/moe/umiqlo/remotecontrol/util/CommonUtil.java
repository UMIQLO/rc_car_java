package moe.umiqlo.remotecontrol.util;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CommonUtil {

    public static String getCurrentDateTimeStr() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    public static String getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp + "";
    }

    public static String getConsoleStyleMsg(String msg) {
        return getCurrentDateTimeStr() + " | " + msg + "\n";
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getAlbumStorageDir(String albumName) {
        // Create a Folder In Storage Picture
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.v("getAlbumStorageDir()", "Directory not created");
        }
        return file;
    }

    public static void saveBitmap(Bitmap image, File folderPath) {
        File targetFile = new File(folderPath, CommonUtil.getCurrentTimestamp() + ".PNG");
        try {
            FileOutputStream output = new FileOutputStream(targetFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
