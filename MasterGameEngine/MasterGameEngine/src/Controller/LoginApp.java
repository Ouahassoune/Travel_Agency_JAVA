package Controller;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        // Wait for the page to finish loading
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Page has finished loading, you can perform additional tasks here if needed
                System.out.println("Page loaded successfully");

                // Get the JavaScript object
                JSObject jsObject = (JSObject) webEngine.executeScript("window");

                // Expose Java object to JavaScript
                jsObject.setMember("javafx", new JavaBridge(webView));
                System.out.println("Java: JavaScript object exposed to JavaBridge");

                // You can now call Java methods from JavaScript using 'javafx.methodName()'
            }
        });

        // Load the HTML content
        webEngine.load(getClass().getResource("/Controller/html/index.html").toExternalForm());

        // Apply optimizations
        webView.setCache(true);

        // Create and show the scene
        Scene scene = new Scene(webView, 1180, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
