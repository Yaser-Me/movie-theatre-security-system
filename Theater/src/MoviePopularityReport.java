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

public class MoviePopularityReport {

    private Stage stage;
    private User currentUser;

    public MoviePopularityReport(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void show() {

        Label title = new Label("Movie Popularity Report");
        title.setFont(new Font("Times New Roman", 24));

        // buttons
        Button backBtn = new Button("Back");
        Button homeBtn = new Button("Home");
        Button logoutBtn = new Button("Logout");

        backBtn.setPrefWidth(200);
        homeBtn.setPrefWidth(200);
        logoutBtn.setPrefWidth(200);

        backBtn.setOnAction(e -> {
            GenerateReport g = new GenerateReport(stage, currentUser);
            g.show();
        });

        homeBtn.setOnAction(e -> {
            ManagerView mv = new ManagerView(stage, currentUser);
            mv.initializeComponents();
        });

        logoutBtn.setOnAction(e -> {
            UserLogin login = new UserLogin(stage);
            login.initializeComponents();
        });

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(backBtn, homeBtn, logoutBtn);

        // table
        TableView<MoviePopularityData> table = new TableView<>();

        TableColumn<MoviePopularityData, String> col1 = new TableColumn<>("Movie Name");
        col1.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        col1.setPrefWidth(300);

        TableColumn<MoviePopularityData, Integer> col2 = new TableColumn<>("Tickets Sold");
        col2.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));
        col2.setPrefWidth(150);

        table.getColumns().addAll(col1, col2);

        ObservableList<MoviePopularityData> list = FXCollections.observableArrayList();

        // get data from db
        try {
            Connection con = DBUtils.establishConnection();

            String query = "SELECT movies.name AS movieName, COUNT(ticket.ticketId) AS ticketsSold " +
                    "FROM movies " +
                    "LEFT JOIN showtime ON movies.movieId = showtime.movieId " +
                    "LEFT JOIN ticket ON showtime.showtimeId = ticket.showtimeId " +
                    "GROUP BY movies.movieId, movies.name " +
                    "ORDER BY ticketsSold DESC, movies.name ASC";


            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = null;
            rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("movieName");
                int tickets = rs.getInt("ticketsSold");
                list.add(new MoviePopularityData(name, tickets));
            }

            if (rs != null) {
                rs.close();
            }

            DBUtils.closeConnection(con, stmt);

        } catch (Exception ex) {
            System.out.println("something went wrong: " + ex.getMessage());
        }

        table.setItems(list);

        VBox layout = new VBox(16);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(title, table, buttons);

        Scene scene = new Scene(layout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }
}