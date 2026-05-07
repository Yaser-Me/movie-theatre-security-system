import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PeakHoursReport {
    private Stage stage;
    private User currentUser;

    public PeakHoursReport(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user ;
    }


    public void show() {
        Label title = new Label("Peak Hours Report");
        title.setFont(new Font("Times New Roman",24));

        Button backBtn = new Button("Back");
        Button homeBtn = new Button("Home");
        Button logoutBtn = new Button("Logout");

        backBtn.setPrefWidth(200);
        homeBtn.setPrefWidth(200);
        logoutBtn.setPrefWidth(200);

        backBtn.setOnAction(e -> {
            GenerateReport generateReport = new GenerateReport(stage,currentUser);
            generateReport.show();
        });

        homeBtn.setOnAction(e -> {
            ManagerView managerView = new ManagerView(stage,currentUser);
            managerView.initializeComponents();
        });

        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(backBtn, homeBtn, logoutBtn);


        // table
        TableView<PeakHoursData> table = new TableView<>();

        // time slot
        TableColumn<PeakHoursData, Integer> timeSlotColumn = new TableColumn<>("Time Slot");
        timeSlotColumn.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        timeSlotColumn.setPrefWidth(200);

        // tickets sold
        TableColumn<PeakHoursData, Integer> ticketsSoldColumn = new TableColumn<>("Tickets Sold");
        ticketsSoldColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));
        ticketsSoldColumn.setPrefWidth(200);

        // add the columns to the table
        table.getColumns().addAll(timeSlotColumn, ticketsSoldColumn);

        // list for the report data
        ObservableList<PeakHoursData> reportList = FXCollections.observableArrayList();

// get data from db
        try {

            Connection con= DBUtils.establishConnection();

            String query = "SELECT showtime.timeSlot, COUNT(ticket.ticketId) AS ticketsSold " +
                    "FROM showtime " +
                    "LEFT JOIN ticket ON showtime.showtimeId = ticket.showtimeId " +
                    "GROUP BY showtime.timeSlot " +
                    "ORDER BY ticketsSold DESC, showtime.timeSlot ASC";


            PreparedStatement stmt= con.prepareStatement(query );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int timeSlot = rs.getInt("timeSlot");
                int ticketsSold =rs.getInt("ticketsSold");

                PeakHoursData data = new PeakHoursData(timeSlot, ticketsSold);
                reportList.add(data);
            }

            rs.close();
            DBUtils.closeConnection(con, stmt);

        } catch (Exception ex) {
            System.out.println("something went wrong: " + ex.getMessage());
        }

        // set the table items
        table.setItems(reportList);

        VBox layout = new VBox(16);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(title, table, buttons);


        Scene scene = new Scene(layout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }


}