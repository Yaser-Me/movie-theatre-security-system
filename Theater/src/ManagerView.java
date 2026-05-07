import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;

public class ManagerView {
    private User currentUser;
    private Stage stage;

    public ManagerView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void initializeComponents() {
        // Title
        Label title = new Label("Welcome Manager");
        title.setFont(new Font("Times New Roman",24));

        // Buttons with matching width to make it look cool and organized
        Button manageMoviesBtn = new Button("Manage Movies");
        manageMoviesBtn.setPrefWidth(200);

        Button manageScheduleBtn = new Button("Manage Show Schedule");
        manageScheduleBtn.setPrefWidth(200);

        Button showAllTicketsBtn = new Button("Show All tickets");
        showAllTicketsBtn.setPrefWidth(200);

        Button generateReportBtn = new Button("Generate Report");
        generateReportBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        // Actions *****
        manageMoviesBtn.setOnAction(e -> {
            ManageMovie manageMovie = new ManageMovie(stage,currentUser);
            manageMovie.show();
        });

        manageScheduleBtn.setOnAction(e -> {
            ManageSchedule manageSchedule = new ManageSchedule(stage,currentUser);
            manageSchedule.show();


        });

        showAllTicketsBtn.setOnAction(e -> {
            ShowAllTickets showAllTickets = new ShowAllTickets(stage,currentUser);
            showAllTickets.initializeComponents();
        });

        generateReportBtn.setOnAction(e -> {
            GenerateReport generateReport =new GenerateReport(stage,currentUser);
            generateReport.show();
        });

        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox ManagerLayout = new VBox(15);
        ManagerLayout.setPadding(new Insets(10));
        ManagerLayout.getChildren().addAll(title,manageMoviesBtn,manageScheduleBtn,showAllTicketsBtn,generateReportBtn,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(ManagerLayout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }

}
