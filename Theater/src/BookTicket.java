import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookTicket {
    private Stage   stage;
    private User    currentUser;
    private int     currentShowtimeNumber;
    private int     currentSeatNumber;

    public BookTicket(Stage stage, User currentUser, int currentShowtimeNumber, int currentSeatNumber) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.currentShowtimeNumber = currentShowtimeNumber;
        this.currentSeatNumber = currentSeatNumber;
    }


    public void show() {

        // Title
        Label title = new Label("Book a Ticket with Payment");
        title.setFont(new Font("Times New Roman",24));

        // showtime ID
        Button showTimeIdBtn = new Button("Showtime ID");
        showTimeIdBtn.setPrefWidth(200);
        showTimeIdBtn.setDisable(true);
        TextField showTimeIdTx = new TextField(Integer.toString(currentShowtimeNumber));
        showTimeIdTx.setEditable(false);
        showTimeIdTx.setPrefWidth(200);
        HBox showTimeIdH = new HBox(10, showTimeIdBtn, showTimeIdTx);

        // Seat Number
        Button seatNumberBtn = new Button("Seat Number");
        seatNumberBtn.setPrefWidth(200);
        seatNumberBtn.setDisable(true);
        TextField seatNumberTx = new TextField(Integer.toString(currentSeatNumber));
        seatNumberTx.setEditable(false);
        seatNumberTx.setPrefWidth(200);
        HBox seatNumberH = new HBox(10, seatNumberBtn, seatNumberTx);

        // Price
        // for this demo the price is fixed to 80
        Button priceBtn = new Button("Ticket Price");
        priceBtn.setPrefWidth(200);
        priceBtn.setDisable(true);
        TextField priceBtnTx = new TextField(Integer.toString(80));
        priceBtnTx.setEditable(false);
        priceBtnTx.setPrefWidth(200);
        HBox priceH = new HBox(10, priceBtn, priceBtnTx);

        Button methodBtn = new Button("Payment Method");
        methodBtn.setPrefWidth(200);
        methodBtn.setDisable(true);

        ComboBox <String> methodCb = new ComboBox<>();
        methodCb.getItems().addAll("Cash","Credit Card");
        methodCb.setPromptText("Select Payment Method");
        methodCb.setPrefWidth(200);
        HBox methodH = new HBox(10, methodBtn, methodCb);



        Button bookTicketBtn = new Button("Book The Ticket");
        bookTicketBtn.setPrefWidth(200);
        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button homeBtn = new Button("Home");
        homeBtn.setPrefWidth(200);
        HBox bookBackHomeH = new HBox(10, bookTicketBtn, backBtn,homeBtn);


        bookTicketBtn.setOnAction(e -> {
            int showtimeNumber = currentShowtimeNumber;
            int seatNumber = currentSeatNumber;
            int price = Integer.parseInt(priceBtnTx.getText().trim());
            String paymentMethod = methodCb.getValue();

            if (paymentMethod == null || paymentMethod.isEmpty()) {
                showAlert("Error", "Please enter payment method");
                return;
            }

            Connection con = null;
            PreparedStatement ticketStmt = null;
            PreparedStatement paymentStmt = null;
            ResultSet generatedKeys = null;

            try {
                con = DBUtils.establishConnection();

                // Start transaction
                con.setAutoCommit(false);

                // 1st: Insert ticket
                String ticketQuery = "INSERT INTO ticket (showtimeId, seat, price) VALUES (?, ?, ?)";
                ticketStmt = con.prepareStatement(ticketQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                ticketStmt.setInt(1, showtimeNumber);
                ticketStmt.setInt(2, seatNumber);
                ticketStmt.setInt(3, price);

                int ticketRows = ticketStmt.executeUpdate();

                if (ticketRows == 0) {
                    throw new Exception("Ticket insert failed.");
                }

                // Get the newly created ticketId
                generatedKeys = ticketStmt.getGeneratedKeys();
                int ticketNewId = -1;

                if (generatedKeys.next()) {
                    ticketNewId = generatedKeys.getInt(1);
                } else {
                    throw new Exception("Could not retrieve generated ticket ID.");
                }

                // 2nd: Insert payment
                String paymentQuery = "INSERT INTO payment (ticketId, amount, method) VALUES (?, ?, ?)";
                paymentStmt = con.prepareStatement(paymentQuery);
                paymentStmt.setInt(1, ticketNewId);
                paymentStmt.setInt(2, price);
                paymentStmt.setString(3, paymentMethod);

                int paymentRows = paymentStmt.executeUpdate();

                if (paymentRows == 0) {
                    throw new Exception("Payment insert failed.");
                }

                // 3rd: If both succeeded, save permanently
                con.commit();

                showSuccess("Success", "Ticket booked and payment saved successfully");
                ActionsRecord.recordAction(currentUser.getUsername(),"a Ticket has been added: " + ticketNewId + " for Showtime Number: " + showtimeNumber);


                StaffView staffView = new StaffView(stage, currentUser);
                staffView.show();

            } catch (Exception ex) {
                try {
                    if (con != null) {
                        con.rollback();
                    }
                } catch (Exception rollbackEx) {
                    System.out.println("Rollback error: " + rollbackEx.getMessage());
                }

                System.out.println("Transaction error: " + ex.getMessage());
                showAlert("Error", "Booking failed. Nothing was saved.");


            } finally {
                try {
                    if (generatedKeys != null) generatedKeys.close();
                    if (ticketStmt != null) ticketStmt.close();
                    if (paymentStmt != null) paymentStmt.close();

                    if (con != null) {
                        con.setAutoCommit(true); // restore default
                        con.close();
                    }
                } catch (Exception ignored) {}
            }
        });


        backBtn.setOnAction(e -> {
            ShowAllShowtimes showAllShowtimes = new ShowAllShowtimes(stage,currentUser);
            showAllShowtimes.initializeComponents();
        });

        homeBtn.setOnAction(e -> {
            StaffView staffView = new StaffView(stage,currentUser);
            staffView.show();
        });

        // Layout
        VBox updateMovieLayout = new VBox(15);
        updateMovieLayout.setPadding(new Insets(10));
        updateMovieLayout.getChildren().addAll(title,showTimeIdH,seatNumberH,priceH,methodH,bookBackHomeH);
        // Scene then stage
        Scene scene = new Scene(updateMovieLayout, 730, 600);
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
