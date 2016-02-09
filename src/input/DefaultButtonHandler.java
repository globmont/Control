package input;

import keyboard.Input;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class DefaultButtonHandler implements ButtonHandler {
	
	private Control control;
	private Robot r;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	ArrayList<Boolean> buttons = new ArrayList<Boolean>();
	
	boolean aPressed, bPressed, xPressed, yPressed, lbPressed, rbPressed, backPressed, startPressed, lsPressed, rsPressed, rtPressed, ltPressed, lsMoving, dpadPressed, lsCardinalPressed;
	int aPolls, bPolls, xPolls, yPolls, lbPolls, rbPolls, backPolls, startPolls, lsPolls, rsPolls, rtPolls, ltPolls, dpadPolls, lsCardinalPolls;
	double dpadPrevious;
	boolean dpadArrows = false;
	//int pollHoldThreshold = 35;
	
	double slowMouseMoveScale = 1;
	double fastMouseMoveScale = 10;
	double mouseMoveScale = fastMouseMoveScale;
	
	double slowScrollScale = 3;
	double fastScrollScale = 6;
	double scrollScale = fastScrollScale;
	
	int[] dpadWhitespaceCodes = {KeyEvent.VK_BACK_SPACE, KeyEvent.VK_ENTER, KeyEvent.VK_SPACE, KeyEvent.VK_TAB};
	int[] dpadArrowCodes = {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT};
	boolean numLockWasOn = false;

	private Input keyboard;

	private double stickCardinalDeadzone = .3;

	public DefaultButtonHandler(Control c, Input keyboard) {
		this.control = c;
		this.r = c.r;
		this.keyboard = keyboard;
	}

	@Override
	public void a(boolean down) {
		if(down) {
			aDown();
		} else {
			aUp();
		}
	}

	@Override
	public void b(boolean down) {
		if(down) {
			bDown();
		} else {
			bUp();
		}
	}

	@Override
	public void x(boolean down) {
		if(down) {
			xDown();
		} else {
			xUp();
		}
	}

	@Override
	public void y(boolean down) {
		if(down) {
			yDown();
		} else {
			yUp();
		}
	}

	@Override
	public void lb(boolean down) {
		if(down) {
			lbDown();
		} else {
			lbUp();
		}
	}

	@Override
	public void rb(boolean down) {
		if(down) {
			rbDown();
		} else {
			rbUp();
		}
		
	}

	@Override
	public void back(boolean down) {
		if(down) {
			backDown();
		} else {
			backUp();
		}
	}

	@Override
	public void start(boolean down) {
		if(down) {
			startDown();
		} else {
			startUp();
		}
	}

	@Override
	public void ls(boolean down) {
		if(down) {
			lsDown();
		} else {
			lsUp();
		}
	}

	@Override
	public void rs(boolean down) {
		if(down) {
			rsDown();
		} else {
			rsUp();
		}
	}
	
	@Override
	public void rt(double value) {
		if(value < -.5) {
			rtDown();
		} else {
			rtUp();
		}
	}

	@Override
	public void lt(double value) {
		if(value > .5) {
			ltDown();
		} else {
			ltUp();
		}
		
	}
	
	public void handleButtonArray(ArrayList<Boolean> list) {
		System.out.println("Polled old method");
		this.buttons = list;
		a(list.get(0));
		b(list.get(1));
		x(list.get(2));
		y(list.get(3));
		lb(list.get(4));
		rb(list.get(5));
		back(list.get(6));
		start(list.get(7));
		ls(list.get(8));
		rs(list.get(9));
	}

	
	public void aDown() {
		if(!aPressed) {
			r.keyPress(KeyEvent.VK_CONTROL);
			aPressed = true;
		}	
	}

	
	public void bDown() {
		if(!bPressed) {
			r.keyPress(KeyEvent.VK_WINDOWS);
			bPressed = true;
		}	
	}

	
	public void xDown() {
		if(!xPressed) {
			r.keyPress(KeyEvent.VK_SHIFT);
			if(lbPressed) {
				Platform.runLater(() -> {
					keyboard.setShift(true);
				});
			}
			xPressed = true;
		}			
	}

	
	public void yDown() {
		if(!yPressed) {
			r.keyPress(KeyEvent.VK_ALT);
			yPressed = true;
		}			
	}

	class Show implements Runnable {

		@Override
		public void run() {
			keyboard.show();
		}
	}


	public void lbDown() {
		if(!lbPressed) {
//			keyboard.show();
			Platform.runLater(() -> {keyboard.show();});
			lbPressed = true;
		}
	}

	
	public void rbDown() {
		if(!rbPressed) {
			rbPressed = true;
			mouseMoveScale = slowMouseMoveScale;
			scrollScale = slowScrollScale;
		}
	}

	
	public void backDown() {
		if(!backPressed) {
			r.keyPress(KeyEvent.VK_ALT);
			r.keyPress(KeyEvent.VK_TAB);
			r.keyRelease(KeyEvent.VK_TAB);

			backPressed = true;
		}	
		
	}

	
	public void startDown() {
		if(!startPressed) {
			dpadArrows = !dpadArrows;
			startPressed = true;
		}
	}

	public void writeString(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isUpperCase(c)) {
				r.keyPress(KeyEvent.VK_SHIFT);
			}

			Integer[] codes = Utils.keyCodes.get(Character.toLowerCase(c));
			pressKeySequence(codes);

			if (Character.isUpperCase(c)) {
				r.keyRelease(KeyEvent.VK_SHIFT);
			}
		}
	}

	public void lsDown() {
		if(!lsPressed) {
			lsPressed = true;
			Platform.runLater(() -> {
				keyboard.setIndex(0);
			});
		}
		
	}

	
	public void rsDown() {
		if(!rsPressed) {
			rsPressed = true;
			r.mousePress(InputEvent.BUTTON3_MASK);
		}
		
		if(rtPressed) {
			r.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		}
	}

	
	public void aUp() {
		if(aPressed) {
			r.keyRelease(KeyEvent.VK_CONTROL);
			aPressed = false;
		}	
	}

	
	public void bUp() {
		if(bPressed) {
			r.keyRelease(KeyEvent.VK_WINDOWS);
			bPressed = false;
		}	
	}

	
	public void xUp() {
		if(xPressed) {
			r.keyRelease(KeyEvent.VK_SHIFT);
			if(lbPressed) {
				Platform.runLater(() -> {
					keyboard.setShift(false);
				});
			}
			xPressed = false;
		}	
	}

	
	public void yUp() {
		if(yPressed) {
			r.keyRelease(KeyEvent.VK_ALT);
			yPressed = false;
		}	
	}

	
	public void lbUp() {
		if(lbPressed) {
			Platform.runLater(() -> {keyboard.hide();});
			lbPressed = false;
		}

	}

	
	public void rbUp() {
		if(rbPressed) {
			rbPressed = false;
			mouseMoveScale = fastMouseMoveScale;
			scrollScale = fastScrollScale;
		}
		
	}

	
	public void backUp() {
		if(backPressed) {
			r.keyRelease(KeyEvent.VK_ALT);
			backPressed = false;
		}			
	}

	
	public void startUp() {
		startPressed = false;
	}

	
	public void lsUp() {
		if(lsPressed) {
			lsPressed = false;
		}
		
	}

	
	public void rsUp() {
		if(rsPressed) {
			r.mouseRelease(InputEvent.BUTTON3_MASK);
			rsPressed = false;
		}
		
		
	}

	
	public void ltDown() {
			
	}

	
	public void rtDown() {

		if(!rtPressed) {
			rtPressed = true;
			if(lbPressed) {
				Platform.runLater(() -> {
					String letter = keyboard.getLetter();
					writeString(letter);
				});
			} else {
				r.mousePress(InputEvent.BUTTON1_DOWN_MASK);


				if (rsPressed) {
					r.mousePress(InputEvent.BUTTON2_DOWN_MASK);
				}
			}
		}

	}

	
	public void rtUp() {
		if(rtPressed) {
			r.mouseRelease(InputEvent.BUTTON1_MASK);
			r.mouseRelease(InputEvent.BUTTON2_MASK);
			rtPressed = false;
		}
		

	}

	
	public void ltUp() {
			
		
	}

	@Override
	public void rsMove(double x, double y) {
		double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
		double mouseY = MouseInfo.getPointerInfo().getLocation().getY();
		
		double dx = x * mouseMoveScale;
		double dy =  y * mouseMoveScale;
		
		mouseX += dx;
		mouseY += dy;

		if(lbPressed) {
			Platform.runLater(() -> {keyboard.updateLocation();});
		}

		r.mouseMove((int) Math.round(mouseX), (int) Math.round(mouseY));

	}

	@Override
	public void lsMove(double x, double y) {
		lsPolls++;
		if(!lbPressed) { //scroll
			if (!lsMoving || (lsMoving && lsPolls % 20 == 0)) {
				r.mouseWheel((int) (y * scrollScale));
				lsMoving = true;
			}
		} else { //keyboard
			if(x >= stickCardinalDeadzone && Utils.isWithin(0, y, stickCardinalDeadzone)) {
				lsCardinalPolls++;
				if(!lsCardinalPressed || lsCardinalPressed && lsCardinalPolls >= 100 && lsCardinalPolls % 10 == 0) {
					lsCardinalPressed = true;
					Platform.runLater(() -> {
						keyboard.moveRight();
					});
				}
				if(lsCardinalPolls == 75) {
					lsCardinalPressed = false;
				}
			} else if(x <= -stickCardinalDeadzone && Utils.isWithin(0, y, stickCardinalDeadzone)) {
				lsCardinalPolls++;
				if(!lsCardinalPressed || lsCardinalPressed && lsCardinalPolls >= 100 && lsCardinalPolls % 10 == 0) {
					lsCardinalPressed = true;
					Platform.runLater(() -> {
						keyboard.moveLeft();
					});
				}

				if(lsCardinalPolls == 75) {
					lsCardinalPressed = false;
				}
			} else if(y >= stickCardinalDeadzone && Utils.isWithin(0, x, stickCardinalDeadzone)) {
				lsCardinalPolls++;
				if(!lsCardinalPressed || lsCardinalPressed && lsCardinalPolls >= 100 && lsCardinalPolls % 10 == 0) {
					lsCardinalPressed = true;
					Platform.runLater(() -> {
						keyboard.movePageLeft();
					});
				}

				if(lsCardinalPolls == 75) {
					lsCardinalPressed = false;
				}
			} else if(y <= -stickCardinalDeadzone && Utils.isWithin(0, x, stickCardinalDeadzone)) {
				lsCardinalPolls++;
				if(!lsCardinalPressed || lsCardinalPressed && lsCardinalPolls >= 100 && lsCardinalPolls % 10 == 0) {
					lsCardinalPressed = true;
					Platform.runLater(() -> {
						keyboard.movePageRight();
					});
				}

				if(lsCardinalPolls == 75) {
					lsCardinalPressed = false;
				}
			}
		}
		
	}

	@Override
	public void rsNotMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lsNotMove() {
		lsMoving = false;
		lsCardinalPressed = false;
		lsPolls = 0;
		lsCardinalPolls = 0;
	}
	
	int i = 0;
	@Override
	public void dpad(double value) {
		int[] codes = (dpadArrows) ? dpadArrowCodes : dpadWhitespaceCodes;
		dpadPolls++;
		if(!dpadPressed || (dpadPressed && dpadPolls >= 100 && dpadPolls % 10 == 0)) {
			dpadPressed = true;
			
			toggleNumLock();
			if(Utils.isWithin(.25, value, .05)) {
				r.keyPress(codes[0]);
			} else if(Utils.isWithin(.5, value, .05)) {
				r.keyPress(codes[1]);
			} else if(Utils.isWithin(.75, value, .05)) {
				r.keyPress(codes[2]);
			} else if(Utils.isWithin(1, value, .05)) {
				r.keyPress(codes[3]);	
			}
			
			toggleNumLock();
			
			      
		}
		
		if(dpadPolls == 99) {
			r.keyRelease(codes[0]);
			r.keyRelease(codes[1]);                         
			r.keyRelease(codes[2]);
			r.keyRelease(codes[3]);
			dpadPressed = false;
		}
		
		if(Utils.isWithin(0,  value,  .05)) {
			dpadPolls = 0;
			r.keyRelease(codes[0]);
			r.keyRelease(codes[1]);                         
			r.keyRelease(codes[2]);
			r.keyRelease(codes[3]);
			dpadPressed = false;									
		}
		
		
	}
	
	
	private void toggleNumLock() {
		r.keyPress(KeyEvent.VK_NUM_LOCK);
		r.keyRelease(KeyEvent.VK_NUM_LOCK);
	}


	private void pressKeySequence(Integer[] keys) {
		for(int i = 0; i < keys.length; i++) {
			r.keyPress(keys[i]);
		}

		for(int i = keys.length - 1; i >= 0; i--) {
			r.keyRelease(keys[i]);
		}
	}


}

