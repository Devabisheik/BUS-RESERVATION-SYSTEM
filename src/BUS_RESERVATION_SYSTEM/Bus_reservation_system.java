package BUS_RESERVATION_SYSTEM;

import java.sql.SQLException;
import java.util.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;

public class Bus_reservation_system {
    //    private static final ArrayList<Booking> bookings = new ArrayList<>();GGGG
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean loopEngine = true;
        byte option = -1;
        while (loopEngine) {

            showMenu();
            System.out.print("Choose an option: ");
            try {
                option = scanner.nextByte();
            } catch (InputMismatchException e) {
                System.out.println("Caught! " + "Please enter a correct options");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine(); // consume newline
            switch (option) {

                case 1:  // Add Bus
                    showBus();
                    break;

                case 2:  // Search Bus
                    try {
                        System.out.println("Enter source to search: ");
                        String source = scanner.nextLine();
                        System.out.println("Enter destination to search: ");
                        String destination = scanner.nextLine();
                        Bus bus = BusDBMS.searchBuses(source,destination);
                        if (bus != null) {
                            System.out.println("Bus Found: " + bus.getBusName() + " (" + bus.getBusNumber() + ")");
                            System.out.println("per seat amount: " + bus.getPerSeatAmount());
                            System.out.println("Available Seats: " + bus.getAvailableSeats());
                        } else {
                            System.out.println("No bus found for this route.");
                        }
                    } catch (SQLException e) {
                        System.out.println("SQLERROR! " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("ERROR! " + e.getMessage());
                    }

                    break;

                case 3:  // Create Booking
                    try {
                        createBookingFlow();
                    } catch (InputMismatchException e) {
                        System.err.println("Caught! " + "please give correct input");
                    } catch (RuntimeException e) {
                        System.err.println("Caught! " + "Please give the correct input");
                    }
                    break;

                case 4:  // Cancel Booking
                    cancelBooking();
                    break;

                case 5:  // List All Bookings
                    listBookings();
                    break;

                case 6:
                    paymentDetails();
                    break;
                case 7:  // Exit
                    loopEngine = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    // ---------------- MENU ----------------
    public static void showMenu() {
        System.out.println("\n==================== MENU ========================");
        System.out.println("1. Show Bus");
        System.out.println("2. Search Bus");
        System.out.println("3. Create Booking");
        System.out.println("4. Cancel Booking");
        System.out.println("5. Show All Bookings");
        System.out.println("6. view payment details");
        System.out.println("7. Exit");
        System.out.println("==================================================");
    }

    public static void paymentDetails()
    {
        System.out.println("Enter a Booking ID: ");
        String bookingId = scanner.nextLine();
        System.out.println("Enter a Passenger ID: ");
        String passengerId = scanner.nextLine();
        try {
            Payament.viewPaymentDetails(bookingId, passengerId);
        }
        catch (SQLException e)
        {
            System.out.println(" SQL  CAUGHT!: " + e.getMessage());
        }
    }

    // ---------------- ADD BUS ----------------
    public static void showBus() {
        System.out.println("================ AVAILABLE BUSES ==================");
        try {
            BusDBMS.readBuses();
        } catch (SQLException e) {
            System.out.println("CAUGHT! " + e.getMessage());
        }
        System.out.println("===================================================");

    }
    // ---------------- CREATE BOOKING FLOW ----------------

    public static void createBookingFlow() throws NumberFormatException, InputMismatchException {

        // ------------------ Passenger Details ------------------
        System.out.print("Enter Passenger Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Gender(male/female): ");
        String gender = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter a Source: ");
        String source = scanner.nextLine();
        System.out.print("Enter Destination: ");
        String dest = scanner.nextLine();

        Bus bus = null;
        Passenger p = null;
        String existingID;

        try {
            validatePassengerDetails(age, gender, email);
            p = new Passenger(name, age, gender, email);
            existingID = PassengerDBMS.insertPassengerDetails(
                    p.getPassengerID(), name, age, gender, email
            );

            bus = BusDBMS.searchBuses(source,dest);

        } catch (PassengerDetailsValidation e) {
            System.err.println("Caught! " + e.getMessage());
            System.out.println("Your booking was cancelled. Try again!");
            return;
        } catch (SQLException e) {
            System.err.println("Caught! " + e.getMessage());
            return;
        }

        if (bus == null) {
            System.out.println("No bus available for this route!");
            return;
        }

        // ------------------ Seat Count ------------------
        System.out.println("Available seats: " + bus.getAvailableSeats());
        System.out.print("How many seats do you want? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        if (bus.getAvailableSeats() < count) {
            System.out.println("Sorry! There are not enough seats available.");
            return;
        }

        int[] seats = new int[count];

        // ------------------ Travel Date ------------------
        System.out.print("Enter Date to Travel (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.next());

        // ------------------ Payment Section ------------------
        int totalAmount = ShortestPathService.calculateCost(source,dest);
        System.out.println("Pay a bill amount for: " + totalAmount);
        System.out.print("Enter amount: ");
        int amount = scanner.nextInt();

        // Prepare Booking object
        Booking booking = new Booking(p, bus, seats, count, date);

        try {
//            paymentProcess(amount, totalAmount,booking.getBookingId(),existingID);

            BookingDBMS.insertBookingDetails(
                    booking.getBookingId(),
                    existingID,
                    bus.getBusName(),
                    count,
                    totalAmount,
                    date,
                    bus.getSource(),
                    bus.getDestination(),
                    amount,
                    p.getEmail()
            );
            paymentProcess(amount, totalAmount,booking.getBookingId(),existingID);
            System.out.println("Payment Successfully completed");
        } catch (RuntimeException | SQLException e) {
            System.err.println("Caught! " + e.getMessage());
            return;
        }

        // ------------------ Seat Allocation (Corrected!) ------------------
        try {
            for (int i = 0; i < count; i++) {
                System.out.print("Enter seat number " + (i + 1) + ": ");

                int seatNo = scanner.nextInt();
                scanner.nextLine();

                boolean allocated = BusDBMS.allocateSeat(
                        seatNo,
                        bus.getBusName(),
                        existingID,
                        booking.getBookingId()
                );

                if (allocated) {
                    System.out.println("Seat " + seatNo + " successfully booked!");
                    seats[i] = seatNo;
                } else {
                    System.out.println("Invalid seat number or already booked!");
                    i--; // retry the same seat
                }
            }
        } catch (SQLException e) {
            System.out.println(" booked_seats Table Caught! " + e.getMessage());
        }
    }


    public static void validatePassengerDetails(int age, String gender, String email) throws PassengerDetailsValidation {
        if (age <= 12 || age > 100) {
            throw new PassengerDetailsValidation("Invalid age! Kindly give a Valid age number or Under age problem");
        }
        if (email.length() <= 8 ||
                !(email.endsWith("@gmail.com") || email.endsWith("@kce.ac.in"))) {
            throw new PassengerDetailsValidation(
                    "Invalid email Id. Please provide a valid Gmail or KCE email with minimum length."
            );
        }

        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female") &&
                gender.equalsIgnoreCase("others")) {
            throw new PassengerDetailsValidation("Invalid gender! please give correct gender");
        }
    }

    // ---------------- CANCEL BOOKING ----------------
    public static void cancelBooking() {
        System.out.print("Enter a Booking Id to Cancel Booking: ");
        String bookingId = scanner.next();
        try {
            BookingDBMS.cancelBooking(bookingId);
        } catch (Exception e) {
            System.out.println("Caught! " + e.getMessage());
        }

    }

    // ---------------- LIST ALL BOOKINGS ----------------
    public static void listBookings() {
        System.out.println("Enter a Booking ID To view Booking Details: ");
        String bookingId = scanner.next();
        try {
            BookingDBMS.readEmployee(bookingId);
        } catch (SQLException e) {
            System.out.println("CAUGHT! " + e.getMessage());
        }
    }

    public static void paymentProcess(int amount, int totalAmount,String bookingId,String passengerId) throws PaymentException , SQLException{
        String status = "PENDING";
        if (amount < 0 || amount > totalAmount) {
            status = "FAILED";
            throw new PaymentException("Your amount was Out of Bound, so kindly pay respective amount");
        }
        if (amount < totalAmount) {
            status = "FAILED";
            throw new PaymentException("Your amount was insufficient you need extra " + (totalAmount - amount) + " rupees");
        }
        status ="SUCCESS";
        Payament.addPaymentDetails(bookingId,passengerId,amount,totalAmount,status);
    }
}