import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StaffView {
    private User currentUser;
    private Stage stage;

    public StaffView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Welcome Staff");
        title.setFont(new Font("Times New Roman",24));

        // Buttons with matching width to make it look cool and organized
        Button showMovieBtn = new Button("Show All Movies");
        showMovieBtn.setPrefWidth(200);
        Button bookTicket = new Button("Book a ticket");
        bookTicket.setPrefWidth(200);
        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);


        // Actions *****
        showMovieBtn.setOnAction(e -> {
            ShowAllMovies showAllMovies = new ShowAllMovies(stage,currentUser);
            showAllMovies.initializeComponents();
        });



        bookTicket.setOnAction(e -> {
            ShowAllShowtimes showAllShowtimes = new ShowAllShowtimes(stage,currentUser);
            showAllShowtimes.initializeComponents();

        });

        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox StaffLayout = new VBox(15);
        StaffLayout.setPadding(new Insets(10));
        StaffLayout.getChildren().addAll(title,showMovieBtn,bookTicket,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Movie Theatre Management System");
        stage.setScene(scene);
        stage.show();
    }


}
