public class MoviePopularityData {
    private String movieName;
    private int ticketsSold;

    public MoviePopularityData(String movieName, int ticketsSold) {
        this.movieName = movieName;
        this.ticketsSold = ticketsSold;
    }

    public String getMovieName() {
        return movieName;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }
}