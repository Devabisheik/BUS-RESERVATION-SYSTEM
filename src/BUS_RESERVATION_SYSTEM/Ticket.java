package BUS_RESERVATION_SYSTEM;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class Ticket {
    private String ticketId;
    private Passenger passenger;
    private Bus bus;
    private int[] seatNumbers;
    private LocalDate issueDate;
    private String bookingId;
    private LocalTime depatureTime;
    private int bookedSeats;

    Ticket(Passenger p, Bus b, int[] seatNumbers, String Id, LocalDate date, int bookedSeats) {
        this.passenger = p;
        this.bus = b;
        this.seatNumbers = seatNumbers;
        this.bookingId = Id;
        this.issueDate = date;
        this.ticketId = generateTicketId();
        this.bookedSeats = bookedSeats;
    }
    Ticket()
    {
        System.out.println("|n");
    }
    public String generateTicketId() {
        return "TID" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 8).toUpperCase();
    }

    public void printTicket() {
        System.out.println("=============== YOUR TICKET DETAILS ==============");
        System.out.println("TICKET ID: " + this.ticketId);
        System.out.println("BUS NAME: " + bus.getBusName());
        System.out.println("YOUR NAME: " + passenger.getName());
        System.out.println("YOUR AGE: " + passenger.getAge());
        System.out.println("DATE: " + this.issueDate);
        System.out.println("BILL AMOUNT: " + bus.calculateAmount(this.bookedSeats));
        System.out.println("DESTINATION: " + bus.getDestination());
        System.out.println("BOOKING ID: " + this.bookingId);
        System.out.println("SEAT NUMBERS: " + Arrays.toString(seatNumbers));
        System.out.println("................HAPPY JOURNEY.............");
    }

    public String getTicketDetails() {
        return
                "🎫✨ *YOUR TICKET DETAILS* ✨🎫\n" +
                        "---------------------------------------------\n" +
                        "🚌 Bus Name       : " + bus.getBusName() + "\n" +
                        "👤 Passenger Name : " + passenger.getName() + "\n" +
                        "🎂 Age            : " + passenger.getAge() + "\n" +
                        "📅 Date           : " + this.issueDate + "\n" +
                        "💰 Bill Amount    : " + bus.calculateAmount(this.bookedSeats) + "\n" +
                        "📍 Destination    : " + bus.getDestination() + "\n" +
                        "📘 Booking ID     : " + this.bookingId + "\n" +
                        "💺 Seat Numbers   : " + Arrays.toString(seatNumbers) + "\n" +
                        "---------------------------------------------\n" +
                        "😊✨ *Wishing You a Happy & Safe Journey!* ✨😊\n";
    }

}