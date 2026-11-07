package Class_Relationships;


class Bill {
    private String Customer_name;
    private double amount;

    public Bill(String Customer_name, double amount) {
        this.Customer_name = Customer_name;
        this.amount = amount;
    }
}

class TaxCalculator {
    public static double addTax(double amount) {
        amount = amount + (18/100);
        return amount;
    }
}

class Receipt {
    private String Customer_name;
    private final double total;

    public Receipt(String Customer_name, double total) {
        this.Customer_name = Customer_name;
        this.total = total;
    }

    public String getSummary() {
        return Customer_name + ", $" +total;
    }
}

class BillService {

    static Receipt checkout(String Customer_name, double amount) {
        TaxCalculator.addTax(amount);
        Receipt receipt = new Receipt(Customer_name, amount);
        return receipt;
    }

}



class Printer {

    public Printer(Receipt receipt) {
        this.print(receipt);
    }

    public void print(Receipt receipt) {
        System.out.println(receipt.getSummary());
    }
}

public class BillingService_DependencyPrac {
    public static void main(String[] args) {
        Bill bill = new Bill("Hardi", 12);
//        Printer p = new Printer("something");

        Receipt receipt = new Receipt("Hardi", 12);
        Printer printer = new Printer(receipt);
        BillService.checkout("Hardi", 12);



    }
}

