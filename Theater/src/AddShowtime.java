import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddShowtime {
    private User currentUser;
    private Stage stage;

    public AddShowtime(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Add Showtime");
        title.setFont(new Font("Times New Roman",24));

        // movie name
        Button movieNameBtn = new Button("Movie Name");
        movieNameBtn.setPrefWidth(200);
        movieNameBtn.setDisable(true);
        TextField movieNameTx = new TextField();
        HBox movieNameH = new HBox(10, movieNameBtn, movieNameTx);

        // Room
        Button roomBtn = new Button("Room Number");
        roomBtn.setPrefWidth(200);
        roomBtn.setDisable(true);
        TextField roomTx = new TextField();
        HBox roomH = new HBox(10, roomBtn, roomTx);

        // date
        Button dateBtn = new Button("Showtime Date");
        dateBtn.setPrefWidth(200);
        dateBtn.setDisable(true);
        TextField dateTx = new TextField();
        HBox dateH = new HBox(10, dateBtn, dateTx);

        // Time Slot
        Button timeSlotBtn = new Button("Time Slot");
        timeSlotBtn.setPrefWidth(200);
        timeSlotBtn.setDisable(true);
        TextField timeSlotTx = new TextField();
        HBox timeSlotH = new HBox(10, timeSlotBtn, timeSlotTx);

        // timselot table
        TableView <String> showtimeTable = new TableView<>();

        TableColumn<String, String> slotColumn = new TableColumn<>("Time Slot");
        slotColumn.setCellValueFactory(cellData -> {
            int index = showtimeTable.getItems().indexOf(cellData.getValue());
            return new javafx.beans.property.SimpleStringProperty("   " + (index + 1));
        });

        TableColumn<String, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()));

        // fixed column widths
        slotColumn.setPrefWidth(100);
        timeColumn.setPrefWidth(220);

        showtimeTable.getColumns().addAll(slotColumn, timeColumn);

        ObservableList<String> data = FXCollections.observableArrayList(
                "08:00 AM to 10:00 AM",
                    "10:00 AM to 12:00 PM",
                    "12:00 PM to 02:00 PM",
                    "02:00 PM to 04:00 PM",
                    "04:00 PM to 06:00 PM",
                    "06:00 PM to 08:00 PM",
                    "08:00 PM to 10:00 PM"
        );
        showtimeTable.setItems(data);

        // fixed table size based on content
        showtimeTable.setPrefWidth(320);
        showtimeTable.setMinWidth(320);
        showtimeTable.setMaxWidth(320);

        // height
        showtimeTable.setPrefHeight(205);
        showtimeTable.setMinHeight(205);
        showtimeTable.setMaxHeight(205);

        // new thing i just learned, this code removes the scroll bar which removes and resizing ... nice
        showtimeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        VBox timeSlotV = new VBox(20,movieNameH,roomH,dateH,timeSlotH);
        timeSlotV.setPrefWidth(420);

        HBox timeSlotHbox = new HBox(20,timeSlotV,showtimeTable);
        timeSlotHbox.setPadding(new Insets(10,0,10,0));

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button addBtn = new Button("Add Showtime");
        addBtn.setPrefWidth(200);
        Button homeBtn = new Button("Home");
        homeBtn.setPrefWidth(200);
        HBox addBackHomeH = new HBox(10, addBtn, backBtn,homeBtn);

        addBtn.setOnAction(e -> {
            String movieName = movieNameTx.getText().trim();
            String roomNumber = roomTx.getText().trim();
            String showDate = dateTx.getText().trim();
            String timeSlot = timeSlotTx.getText().trim();

            // movie name empty or valid check
            if (movieName.isEmpty( )) {
                showAlert("Error", "Movie field is empty");
                return;
            }else if(!InputValidator.validateMovieName(movieName)){
                showAlert("Error", "Movie Name Is Not Valid");
                return;
            }

            // room number empty or valid check
            if (roomNumber.isEmpty( )) {
                showAlert("Error", "Room Number field is empty");
                return;
            }else if(!InputValidator.validateRoomNumber(roomNumber)){
                showAlert("Error", "Room Number Is Not Valid");
                return;
            }

            // showtime date empty or valid check
            if (showDate.isEmpty( )) {
                showAlert("Error", "Showtime field is empty");
                return;
            }else if(!InputValidator.validateShowtimeDate(showDate)){
                showAlert("Error", "Showtime date Is Not Valid, please follow this format YYYY-MM-DD");
                return;
            }

            // Time Slot empty or valid or check
            if (timeSlot.isEmpty( )) {
                showAlert("Error", "Time Slot field is empty");
                return;
            }else if(!InputValidator.validateTimeSlot(timeSlot)){
                showAlert("Error", "Time Slot Is Not Valid, should be 1-7");
                return;
            }



            if (!MovieCheck.movieExists(movieName)) {
                showAlert("Error", "Movie does not exist");
            } else if (!MovieCheck.roomExists(Integer.parseInt(roomNumber))) {
                showAlert("Error", "Room does not exist");
            } else if (MovieCheck.isTimeSlotTaken(roomNumber, showDate, timeSlot)) {
                showAlert("Error", "Showtime timeslot taken");
            } else {
                int movieId = MovieCheck.getMovieIdByName(movieName);

                if (movieId == -1) {
                    showAlert("Error", "Movie ID could not be found");
                    return;
                }

                Connection con = null;
                PreparedStatement stmt = null;

                try {
                    con = DBUtils.establishConnection();

                    String query = "INSERT INTO showtime (movieId, room, date, timeSlot) VALUES (?, ?, ?, ?)";
                    stmt = con.prepareStatement(query);

                    stmt.setInt(1, movieId);
                    stmt.setInt(2, Integer.parseInt(roomNumber));
                    stmt.setString(3, showDate);
                    stmt.setInt(4, Integer.parseInt(timeSlot));

                    stmt.executeUpdate();

                    showSuccess("Success", "Showtime added successfully");

                    DBUtils.closeConnection(con, stmt);
                    ActionsRecord.recordAction(currentUser.getUsername(),"Showtime Has Been Added for : " + movieName );


                    ManageSchedule manageSchedule = new ManageSchedule(stage, currentUser);
                    manageSchedule.show();

                } catch (Exception ex) {
                    System.out.println("Insert error: " + ex.getMessage());
                    try {
                        DBUtils.closeConnection(con, stmt);
                    } catch (Exception ignored) {}
                }
            }
        });


        backBtn.setOnAction(e -> {
            ManageSchedule manageSchedule = new ManageSchedule(stage,currentUser);
            manageSchedule.show();
        });

        homeBtn.setOnAction(e -> {
            ManagerView managerView = new ManagerView(stage,currentUser);
            managerView.initializeComponents();
        });

        // Layout
        VBox addShowtimeLayout = new VBox(15);
        addShowtimeLayout.setPadding(new Insets(10));
        addShowtimeLayout.getChildren().addAll(title,timeSlotHbox,addBackHomeH);

        // Scene then stage
        Scene scene = new Scene(addShowtimeLayout, 730, 600);
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
