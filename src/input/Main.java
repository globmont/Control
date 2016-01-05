package input;

import application.Input;

public class Main {
	public static void main(String[] args) {
		Control c = new Control();
		Input keyboard = new Input();
		new Thread(keyboard).start();
//		keyboard.hide();
		ButtonHandler h = new DefaultButtonHandler(c, keyboard);
		c.addButtonHandler(h);
		c.start();
	}
}
