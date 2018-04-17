package moe.umiqlo.remotecontrol.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;


public class Config {

    private static Config instance;

    private Config() {
        cameraUrl = "http://192.168.8.1:8083/?action=snapshot";
        controlHost = "192.168.8.1";
        controlPort = 2001;
        leftMotorSpeed = 255;
        rightMotorSpeed = 255;
        lastAccess = "defaultSetting";
    }

    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = new Config();
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
        editor.putString("setting_json", this.toString());
        editor.apply();
    }

    private String cameraUrl;
    private String controlHost;
    private int controlPort;
    private int leftMotorSpeed;
    private int rightMotorSpeed;
    private String lastAccess;

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public String getControlHost() {
        return controlHost;
    }

    public void setControlHost(String controlHost) {
        this.controlHost = controlHost;
    }

    public int getControlPort() {
        return controlPort;
    }

    public void setControlPort(int controlPort) {
        this.controlPort = controlPort;
    }

    public int getLeftMotorSpeed() {
        return leftMotorSpeed;
    }

    public void setLeftMotorSpeed(int leftMotorSpeed) {
        this.leftMotorSpeed = leftMotorSpeed;
    }

    public int getRightMotorSpeed() {
        return rightMotorSpeed;
    }

    public void setRightMotorSpeed(int rightMotorSpeed) {
        this.rightMotorSpeed = rightMotorSpeed;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
