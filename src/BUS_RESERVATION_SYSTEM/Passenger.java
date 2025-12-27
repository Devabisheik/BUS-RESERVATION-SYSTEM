package BUS_RESERVATION_SYSTEM;

import java.util.UUID;

class Passenger {
    private String name;
    private int age;
    private String gender;
    private String email;
    private String passengerID = generatePassengersId();
    public Passenger(String name, int age, String gender, String email) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
    }

    public String generatePassengersId() {
        //uniform unique identifier it give a random 128 bit value contain "-" is
        // replace by empty char and convert to substring of size 7
        return "PID" + UUID.randomUUID().toString().replace("-", "").substring(0,5).toUpperCase();
    }
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getemail() {
        return email;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public String getEmail() {
        return email;
    }
}