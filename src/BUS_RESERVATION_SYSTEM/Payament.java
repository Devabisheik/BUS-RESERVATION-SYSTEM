package BUS_RESERVATION_SYSTEM;

import java.sql.*;
import java.util.UUID;

public class Payament {

    private static String paymentID;

    public static String generatePaymentId() {
        return "PID" + UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
    }

    public static void addPaymentDetails(String bookingId, String passengerId, int amountPaid, int totalAmount, String paymentStatus) throws SQLException {
        paymentID = generatePaymentId();
        String query = "INSERT INTO payments (payment_id,booking_id,passenger_id,amount_paid,payment_status) VALUES(?,?,?,?,?)";
        Connection con = Database.getconnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, paymentID);
        pst.setString(2, bookingId);
        pst.setString(3, passengerId);
        pst.setInt(4, amountPaid);
        pst.setString(5, paymentStatus);
        int rows = pst.executeUpdate();
        if (rows == 1) {
            System.out.println("Payment details was inserted successfully");
        } else {
            System.out.println("Payment details was noot inserted!");

        }
        con.close();
    }

    public static void viewPaymentDetails(String bookigId, String passengerId) throws SQLException {
        String query = "SELECT * FROM payments WHERE booking_id = ? AND passenger_id = ?";
        Connection con = Database.getconnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, bookigId);
        pst.setString(2, passengerId);
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            System.out.println("There is no payments Details available in database");
            return;
        } else {
            System.out.println("========================= Your Payments details ====================");
            System.out.println("PASSENGER ID: " + rs.getString(1));
            System.out.println("BOOKING ID: " + rs.getString(2));
            System.out.println("PASSENGER ID: " + rs.getString(3));
            Timestamp ts = rs.getTimestamp("payment_date");
            String paymentDate = ts.toLocalDateTime().toString();
            System.out.println("AMOUNT PAID: " + rs.getInt(4));
            System.out.println("PAID DATE: " + paymentDate);
            System.out.println("PAYMENT STATUS: " + rs.getString(6));
        }
        System.out.println("====================================================================");

        con.close();

    }
}
