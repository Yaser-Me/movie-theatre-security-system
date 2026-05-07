import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthenticationService {
    public static User authenticate(String username, String suppliedPassword){
        Connection con = DBUtils.establishConnection();
        String query = "SELECT * FROM users WHERE username = ?;";
        User loggedInUser = null;

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);

            ResultSet rs = null;
            rs = statement.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                boolean correctPassword = BCrypt.checkpw(suppliedPassword, storedPassword);

                if (correctPassword){
                    loggedInUser =  new User(
                            rs.getString("username"),
                            storedPassword,
                            rs.getString("role")
                    );
                }
            }

            if (rs != null) {
                rs.close();
            }

            DBUtils.closeConnection(con, statement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return loggedInUser;
    }
}
