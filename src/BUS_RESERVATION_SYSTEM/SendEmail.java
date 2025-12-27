package BUS_RESERVATION_SYSTEM;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.util.Properties;

public class SendEmail {

    public static void getEmailNotification(String emailId,
                                            String booking_id,
                                            String passenger_id,
                                            String bus_name,
                                            int noSeats,
                                            int total_amount,
                                            String date,
                                            String source,
                                            String destination,
                                            int amount) {

        // Sender email credentials
        String fromEmail = "abisheikmister@gmail.com";           // Your email
        String appPassword = "trzq surf nkix flxz";      // App password (NOT Gmail password)

        // Receiver email
        String toEmail = emailId;

        // SMTP server settings
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Google SMTP server
        props.put("mail.smtp.port", "587");            // TLS port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            // Create message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject("Booking Confirmation - Abisheik.R's Bus Service");

            String emailBody = "Hello,\n\n" +
                    "Your bus booking was successful! Here are your booking details:\n\n" +
                    "Booking ID      : " + booking_id + "\n" +
                    "Passenger ID    : " + passenger_id + "\n" +
                    "Bus Name        : " + bus_name + "\n" +
                    "Number of Seats : " + noSeats + "\n" +
                    "Total Paid      : " + total_amount + "\n" +
                    "Date of Journey : " + date + "\n" +
                    "Source     : " + source + "\n" +
                    "Destination     : " + destination + "\n\n" +
                    "Thank you for booking with us!\n" +
                    "Have a safe journey.\n\n" +
                    "Regards,\n" +
                    "Abisheik.R Bus Service";

            msg.setText(emailBody);


            // Send email
            Transport.send(msg);

            System.out.println("\nEmail sent successfully! Please Check a Mail For Details");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Email sending failed.");
        }
    }
}
