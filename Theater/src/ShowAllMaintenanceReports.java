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

public class ShowAllMaintenanceReports {
    private User currentUser;
    private Stage stage;

    public ShowAllMaintenanceReports(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        // Title
        Label title = new Label("Show All Maintenance Reports");
        title.setFont(new Font("Times New Roman",24));

        // To display data in a table, use the JavaFX TableView
        TableView<MaintenanceReport> table = new TableView<>();

        // Define the first column of the table, <Movie, Integer> means the data type
        TableColumn<MaintenanceReport, Integer> idColumn = new TableColumn<>("ID");
        // PropertyValueFactory<>("id") will call the getId() method in the model class
        // which will fill the cell with the command id value for every row.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reportId"));

        // Movie name
        TableColumn<MaintenanceReport, String> equipmentColumn = new TableColumn<>("Equipment");
        equipmentColumn.setCellValueFactory(new PropertyValueFactory<>("equipment"));

        // Movie Director
        TableColumn<MaintenanceReport, String> descriptionColumn = new TableColumn<>("Report Details");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));




        // Add all columns to the table
        table.getColumns().addAll(idColumn, equipmentColumn, descriptionColumn);

        // We will use Observable List to hold the data retrieved from the database
        ObservableList<MaintenanceReport> reportList = FXCollections.observableArrayList();
        
        // Retrieve data from DB and fill up the table
        try{
            Connection con = DBUtils.establishConnection();
            String query = "SELECT reportId, equipment, description FROM maintenancereport";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = null;
            rs = stmt.executeQuery();

            while (rs.next()) {
                MaintenanceReport maintenanceReport = new MaintenanceReport(rs.getInt("reportId"), rs.getString("equipment"), rs.getString("description"));
                reportList.add(maintenanceReport);
            }

            if (rs != null) {
                rs.close();
            }

            DBUtils.closeConnection(con, stmt);
        }catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
        //Set the table to watch the observable list
        //the table will read data from it, and will also update upon any change
        table.setItems(reportList);

        // back button based on role, trying new thing

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> {
            if (AuthorizationService.isManager(currentUser)){
                GenerateReport generateReport = new GenerateReport(stage,currentUser);
                generateReport.show();
            }else if (AuthorizationService.isMaintenance(currentUser)){
                MaintenanceView maintenanceView = new MaintenanceView(stage,currentUser);
                maintenanceView.show();
            }
        });

        // to logout
        Button logoutBtn = new Button("logout");
        logoutBtn.setPrefWidth(200);
        logoutBtn.setOnAction(e -> {
            UserLogin userLogin = new UserLogin(stage);
            userLogin.initializeComponents();
        });

        HBox logoutBackBtns = new HBox(backBtn,logoutBtn);


        // Create the layout (VBox that contains the table)
        VBox vbox = new VBox(title,table,logoutBackBtns);
        // Add the layout to the scene
        Scene scene = new Scene(vbox, 1100, 500);

        //Add the scene to stage
        stage.setScene(scene);
        stage.setTitle("Movie Theatre Management System");
        stage.show();
        return;
    }

}