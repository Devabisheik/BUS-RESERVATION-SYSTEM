package BUS_RESERVATION_SYSTEM;
import java.sql.*;
class Bus {
    private String busName;
    private String source;
    private String busNumber;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private boolean[] seatStatus;
    private int perSeatAmount;

    public Bus(String busName, String busNumber, String source,String destination, int totalSeats, int perSeatAmount) {
        this.busName = busName;
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.seatStatus = new boolean[totalSeats];
        this.perSeatAmount = perSeatAmount;
    }

    public int getPerSeatAmount() {
        return perSeatAmount;
    }

    public void setPerSeatAmount(int perSeatAmount) {
        this.perSeatAmount = perSeatAmount;
    }

    public boolean[] getSeatStatus() {
        return seatStatus;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getBusName() {
        return busName;
    }

    public boolean isAvailable() {
        return availableSeats > 0;
    }




    public void releaseSeat(int seatNo) {
        if (!seatStatus[seatNo]) return;
        seatStatus[seatNo] = false;
        availableSeats++;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public String getBusDetails() {
        return "\nBUS NAME: " + busName +
                "\tBUS NUMBER: " + busNumber +
                "\tDESTINATION: " + destination +
                "\tPER HEAD AMOUNT: " + perSeatAmount +
                "\tTOTAL SEATS: " + totalSeats +
                "\tAVAILABLE SEATS: " + availableSeats;
    }

    public int calculateAmount(int numberSeat) {
        return getPerSeatAmount() * numberSeat;
    }


}
