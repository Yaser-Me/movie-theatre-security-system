import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateMovie {
    private Stage   stage;
    private User    currentUser;

    private String  currentMovieName;
    private String  currentMovieDirector;
    private Integer currentMovieDuration;
    private String  currentMovieDescription;
    private String  currentMovieGenre;
/*
    public UpdateMovie(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
*/

    public UpdateMovie(Stage stage, User currentUser, String currentMovieName, String currentMovieDirector, Integer currentMovieDuration, String currentMovieDescription, String currentMovieGenre) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.currentMovieName = currentMovieName;
        this.currentMovieDirector = currentMovieDirector;
        this.currentMovieDuration = currentMovieDuration;
        this.currentMovieDescription = currentMovieDescription;
        this.currentMovieGenre = currentMovieGenre;
    }

    public void show() {

        // Title
        Label title = new Label("Update Movie Details");
        title.setFont(new Font("Times New Roman",24));

        // movie name
        Button movieNameBtn = new Button("Movie Name");
        movieNameBtn.setPrefWidth(200);
        movieNameBtn.setDisable(true);
        TextField movieNameTx = new TextField(currentMovieName);
        movieNameTx.setEditable(false);
        HBox movieNameH = new HBox(10, movieNameBtn, movieNameTx);

        // movie Director
        Button directorBtn = new Button("Movie Director");
        directorBtn.setPrefWidth(200);
        directorBtn.setDisable(true);
        TextField directorTx = new TextField(currentMovieDirector);
        HBox directorH = new HBox(10, directorBtn, directorTx);

        // movie Duration
        Button durationBtn = new Button("Movie Duration");
        durationBtn.setPrefWidth(200);
        durationBtn.setDisable(true);
        TextField durationTx = new TextField(Integer.toString(currentMovieDuration));
        HBox durationH = new HBox(10, durationBtn, durationTx);

        // movie Description
        Button descriptionBtn = new Button("Movie Description");
        descriptionBtn.setPrefWidth(200);
        descriptionBtn.setDisable(true);
        // trying TextArea for larger text field with Wrap functionality
        TextArea descriptionTx = new TextArea(currentMovieDescription);
        descriptionTx.setPrefSize(500,100);
        descriptionTx.setWrapText(true);
        HBox descriptionH = new HBox(10, descriptionBtn, descriptionTx);

        // movie Genre
        Button genreBtn = new Button("Movie Genre");
        genreBtn.setPrefWidth(200);
        genreBtn.setDisable(true);
        TextField genreTx = new TextField(currentMovieGenre);
        HBox genreH = new HBox(10, genreBtn, genreTx);


        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button updateBtn = new Button("Update Movie");
        updateBtn.setPrefWidth(200);
        Button homeBtn = new Button("Home");
        homeBtn.setPrefWidth(200);
        HBox updateBackHomeH = new HBox(10, updateBtn, backBtn,homeBtn);

        updateBtn.setOnAction(e -> {
            String movieName = movieNameTx.getText().trim();
            String movieDirector = directorTx.getText().trim();
            String movieDuration = durationTx.getText().trim();
            String movieDescription = descriptionTx.getText().trim();
            String movieGenre = genreTx.getText().trim();
            // movie name empty or valid check
            if (movieName.isEmpty( )) {
                showAlert("Error", "Movie field is empty");
                return;
            }else if(!InputValidator.validateMovieName(movieName)){
                showAlert("Error", "Movie Name Is Not Valid");
                return;
            }

            // movie director empty or valid check
            if (movieDirector.isEmpty( )) {
                showAlert("Error", "Director field is empty");
                return;
            }else if(!InputValidator.validateDirectorName(movieDirector)){
                showAlert("Error", "Director Name Is Not Valid");
                return;
            }

            // movie duration empty or valid check
            if (movieDuration.isEmpty( )) {
                showAlert("Error", "Movie Duration field is empty");
                return;
            }else if(!InputValidator.validateMovieDuration(movieDuration)){
                showAlert("Error", "Duration Is Not Valid");
                return;
            }

            // movie description empty or valid or length check
            if (movieDescription.isEmpty( )) {
                showAlert("Error", "Movie Description field is empty");
                return;
            }else if(!InputValidator.validateMovieDescription(movieDescription)){
                showAlert("Error", "Movie description Is Not Valid");
                return;
            }else if (movieDescription.length()< 10){
                showAlert("Error", "Movie description Is too Short");
                return;
            }

            // movie genre empty or valid check
            if (movieGenre.isEmpty( )) {
                showAlert("Error", "Movie Genre field is empty");
                return;
            }else if(!InputValidator.validateMovieGenre(genreTx.getText().trim())){
                showAlert("Error", "Genre Is Not Valid");
                return;
            }

                Connection con = null;
                PreparedStatement stmt = null;

                try {
                    con = DBUtils.establishConnection();

                    String query = "UPDATE movies SET  director = ?,duration = ?, description=?,genre=? WHERE name = ?;";
                    stmt = con.prepareStatement(query);
                    stmt.setString(1, movieDirector);
                    stmt.setInt(2, Integer.parseInt(movieDuration));
                    stmt.setString(3, movieDescription);
                    stmt.setString(4, movieGenre);
                    stmt.setString(5, movieName);


                    stmt.executeUpdate();

                    showSuccess("Success", "Movie updated successfully");

                    DBUtils.closeConnection(con, stmt);
                    ActionsRecord.recordAction(currentUser.getUsername()," Movie information has been Update: " + movieName );


                    // going to main screen after adding
                    ManagerView managerView = new ManagerView(stage, currentUser);
                    managerView.initializeComponents();

                } catch (Exception ex) {
                    System.out.println("Insert error:" + ex.getMessage());
                    try {
                        DBUtils.closeConnection(con, stmt);
                    } catch (Exception ignored) {}
                }

        });


        backBtn.setOnAction(e -> {
            ManageMovie manageMovie = new ManageMovie(stage,currentUser);
            manageMovie.show();
        });

        homeBtn.setOnAction(e -> {
            ManagerView managerView = new ManagerView(stage,currentUser);
            managerView.initializeComponents();
        });

        // Layout
        VBox updateMovieLayout = new VBox(15);
        updateMovieLayout.setPadding(new Insets(10));
        updateMovieLayout.getChildren().addAll(title,movieNameH,directorH,durationH,descriptionH,genreH,updateBackHomeH);

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
