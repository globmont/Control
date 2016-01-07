package input;

/**
 * Created by Nikhil on 1/6/2016.
 */
public class StickState implements State {
    public enum Mode {
        DISCRETE,
        CONTINUOUS;
    }

    private Mode mode;
    private Control.Stick stick;
    private Control control;

    public StickState(Mode mode, Control.Stick stick, Control control, String[][] bindingType) {
        this.mode = mode;
        this.control = control;
        this.stick = stick;

        if(mode == Mode.DISCRETE) {
            Control.Button[] cardinalButtons = control.getStickToCardinal(stick);
            for(int i = 0; i < cardinalButtons.length; i++) {
                control.addBindingType(cardinalButtons[i], bindingType[i]);
            }
        } else if(mode == Mode.CONTINUOUS) {
            Control.Button stickButton = (stick == Control.Stick.RS) ? Control.Button.MRS : Control.Button.MLS;
            control.addBindingType(stickButton, bindingType[0]);
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Control.Stick getStick() {
        return stick;
    }

    public Control getControl() {
        return control;
    }
}
