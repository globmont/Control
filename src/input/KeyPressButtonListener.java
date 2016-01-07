package input;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Nikhil on 1/5/2016.
 */
public class KeyPressButtonListener implements ButtonListener {

    private HashMap<Control.Button, String[][]> keyBindings = new HashMap<>();
    Robot r;

    public KeyPressButtonListener() {

        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // TODO: 1/5/2016 replace hardcoded bindings with file read
        keyBindings.put(Control.Button.A, new String[][] {{}, {"ctrl"}});
        keyBindings.put(Control.Button.B, new String[][] {{}, {"windows"}});
        keyBindings.put(Control.Button.X, new String[][] {{}, {"shift"}});
        keyBindings.put(Control.Button.Y, new String[][] {{}, {"alt"}});
        keyBindings.put(Control.Button.DU, new String[][] {{}, {"backspace"}});
        keyBindings.put(Control.Button.DR, new String[][] {{}, {"enter"}});
        keyBindings.put(Control.Button.DD, new String[][] {{}, {"space"}});
        keyBindings.put(Control.Button.DL, new String[][] {{}, {"tab"}});
        keyBindings.put(Control.Button.ADU, new String[][] {{"numLock"}, {"upArrow"}});
        keyBindings.put(Control.Button.ADR, new String[][] {{"numLock"}, {"rightArrow"}});
        keyBindings.put(Control.Button.ADD, new String[][] {{"numLock"}, {"downArrow"}});
        keyBindings.put(Control.Button.ADL, new String[][] {{"numLock"}, {"leftArrow"}});
        keyBindings.put(Control.Button.BACK, new String[][] {{}, {"altTab"}});

    }

    @Override
    public void pressed(Control.Button button) {
        String[][] binding = keyBindings.get(button);
        for(int i = 0; i < binding[0].length; i++) {
            String key = binding[0][i];
            Integer[] codes = Utils.keyCodes.get(key);
            pressKeySequence(codes);
            releaseKeySequence(codes);
        }
//        System.out.println(Arrays.toString(binding[1]));
//        System.out.println(binding[1].length);
        for(int j = 0; j < binding[1].length; j++) {
            String key = binding[1][j];
//            System.out.println("Pressed: " + key);
            Integer[] codes = Utils.keyCodes.get(key);
            pressKeySequence(codes);
        }

    }

    @Override
    public void notPressed(Control.Button button) {
        String[][] binding = keyBindings.get(button);
        for(int i = binding[1].length - 1; i >= 0; i--) {
            String key = binding[1][i];
//            System.out.println("Released: " + key);
            Integer[] codes = Utils.keyCodes.get(key);
            releaseKeySequence(codes);
        }
        for(int i = binding[0].length - 1; i >= 0; i--) {
            String key = binding[0][i];
            Integer[] codes = Utils.keyCodes.get(key);
            pressKeySequence(codes);
            releaseKeySequence(codes);
        }
    }

    public void setKeyBinding(Control.Button button, String[][] binding) {
        keyBindings.put(button, binding);
    }

    public void removeKeyBinding(Control.Button button) {
        keyBindings.remove(button);
    }

    private void pressKeySequence(Integer[] keys) {
        for(int i = 0; i < keys.length; i++) {
            r.keyPress(keys[i]);
//            System.out.println("Pressed: " + keys[i]);
        }
    }

    private void releaseKeySequence(Integer[] keys) {
        for(int i = keys.length - 1; i >= 0; i--) {
            r.keyRelease(keys[i]);
//            System.out.println("Released: " + keys[i]);
        }
    }
}
