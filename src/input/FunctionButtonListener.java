package input;

import application.Input;
import javafx.application.Platform;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Nikhil on 1/6/2016.
 */
public class FunctionButtonListener implements ButtonListener {
    private Input keyboard;
    private Control control;
    private HashMap<Control.Button, String[]> functionMappings = new HashMap<>();
    private Robot r;

    public FunctionButtonListener(Input keyboard, Control control) {
        this.keyboard = keyboard;
        this.control = control;
        try {
            this.r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }


    public void setMappings(HashMap<Control.Button, String[]> mappings) {
        this.functionMappings = mappings;
    }

    public void addMapping(Control.Button button, String[] functions) {
        functionMappings.put(button, functions);
    }

    public void clearMappings() {
        this.functionMappings = new HashMap<>();
    }

    @Override
    public void pressed(Control.Button button) {
        for(String rootName : functionMappings.get(button)) {
            callMethod(rootName + "Pressed", button);
        }
    }

    @Override
    public void notPressed(Control.Button button) {
        for(String rootName : functionMappings.get(button)) {
            callMethod(rootName + "NotPressed", button);

        }
    }

    private void callMethod(String name, Control.Button button) {
        Method method = null;
        try {
            method = this.getClass().getMethod(name, Control.Button.class);
            method.invoke(this, button);
        } catch (NoSuchMethodException e) {
//            System.out.println("Method '" + name + "' was not found. Ensure that this was intentional!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void keyboardTogglePressed(Control.Button button) {
        control.setKeyboardVisible(true);
        Platform.runLater(() -> {
            keyboard.show();
        });
    }

    public void keyboardToggleNotPressed(Control.Button button) {
        control.setKeyboardVisible(false);
        Platform.runLater(() -> {
           keyboard.hide();
        });
    }

    public void toggleDpadPressed(Control.Button button) {
        control.setDpadToggle(!control.getDpadToggle());
    }

    public void keyboardMoveLeftPressed(Control.Button button) {
        if (control.isKeyboardVisible()) {
            Platform.runLater(() -> {
                keyboard.moveLeft();
            });
        }
    }

    public void keyboardMovePageLeftPressed(Control.Button button) {
        if(control.isKeyboardVisible()) {
            Platform.runLater(() -> {
                keyboard.movePageLeft();
            });
        }
    }

    public void keyboardMoveRightPressed(Control.Button button) {
        if(control.isKeyboardVisible()) {
            Platform.runLater(() -> {
                keyboard.moveRight();
            });
        }
    }

    public void keyboardMovePageRightPressed(Control.Button button) {
        if(control.isKeyboardVisible()) {
            Platform.runLater(() -> {
                keyboard.movePageRight();
            });
        }
    }


    public void keyboardShiftPressed(Control.Button button) {
        if(control.isKeyboardVisible()) {
            control.setKeyboardShift(true);
            Platform.runLater(() -> {
                keyboard.setShift(true);
            });
        }
    }

    public void keyboardShiftNotPressed(Control.Button button) {
        if(control.isKeyboardVisible()) {
            control.setKeyboardShift(false);
            Platform.runLater(() -> {
                keyboard.setShift(false);
            });
        }
    }

    public void keyboardSelectPressed(Control.Button button) {
        if(control.isKeyboardVisible()) {
            Platform.runLater(() -> {
                String letter = keyboard.getLetter();
                writeString(letter);
            });
        }
    }

    public void keyboardResetPressed(Control.Button button) {
        if(control.isKeyboardVisible()) {
            Platform.runLater(() -> {
               keyboard.setIndex(0);
            });
        }
    }

    public void scrollUpPressed(Control.Button button) {
        if(!control.isKeyboardVisible()) {
            int scrollAmount = -control.getScrollScale();
            r.mouseWheel(scrollAmount);
        }
    }

    public void scrollDownPressed(Control.Button button) {
        if(!control.isKeyboardVisible()) {
            int scrollAmount = control.getScrollScale();
            r.mouseWheel(scrollAmount);
        }
    }

    public void slowTogglePressed(Control.Button button) {
        control.setScrollScale(control.getSlowScrollScale());
        control.setMouseMoveScale(control.getSlowMouseMoveScale());
    }

    public void slowToggleNotPressed(Control.Button button) {
        control.setScrollScale(control.getFastScrollScale());
        control.setMouseMoveScale(control.getFastMouseMoveScale());
    }

    public void mouseMovePressed(Control.Button button) {
        double xPos = 0;
        double yPos = 0;

        if(button == Control.Button.MRS) {
            xPos = control.getAdjustedStickValue(Control.Stick.XROT);
            yPos = control.getAdjustedStickValue(Control.Stick.YROT);
        } else if(button == Control.Button.MLS) {
            xPos = control.getAdjustedStickValue(Control.Stick.XAXIS);
            yPos = control.getAdjustedStickValue(Control.Stick.YAXIS);
        }

        double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
        double mouseY = MouseInfo.getPointerInfo().getLocation().getY();

        double dx = xPos * control.getMouseMoveScale();
        double dy =  yPos * control.getMouseMoveScale();

        mouseX += dx;
        mouseY += dy;

        if(control.isKeyboardVisible()) {
            Platform.runLater(() -> {keyboard.updateLocation();});
        }

        r.mouseMove((int) Math.round(mouseX), (int) Math.round(mouseY));
    }

    private void typeKeySequence(Integer[] keys) {
        for(int i = 0; i < keys.length; i++) {
            r.keyPress(keys[i]);
        }

        for(int i = keys.length - 1; i >= 0; i--) {
            r.keyRelease(keys[i]);
        }
    }

    private void pressKeySequence(Integer[] keys) {
        for(int i = 0; i < keys.length; i++) {
            r.keyPress(keys[i]);
        }
    }

    private void releaseKeySequence(Integer[] keys) {
        for(int i = keys.length - 1; i >= 0; i--) {
            r.keyRelease(keys[i]);
        }
    }

    private void writeString(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                pressKeySequence(Utils.keyCodes.get("shift"));
            }

            Integer[] codes = Utils.keyCodes.get(Character.toString(Character.toLowerCase(c)));
            typeKeySequence(codes);

            if (Character.isUpperCase(c)) {
                releaseKeySequence(Utils.keyCodes.get("shift"));
            }
        }
    }

}
