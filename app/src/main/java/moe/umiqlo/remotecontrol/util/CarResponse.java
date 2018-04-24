package moe.umiqlo.remotecontrol.util;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarResponse {

    private static CarResponse instance;

    public static synchronized CarResponse getInstance() {
        if (instance == null) {
            instance = new CarResponse();
        }
        return instance;
    }

    public void setInstance() {
        instance = this;
    }


    @SerializedName("CMD")
    @Expose
    private String cmd;
    @SerializedName("MotorSpeedLeft")
    @Expose
    private Integer motorSpeedLeft;
    @SerializedName("MotorSpeedRight")
    @Expose
    private Integer motorSpeedRight;
    @SerializedName("ServoPoint")
    @Expose
    private Integer servoPoint;
    @SerializedName("DisplayDistance")
    @Expose
    private Integer displayDistance;

    public String getCMD() {
        return cmd;
    }

    public void setCMD(String cmd) {
        this.cmd = cmd;
    }

    public Integer getMotorSpeedLeft() {
        return motorSpeedLeft;
    }

    public void setMotorSpeedLeft(Integer motorSpeedLeft) {
        this.motorSpeedLeft = motorSpeedLeft;
    }

    public Integer getMotorSpeedRight() {
        return motorSpeedRight;
    }

    public void setMotorSpeedRight(Integer motorSpeedRight) {
        this.motorSpeedRight = motorSpeedRight;
    }

    public Integer getServoPoint() {
        return servoPoint;
    }

    public void setServoPoint(Integer servoPoint) {
        this.servoPoint = servoPoint;
    }

    public Integer getDisplayDistance() {
        return displayDistance;
    }

    public void setDisplayDistance(Integer displayDistance) {
        this.displayDistance = displayDistance;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
