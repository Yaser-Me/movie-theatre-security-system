public class Ticket {
    private int ticketId;
    private int showtimeId;
    private int seat;
    private int price;

    // we have 2 constructors 1 for creating a ticket, it has no movieId because the database provide them <Auto increment>
    // the other for searching for a ticket in the database <SELECT> which has the ticketId

    // 1st constructor: for adding a new ticket
    public Ticket(int showtimeId, int seat,int price ) {
        this.price = price;
        this.seat = seat;
        this.showtimeId = showtimeId;
    }

    // 2nd constructor: for searching a movie


    public Ticket(int ticketId,int showtimeId, int seat,int price) {
        this.price = price;
        this.seat = seat;
        this.showtimeId = showtimeId;
        this.ticketId = ticketId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }

    public int getTicketId() {
        return ticketId;
    }

}
