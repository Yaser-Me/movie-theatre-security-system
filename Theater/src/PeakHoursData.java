public class PeakHoursData {
    private int timeSlot;
    private int ticketsSold;

    public PeakHoursData (int timeSlot, int ticketsSold) {
        this.timeSlot = timeSlot;
        this.ticketsSold = ticketsSold;
    }

    public int getTimeSlot() {
        return timeSlot ;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }
}