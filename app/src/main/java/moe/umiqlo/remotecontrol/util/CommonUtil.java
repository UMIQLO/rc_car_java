package moe.umiqlo.remotecontrol.util;

import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;


public class CommonUtil {
    public static String getCurrentDateTimeStr() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    public static String getConsoleStyleMsg(String msg) {
        return getCurrentDateTimeStr() + " | " + msg + "\n";
    }

}
