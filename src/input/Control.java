package input;

import application.Input;
import joystick.JInputJoystick;
import net.java.games.input.Controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Control {
	JInputJoystick controller;
	Robot r;
	
	long pollNumber = 0;
	double stickDeadzoneRadius = .15;
	private double stickCardinalDeadzone = .3;
	double triggerDeadzone = .05;
	int pollingTime = 8;


	private boolean dpadToggle = false;
	private boolean keyboardVisible = false;
	private boolean keyboardShift = false;

	int slowMouseMoveScale = 1;
	int fastMouseMoveScale = 10;
	int mouseMoveScale = fastMouseMoveScale;

	int slowScrollScale = 1;
	int fastScrollScale = 3;
	int scrollScale = fastScrollScale;


	private ButtonBroadcaster bb = new DefaultButtonBroadcaster(10, 100, ButtonState.Mode.SINGLE);
	private ArrayList<ButtonHandler> handlers = new ArrayList<>();

	private HashMap<Button, String[]> bindingTypes = new HashMap<>();
	private KeyPressButtonListener keyPressListener;
	private FunctionButtonListener functionListener;
	private MouseEventButtonListener mouseEventListener;
	private HashMap<Stick, StickState> stickStateMap = new HashMap<>();
	private HashMap<Stick, Button[]> stickToCardinalMap = new HashMap<>();

	private Input keyboard;

	public enum Button {
		A,
		B,
		X,
		Y,
		LB,
		RB,
		BACK,
		START,
		LS,
		RS,
		DU,
		DD,
		DL,
		DR,
		ADU,
		ADD,
		ADL,
		ADR,
		RT,
		LT,
		LSU,
		LSR,
		LSD,
		LSL,
		RSU,
		RSR,
		RSD,
		RSL,
		MLS,
		MRS;
}
	
	public enum Stick {
		XROT,
		YROT,
		XAXIS,
		YAXIS,
		LS,
		RS;
	}
	
	
	public Control(Input keyboard) {

		this.keyboard = keyboard;

		stickToCardinalMap.put(Stick.LS, new Button[] {Button.LSU, Button.LSR, Button.LSD, Button.LSL});
		stickToCardinalMap.put(Stick.RS, new Button[] {Button.RSU, Button.RSR, Button.RSD, Button.RSL});

		try {
			r = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		try {
			controller = new JInputJoystick(Controller.Type.GAMEPAD);
			if (controller.isControllerConnected()) {
				System.out.println("Controller connected successfully!");
			} else {
				System.out.println("Please connect controller!");
				while(!controller.isControllerConnected()) {
					controller = new JInputJoystick(Controller.Type.GAMEPAD);
				}
				System.out.println("Controller connected successfully!");
			}
		} catch(Exception e){
			System.out.println("Please connect controller!");
			while(!controller.isControllerConnected()) {
				controller = new JInputJoystick(Controller.Type.GAMEPAD);
			}
			System.out.println("Controller connected successfully!");
		}
	}
	
	public void start() {
		run();
	}
	
	private void run() {

		while(true) {
			while(!controller.pollController()) {
				System.err.println("Couldn't reach controller! Waiting for reconnect.");

			}
			
			pollNumber++;

			ArrayList<Control.Button> pressedButtons = getPressedButtons();
			bb.pollButtons(pressedButtons);
			
			
			try {
				Thread.sleep(pollingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addButtonHandler(ButtonHandler b) {
		handlers.add(b);
	}

	public double getAdjustedStickValue(Stick stick) {
		if(stick == Stick.XAXIS) {
			return (Utils.isWithin(0, controller.getXAxisValue(), stickDeadzoneRadius)) ? 0 : controller.getXAxisValue(); 
		} else if(stick == Stick.YAXIS) {
			return (Utils.isWithin(0, controller.getYAxisValue(), stickDeadzoneRadius)) ? 0 : controller.getYAxisValue(); 
		} else if(stick == Stick.XROT) {
			return (Utils.isWithin(0, controller.getXRotationValue(), stickDeadzoneRadius)) ? 0 : controller.getXRotationValue();
		} else if(stick == Stick.YROT) {
			return (Utils.isWithin(0, controller.getYRotationValue(), stickDeadzoneRadius)) ? 0 : controller.getYRotationValue();
		}
		
		return Double.NaN;
	}

	private ArrayList<Control.Button> getPressedButtons() {
		ArrayList<Boolean> buttonValues = controller.getButtonsValues();
		ArrayList<Control.Button> list = new ArrayList<>();
		if(buttonValues.get(0)) {
			list.add(Button.A);
		}
		if(buttonValues.get(1)) {
			list.add(Button.B);
		}
		if(buttonValues.get(2)) {
			list.add(Button.X);
		}
		if(buttonValues.get(3)) {
			list.add(Button.Y);
		}
		if(buttonValues.get(4)) {
			list.add(Button.LB);
		}
		if(buttonValues.get(5)) {
			list.add(Button.RB);
		}
		if(buttonValues.get(6)) {
			list.add(Button.BACK);
		}
		if(buttonValues.get(7)) {
			list.add(Button.START);
		}
		if(buttonValues.get(8)) {
			list.add(Button.LS);
		}
		if(buttonValues.get(9)) {
			list.add(Button.RS);
		}

		double hatPosition = controller.getHatSwitchPosition();
		if(hatPosition == .25) {
			if(!dpadToggle) {
				list.add(Button.DU);
			} else {
				list.add(Button.ADU);
			}
		} else if(hatPosition == .5) {
			if(!dpadToggle) {
				list.add(Button.DR);
			} else {
				list.add(Button.ADR);
			}
		} else if(hatPosition == .75) {
			if(!dpadToggle) {
				list.add(Button.DD);
			} else {
				list.add(Button.ADD);
			}
		} else if(hatPosition == 1) {
			if(!dpadToggle) {
				list.add(Button.DL);
			} else {
				list.add(Button.ADL);
			}
		}

		if(!Utils.isWithin(0, controller.getZAxisValue(), triggerDeadzone)) {
			if(controller.getZAxisValue() < 0) {
				list.add(Button.RT);
			} else {
				list.add(Button.LT);
			}
		}

		double rsX = getAdjustedStickValue(Stick.XROT);
		double rsY = getAdjustedStickValue(Stick.YROT);
		double lsX = getAdjustedStickValue(Stick.XAXIS);
		double lsY = getAdjustedStickValue(Stick.YAXIS);


		if(!Utils.isWithin(0, rsX, stickDeadzoneRadius) || !Utils.isWithin(0, rsY, stickDeadzoneRadius)) {
			list.add(Button.MRS);
		}

		if(!Utils.isWithin(0, lsX, stickDeadzoneRadius) || !Utils.isWithin(0, lsY, stickDeadzoneRadius)) {
			list.add(Button.MLS);
		}

		if(rsX >= stickCardinalDeadzone && Utils.isWithin(0, rsY, stickCardinalDeadzone)) {
			list.add(Button.RSR);
		} else if(rsX <= -stickCardinalDeadzone && Utils.isWithin(0, rsY, stickCardinalDeadzone)) {
			list.add(Button.RSL);
		} else if(rsY >= stickCardinalDeadzone && Utils.isWithin(0, rsX, stickCardinalDeadzone)) {
			list.add(Button.RSD);
		} else if(rsY <= -stickCardinalDeadzone && Utils.isWithin(0, rsX, stickCardinalDeadzone)) {
			list.add(Button.RSU);
		}

		if(lsX >= stickCardinalDeadzone && Utils.isWithin(0, lsY, stickCardinalDeadzone)) {
			list.add(Button.LSR);
		} else if(lsX <= -stickCardinalDeadzone && Utils.isWithin(0, lsY, stickCardinalDeadzone)) {
			list.add(Button.LSL);
		} else if(lsY >= stickCardinalDeadzone && Utils.isWithin(0, lsX, stickCardinalDeadzone)) {
			list.add(Button.LSD);
		} else if(lsY <= -stickCardinalDeadzone && Utils.isWithin(0, lsX, stickCardinalDeadzone)) {
			list.add(Button.LSU);
		}

		return list;
	}

	public boolean getDpadToggle() {
		return dpadToggle;
	}

	public void setDpadToggle(boolean value) {
		dpadToggle = value;
	}


	public boolean isKeyboardVisible() {
		return keyboardVisible;
	}

	public void setKeyboardVisible(boolean keyboardVisible) {
		this.keyboardVisible = keyboardVisible;
	}

	public boolean isKeyboardShift() {
		return keyboardShift;
	}

	public void setKeyboardShift(boolean keyboardShift) {
		this.keyboardShift = keyboardShift;
	}

	public void addBindingType(Button button, String[] bindingType) {
		bindingTypes.put(button, bindingType);
	}

	public void removeBindingType(Button button, String[] bindingType) {
		bindingTypes.remove(button, bindingType);
	}

	public Button[] getStickToCardinal(Stick stick) {
		return stickToCardinalMap.get(stick);
	}

	public int getScrollScale() {
		return scrollScale;
	}

	public void setScrollScale(int scale) {
		scrollScale = scale;
	}

	public int getFastScrollScale() {
		return fastScrollScale;
	}

	public void setFastScrollScale(int scale) {
		fastScrollScale = scale;
	}

	public int getSlowScrollScale() {
		return slowScrollScale;
	}

	public void setSlowScrollScale(int scale) {
		slowScrollScale = scale;
	}

	public int getSlowMouseMoveScale() {
		return slowMouseMoveScale;
	}

	public void setSlowMouseMoveScale(int slowMouseMoveScale) {
		this.slowMouseMoveScale = slowMouseMoveScale;
	}

	public int getMouseMoveScale() {
		return mouseMoveScale;
	}

	public void setMouseMoveScale(int mouseMoveScale) {
		this.mouseMoveScale = mouseMoveScale;
	}

	public int getFastMouseMoveScale() {
		return fastMouseMoveScale;
	}

	public void setFastMouseMoveScale(int fastMouseMoveScale) {
		this.fastMouseMoveScale = fastMouseMoveScale;
	}

	public void bindKeys() {

		for(Button button : bindingTypes.keySet()) {
			bb.clearListeners(button);
			String[] bindingType = bindingTypes.get(button);
			for(String binding : bindingType) {
				switch (binding) {
					case "keyPress":
						bb.addListener(button, keyPressListener);
						break;
					case "function":
						bb.addListener(button, functionListener);
						break;
					case "mouseEvent":
						bb.addListener(button, mouseEventListener);
						break;
				}
			}
		}

		/*bb.setMode(Button.DU, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.DR, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.DD, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.DL, ButtonState.Mode.REPEAT_DELAY);

		bb.setMode(Button.ADU, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.ADR, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.ADD, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.ADL, ButtonState.Mode.REPEAT_DELAY);

		bb.setMode(Button.LSU, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.LSR, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.LSD, ButtonState.Mode.REPEAT_DELAY);
		bb.setMode(Button.LSL, ButtonState.Mode.REPEAT_DELAY);

		bb.setMode(Button.MRS, ButtonState.Mode.REPEAT_INSTANT);
		bb.setPushFrequency(Button.MRS, 1);*/
	}

	public void setButtonMode(Button button, ButtonState.Mode mode) {
		bb.setMode(button, mode);
	}

	public void setButtonHoldStart(Button button, int holdStart) {
		bb.setHoldStart(button, holdStart);
	}

	public void setButtonPushFrequency(Button button, int pushFrequency) {
		bb.setPushFrequency(button, pushFrequency);
	}

	public void clearBindingTypes() {
		bindingTypes.clear();
	}

	public void resetListeners() {
		keyPressListener = new KeyPressButtonListener();
		functionListener = new FunctionButtonListener(keyboard, this);
		mouseEventListener = new MouseEventButtonListener();
	}

	public KeyPressButtonListener getKeyPressListener() {
		return keyPressListener;
	}

	public FunctionButtonListener getFunctionListener() {
		return functionListener;
	}

	public MouseEventButtonListener getMouseEventListener() {
		return mouseEventListener;
	}
}
