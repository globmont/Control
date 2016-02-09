package input;

import keyboard.Input;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

	public static TrayIcon tray;
	public static void main(String[] args) {
		setupTray();
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

	public static void setupTray() {
		Image icon = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/controllerDisconnect.png"));
		SystemTray sysTray = SystemTray.getSystemTray();
		PopupMenu menu = new PopupMenu();
		MenuItem exitItem = new MenuItem("Exit");



		menu.add(exitItem);
/*
		int trayIconWidth = sysTray.getTrayIconSize().width;
		int trayIconHeight = sysTray.getTrayIconSize().height;
		System.out.println(trayIconWidth + "\t" + trayIconHeight);
		icon = icon.getScaledInstance(trayIconWidth, trayIconHeight, Image.SCALE_SMOOTH);*/
		tray = new TrayIcon(icon,"Control",menu);

		tray.setImageAutoSize(true);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				sysTray.remove(tray);
				System.exit(0);
			}
		});
		try {
			sysTray.add(tray);
		}
		catch(Exception e) {
			System.out.println("Unable to add System Tray icon");
			System.exit(0);
		}
	}
}
