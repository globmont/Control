package input;

import java.util.ArrayList;

/**
 * Created by Nikhil on 1/5/2016.
 */
public interface ButtonBroadcaster {
    public void pollButtons(ArrayList<Control.Button> buttons);
    public void addListener(ButtonListener listener);
    public void addListener(Control.Button button, ButtonListener listener);
    public void clearListeners(Control.Button button);
    public void removeListener(ButtonListener listener);
    public void setMode(Control.Button button, ButtonState.Mode mode);
    public void setPushFrequency(Control.Button button, int frequency);
    public void setHoldStart(Control.Button button, int holdStart);
}
