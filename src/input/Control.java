package input;

import joystick.JInputJoystick;
import net.java.games.input.Controller;

import java.awt.*;
import java.util.ArrayList;

public class Control {
	JInputJoystick controller;
	Robot r;
	
	long pollNumber = 0;
	double stickDeadzoneRadius = .15;
		double triggerDeadzone = .05;
		int pollingTime = 8;

		private ArrayList<ButtonHandler> handlers = new ArrayList<ButtonHandler>();

		private enum Button {
			A(0),
			B(1),
			X(2),
			Y(3),
			LB(4),
			RB(5),
			BACK(6),
		START(7),
		LS(8),
		RS(9);
		
		private int index;
		
		private Button(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	}
	
	private enum Stick {
		XROT,
		YROT,
		XAXIS,
		YAXIS;
	}
	
	
	public Control() {

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
			if(!controller.pollController()) {
				System.err.println("Couldn't reach controller! Exiting.");
				break;
			}
			
			pollNumber++;
			
			
			
			
			for(ButtonHandler h: handlers) {
				
				if(!Utils.isWithin(0, getAdjustedStickValue(Stick.XROT), 0) || !Utils.isWithin(0, getAdjustedStickValue(Stick.YROT), 0)) {
					h.rsMove(getAdjustedStickValue(Stick.XROT), getAdjustedStickValue(Stick.YROT));
				} else {
					h.rsNotMove();
				}
				
				if(!Utils.isWithin(0, getAdjustedStickValue(Stick.XAXIS), 0) || !Utils.isWithin(0, getAdjustedStickValue(Stick.YAXIS), 0)) {
					h.lsMove(getAdjustedStickValue(Stick.XAXIS), getAdjustedStickValue(Stick.YAXIS));
				} else {
					h.lsNotMove();
				}
				
				if(!Utils.isWithin(0, controller.getZAxisValue(), triggerDeadzone)) {

					if(controller.getZAxisValue() < 0) {
						h.rt(controller.getZAxisValue());
					} else {
						h.lt(controller.getZAxisValue());
					}
				}
				
				h.dpad(controller.getHatSwitchPosition());
				
				h.handleButtonArray(controller.getButtonsValues());

			}
			
			
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
	
	private boolean getButtonValue(Button b) {
		return controller.getButtonValue(b.getIndex());
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
	
		
}
