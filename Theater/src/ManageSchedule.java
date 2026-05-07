import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ManageSchedule {
    private User currentUser;
    private Stage stage;

    public ManageSchedule(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Manage Show Schedule");
        title.setFont(new Font("Times New Roman",24));

        // addShow button
        Button addShowBtn = new Button("Add a new Showtime");
        addShowBtn.setPrefWidth(200);

        // deleteShow button
        Button deleteShowBtn = new Button("Delete a Showtime");
        deleteShowBtn.setPrefWidth(200);
        // deleteShow textField
        TextField deleteShowNumber = new TextField();
        deleteShowNumber.setPromptText("ShowTime ID");
        // deleteShow Hbox
        HBox deleteShowBox = new HBox(10, deleteShowBtn, deleteShowNumber);


        Button showAllShowsBtn = new Button("Show All Showtimes");
        showAllShowsBtn.setPrefWidth(200);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        // Actions *****
        addShowBtn.setOnAction(e -> {
            AddShowtime addShowtime = new AddShowtime(stage,currentUser);
            addShowtime.show();
        });

        deleteShowBtn.setOnAction(e -> {
            // to be removed
            System.out.println("Delete Showtime");
            String showNumber = deleteShowNumber.getText();

            if (showNumber.isEmpty()) {
                showAlert("Error", "Please enter Showtime Number to delete");
                return;
            }// simple check to make sure the showNumber is actually a number, no need to create a whole InputValidator function
            else if (!showNumber.matches("\\d+")) {
                showAlert("Error", "Showtime ID must be a number");
                return;
            }


            if (!MovieCheck.showTimeExists(Integer.parseInt(showNumber))) {
                showAlert("Error", "Showtime does not exist");
            }else if (MovieCheck.hasTicket(Integer.parseInt(showNumber))){
                showAlert("Error", "Can't delete showtime, tickets already exist");
            }else {

                Connection con = null;
                PreparedStatement stmt = null;
                try {
                    con = DBUtils.establishConnection();
                    String query = "DELETE FROM showtime WHERE showtimeId = ?";
                    stmt = con.prepareStatement(query);
                    stmt.setInt(1, Integer.parseInt(showNumber));


                    stmt.executeUpdate();

                    DBUtils.closeConnection(con, stmt);
                    showSuccess("Success", "Showtime Deleted successfully");
                    ActionsRecord.recordAction(currentUser.getUsername(),"Showtime Has Been Deleted: " + showNumber );

                } catch (Exception ex) {
                    System.out.println("Insert error:" + ex.getMessage());
                    try {
                        DBUtils.closeConnection(con, stmt);
                    } catch (Exception ignored) {
                    }
                }
            }
        });


        // showAllShowsBtn --> goes to ShowAllShowtimes
        showAllShowsBtn.setOnAction(e -> {
            ShowAllShowtimes showAllShowtimes = new ShowAllShowtimes(stage,currentUser);
            showAllShowtimes.initializeComponents();
        });

        // back --> return to ManagerView
        backBtn.setOnAction(e -> {

            ManagerView managerView = new ManagerView(stage,currentUser);
            managerView.initializeComponents();
        });
        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox ManagerLayout = new VBox(15);
        ManagerLayout.setPadding(new Insets(10));
        ManagerLayout.getChildren().addAll(title,addShowBtn,deleteShowBox,showAllShowsBtn,backBtn,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(ManagerLayout, 600, 600);
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
