package input;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Nikhil on 1/5/2016.
 */
public class KeyPressButtonListener implements ButtonListener {

    private HashMap<Control.Button, String[][]> keyBindings;
    Robot r;

    public KeyPressButtonListener(HashMap<Control.Button, String[][]> keyBindings) {
        this.keyBindings = keyBindings;
    }

    public KeyPressButtonListener() {
        keyBindings = new HashMap<>();
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }


    public void setMappings(HashMap<Control.Button, String[][]> mappings) {
        this.keyBindings = mappings;
    }

    public void addMapping(Control.Button button, String[][] keys) {
        keyBindings.put(button, keys);
//        System.out.println("Added mapping: " + Arrays.toString(keys[0]) + " " + Arrays.toString(keys[1]) + ". New size: " + keyBindings.size());
    }

    public void clearMappings() {
        this.keyBindings = new HashMap<>();
    }

    @Override
    public void pressed(Control.Button button) {
        String[][] binding = keyBindings.get(button);
//        System.out.println("Binding: " + Arrays.toString(binding[0]) + " " + Arrays.toString(binding[1]) + ". New size: " + keyBindings.size());
//        System.out.println("Length of binding[0]: " + binding[0].length);
        for(int i = 0; i < binding[0].length; i++) {
            String key = binding[0][i];
            Integer[] codes = Utils.keyCodes.get(key);
            pressKeySequence(codes);
            releaseKeySequence(codes);
        }
        for(int j = 0; j < binding[1].length; j++) {
            String key = binding[1][j];
            Integer[] codes = Utils.keyCodes.get(key);
            pressKeySequence(codes);
        }

    }

    // TODO: 1/6/2016 figure out why shift arrow isn't working properly

    @Override
    public void notPressed(Control.Button button) {
        String[][] binding = keyBindings.get(button);
        for(int i = binding[1].length - 1; i >= 0; i--) {
            String key = binding[1][i];
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
        }
    }

    private void releaseKeySequence(Integer[] keys) {
        for(int i = keys.length - 1; i >= 0; i--) {
            r.keyRelease(keys[i]);
//            System.out.println("Released: " + keys[i]);
        }
    }
}
