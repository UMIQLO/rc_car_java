package moe.umiqlo.remotecontrol.util;

import java.util.ArrayList;

public class DebugMessage {

    private static DebugMessage instance;

    private DebugMessage() {
        debugMessage = new ArrayList<>();
    }

    public static synchronized DebugMessage getInstance() {
        if (instance == null) {
            instance = new DebugMessage();
        }
        return instance;
    }

    private ArrayList<String> debugMessage;

    public ArrayList<String> getMessage() {
        return debugMessage;
    }

    public void setMessage(String message) {
        debugMessage.add(message);
    }
}
