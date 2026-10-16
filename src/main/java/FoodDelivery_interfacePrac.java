interface DeliveryPartner {
    //method
    void deliver(String item, String address);
}

class BikeDelivery implements DeliveryPartner {
    public void deliver(String item, String address) {
        System.out.println("Bike Delivering " +item+ " to " +address);
    }
}

class DroneDelivery implements DeliveryPartner {
    public void deliver(String item, String address) {
        System.out.println("Drone flying " +item+ " to " +address);
    }
}


public class FoodDelivery_interfacePrac {

    private DeliveryPartner Partner;


    FoodDelivery_interfacePrac(DeliveryPartner Partner) {
        this.Partner = Partner;
    }

    public void setDeliveryPartner(DeliveryPartner Partner) {
        this.Partner = Partner;
    }


    public void  placeOrder(String item, String address) {
        Partner.deliver(item, address);
    }

    public static void main(String[] args) {
        FoodDelivery_interfacePrac fd = new FoodDelivery_interfacePrac(new BikeDelivery());
        fd.placeOrder("pizza", "Boston");


        fd.setDeliveryPartner(new DroneDelivery());
        fd.placeOrder("burger", "seattle");

    }

}
