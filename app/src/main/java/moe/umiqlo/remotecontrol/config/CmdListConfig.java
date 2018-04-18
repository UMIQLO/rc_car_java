package moe.umiqlo.remotecontrol.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class CmdListConfig {

    private static CmdListConfig instance;

    private CmdListConfig() {
        cmdForward = "Forward";
        cmdBackward = "Backward";
        cmdLeft = "Left";
        cmdRight = "Right";
        cmdSpeed = "Speed";
        cmdStop = "Stop";
        cmdServoLeft = "ServoL";
        cmdServoRight = "ServoR";
        cmdLED = "LED";
        cmdFPS = 40;
        lastAccess = "defaultSetting";
    }

    public static synchronized CmdListConfig getInstance() {
        if (instance == null) {
            instance = new CmdListConfig();
        }
        return instance;
    }

    public void setInstance() {
        instance = this;
    }

    public void save(Context context) {
        // save config to SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cmd_json", this.toString());
        editor.apply();
    }

    private String cmdForward;
    private String cmdBackward;
    private String cmdLeft;
    private String cmdRight;
    private String cmdSpeed;
    private String cmdStop;
    private String cmdServoLeft;
    private String cmdServoRight;
    private String cmdLED;
    private int cmdFPS;
    private String lastAccess;

    public String getCmdForward() {
        return cmdForward;
    }

    public void setCmdForward(String cmdForward) {
        this.cmdForward = cmdForward;
    }

    public String getCmdBackward() {
        return cmdBackward;
    }

    public void setCmdBackward(String cmdBackward) {
        this.cmdBackward = cmdBackward;
    }

    public String getCmdLeft() {
        return cmdLeft;
    }

    public void setCmdLeft(String cmdLeft) {
        this.cmdLeft = cmdLeft;
    }

    public String getCmdRight() {
        return cmdRight;
    }

    public void setCmdRight(String cmdRight) {
        this.cmdRight = cmdRight;
    }

    public String getCmdSpeed() {
        return cmdSpeed;
    }

    public void setCmdSpeed(String cmdSpeed) {
        this.cmdSpeed = cmdSpeed;
    }

    public String getCmdStop() {
        return cmdStop;
    }

    public void setCmdStop(String cmdStop) {
        this.cmdStop = cmdStop;
    }

    public String getCmdServoLeft() {
        return cmdServoLeft;
    }

    public void setCmdServoLeft(String cmdServoLeft) {
        this.cmdServoLeft = cmdServoLeft;
    }

    public String getCmdServoRight() {
        return cmdServoRight;
    }

    public void setCmdServoRight(String cmdServoRight) {
        this.cmdServoRight = cmdServoRight;
    }

    public String getCmdLED() {
        return cmdLED;
    }

    public void setCmdLED(String cmdLED) {
        this.cmdLED = cmdLED;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }

    public int getCmdFPS() {
        return cmdFPS;
    }

    public void setCmdFPS(int cmdFPS) {
        this.cmdFPS = cmdFPS;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
