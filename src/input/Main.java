package input;

import application.Input;

public class Main {
	public static void main(String[] args) {
		Utils.init();
		Input keyboard = new Input();
		Control c = new Control(keyboard);
		ConfigHandler config = new ConfigHandler(c, "config.ini");
		config.readConfig();
		config.applyButtonStateParameters();
		new Thread(keyboard).start();
//		keyboard.run();
//		keyboard.hide();
		ButtonHandler h = new DefaultButtonHandler(c, keyboard);
		c.addButtonHandler(h);
		c.start();
	}
}
