import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MaintenanceView {
    private User currentUser;
    private Stage stage;

    public MaintenanceView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Welcome Maintenance Staff");
        title.setFont(new Font("Times New Roman",24));

        // Buttons with matching width to make it look cool and organized
        Button maintenanceReportBtn = new Button("Write a Maintenance Report");
        maintenanceReportBtn.setPrefWidth(200);

        Button showAllReportsBtn = new Button("Show All Maintenance Reports");
        showAllReportsBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        // Actions *****
        maintenanceReportBtn.setOnAction(e -> {
            WriteMaintenanceReport writeMaintenanceReport = new WriteMaintenanceReport(stage, currentUser);
            writeMaintenanceReport.show();
        });

        showAllReportsBtn.setOnAction(e -> {
            ShowAllMaintenanceReports showAllMaintenanceReports = new ShowAllMaintenanceReports(stage,currentUser);
            showAllMaintenanceReports.initializeComponents();

        });

        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox maintenanceLayout = new VBox(15);
        maintenanceLayout.setPadding(new Insets(10));
        maintenanceLayout.getChildren().addAll(title,maintenanceReportBtn,showAllReportsBtn,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(maintenanceLayout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }
}
