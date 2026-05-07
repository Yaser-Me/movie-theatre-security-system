public class ShowTime {
    private int showTimeId;
    private int movieId;
    private int roomId;
    private String showDate;
    private int timeSlot;

    // we have 2 constructors 1 for creating a Showtime, it has no showTimeId because the database provide them <Auto increment>
    // the other for searching for a showtime in the database <SELECT> which has the showTimeId

    // 1st constructor: for adding a new movie
    public ShowTime(int movieId, int roomId, String showDate, int timeSlot) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.showDate = showDate;
        this.timeSlot = timeSlot;
    }

    // 2nd constructor: for searching a showtime
    public ShowTime(int showTimeId, int  movieId, int roomId, String showDate, int timeSlot) {
        this.showTimeId = showTimeId;
        this.movieId = movieId;
        this.roomId = roomId;
        this.showDate = showDate;
        this.timeSlot = timeSlot;
    }

    public int getShowTimeId() {
        return showTimeId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }
}
