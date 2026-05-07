import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class ShowAllTickets {
    private User currentUser;
    private Stage stage;

    public ShowAllTickets(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        // Title
        Label title = new Label("Show All Tickets");
        title.setFont(new Font("Times New Roman",24));

        TableView<Ticket> table = new TableView<>();

        // Ticket ID
        TableColumn<Ticket, Integer> ticketIdColumn = new TableColumn<>("Ticket ID");
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticketId"));

        // Movie Name
        TableColumn<Ticket, String> movieNameColumn = new TableColumn<>("Movie Name");

        // Showtime ID
        TableColumn<Ticket, Integer> showtimeIdColumn = new TableColumn<>("Showtime ID");
        showtimeIdColumn.setCellValueFactory(new PropertyValueFactory<>("showtimeId"));

        // Showtime Date
        TableColumn<Ticket, String> showDateColumn = new TableColumn<>("Showtime Date");

        // Seat Number
        TableColumn<Ticket, Integer> seatColumn = new TableColumn<>("Seat Number");
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));

        // Price
        TableColumn<Ticket, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(ticketIdColumn,movieNameColumn,showtimeIdColumn,showDateColumn,seatColumn,priceColumn);

        ObservableList<Ticket> ticketList = FXCollections.observableArrayList();

        // Maps to show extra info without changing Ticket class
        Map<Integer, String> movieNameMap = new HashMap<>();
        Map<Integer, String> showDateMap = new HashMap<>();

        try {
            Connection con = DBUtils.establishConnection();

            String query = """
                    SELECT ticket.ticketId, ticket.showtimeId, ticket.seat, ticket.price,
                           movies.name AS movieName, showtime.date AS showDate
                    FROM ticket
                    JOIN showtime ON ticket.showtimeId = showtime.showtimeId
                    JOIN movies ON showtime.movieId = movies.movieId
                    """;

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int showtimeId = rs.getInt("showtimeId");

                // store extra display values
                movieNameMap.put(rs.getInt("ticketId"), rs.getString("movieName"));
                showDateMap.put(rs.getInt("ticketId"), rs.getString("showDate"));

                Ticket ticket = new Ticket(
                        rs.getInt("ticketId"),
                        showtimeId,
                        rs.getInt("seat"),
                        rs.getInt("price")
                );

                ticketList.add(ticket);
            }

            rs.close();
            DBUtils.closeConnection(con, stmt);

        } catch (SQLException e) {
            System.out.println("Error fetching ticket data: " + e.getMessage());
        }

        // Show movie name using ticketId
        movieNameColumn.setCellValueFactory(cellData -> {
            int ticketId = cellData.getValue().getTicketId();
            String movieName = movieNameMap.get(ticketId);
            return new SimpleStringProperty(movieName);
        });

        // Show showtime date using ticketId
        showDateColumn.setCellValueFactory(cellData -> {
            int ticketId = cellData.getValue().getTicketId();
            String showDate = showDateMap.get(ticketId);
            return new SimpleStringProperty(showDate);
        });

        table.setItems(ticketList);

        // Buttons
        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> {
            if (AuthorizationService.isManager(currentUser)) {
                ManagerView managerView = new ManagerView(stage, currentUser);
                managerView.initializeComponents();
            } else if (AuthorizationService.isStaff(currentUser)) {
                StaffView staffView = new StaffView(stage, currentUser);
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

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);
        logoutBtn.setOnAction(e -> {
            UserLogin userLogin = new UserLogin(stage);
            userLogin.initializeComponents();
        });

        HBox buttons = new HBox(10, backBtn, homeBtn, logoutBtn);
        VBox layout = new VBox(title,table, buttons);

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Movie Theatre Management System");
        stage.show();
    }
}
