package input;

import java.util.ArrayList;

/**
 * Created by Nikhil on 1/5/2016.
 */
public class ButtonState implements State {

    public enum Mode {
        SINGLE,
        REPEAT_DELAY,
        REPEAT_INSTANT
    }

    private int polls;
    private boolean pressed;
    private int pushFrequency;
    private int holdStart;
    private Mode mode;
    private ArrayList<ButtonListener> listeners = new ArrayList<>();

    public ButtonState(int pushFrequency, int holdStart, Mode mode) {
        this.pushFrequency = pushFrequency;
        this.holdStart = holdStart;
        this.mode = mode;
    }

    public ButtonState() {
        this(10, 100, Mode.SINGLE);
    }


    public int getPolls() {
        return polls;
    }

    public void setPolls(int polls) {
        this.polls = polls;
    }

    public void incrementPolls() {
        this.polls++;
    }

    public void decrementPolls() {
        this.polls--;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public int getPushFrequency() {
        return pushFrequency;
    }

    public void setPushFrequency(int pushFrequency) {
        this.pushFrequency = pushFrequency;
    }

    public int getHoldStart() {
        return holdStart;
    }

    public void setHoldStart(int holdStart) {
        this.holdStart = holdStart;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void addListener(ButtonListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ButtonListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public ArrayList<ButtonListener> getListeners() {
        return listeners;
    }

}
