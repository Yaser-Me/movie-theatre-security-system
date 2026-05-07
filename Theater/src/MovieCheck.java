import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// this class does checks from the database
public class MovieCheck {
    // Check if movie exists
    public static boolean movieExists(String movieName) {
        String query = "SELECT * FROM movies WHERE name = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, movieName);

            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return exists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean showTimeExists(int showtimeId) {
        String query = "SELECT * FROM showtime WHERE showtimeId = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, showtimeId);

            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return exists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Check if movie has a showtime
    public static boolean hasShowtime(String movieName) {
        String query = "SELECT * FROM showtime WHERE movieId = ?";

        int movieId = getMovieIdByName(movieName);

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, movieId);

            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return exists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Check if showtime taken
    public static boolean isTimeSlotTaken(String roomNumber, String showtimeDate, String showtimeTimeSlot) {
        String query = "SELECT * FROM showtime WHERE room =? AND date = ? AND timeSlot = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, Integer.parseInt(roomNumber));
            stmt.setString(2, showtimeDate);
            stmt.setInt(3, Integer.parseInt(showtimeTimeSlot));

            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return exists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static int getMovieIdByName(String movieName) {
        String query = "SELECT movieId FROM movies WHERE name = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, movieName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int movieId = rs.getInt("movieId");
                rs.close();
                DBUtils.closeConnection(con, stmt);
                return movieId;
            }

            rs.close();
            DBUtils.closeConnection(con, stmt);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public static boolean isSeatTaken(int showtimeId, int seatNumber) {
        String query = "SELECT * FROM ticket WHERE showtimeId = ? AND seat = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, showtimeId);
            stmt.setInt(2, seatNumber);

            ResultSet rs = stmt.executeQuery();

            boolean seatTaken = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return seatTaken;

        } catch (Exception e) {
            System.out.println("Error checking seat availability: " + e.getMessage());
        }

        return false;
    }

    public static boolean hasTicket(int showtimeId) {
        String query = "SELECT * FROM ticket WHERE showtimeId = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, showtimeId);

            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return exists;

        } catch (Exception e) {
            System.out.println("Error checking tickets for showtime: " + e.getMessage());
        }

        return false;
    }

    // to get ticketId from ticket database by showtimeId and seat Number
    public static int getTicketId(int showtimeId, int seatNumber) {
        String query = "SELECT ticketId FROM ticket WHERE showtimeId = ? AND seat = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, showtimeId);
            stmt.setInt(2, seatNumber);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int ticketId = rs.getInt("ticketId");

                rs.close();
                DBUtils.closeConnection(con, stmt);

                return ticketId;
            }

            rs.close();
            DBUtils.closeConnection(con, stmt);

        } catch (Exception e) {
            System.out.println("Error fetching ticketId: " + e.getMessage());
        }

        return -1; // means not found

    }


    public static boolean roomExists(int roomId) {
        String query = "SELECT * FROM rooms WHERE roomId = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            DBUtils.closeConnection(con, stmt);
            return exists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }



}
