package Design_Principles;

//class Price_and_TaxFormat {
//    public static double calcPrice_and_Tax(double price) {
//         double tax = price * 0.18;
//        double total = price + tax;
//        return total;
//    }
//}

class TaxCalculator {
    private static final double Tax_Rate = 0.18;

    public static double addTax(double price) {
        return price + (price * Tax_Rate);
    }
}

class PriceFormatter {
    public static String format (double amount) {
        return "Rs. " + String.format("%.2f", amount);
    }
}

class GroceryStore {

    private static final double Free_Delivery_Threshold = 500;

    public void checkout(double price) {
        double total = TaxCalculator.addTax(price);
        System.out.println("Total: Rs. " + String.format("%.2f", total));
        if (total >= Free_Delivery_Threshold) {
            System.out.println("You get free delivery!");
        }
    }
}

class Pharmacy {

    private static final double Free_Delivery_Threshold = 500;

    public void checkout(double price) {

        double total = TaxCalculator.addTax(price);

        System.out.println("Total: Rs. " + String.format("%.2f", total));
        if (total >= Free_Delivery_Threshold) {
            System.out.println("You get a free health checkup coupon!");
        }
    }
}

class Electronics {

    private static final double Free_Delivery_Threshold = 500;

    public void checkout(double price) {
        double total = TaxCalculator.addTax(price);
        System.out.println("Total: Rs. " + String.format("%.2f", total));
        if (total >= Free_Delivery_Threshold) {
            System.out.println("You get an extended warranty!");
        }
    }
}

public class DRYPrac_GroceryStore {
    public static void main(String[] args) {

        GroceryStore groceryStore = new GroceryStore();
        groceryStore.checkout(500);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.checkout(900);

        Electronics electronics = new Electronics();
        electronics.checkout(1000);

    }
}
