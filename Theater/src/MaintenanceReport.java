public class MaintenanceReport {
    private int     reportId;
    private String  equipment;
    private String  description;


    public MaintenanceReport(int reportId, String equipment, String description) {
        this.reportId = reportId;
        this.equipment = equipment;
        this.description = description;
    }

    public MaintenanceReport(String equipment, String description) {
        this.equipment = equipment;
        this.description = description;
    }

    public int getReportId() {
        return reportId;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getDescription() {
        return description;
    }
}
