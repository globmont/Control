package input;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Nikhil on 1/6/2016.
 */
public class MouseEventButtonListener implements ButtonListener {
    private Robot r;
    private HashMap<Control.Button, String> mouseMappings = new HashMap<>();

    public MouseEventButtonListener() {
        try {
            this.r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public void setMappings(HashMap<Control.Button, String> mappings) {
        this.mouseMappings = mappings;
    }

    public void addMapping(Control.Button button, String mouseButton) {
        mouseMappings.put(button, mouseButton);
    }

    public void clearMappings() {
        this.mouseMappings = new HashMap<>();
    }

    @Override
    public void pressed(Control.Button button) {
        String binding = mouseMappings.get(button);
        Integer[] codes = Utils.keyCodes.get(binding);
        pressMouseSequence(codes);
    }

    @Override
    public void notPressed(Control.Button button) {
        String binding = mouseMappings.get(button);
        Integer[] codes = Utils.keyCodes.get(binding);
        releaseMouseSequence(codes);
    }

    private void pressMouseSequence(Integer[] keys) {
        for(int i = 0; i < keys.length; i++) {
            r.mousePress(keys[i]);
        }
    }

    private void releaseMouseSequence(Integer[] keys) {
        for(int i = keys.length - 1; i >= 0; i--) {
            r.mouseRelease(keys[i]);
        }
    }
}
