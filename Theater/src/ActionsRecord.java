import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;


public class ActionsRecord {
    private int actionId;
    private String actionUser;
    private String actionTaken;
    private String actionDate;

    public ActionsRecord(int actionId, String actionUser, String actionTaken, String actionDate) {
        this.actionId = actionId;
        this.actionUser = actionUser;
        this.actionTaken = actionTaken;
        this.actionDate = actionDate;
    }

    public ActionsRecord(String actionUser, String actionTaken, String actionDate) {
        this.actionUser = actionUser;
        this.actionTaken = actionTaken;
        this.actionDate = actionDate;
    }

    public int getActionId() {
        return actionId;
    }

    public String getActionUser() {
        return actionUser;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public String getActionDate() {
        return actionDate;
    }

    public static void recordAction (String userName , String actionTaken){
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DBUtils.establishConnection();

            String query = "INSERT INTO actions (actionUser, actionTaken, actionDate) VALUES (?, ?, ?)";
            stmt = con.prepareStatement(query);
            stmt.setString(1, userName);
            stmt.setString(2, actionTaken);
            // this will add the current date up to the milliseconds boi which is the same format as DATETIME
            stmt.setString(3, LocalDateTime.now().toString().replace("T"," ") );


            stmt.executeUpdate();


            DBUtils.closeConnection(con, stmt);
            System.out.println("action taken");


        } catch (Exception ex) {
            System.out.println("Insert error:" + ex.getMessage());
            try {
                DBUtils.closeConnection(con, stmt);
            } catch (Exception ignored) {}
        }
    }

    }
