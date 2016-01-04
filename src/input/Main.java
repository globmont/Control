package input;

public class Main {
	public static void main(String[] args) {
		Control c = new Control();
		ButtonHandler h = new DefaultButtonHandler(c);
		c.addButtonHandler(h);
		c.start();
	}
}
