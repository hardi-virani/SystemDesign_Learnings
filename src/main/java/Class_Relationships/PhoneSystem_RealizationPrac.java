package Class_Relationships;

import java.util.ArrayList;
import java.util.List;

interface Chargeable {

    void charge();

    void getBatteryLevel();

}

interface Trackable {

    void getLocation();
}

class Vehicle {

    private final String name;

    public Vehicle(String name) {
        this.name = name;
    }

    public void move() {
        System.out.println("Moving " + this.name);

    }
}
class Phone implements Chargeable {

        private String name;
        public Phone(String name) {
            this.name = name;
        }

        @Override
        public void charge() {
            System.out.println(this.name + " is charging");
        }

        @Override
        public void getBatteryLevel() {
            System.out.println(this.name + " has 20% battery level");

        }


}

class Parcel implements Trackable {
        private final String name;

        public Parcel(String name) {
            this.name = name;
        }

    @Override
    public void getLocation() {
        System.out.println(this.name+ " is at Boston location");

    }
}

class ElectricVehicle extends Vehicle implements Chargeable, Trackable {

        public ElectricVehicle(String name) {
            super(name);
    }

    @Override
    public void charge() {
        System.out.println("ElectricVehicle is charging");

    }

    @Override
    public void getBatteryLevel() {
        System.out.println("ElectricVehicle is at 45% battery level");
    }

    @Override
    public void getLocation() {
            System.out.println("ElectricVehicle is at Boston location");
    }
}



public class PhoneSystem_RealizationPrac {
    public static void main(String[] args) {

        List<Chargeable> charge = new ArrayList<>();
        charge.add(new Phone("Iphone"));
        charge.add(new ElectricVehicle("Iphone"));


        List<Trackable> track = new ArrayList<>();
        track.add(new Parcel("MacBook Air"));
        track.add(new ElectricVehicle("Scooter"));

        for(Chargeable c : charge) {
            System.out.println("Charging all of them");
            c.charge();
            System.out.println();
        }

        for(Trackable t : track) {
            System.out.println("Tracking all of them");
            t.getLocation();
            System.out.println();
        }

    }
}
