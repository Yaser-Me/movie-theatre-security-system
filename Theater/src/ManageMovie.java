import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageMovie {
    private User currentUser;
    private Stage stage;

    public ManageMovie(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Manage Movies");
        title.setFont(new Font("Times New Roman",24));

        // addMovie button
        Button addMovieBtn = new Button("Add a new movie");
        addMovieBtn.setPrefWidth(200);


        // deleteMovie button
        Button deleteMovie = new Button("Delete a movie");
        deleteMovie.setPrefWidth(200);
        // deleteMovie textField
        TextField deleteMovieName = new TextField();
        deleteMovieName.setPromptText("Movie Name");
        // deleteMovie Hbox
        HBox deleteBox = new HBox(10, deleteMovie, deleteMovieName);


        // updateMovie button
        Button updateMovieBtn = new Button("Update movie Details");
        updateMovieBtn.setPrefWidth(200);
        // deleteMovie textField
        TextField updateMovieName = new TextField();
        updateMovieName.setPromptText("Movie Name");
        // deleteMovie Hbox
        HBox updateBox = new HBox(10, updateMovieBtn, updateMovieName);

        Button showMovieBtn = new Button("Show All Movies");
        showMovieBtn.setPrefWidth(200);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);



        // Actions *****
        addMovieBtn.setOnAction(e -> {
            AddMovie addMovie = new AddMovie(stage,currentUser);
            addMovie.show();

        });

        deleteMovie.setOnAction(e -> {
            // to be removed
            String movieName = deleteMovieName.getText();

            if (movieName.isEmpty()) {
                showAlert("Error", "Please enter movie name to delete");
                return;
            }



            if (!MovieCheck.movieExists(movieName)) {
                System.out.println("not here");
                showAlert("Error", "Movie does not exist");
            }else if(MovieCheck.hasShowtime(movieName)){
                System.out.println("has showtime");
                showAlert("Error", "can't delete movie, there's a show time");
            } else {
                Connection con = null;
                PreparedStatement stmt = null;
                try {
                    con = DBUtils.establishConnection();
                    String query = "DELETE FROM movies WHERE name = ?";
                    stmt = con.prepareStatement(query);
                    stmt.setString(1, movieName);


                    stmt.executeUpdate();

                    DBUtils.closeConnection(con, stmt);
                    showSuccess("Success", movieName + " Movie Deleted successfully");
                    ActionsRecord.recordAction(currentUser.getUsername()," Movie has been Deleted: " + movieName );
                } catch (Exception ex) {
                    System.out.println("Insert error:" + ex.getMessage());
                    try {
                        DBUtils.closeConnection(con, stmt);
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        updateMovieBtn.setOnAction(e -> {
            // to be removes
            System.out.println("update movie");
            String movieName = updateMovieName.getText();

            if (movieName.isEmpty()) {
                showAlert("Error", "Please enter movie name to update");
                return;
            }

            if (!MovieCheck.movieExists(movieName)) {
                showAlert("Error", "there's no movie with this name");
            } else {
                String currentMovieDirector = "";
                int currentMovieDuration = 0;
                String currentMovieDescription ="";
                String currentMovieGenre ="";
                try{
                    Connection con = DBUtils.establishConnection();
                    String query = "SELECT director,duration,description,genre FROM movies WHERE name = ?";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, movieName);

                    ResultSet rs = null;
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        currentMovieDirector = rs.getString("director");
                        currentMovieDuration = rs.getInt("duration");
                        currentMovieDescription = rs.getString("description");
                        currentMovieGenre = rs.getString("genre");
                    }

                    if (rs != null) {
                        rs.close();
                    }

                    DBUtils.closeConnection(con, stmt);
                }catch (SQLException ee) {
                    System.out.println("Error fetching data: " + ee.getMessage());
                }

                UpdateMovie updateMovie = new UpdateMovie(stage,currentUser,movieName,currentMovieDirector,currentMovieDuration,currentMovieDescription,currentMovieGenre);
                updateMovie.show();
            }
        });
        // showMovieBtn --> goes to ShowAllMovies
        showMovieBtn.setOnAction(e -> {
            ShowAllMovies showAllMovies = new ShowAllMovies(stage,currentUser);
            showAllMovies.initializeComponents();
        });

        // back --> return to ManagerView
        backBtn.setOnAction(e -> {
            ManagerView managerView = new ManagerView(stage,currentUser);
            managerView.initializeComponents();
        });
        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox ManagerLayout = new VBox(15);
        ManagerLayout.setPadding(new Insets(10));
        ManagerLayout.getChildren().addAll(title,addMovieBtn,deleteBox,updateBox,showMovieBtn,backBtn,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(ManagerLayout, 600, 600);
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
