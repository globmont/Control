package application;

import com.sun.webkit.WebPage;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Scanner;


public class Input extends Application implements Runnable {
	
	private static Stage primaryStage;
	private static Stage subStage;
	private BorderPane rootLayout;
	private static WebEngine webengine;
	private static JFrame window;


	@Override
	public void start(Stage primaryStage) {
		initRootLayout();
	}
	
	public void initRootLayout() {
		try {
//			primaryStage.show();

			rootLayout = FXMLLoader.load(getClass().getResource("Layout.fxml"));
			Scene scene = new Scene(rootLayout,630,150);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("flatterfx.css").toExternalForm());
			
			WebView web = (WebView) scene.lookup("#main");
			web.setContextMenuEnabled(false);
			this.webengine = web.getEngine();
            webengine.documentProperty().addListener(new DocListener());

			String jquery = new Scanner(getClass().getResourceAsStream("/html/jquery.js")).useDelimiter(("\\A")).next();
			String script = new Scanner(getClass().getResourceAsStream("/html/script.js")).useDelimiter(("\\A")).next();


			String url = getClass().getResource("/html/index.html").toExternalForm();
//			System.out.println(url);
//			webengine.executeScript(jquery);
//			webengine.executeScript(script);
			webengine.load(url);

			window = new JFrame();
			window.setAutoRequestFocus(false);
			window.setAlwaysOnTop(true);
			window.setFocusable(false);
			window.setFocusableWindowState(false);
			window.setType(JFrame.Type.UTILITY);
			window.setUndecorated(true);
			final JFXPanel panel = new JFXPanel();
			panel.setScene(scene);
			window.add(panel);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public void moveLeft() {
		webengine.executeScript("moveLeft()");
	}

	public void movePageLeft() {
//		int index = getIndex();
//		setIndex(index - 9);
		for(int i = 0; i < 5; i++) {
			moveLeft();
		}
	}

	public void moveRight() {
		webengine.executeScript("moveRight()");
	}

	public void movePageRight() {
//		int index = getIndex();
//		setIndex(index + 9);
		for(int i = 0; i < 5; i++) {
			moveRight();
		}
	}

	public String getLetter() {
		return (String) webengine.executeScript("getSelectedLetter()");
	}

	public void setShift(boolean value) {
		webengine.executeScript("setShift(" + value + ")");
	}

	public void setIndex(int value) {
		webengine.executeScript("setIndex(" + value + ");");
	}

	public int getIndex() {
		return (Integer) webengine.executeScript("index");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		launch();
	}

	public void hide() {
		window.setVisible(false);

	}

	public void show() {
		updateLocation();
		window.setVisible(true);
	}

	public boolean isVisible() {
		return window.isVisible();
	}

	public void updateLocation() {
		window.setLocation((int) Math.round(MouseInfo.getPointerInfo().getLocation().getX()),
						   (int) Math.round(MouseInfo.getPointerInfo().getLocation().getY()));

		JSObject dimObj = (JSObject) webengine.executeScript("getDimensions()");
		int width = (Integer) dimObj.getMember("width");
		int height = (Integer) dimObj.getMember("height");
		window.setSize(width, height);
	}

	class DocListener implements ChangeListener<Document>{
         @Override
         public void changed(ObservableValue<? extends Document> observable, Document oldValue, Document newValue) {
             try {

                 // Use reflection to retrieve the WebEngine's private 'page' field.
                 Field f = webengine.getClass().getDeclaredField("page");
                 f.setAccessible(true);
                 WebPage page = (WebPage) f.get(webengine);
                 page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB());

             } catch (Exception e) {
             }

         }
     }


}
