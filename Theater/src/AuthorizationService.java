import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthorizationService {

    public static boolean isManager(User currentUser) {
        if (currentUser == null){
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase("MANAGER");
    }

    public static boolean isStaff(User currentUser) {
        if (currentUser == null){
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase("STAFF");
    }

    public static boolean isMaintenance(User currentUser) {
        if (currentUser == null){
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase("MAINTENANCE");
    }

    // account lock mechanism:
    // the logic used : enter username and password --> check if account exist --> check if account isLocked
    // if locked --> deny login
    // if not locked --> check password
    // if password wrong --> increment failed attempts in db
    // if password correct --> reset failed attempts and login
    // it's a simple account lock logic, i used chatgpt for some sql and syntax   help


    // check if account is locked
    public static boolean isAccountLocked(String username) {
        String query = "SELECT isLocked FROM users WHERE username = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean locked = rs.getBoolean("isLocked");
                rs.close();
                DBUtils.closeConnection(con, stmt);
                return locked;
            }

            rs.close();
            DBUtils.closeConnection(con, stmt);

        } catch (Exception e) {
            System.out.println("Error checking lock status: " + e.getMessage());
        }

        return false;
    }

    // increase failed attempts, lock after 3 tries
    public static void incrementFailedAttempts(String username) {
        String selectQuery = "SELECT failedAttempts FROM users WHERE username = ?";
        String updateQuery = "UPDATE users SET failedAttempts = ?, isLocked = ? WHERE username = ?";

        try {
            Connection con = DBUtils.establishConnection();

            PreparedStatement selectStmt = con.prepareStatement(selectQuery);
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int attempts = rs.getInt("failedAttempts") + 1;
                boolean lock = attempts >= 3;

                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, attempts);
                updateStmt.setBoolean(2, lock);
                updateStmt.setString(3, username);
                updateStmt.executeUpdate();
                if (attempts == 3){
                    ActionsRecord.recordAction(username , "Account locked after 3 attempts");
                }

                updateStmt.close();
            }

            rs.close();
            selectStmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error updating failed attempts: " + e.getMessage());
        }
    }

    // reset after successful login
    public static void resetFailedAttempts(String username) {
        String query = "UPDATE users SET failedAttempts = 0, isLocked = FALSE WHERE username = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.executeUpdate();

            DBUtils.closeConnection(con, stmt);

        } catch (Exception e) {
            System.out.println("Error resetting failed attempts: " + e.getMessage());
        }
    }

}