package moe.umiqlo.remotecontrol.util;

import com.google.gson.Gson;


public class Config {
    private String CAMERA_URL;
    private String CONTROL_HOST;
    private int CONTROL_PORT;
    private int LEFT_MOTOR_SPEED;
    private int RIGHT_MOTOR_SPEED;

    public String getCAMERA_URL() {
        return CAMERA_URL;
    }

    public void setCAMERA_URL(String CAMERA_URL) {
        this.CAMERA_URL = CAMERA_URL;
    }

    public String getCONTROL_HOST() {
        return CONTROL_HOST;
    }

    public void setCONTROL_HOST(String CONTROL_HOST) {
        this.CONTROL_HOST = CONTROL_HOST;
    }

    public int getCONTROL_PORT() {
        return CONTROL_PORT;
    }

    public void setCONTROL_PORT(int CONTROL_PORT) {
        this.CONTROL_PORT = CONTROL_PORT;
    }

    public int getLEFT_MOTOR_SPEED() {
        return LEFT_MOTOR_SPEED;
    }

    public void setLEFT_MOTOR_SPEED(int LEFT_MOTOR_SPEED) {
        this.LEFT_MOTOR_SPEED = LEFT_MOTOR_SPEED;
    }

    public int getRIGHT_MOTOR_SPEED() {
        return RIGHT_MOTOR_SPEED;
    }

    public void setRIGHT_MOTOR_SPEED(int RIGHT_MOTOR_SPEED) {
        this.RIGHT_MOTOR_SPEED = RIGHT_MOTOR_SPEED;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
