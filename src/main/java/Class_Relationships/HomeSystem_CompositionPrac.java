package Class_Relationships;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

class Owner {

    private final String name;

    public Owner(String name) {
        this.name = name;
    }

    public String getName() { return name; }


}

class House {

    private final String address;
    private Owner owner;
    private List<Room> rooms = new ArrayList<>();

    public House(String address, Owner owner) {
        this.address = address;
        this.owner = owner; // AGGREGATION: owner comes from outside
    }

    public void addRoom(String name, double area) {
        rooms.add(new Room(name, area)); // COMPOSITION: house creates its rooms

    }

    public void removeRoom(String name) {
        rooms.removeIf(room -> room.getName().equals(name));
    }

    public double getTotalArea() {
        double totalarea = 0;
        for (Room room : rooms) {
            totalarea += room.getArea();
        }
        return totalarea;
    }


    public void printLayout() {
        System.out.println("Address: "+address + " Owner: "+owner.getName());
        System.out.println("Rooms: ");
        for(Room r : rooms) {
           r.describe();

        }
        System.out.println("Total Area of House: "+getTotalArea());
    }


}

class Room {

    private String name;
    private double area;
    
    public Room(String name, double area) {
        this.name = name;
        this.area = area;
    }

    public double getArea() {
        return area;
    }

    public void describe() {
        System.out.print("Room Name: " +name+ " ");
        System.out.println("Area: "+area);
    }

    public String getName() {
        return name;
    }

}

public class HomeSystem_CompositionPrac {

    public static void main(String[] args) {
        Owner o1 = new Owner("Hardi");
        House h1 = new House("677C", o1);
        h1.addRoom("Study Room", 1.5);
        h1.addRoom("Powder Room", 1.5);
        h1.addRoom("Bed Room", 1.5);

        h1.removeRoom("Powder Room");

        h1.printLayout();

        h1 = null;

        System.out.println("Owner Still Exists: " +o1.getName());



    }
}
