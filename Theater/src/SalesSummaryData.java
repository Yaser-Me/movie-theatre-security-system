public class SalesSummaryData {
    private int totalTickets;
    private int totalSales;

    public SalesSummaryData(int totalTickets,int totalSales ) {
        this.totalTickets = totalTickets;
        this.totalSales =totalSales;
    }

    public int getTotalTickets() {
        return totalTickets ;
    }


    public int getTotalSales() {
        return totalSales;
    }
}