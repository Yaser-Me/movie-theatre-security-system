import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddMovie {
    private User currentUser;
    private Stage stage;

    public AddMovie(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Add Movie");
        title.setFont(new Font("Times New Roman",24));

        // movie name
        Button movieNameBtn = new Button("Movie Name");
        movieNameBtn.setPrefWidth(200);
        movieNameBtn.setDisable(true);
        TextField movieNameTx = new TextField();
        HBox movieNameH = new HBox(10, movieNameBtn, movieNameTx);

        // movie Director
        Button directorBtn = new Button("Movie Director");
        directorBtn.setPrefWidth(200);
        directorBtn.setDisable(true);
        TextField directorTx = new TextField();
        HBox directorH = new HBox(10, directorBtn, directorTx);

        // movie Duration
        Button durationBtn = new Button("Movie Duration");
        durationBtn.setPrefWidth(200);
        durationBtn.setDisable(true);
        TextField durationTx = new TextField();
        HBox durationH = new HBox(10, durationBtn, durationTx);

        // movie Description
        Button descriptionBtn = new Button("Movie Description");
        descriptionBtn.setPrefWidth(200);
        descriptionBtn.setDisable(true);
        // trying TextArea for larger text field with Wrap functionality
        TextArea descriptionTx = new TextArea();
        descriptionTx.setPrefSize(500,100);
        descriptionTx.setWrapText(true);
        HBox descriptionH = new HBox(10, descriptionBtn, descriptionTx);

        // movie Genre
        Button genreBtn = new Button("Movie Genre");
        genreBtn.setPrefWidth(200);
        genreBtn.setDisable(true);
        TextField genreTx = new TextField();
        HBox genreH = new HBox(10, genreBtn, genreTx);



        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button addBtn = new Button("Add Movie");
        addBtn.setPrefWidth(200);
        Button homeBtn = new Button("Home");
        homeBtn.setPrefWidth(200);
        HBox addBackHomeH = new HBox(10, addBtn, backBtn,homeBtn);

        addBtn.setOnAction(e -> {
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


            if (MovieCheck.movieExists(movieName)) {
                showAlert("Error", "Movie cannot be added, already exist");
            } else {
                Connection con = null;
                PreparedStatement stmt = null;

                try {
                    con = DBUtils.establishConnection();

                    String query = "INSERT INTO movies (name, director, duration, description, genre) VALUES (?, ?, ?, ?, ?)";
                    stmt = con.prepareStatement(query);
                    stmt.setString(1, movieName);
                    stmt.setString(2, movieDirector);
                    stmt.setInt(3, Integer.parseInt(movieDuration));
                    stmt.setString(4, movieDescription);
                    stmt.setString(5, movieGenre);

                    stmt.executeUpdate();

                    showSuccess("Success", "Movie Added successfully");
                    ActionsRecord.recordAction(currentUser.getUsername()," Movie has been Added: " + movieName );

                    DBUtils.closeConnection(con, stmt);

                    // going to main screen after adding
                    ManagerView managerView = new ManagerView(stage, currentUser);
                    managerView.initializeComponents();

                } catch (Exception ex) {
                    System.out.println("Insert error:" + ex.getMessage());
                    try {
                        DBUtils.closeConnection(con, stmt);
                    } catch (Exception ignored) {}
                }
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
        VBox addMovieLayout = new VBox(15);
        addMovieLayout.setPadding(new Insets(10));
        addMovieLayout.getChildren().addAll(title,movieNameH,directorH,durationH,descriptionH,genreH,addBackHomeH);

        // Scene then stage
        Scene scene = new Scene(addMovieLayout, 730, 600);
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
