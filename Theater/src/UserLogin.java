import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class UserLogin {
    private Scene loginScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Stage stage;

    public UserLogin(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));
        // title
        Label title = new Label("User Login");
        title.setFont(new Font("Times New Roman",24));

        // added for user clarification and because i learned a new thing and i wanted to try it...
        usernameField.setPromptText("Enter Username");
        passwordField.setPromptText("Enter Password");

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(200);
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                validateLogin();
            }
        });

        // close button, another new thing
        Button closeButton = new Button("Close");
        closeButton.setPrefWidth(200);
        closeButton.setOnAction(e -> {
            stage.close();
        });

        HBox buttonsBox = new HBox(10, loginButton, closeButton);

        loginLayout.getChildren().addAll(title,new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                buttonsBox);

        loginScene = new Scene(loginLayout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(loginScene);
        stage.show();
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // special alert in case username or password are empty, just for clarification for user
        // also good to check here instead of sending empty fields to the Database
        if(username.isEmpty() || password.isEmpty()){
            showAlert("ERROR", "Fields cannot be empty");
            return;
        }

        // 1) check if account is locked
        if (AuthorizationService.isAccountLocked(username)) {
            showAlert("Account Locked", "This account is locked after 3 failed login attempts.");
            return;
        }

        // Authenticate the user using the authentication service module
        User loggedInUser = AuthenticationService.authenticate(username, password);
        if(loggedInUser != null){
            // reset failed attempts if both username and password are correct
            AuthorizationService.resetFailedAttempts(username);
            // Authorization, grant the logged-in user access based on the role
            if (AuthorizationService.isManager(loggedInUser)){
                ManagerView managerView = new ManagerView(stage,loggedInUser);
                managerView.initializeComponents();
            } else if (AuthorizationService.isStaff(loggedInUser)) {
                StaffView staffView = new StaffView(stage,loggedInUser);
                staffView.show();
            }else if (AuthorizationService.isMaintenance(loggedInUser)) {
                MaintenanceView maintenanceView = new MaintenanceView(stage, loggedInUser);
                maintenanceView.show();
            }
            }else{
            AuthorizationService.incrementFailedAttempts(username);
            // check if now locked
            if (AuthorizationService.isAccountLocked(username)) {
                showAlert("Account Locked", "Account locked after 3 failed login attempts.");
            } else {
                showAlert("Authentication Failed", "Invalid username or password.");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
