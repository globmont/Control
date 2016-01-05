package application;

import com.sun.webkit.WebPage;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Document;

import java.awt.*;
import java.lang.reflect.Field;


public class Input extends Application implements Runnable {
	
	private static Stage primaryStage;
	private static Stage subStage;
	private BorderPane rootLayout;
	private static WebEngine webengine;


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.initStyle(StageStyle.UTILITY);
		this.primaryStage.setMaxHeight(0);
		this.primaryStage.setMaxWidth(0);
		this.primaryStage.setX(Double.MAX_VALUE);

		
		initRootLayout();
	}
	
	public void initRootLayout() {
		try {
			primaryStage.show();
			
			rootLayout = (BorderPane) FXMLLoader.load(getClass().getResource("Layout.fxml"));
			
			Scene scene = new Scene(rootLayout,630,150);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("flatterfx.css").toExternalForm());
			
			WebView web = (WebView) scene.lookup("#main");
			web.setContextMenuEnabled(false);
			this.webengine = web.getEngine();
            webengine.documentProperty().addListener(new DocListener());

			String url = getClass().getResource("/html/index.html").toExternalForm();
			web.getEngine().load(url);


			this.subStage = new Stage();
			subStage.initStyle(StageStyle.TRANSPARENT);
			subStage.initModality(Modality.APPLICATION_MODAL);
			subStage.setTitle("SubStage");
			subStage.initOwner(this.primaryStage);
			subStage.setScene(scene);
			subStage.show();
			hide();

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public void moveLeft() {
		webengine.executeScript("moveLeft()");
	}

	public void moveRight() {
		webengine.executeScript("moveRight()");
	}

	public String getLetter() {
		return (String) webengine.executeScript("getSelectedLetter()");
	}

	public void setShift(boolean value) {
		webengine.executeScript("setShift(" + value + ")");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		launch();
	}

	public void hide() {
		/*System.out.println("Hide Substage: " + subStage);
		System.out.println("Hide Primary Stage: " + primaryStage);
		System.out.println("Hide Platform FX Thread: " + Platform.isFxApplicationThread());*/
		subStage.hide();

	}

	public void show() {
		/*System.out.println("Show Substage: " + subStage);
		System.out.println("Show Primary Stage: " + primaryStage);
		System.out.println("Show Platform FX Thread: " + Platform.isFxApplicationThread());*/
		updateLocation();
		subStage.show();
	}

	public void updateLocation() {
		subStage.setX(MouseInfo.getPointerInfo().getLocation().getX());
		subStage.setY(MouseInfo.getPointerInfo().getLocation().getY());
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
