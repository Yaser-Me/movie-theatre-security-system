import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ShowAllShowtimes {
    private User currentUser;
    private Stage stage;

    public ShowAllShowtimes(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        // Title
        Label title = new Label("Show All Showtimes");
        title.setFont(new Font("Times New Roman",24));

        // To display data in a table, use the JavaFX TableView
        TableView<ShowTime> table = new TableView<>();

        // Define the first column of the table, <ShowTime, Integer> means the data type
        TableColumn<ShowTime, Integer> idColumn = new TableColumn<>("ID");
        // PropertyValueFactory<>("id") will call the getId() method in the model class
        // which will fill the cell with the command id value for every row.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("showTimeId"));

        // Movie name instead of ID
        TableColumn<ShowTime, String> movieNameColumn = new TableColumn<>("Movie Name");


        // Room
        TableColumn<ShowTime, Integer> roomIdColumn = new TableColumn<>("Room");
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        // Showtime Date
        TableColumn<ShowTime, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("showDate"));

        // showtime ... time
        TableColumn<ShowTime, Integer> timeSlotColumn = new TableColumn<>("Time Slot");
        timeSlotColumn.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        timeSlotColumn.setPrefWidth(120);

        // Add all columns to the table
        table.getColumns().addAll(idColumn, movieNameColumn, roomIdColumn, dateColumn, timeSlotColumn);

        // We will use Observable List to hold the data retrieved from the database
        ObservableList<ShowTime> ShowTimeList = FXCollections.observableArrayList();

        // this is not my code, i had a hard time using the movieId from Showtime class to get the movie name from the movies database
        // at first my ShowAllShowtimes showed the movieId which is practical but isn't user friendly, and if the movie name changes, will cause problems
        // so i asked chatgpt to solve this particular problem, and they suggest using Map/HashMap

        // This map will connect movieId -> movieName
        Map<Integer, String> movieNameMap = new HashMap<>();

        // Retrieve data from DB and fill up the table
        try{
            Connection con = DBUtils.establishConnection();
            //----
            String query = """
                    SELECT showtime.showtimeId, showtime.movieId, movies.name AS movieName,showtime.room, showtime.date, showtime.timeSlot
                    FROM showtime
                    JOIN movies ON showtime.movieId = movies.movieId
                    """;
            //----
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            
            while (rs.next()) {
                int movieId = rs.getInt("movieId");
                String movieName = rs.getString("movieName");

                // store movieId -> movieName
                movieNameMap.put(movieId, movieName);

                ShowTime showTime = new ShowTime(
                        rs.getInt("showtimeId"),
                        movieId,
                        rs.getInt("room"),
                        rs.getString("date"),
                        rs.getInt("timeSlot")
                );
                ShowTimeList.add(showTime);
            }
            rs.close();
            DBUtils.closeConnection(con, stmt);
        }catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }

        // Tell JavaFX how to display movieName using movieId
        movieNameColumn.setCellValueFactory(cellData -> {
            int movieId = cellData.getValue().getMovieId();
            String movieName = movieNameMap.get(movieId);
            return new SimpleStringProperty(movieName);
        });


        //Set the table to watch the observable list
        //the table will read data from it, and will also update upon any change
        table.setItems(ShowTimeList);



        // timslot table
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

        HBox timeSlotHbox = new HBox(10,table,showtimeTable);


        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> {
            if (AuthorizationService.isManager(currentUser)){
                ManageSchedule manageSchedule = new ManageSchedule(stage,currentUser);
                manageSchedule.show();
            }else if (AuthorizationService.isStaff(currentUser)){
                // need work here
                StaffView staffView = new StaffView(stage,currentUser);
                staffView.show();
            }
        });

        Button homeBtn = new Button("Home");
        homeBtn.setPrefWidth(200);
        homeBtn.setOnAction(e -> {
            if (AuthorizationService.isManager(currentUser)) {
                ManagerView managerView = new ManagerView(stage, currentUser);
                managerView.initializeComponents();
            } else if (AuthorizationService.isStaff(currentUser)) {
                StaffView staffView = new StaffView(stage, currentUser);
                staffView.show();
            }
        });

        // to logout
        Button logoutBtn = new Button("logout");
        logoutBtn.setPrefWidth(200);
        logoutBtn.setOnAction(e -> {
            UserLogin userLogin = new UserLogin(stage);
            userLogin.initializeComponents();
        });

        HBox logoutBackBtns = new HBox(10,backBtn,homeBtn,logoutBtn);

        if (AuthorizationService.isManager(currentUser)){
            // Create the layout (VBox that contains the table)
            VBox vbox = new VBox(title,timeSlotHbox,logoutBackBtns);
            // Add the layout to the scene
            Scene scene = new Scene(vbox, 800, 600);

            //Add the scene to stage
            stage.setScene(scene);
            stage.setTitle("Movie Theatre Management System");
            stage.show();

        }else if (AuthorizationService.isStaff(currentUser)){
            // show time buttons
            Button showtimeBtn = new Button("Showtime Number");
            showtimeBtn.setPrefWidth(200);
            showtimeBtn.setDisable(true);
            TextField showtimeTx = new TextField();
            showtimeTx.setPrefWidth(200);
            HBox showtimeH = new HBox(10, showtimeBtn, showtimeTx);

            // seat number buttons
            Button seatNumberBtn = new Button("Seat Number");
            seatNumberBtn.setPrefWidth(200);
            seatNumberBtn.setDisable(true);
            TextField seatNumberTx = new TextField();
            seatNumberTx.setPrefWidth(200);
            HBox seatNumberH = new HBox(10, seatNumberBtn, seatNumberTx);

            Button bookTicketBtn = new Button("Book a Ticket");
            bookTicketBtn.setPrefWidth(410);


            // tried FileInputStream but its full of crashes,
            // looked around and it seems getClass().getResourceAsStream is alot afer
            ImageView imageView = new ImageView();
            try {
                var stream = getClass().getResourceAsStream("/Seats.png");

                if (stream != null) {
                    Image image = new Image(stream);
                    imageView.setImage(image);
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(410);
                } else {
                    System.out.println("Image not found, continuing without image.");
                }

            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            }


            bookTicketBtn.setOnAction(e -> {
                String showtimeNumber = showtimeTx.getText().trim();
                String seatNumber = seatNumberTx.getText().trim();

                // showtime number empty or num check
                if (showtimeNumber.isEmpty( )) {
                    showAlert("Error", "Showtime Number field is empty");
                    return;
                // simple check to make sure the showtimeNumber is actually a number, no need to create a whole InputValidator function
                }else if(!showtimeNumber.matches("\\d+")) {
                    showAlert("Error", "Showtime ID must be a number");
                    return;
                }

                // seat number number empty or num check
                if (seatNumber.isEmpty( )) {
                    showAlert("Error", "Seat Number field is empty");
                    return;
                    // simple check to make sure the seat number is 1-10
                }else if(!seatNumber.matches("^(10|[1-9])$")) {
                    showAlert("Error", "Seat number must be a number from 1-10");
                    return;
                }

                Boolean seatTaken = true;

                if (!MovieCheck.showTimeExists(Integer.parseInt(showtimeNumber))) {
                    showAlert("Error", "Showtime Does Not Exist, please pick on of the above");
                    return;
                }else if (MovieCheck.isSeatTaken(Integer.parseInt(showtimeNumber) ,Integer.parseInt(seatNumber))){
                    showAlert("Error", "Seat Already Taken");
                    return;
                }

                BookTicket bookTicket = new BookTicket(stage,currentUser,Integer.parseInt(showtimeNumber),Integer.parseInt(seatNumber));
                bookTicket.show();


            });

            // new title for staff
            title.setText("Book a Ticket");
            // Create the layout (VBox that contains the table)
            VBox vbox = new VBox(5,title,timeSlotHbox,imageView,showtimeH,seatNumberH,bookTicketBtn,logoutBackBtns);
            // Add the layout to the scene
            Scene scene = new Scene(vbox, 800, 800);

            //Add the scene to stage
            stage.setScene(scene);
            stage.setTitle("Movie Theatre Management System");
            stage.show();
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