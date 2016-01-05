package application;
	
import java.lang.reflect.Field;

import javafx.application.Platform;
import org.w3c.dom.Document;

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


public class Input extends Application implements Runnable {
	
	private Stage primaryStage;
	private Stage subStage;
	private BorderPane rootLayout;
	private WebEngine webengine;


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
			this.webengine = web.getEngine();
            webengine.documentProperty().addListener(new DocListener());
//            webengine.loadContent("<body>Test Transparent</body>");

			String url = getClass().getResource("/html/index.html").toExternalForm();
			web.getEngine().load(url);
			
			
			
			this.subStage = new Stage();
			subStage.initStyle(StageStyle.TRANSPARENT);
			subStage.initModality(Modality.WINDOW_MODAL);
			subStage.setTitle("SubStage");
			subStage.initOwner(this.primaryStage);
			subStage.setScene(scene);
			subStage.hide();
			
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
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		launch();
	}

	public void hide() {
		System.out.println("Substage: " + subStage);
		subStage.hide();
	}

	public void show() {
		subStage.show();
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
