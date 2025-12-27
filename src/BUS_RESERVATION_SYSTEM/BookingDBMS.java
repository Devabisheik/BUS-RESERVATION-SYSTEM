package BUS_RESERVATION_SYSTEM;

import java.sql.*;
import java.time.LocalDate;   // <-- REQUIRED IMPORT
import java.util.*;

public class BookingDBMS {

    public static void insertBookingDetails(
            String booking_id,
            String passenger_id,
            String bus_name,
            int noSeats,
            int total_amount,
            LocalDate date,
            String source,
            String destination,
            int amount,
            String emailId
    ) throws SQLException {

        String query = "INSERT INTO bookings (booking_id, passenger_id, bus_name, number_of_seats, total_amount, booking_date,passenger_source, " +
                "passenger_destination, amount_paid) VALUES (?,?,?,?,?,?,?,?,?)";

        Connection con = Database.getconnection();
        PreparedStatement pst = con.prepareStatement(query);

        pst.setString(1, booking_id);
        pst.setString(2, passenger_id);
        pst.setString(3, bus_name);
        pst.setInt(4, noSeats);
        pst.setInt(5, total_amount);

        // Convert LocalDate -> java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        pst.setDate(6, sqlDate);

        pst.setString(7, source);
        pst.setString(8, destination);
        pst.setInt(9, amount);

        int rows = pst.executeUpdate();
        if (rows == 1) {
            System.out.println("data inserted successfully!");
        } else {
            System.out.println("data was not inserted!");
        }
        new Thread(() -> {
            SendEmail.getEmailNotification(emailId, booking_id, passenger_id, bus_name, noSeats, total_amount,
                    date.toString(), source, destination, amount);
        }).start();
        pst.close();
        con.close();
    }

    public static void cancelBooking(String booking_id) throws SQLException {

        Connection con = Database.getconnection();
        String query1 = "DELETE FROM bookings WHERE booking_id = ?";
        PreparedStatement pst1 = con.prepareStatement(query1);
        pst1.setString(1, booking_id);
        int row1 = pst1.executeUpdate();
        if (row1 == 1) {
            System.out.println("Booking cancelled successfully!");
            String paymentUpdateQuery = "UPDATE payments SET payment_status ='REFUNDED' WHERE booking_id = ?";
            PreparedStatement paymentQuery = con.prepareStatement(paymentUpdateQuery);
            paymentQuery.setString(1, booking_id);
            int amountPaid = paymentQuery.executeUpdate();
            if (amountPaid > 0) {
                System.out.println("Amount was refunded successfully");
            } else {
                System.out.println("unable to refund the amount");
            }
        } else {
            System.out.println("Booking cancellation failed!");
        }
    }

    public static void readEmployee(String bookingId) throws SQLException {
        String query = "SELECT * FROM bookings WHERE booking_id = ?";

        Connection con = Database.getconnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, bookingId);
        ResultSet rs = pst.executeQuery();

        if (!rs.next()) {
            System.out.println("You Did Not Book Yet");
            return;
        }

        System.out.println("==================== Your Booking Details ==========================");

        do {
            System.out.println("Booking Id: " + rs.getString("booking_id"));
            System.out.println("Passenger Id: " + rs.getString("passenger_id"));
            System.out.println("Bus Name: " + rs.getString("bus_name"));
            System.out.println("Number of Seats Booked: " + rs.getInt("number_of_seats"));
            System.out.println("Total Amount: " + rs.getInt("total_amount"));
            System.out.println("Booked Date: " + rs.getDate("booking_date"));
            System.out.println("Your Destination: " + rs.getString("passenger_destination"));
            System.out.println("Amount You Paid: " + rs.getInt("amount_paid"));
            System.out.println();
        } while (rs.next());

        System.out.println("==================================================================");

        con.close();
    }

}
