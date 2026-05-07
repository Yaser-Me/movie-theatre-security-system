import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SalesSummaryReport {
    private User currentUser;
    private Stage stage;

    public SalesSummaryReport(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void show() {
        // title
        Label title = new Label("Sales Summary Report");
        title.setFont(new Font("Times New Roman", 24));


        // table
        TableView<SalesSummaryData> table = new TableView<>();

        // total tickets
        TableColumn<SalesSummaryData, Integer> totalTicketsColumn = new TableColumn<>("Total Tickets");
        totalTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("totalTickets"));
        totalTicketsColumn.setPrefWidth(200);

        // total sales
        TableColumn<SalesSummaryData, Integer> totalSalesColumn = new TableColumn<>("Total Sales");
        totalSalesColumn.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        totalSalesColumn.setPrefWidth(200);


        // add columns to table
        table.getColumns().addAll(totalTicketsColumn, totalSalesColumn);

        ObservableList<SalesSummaryData> reportList = FXCollections.observableArrayList();

        // get data from db
        try {
            Connection con = DBUtils.establishConnection();

            String query = "SELECT COUNT(ticketId) AS totalTickets, SUM(price) AS totalSales " +
                    "FROM ticket";

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int totalTickets = rs.getInt("totalTickets");
                int totalSales = rs.getInt("totalSales");

                SalesSummaryData data = new SalesSummaryData(totalTickets, totalSales);
                reportList.add(data);
            }

            rs.close();
            DBUtils.closeConnection(con, stmt);

        } catch (Exception ex) {
            System.out.println("something went wrong: " + ex.getMessage());
        }

        // set table items
        table.setItems(reportList);



        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        Button homeBtn = new Button("Home");
        homeBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        backBtn.setOnAction(e -> {
            GenerateReport generateReport = new GenerateReport( stage, currentUser);
            generateReport.show();
        });


        homeBtn.setOnAction(e -> {
            ManagerView managerView = new ManagerView(stage, currentUser);
            managerView.initializeComponents();
        });

        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });



        HBox buttons = new HBox(10, backBtn, homeBtn, logoutBtn);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(title, table,  buttons);

        Scene scene = new Scene(layout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }
}