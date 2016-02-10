package input;

import keyboard.Input;
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
	private double stickCardinalDeadzone = .4;
	double triggerDeadzone = .05;
	int pollingTime = 8;
	int reconnectTime = 2000;

	private ArrayList<Control.Button> buttonList = new ArrayList<>();
	private ArrayList<Control.Button> pressedButtons;
	private ArrayList<Boolean> buttonValues;

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

		waitOnReconnect();
	}

	public void waitOnReconnect() {
		System.out.println("Please connect controller!");
		Main.tray.setImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/controllerDisconnect.png")));


		if(controller == null) {
			controller = new JInputJoystick(Controller.Type.GAMEPAD);
		}

		while(!controller.isControllerConnected() || !controller.pollController()) {
			controller.refreshControllers(Controller.Type.GAMEPAD);
//			System.gc();
			try {
				Thread.sleep(reconnectTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Main.tray.displayMessage("Control", "Controller connected", TrayIcon.MessageType.INFO);
		Main.tray.setImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/controller.png")));
		System.out.println("Controller connected successfully!");
	}

	public void start() {
		run();
	}
	
	private void run() {

		while(true) {
			if(!controller.pollController()) {
				Main.tray.displayMessage("Control", "Controller disconnected", TrayIcon.MessageType.INFO);
				waitOnReconnect();
			}
			
			pollNumber++;

			pressedButtons = getPressedButtons();
			bb.pollButtons(pressedButtons);
			
//			System.gc();
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

	private ArrayList<Button> getPressedButtons() {
		buttonValues = controller.getButtonsValues();
		buttonList.clear();
		if(buttonValues.get(0)) {
			buttonList.add(Button.A);
		}
		if(buttonValues.get(1)) {
			buttonList.add(Button.B);
		}
		if(buttonValues.get(2)) {
			buttonList.add(Button.X);
		}
		if(buttonValues.get(3)) {
			buttonList.add(Button.Y);
		}
		if(buttonValues.get(4)) {
			buttonList.add(Button.LB);
		}
		if(buttonValues.get(5)) {
			buttonList.add(Button.RB);
		}
		if(buttonValues.get(6)) {
			buttonList.add(Button.BACK);
		}
		if(buttonValues.get(7)) {
			buttonList.add(Button.START);
		}
		if(buttonValues.get(8)) {
			buttonList.add(Button.LS);
		}
		if(buttonValues.get(9)) {
			buttonList.add(Button.RS);
		}

		double hatPosition = controller.getHatSwitchPosition();
		if(hatPosition == .25) {
			if(!dpadToggle) {
				buttonList.add(Button.DU);
			} else {
				buttonList.add(Button.ADU);
			}
		} else if(hatPosition == .5) {
			if(!dpadToggle) {
				buttonList.add(Button.DR);
			} else {
				buttonList.add(Button.ADR);
			}
		} else if(hatPosition == .75) {
			if(!dpadToggle) {
				buttonList.add(Button.DD);
			} else {
				buttonList.add(Button.ADD);
			}
		} else if(hatPosition == 1) {
			if(!dpadToggle) {
				buttonList.add(Button.DL);
			} else {
				buttonList.add(Button.ADL);
			}
		}

		if(!Utils.isWithin(0, controller.getZAxisValue(), triggerDeadzone)) {
			if(controller.getZAxisValue() < 0) {
				buttonList.add(Button.RT);
			} else {
				buttonList.add(Button.LT);
			}
		}

		double rsX = getAdjustedStickValue(Stick.XROT);
		double rsY = getAdjustedStickValue(Stick.YROT);
		double lsX = getAdjustedStickValue(Stick.XAXIS);
		double lsY = getAdjustedStickValue(Stick.YAXIS);


		if(!Utils.isWithin(0, rsX, stickDeadzoneRadius) || !Utils.isWithin(0, rsY, stickDeadzoneRadius)) {
			buttonList.add(Button.MRS);
		}

		if(!Utils.isWithin(0, lsX, stickDeadzoneRadius) || !Utils.isWithin(0, lsY, stickDeadzoneRadius)) {
			buttonList.add(Button.MLS);
		}

		if(rsX >= stickCardinalDeadzone && Utils.isWithin(0, rsY, stickCardinalDeadzone)) {
			buttonList.add(Button.RSR);
		} else if(rsX <= -stickCardinalDeadzone && Utils.isWithin(0, rsY, stickCardinalDeadzone)) {
			buttonList.add(Button.RSL);
		} else if(rsY >= stickCardinalDeadzone && Utils.isWithin(0, rsX, stickCardinalDeadzone)) {
			buttonList.add(Button.RSD);
		} else if(rsY <= -stickCardinalDeadzone && Utils.isWithin(0, rsX, stickCardinalDeadzone)) {
			buttonList.add(Button.RSU);
		}

		if(lsX >= stickCardinalDeadzone && Utils.isWithin(0, lsY, stickCardinalDeadzone)) {
			buttonList.add(Button.LSR);
		} else if(lsX <= -stickCardinalDeadzone && Utils.isWithin(0, lsY, stickCardinalDeadzone)) {
			buttonList.add(Button.LSL);
		} else if(lsY >= stickCardinalDeadzone && Utils.isWithin(0, lsX, stickCardinalDeadzone)) {
			buttonList.add(Button.LSD);
		} else if(lsY <= -stickCardinalDeadzone && Utils.isWithin(0, lsX, stickCardinalDeadzone)) {
			buttonList.add(Button.LSU);
		}

		return buttonList;
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
