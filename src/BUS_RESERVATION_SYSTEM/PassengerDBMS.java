package BUS_RESERVATION_SYSTEM;

import java.sql.*;

public class PassengerDBMS {
    public static void readPassengenersDetails() throws SQLException {
        String query = "SELECT * FROM passengers";
        Connection con = Database.getconnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("===================== PASSENGERS DETAILS ================");
        while (rs.next()) {
            System.out.printf("Passengers ID: %d\tPassenger Name: %s\tAge: %d\tGender: %s\tEmail ID: %s", rs.getInt(1), rs.getString(2),
                    rs.getInt(3), rs.getString(4), rs.getString(5));
        }
        System.out.println("=========================================================");
        con.close();

    }
    public static String insertPassengerDetails(String ID, String name, int age, String gender, String email)
            throws SQLException {

        String checkEmail = "SELECT passenger_id FROM passengers WHERE email = ?";
        Connection con = Database.getconnection();
        PreparedStatement pst1 = con.prepareStatement(checkEmail);
        pst1.setString(1, email);
        ResultSet rs = pst1.executeQuery();

        // If email already exists
        if (rs.next()) {

            // Get EXISTING passenger_id
            String existingID = rs.getString("passenger_id");

            String updatePassenger =
                    "UPDATE passengers SET name = ?, age = ?, gender = ? WHERE email = ?";
            PreparedStatement updatepst = con.prepareStatement(updatePassenger);

            updatepst.setString(1, name);
            updatepst.setInt(2, age);
            updatepst.setString(3, gender);
            updatepst.setString(4, email);

            updatepst.executeUpdate();

            // Return existing passenger ID → VERY IMPORTANT
            return existingID;
        }

        // If new passenger → insert using new ID
        String insertQuery =
                "INSERT INTO passengers (passenger_id, name, age, gender, email) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(insertQuery);

        pst.setString(1, ID);
        pst.setString(2, name);
        pst.setInt(3, age);
        pst.setString(4, gender);
        pst.setString(5, email);

        int rows = pst.executeUpdate();
        if(rows==1)
        {
            System.out.println("Passenger Details Was Successfully Inserted!");
        }
        else
        {
            System.out.println("Passenger details was not inserted!");
        }

        // Return NEW passenger ID
        return ID;
    }



}
