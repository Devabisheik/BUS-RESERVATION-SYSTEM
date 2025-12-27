package BUS_RESERVATION_SYSTEM;
import java.sql.*;
public class BusDBMS {
    public static void readBuses() throws SQLException {
        Connection con = Database.getconnection();
        Statement stmt = con.createStatement();
        String query = "SELECT * FROM buses";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("==================================================================================================");
        while(rs.next())
        {
            System.out.printf("SNo: %d\tBusName: %s\tBusNumber: %s\tSource: %s\tDestination: %s\tTotal Seats: %d\tAvailable Seats: %d\n" , rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),
            rs.getInt(6),rs.getInt(7));
        }
        System.out.println("==================================================================================================");
        con.close();
    }
    public static Bus searchBuses(String source , String destination) throws SQLException
    {
        String query = "SELECT * FROM buses WHERE destination = ? AND source = ?";
        Connection con = Database.getconnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1,destination);
        pst.setString(2,source);
        ResultSet rs =  pst.executeQuery();
        Bus bus = null;
        while(rs.next())
        {
            bus = new Bus(
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getInt(6),
                    rs.getInt(7)
            );
        }
        return bus;
    }
    public static boolean allocateSeat(int seatNo, String busName, String passengerId, String bookingId) throws SQLException {

        // 1) Validate seat number
        if (seatNo <= 0) {
            System.out.println("Invalid seat number!");
            return false;
        }

        Connection con = Database.getconnection();

        // 2) Check available seats in buses
        String busesQuery = "SELECT available_seats FROM buses WHERE bus_name = ?";
        PreparedStatement pst1 = con.prepareStatement(busesQuery);
        pst1.setString(1, busName);
        ResultSet rs1 = pst1.executeQuery();

        if (!rs1.next()) {
            System.out.println("Bus not found!");
            return false;
        }

        int available = rs1.getInt("available_seats");

        if (available <= 0) {
            System.out.println("No seats available on this bus!");
            return false;
        }

        // 3) Check if seat is already booked
        String seatCheck = "SELECT * FROM booked_seats WHERE bus_name = ? AND seat_number = ?";
        PreparedStatement pst2 = con.prepareStatement(seatCheck);
        pst2.setString(1, busName);
        pst2.setInt(2, seatNo);
        ResultSet rs2 = pst2.executeQuery();

        if (rs2.next()) {
            System.out.println("Seat " + seatNo + " is already booked!");
            return false;
        }

        // 4) Book the seat → Insert into booked_seats
        String insertSeat = "INSERT INTO booked_seats (booking_id, passenger_id, bus_name, seat_number) VALUES (?, ?, ?, ?)";
        PreparedStatement pst3 = con.prepareStatement(insertSeat);
        pst3.setString(1, bookingId);
        pst3.setString(2, passengerId);
        pst3.setString(3, busName);
        pst3.setInt(4, seatNo);

        int seatInserted = pst3.executeUpdate();

        if (seatInserted != 1) {
            System.out.println("Failed to allocate seat!");
            return false;
        }

        // 5) Decrement available seats in buses table
        String updateBus = "UPDATE buses SET available_seats = available_seats - 1 WHERE bus_name = ?";
        PreparedStatement pst4 = con.prepareStatement(updateBus);
        pst4.setString(1, busName);
        pst4.executeUpdate();

        System.out.println("Seat " + seatNo + " successfully booked!");

        return true;
    }
}

