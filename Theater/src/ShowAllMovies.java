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

public class ShowAllMovies {
    private User currentUser;
    private Stage stage;

    public ShowAllMovies(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        // Title
        Label title = new Label("Show All Movies");
        title.setFont(new Font("Times New Roman",24));

        // To display data in a table, use the JavaFX TableView
        TableView<Movie> table = new TableView<>();

        // Define the first column of the table, <Movie, Integer> means the data type
        TableColumn<Movie, Integer> idColumn = new TableColumn<>("ID");
        // PropertyValueFactory<>("id") will call the getId() method in the model class
        // which will fill the cell with the command id value for every row.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("movieId"));

        // Movie name
        TableColumn<Movie, String> movieNameColumn = new TableColumn<>("Movie Name");
        movieNameColumn.setCellValueFactory(new PropertyValueFactory<>("movieName"));

        // Movie Director
        TableColumn<Movie, String> directorColumn = new TableColumn<>("Director");
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("movieDirector"));

        // Movie Duration
        TableColumn<Movie, Integer> durationColumn = new TableColumn<>("Duration in hours");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("movieDuration"));

        // Movie Description
        TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("movieDescription"));

        // Movie Genre
        TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("movieGenre"));


        // Add all columns to the table
        table.getColumns().addAll(idColumn, movieNameColumn, directorColumn, durationColumn, descriptionColumn, genreColumn);

        // We will use Observable List to hold the data retrieved from the database
        ObservableList<Movie> MovieList = FXCollections.observableArrayList();
        
        // Retrieve data from DB and fill up the table
        try{
            Connection con = DBUtils.establishConnection();
            String query = "SELECT movieId, name, director, duration, description, genre FROM movies";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = null;
            rs = stmt.executeQuery();

            
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("movieId"), rs.getString("name"), rs.getString("director")
                        , rs.getInt("duration"), rs.getString("description"),rs.getString("genre"));
                MovieList.add(movie);
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
        table.setItems(MovieList);

        // back button based on role, trying new thing

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> {
            if (AuthorizationService.isManager(currentUser)){
                ManageMovie manageMovie = new ManageMovie(stage,currentUser);
                manageMovie.show();
            }else if (AuthorizationService.isStaff(currentUser)){
                StaffView staffView = new StaffView(stage,currentUser);
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