package input;

import java.util.ArrayList;

public interface ButtonHandler {
	public void a(boolean down);
	public void b(boolean down);
	public void x(boolean down);
	public void y(boolean down);
	public void lb(boolean down);
	public void rb(boolean down);
	public void back(boolean down);
	public void start(boolean down);
	public void ls(boolean down);
	public void rs(boolean down);
	
	/*public void aDown();
	public void bDown();
	public void xDown();
	public void yDown();
	public void lbDown();
	public void rbDown();
	public void backDown();
	public void startDown();
	public void lsDown();
	public void rsDown();
	public void ltDown();
	public void rtDown();
	
	public void aUp();
	public void bUp();
	public void xUp();
	public void yUp();
	public void lbUp();
	public void rbUp();
	public void backUp();
	public void startUp();
	public void lsUp();
	public void rsUp();
	public void rtUp();
	public void ltUp();*/
	
	public void rt(double value);
	public void lt(double value);
	public void dpad(double value);
	public void handleButtonArray(ArrayList<Boolean> b);
	
	public void rsMove(double x, double y);
	public void lsMove(double x, double y);
	public void rsNotMove();
	public void lsNotMove();
}
