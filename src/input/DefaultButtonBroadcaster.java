package input;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultButtonBroadcaster implements ButtonBroadcaster {

	private HashMap<Control.Button, ButtonState> states = new HashMap<>();

	public DefaultButtonBroadcaster(int pollFrequency, int holdStart, ButtonState.Mode mode) {
		for(Control.Button button : Control.Button.values()) {
			states.put(button, new ButtonState(pollFrequency, holdStart, mode));
		}
	}

	@Override
	public void pollButtons(ArrayList<Control.Button> buttons) {
		for(Control.Button button : Control.Button.values()) {
			if(buttons.contains(button)) {
				pollButton(button, true);
			} else {
				pollButton(button, false);
			}
		}
	}

	private void pollButton(Control.Button button, boolean pressed) {
		if(pressed) {
			buttonPressed(button);
		} else {
			buttonNotPressed(button);
		}
	}

	private void buttonPressed(Control.Button button) {
		switch(states.get(button).getMode()) {
			case SINGLE:
				singlePressed(button);
				break;
			case REPEAT_DELAY:
				repeatDelayPressed(button);
				break;
			case REPEAT_INSTANT:
				repeatInstantPressed(button);
				break;
		}
	}

	private void singlePressed(Control.Button button) {
		ButtonState state = states.get(button);
		if(!state.isPressed()) {
			state.setPressed(true);
			emitPressed(button);
		}
	}

	private void repeatDelayPressed(Control.Button button) {
		ButtonState state = states.get(button);
		state.incrementPolls();
		if(!state.isPressed() || (state.isPressed() && state.getPolls() >= state.getHoldStart() && state.getPolls() % state.getPushFrequency() == 0)) {
			state.setPressed(true);
			emitPressed(button);
			emitNotPressed(button);
		}

		if(state.getPolls() == state.getHoldStart() - 1) {
			state.setPressed(false);
		}
	}

	private void repeatInstantPressed(Control.Button button) {
		ButtonState state = states.get(button);
		state.incrementPolls();
		if(!state.isPressed() || (state.isPressed() && state.getPolls() % state.getPushFrequency() == 0)) {
			state.setPressed(true);
			emitPressed(button);
			emitNotPressed(button);
		}
	}

	private void emitPressed(Control.Button button) {
//		System.out.println("Emitted pressed");
		for(ButtonListener listener : states.get(button).getListeners()) {
			listener.pressed(button);
		}
	}

	private void buttonNotPressed(Control.Button button) {
		switch(states.get(button).getMode()) {
			case SINGLE:
				singleNotPressed(button);
				break;
			case REPEAT_DELAY:
				repeatNotPressed(button);
				break;
			case REPEAT_INSTANT:
				repeatNotPressed(button);
				break;
		}
	}

	private void singleNotPressed(Control.Button button) {
		ButtonState state = states.get(button);
		if(state.isPressed()) {
			state.setPressed(false);
			emitNotPressed(button);
		}
	}

	private void repeatNotPressed(Control.Button button) {
		ButtonState state = states.get(button);
		if(state.isPressed() || state.getPolls() != 0) {
			state.setPressed(false);
			state.setPolls(0);
			emitNotPressed(button);
		}
	}

	private void emitNotPressed(Control.Button button) {
//		System.out.println("Emitted not pressed");
		for(ButtonListener listener : states.get(button).getListeners()) {
			listener.notPressed(button);
		}
	}

	@Override
	public void addListener(ButtonListener listener) {
		for(Control.Button button: Control.Button.values()) {
			addListener(button, listener);
		}
	}

	@Override
	public void addListener(Control.Button button, ButtonListener listener) {
		states.get(button).addListener(listener);
	}

	@Override
	public void clearListeners(Control.Button button) {
		states.get(button).clearListeners();
	}

	@Override
	public void removeListener(ButtonListener listener) {
		for(ButtonState state: states.values()) {
			state.removeListener(listener);
		}
	}

	@Override
	public void setMode(Control.Button button, ButtonState.Mode mode) {
		states.get(button).setMode(mode);
	}

	@Override
	public void setPushFrequency(Control.Button button, int frequency) {
		states.get(button).setPushFrequency(frequency);
	}
}

