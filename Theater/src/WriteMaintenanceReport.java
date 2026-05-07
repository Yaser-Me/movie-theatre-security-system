import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class WriteMaintenanceReport {
    private User currentUser;
    private Stage stage;

    public WriteMaintenanceReport(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void show() {
        // title
        Label title = new Label("Write Maintenance Report");
        title.setFont(new Font("Times New Roman", 24));

        // equipment
        Button equipmentBtn = new Button("Equipment");
        equipmentBtn.setPrefWidth(200);
        equipmentBtn.setDisable(true);
        TextField equipmentTx = new TextField();
        HBox equipmentH = new HBox(10, equipmentBtn, equipmentTx);

        // report details
        Button detailsBtn = new Button("Report Details");
        detailsBtn.setPrefWidth(200);
        detailsBtn.setDisable(true);
        TextArea detailsTx = new TextArea();
        detailsTx.setPrefSize(400, 120);
        detailsTx.setWrapText(true);
        HBox detailsH = new HBox(10, detailsBtn, detailsTx);

        Button submitBtn = new Button("Submit Report");
        submitBtn.setPrefWidth(200);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        HBox buttonsH = new HBox(10, submitBtn, backBtn, logoutBtn);

        submitBtn.setOnAction(e -> {
            String equipment = equipmentTx.getText().trim();
            String details = detailsTx.getText().trim();

            // Equipment Checks
            if (equipment.isEmpty( )) {
                showAlert("Error", "Equipment field is empty");
                return;
            }else if(!InputValidator.validateMaintenanceReport(equipment)){
                showAlert("Error", "Report Details are Not Valid");
                return;
            }

            // Report Details Checks
            if (details.isEmpty( )) {
                showAlert("Error", "Report Details field is empty");
                return;
            }else if(!InputValidator.validateMaintenanceReport(details)){
                showAlert("Error", "Report Details are Not Valid");
                return;
            }else if (details.length()< 10){
                showAlert("Error", "Report Detail Is too Short");
                return;
            }

            Connection con = null;
            PreparedStatement stmt = null;
            try {
                con = DBUtils.establishConnection();

                String query = "INSERT INTO maintenancereport (equipment, description) VALUES (?, ?)";
                stmt = con.prepareStatement(query);
                stmt.setString(1, equipment);
                stmt.setString(2, details);


                stmt.executeUpdate();

                showSuccess("Success", "Report Submitted successfully");

                String actionText = equipment + ": " + details;
                ActionsRecord.recordAction(currentUser.getUsername(), "Wrote a Maintenance report about: " + equipment);

                DBUtils.closeConnection(con, stmt);

                // going to main screen after adding
                ManagerView managerView = new ManagerView(stage, currentUser);
                managerView.initializeComponents();

            } catch (Exception ex) {
                System.out.println("Insert error:" + ex.getMessage());
                try {
                    DBUtils.closeConnection(con, stmt);
                } catch (Exception ignored) {}
            }

            MaintenanceView maintenanceView = new MaintenanceView(stage, currentUser);
            maintenanceView.show();
        });

        backBtn.setOnAction(e -> {
            MaintenanceView maintenanceView = new MaintenanceView(stage, currentUser);
            maintenanceView.show();
        });

        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(title, equipmentH, detailsH, buttonsH);


        Scene scene = new Scene(layout, 700, 600);

        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        // learned of a new alert type ^^
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}