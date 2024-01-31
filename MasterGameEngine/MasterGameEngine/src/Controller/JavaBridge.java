package Controller;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.stage.StageStyle;
import util.DBCon;


public class JavaBridge {
    private String errorMessage = "";
    static public String usernamex;
    static public String usernamey;
    private AtomicReference<String> usernameRef = new AtomicReference<>();
    private GameEngineController gameEngineController1;
    private DBCon dbCon;
    private WebView webView; // Declare as a class-level field
    public JavaBridge() {
        // Initialization code if needed
    }
    public JavaBridge(WebView webView) {
        this.webView = webView;
    }

    public void performLogino(String username, String password) {
        System.out.println("Java: performLogin method called with username: " + username + ", password: " + password);

        // Check if the login is for the admin
        if ("admin".equalsIgnoreCase(username) && "admin".equals(password)) {
            Platform.runLater(() -> {
                // Get the current stage
                Stage stage = (Stage) webView.getScene().getWindow();

                // Close the current stage (login page)
                stage.close();

                // Open the new stage (LayoutView.fxml)
                openLayoutViewStage();
            });
        } else {
            // Validate login credentials using the database connection
            if (dbCon.validateCredentials(username, password)) {
                this.usernamex = username; // Store the username
                usernameRef.set(username);

                // Login successful for a regular user
                System.out.println("Java: Login successful. Stored username: " + this.usernamex);
                System.out.println("Java: Login successful");

                // You can update JavaFX UI or perform any other necessary actions
                // For simplicity, let's print a message for now
                Platform.runLater(() -> {
                    // Get the current stage
                    Stage stage = (Stage) webView.getScene().getWindow();

                    // Close the current stage (login page)
                    stage.close();

                    // Open the new stage (GameEngine.fxml)
                    openGameEngineStage(username);
                });
            } else {
                // Login failed
                errorMessage = "Invalid username or password"; // Set error message
                System.out.println("Java: " + errorMessage);

                // You can update JavaFX UI or perform any other necessary actions
                // For simplicity, let's print a message for now
                Platform.runLater(() -> {
                    // Call a JavaScript function to update the UI with the error message
                    webView.getEngine().executeScript("showErrorMessage('" + errorMessage + "')");
                });
            }
        }
    }

    // Method to open the LayoutView stage
    private void openLayoutViewStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/View/LayoutView.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage layoutViewStage = new Stage();
            layoutViewStage.initStyle(StageStyle.UNDECORATED);
            layoutViewStage.setTitle("Layout View");
            layoutViewStage.setScene(new Scene(root));

            // Show the new stage
            layoutViewStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or log the error message
        }
    }

    private void openGameEngineStage(String username) {
        // Implement code to open the GameEngine.fxml stage
        // For simplicity, let's print a message for now
        System.out.println("Move to GameEngine.fxml");
        // You may want to load a new FXML file and set up a new scene
        // Example: FXMLLoader.load(getClass().getResource("GameEngine.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/GameEngine.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/View/LayoutView.fxml"));


        try {
            // Load the FXML file and create a new stage
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Game Engine");
            stage.setScene(scene);

            // Access the controller of GameEngine.fxml to set the username
            GameEngineController gameEngineController = loader.getController();
            gameEngineController.setUsername(username);


            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleContinueButtonClick() {
        // Access the stored username and use it in the handleContinueButtonClick method
        String storedUsername = usernamey;

        Platform.runLater(() -> {
            // Load another FXML page here
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/GameEngine.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) webView.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);

                // Access the GameEngineController instance and set the username
                GameEngineController gameEngineController = loader.getController();
                gameEngineController.setUsername(storedUsername);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }




    public void performRegistration(String username, String email, String password) {
        System.out.println("op");
        // Insert user data into the database
        if (insertUserData(username, email, password)) {
            System.out.println("Java: Registration successful");

            Platform.runLater(() -> {
                // Call a JavaScript function to update the UI with a success message
                webView.getEngine().executeScript("showSuccessMessage('Registration successful!')");
            });

            // ... (existing code for success)
        } else {
            // Database insertion failed
            errorMessage = "Failed to register user"; // Set error message
            System.out.println("Java: " + errorMessage);

            Platform.runLater(() -> {
                // Call a JavaScript function to update the UI with the error message
                webView.getEngine().executeScript("showErrorMessage('" + errorMessage + "')");
            });
        }
    }

    private boolean insertUserData(String username, String email, String password) {
        try {
            // Execute an SQL INSERT query to add user data into the database
            String insertQuery = "INSERT INTO users (username, Email, password) VALUES (?, ?, ?)";
            dbCon.executeUpdateQuery(insertQuery, username, email, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // ADDED
    public void setUsername(String username) {
//        usernameLabel.setText(user);

        this.usernamey = username;
//        updateUsernameLabel();
    }
    public String getUsername(){
        return usernamey;
    }

//    private void updateUsernameLabel() {
//        // Update the usernameLabel with the username
//        usernameLabel.setText(username);
//    }
}
