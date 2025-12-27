package BUS_RESERVATION_SYSTEM;

import java.time.*;
import java.util.*;

class Booking {
    private String bookingId = generateBookingId();
    private Passenger passenger;
    private Bus bus;
    private int[] seatNumbers;
    private Ticket ticket;
    private LocalDate bookingDate;
    private String status;
    private int bookedSeats;

    Booking(Passenger p, Bus b, int[] seatNumbers, int count,LocalDate date) {
        this.passenger = p;
        this.bus = b;
        this.seatNumbers = seatNumbers;
        this.bookedSeats = count;
        this.bookingDate =date;
        ticket = new Ticket(
                passenger,
                bus,
                seatNumbers,
                bookingId,
                bookingDate,
                bookedSeats
        );
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public String generateBookingId() {
        //uniform unique identifier it give a random 128 bit value contain "-" is
        // replace by empty char and convert to substring of size 7
        return "BID" + UUID.randomUUID().toString().replace("-", "").substring(0, 7).toUpperCase();
    }


    public String getBookingId() {
        return bookingId;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getBookingDetails() {
        return "BOOKING ID: " + bookingId + "\n"
                + "PASSENGER: " + passenger.getName() + "\n"
                + "BUS: " + bus.getBusName() + " (" + bus.getBusNumber() + ")\n"
                + "SEATS: " + Arrays.toString(seatNumbers) + "\n"
                + "DATE: " + bookingDate + "\n";
    }

}