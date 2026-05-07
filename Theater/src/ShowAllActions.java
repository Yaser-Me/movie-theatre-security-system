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

public class ShowAllActions {
    private User currentUser;
    private Stage stage;

    public ShowAllActions(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        // Title
        Label title = new Label("Show All Actions");
        title.setFont(new Font("Times New Roman",24));

        // To display data in a table, use the JavaFX TableView
        TableView<ActionsRecord> table = new TableView<>();

        TableColumn<ActionsRecord, Integer> idColumn = new TableColumn<>("ID");
        // PropertyValueFactory<>("id") will call the getId() method in the model class
        // which will fill the cell with the command id value for every row.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("actionId"));

        // Action User
        TableColumn<ActionsRecord, String> actionUserColumn = new TableColumn<>("User");
        actionUserColumn.setCellValueFactory(new PropertyValueFactory<>("actionUser"));

        // Action Taken
        TableColumn<ActionsRecord, String> actionTakenColumn = new TableColumn<>("Action");
        actionTakenColumn.setCellValueFactory(new PropertyValueFactory<>("actionTaken"));

        // Action Date
        TableColumn<ActionsRecord, String> actionDateColumn = new TableColumn<>("Time of Action");
        actionDateColumn.setCellValueFactory(new PropertyValueFactory<>("actionDate"));


        // Add all columns to the table
        table.getColumns().addAll(idColumn, actionUserColumn, actionTakenColumn, actionDateColumn);

        // We will use Observable List to hold the data retrieved from the database
        ObservableList<ActionsRecord> actionsRecordsList = FXCollections.observableArrayList();
        
        // Retrieve data from DB and fill up the table
        try{
            Connection con = DBUtils.establishConnection();
            String query = "SELECT actionId, actionUser, actionTaken, actionDate FROM actions";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = null;
            rs = stmt.executeQuery();

            while (rs.next()) {
                ActionsRecord actionsRecord = new ActionsRecord(rs.getInt("actionId"), rs.getString("actionUser"), rs.getString("actionTaken")
                        , rs.getString("actionDate"));
                actionsRecordsList.add(actionsRecord);
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
        table.setItems(actionsRecordsList);

        // back button based on role, trying new thing

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> {
        GenerateReport generateReport = new GenerateReport(stage,currentUser);
        generateReport.show();
        });

        Button homeBtn = new Button("Home");
        homeBtn.setPrefWidth(200);
        homeBtn.setOnAction(e -> {

            ManagerView managerView = new ManagerView(stage, currentUser);
            managerView.initializeComponents();
        });


        // to logout
        Button logoutBtn = new Button("logout");
        logoutBtn.setPrefWidth(200);
        logoutBtn.setOnAction(e -> {
            UserLogin userLogin = new UserLogin(stage);
            userLogin.initializeComponents();
        });

        HBox logoutBackBtns = new HBox(backBtn, homeBtn, logoutBtn);

        // Create the layout (VBox that contains the table)
        VBox vbox = new VBox(title,table,logoutBackBtns);
        // Add the layout to the scene
        Scene scene = new Scene(vbox, 600, 600);

        //Add the scene to stage
        stage.setScene(scene);
        stage.setTitle("Movie Theatre Management System");
        stage.show();
        return;
    }

}