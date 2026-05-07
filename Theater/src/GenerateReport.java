import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GenerateReport {
    private User currentUser;
    private Stage stage;

    public GenerateReport(Stage primaryStage, User user ) {
        this.stage = primaryStage;

        this.currentUser = user;
    }

    public void show() {
        // title
        Label title = new Label("Generate Report");
        title.setFont(new Font("Times New Roman",24));

        Button moviePopularityBtn = new Button("Movie Popularity report ");
        moviePopularityBtn.setPrefWidth(200);

        Button peakHoursBtn = new Button("Peak Hours Report");
        peakHoursBtn.setPrefWidth(200);

        Button showAllActionsBtn = new Button("Show All Actions");
        showAllActionsBtn.setPrefWidth(200);

        Button salesSummaryBtn = new Button("Sales Summary Report");
        salesSummaryBtn.setPrefWidth(200);


        Button maintenanceReportBtn = new Button("Maintenance Report");
        maintenanceReportBtn.setPrefWidth(200);


        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        moviePopularityBtn.setOnAction(e -> {
            MoviePopularityReport moviePopularityReport = new MoviePopularityReport(stage,currentUser);
            moviePopularityReport.show();
        });

        peakHoursBtn.setOnAction(e -> {
            PeakHoursReport peakHoursReport = new PeakHoursReport(stage,currentUser);
            peakHoursReport.show();
        });

        showAllActionsBtn.setOnAction(e -> {
        ShowAllActions showAllActions = new ShowAllActions(stage,currentUser);
        showAllActions.initializeComponents();
        });

        salesSummaryBtn.setOnAction(e -> {
            SalesSummaryReport salesSummaryReport = new SalesSummaryReport(stage, currentUser);
            salesSummaryReport.show();
        });



        maintenanceReportBtn.setOnAction(e -> {
            ShowAllMaintenanceReports showAllMaintenanceReports = new ShowAllMaintenanceReports(stage,currentUser);
            showAllMaintenanceReports.initializeComponents();
        });






        backBtn.setOnAction(e -> {
            ManagerView managerView = new ManagerView(stage,currentUser);
            managerView.initializeComponents();
        });

        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        VBox layout = new VBox(15);

        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(title, moviePopularityBtn, peakHoursBtn, showAllActionsBtn, salesSummaryBtn, maintenanceReportBtn, backBtn, logoutBtn);


        Scene scene = new Scene(layout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }
}