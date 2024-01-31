package Controller;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LoginApp2 extends Application {

    private WebView webView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        webView = initializeWebView("Controller/html/indexds.html");

        // Create and show the scene
        Scene scene = new Scene(webView, 1180, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public WebView initializeWebView(String htmlFilePath) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        // Wait for the page to finish loading
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Page has finished loading, you can perform additional tasks here if needed
                System.out.println("Page loaded successfully");
            }
        });

        try {
            // Load the HTML content
            webEngine.load(getClass().getClassLoader().getResource(htmlFilePath).toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
            // Handle loading exception here
        }

        // Apply optimizations
        webView.setCache(true);
        webEngine.setJavaScriptEnabled(true);

        return webView;
    }

    public WebView getWebView() {
        return webView;
    }
}
