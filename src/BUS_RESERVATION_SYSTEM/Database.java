package BUS_RESERVATION_SYSTEM;
import java.sql.*;
public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/Bus_Reservation_System";
    private static final String USER = "root";
    private static final String PASSWORD = "Abi@2007";
    public static Connection getconnection() throws SQLException {
       return  DriverManager.getConnection(URL,USER,PASSWORD);
    }

    }