public class Movie {
    private int movieId;
    private String movieName;
    private String movieDirector;
    private int movieDuration;
    private String movieDescription;
    private String movieGenre;

    // we have 2 constructors 1 for creating a movie, it has no movieId because the database provide them <Auto increment>
    // the other for searching for a movie in the database <SELECT> which has the movieId

    // 1st constructor: for adding a new movie
    public Movie( String movieName, String movieDirector, int movieDuration, String movieDescription, String movieGenre) {
        this.movieName = movieName;
        this.movieDirector = movieDirector;
        this.movieDuration = movieDuration;
        this.movieDescription = movieDescription;
        this.movieGenre = movieGenre;

    }
    // 2nd constructor: for searching a movie
    public Movie(int movieId, String movieName, String movieDirector, int movieDuration, String movieDescription, String movieGenre) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDirector = movieDirector;
        this.movieDuration = movieDuration;
        this.movieDescription = movieDescription;
        this.movieGenre = movieGenre;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public int getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(int movieDuration) {
        this.movieDuration = movieDuration;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }
}
