//Balance being encapsulated is missing otherwise this program would have been quite different, but for practice purpose this is fine.
// Protected means, that variable can be access within the same package, but if the main would have been in another folder than we wouldn't been able to access the balance, but also for practice we can make it balance private to achive encapsulation in this program but its fine for now.

class Account {
    protected String owner;
    protected double balance;

    public Account(String owner, double balance) {
        this.owner = owner;
        this.balance = balance;
    }

//    public String setOwner(String owner) {
//        this.owner = owner;
//    }
//
//    public double setBalance(double balance) {
//        this.balance = balance;
//    }

    public void deposit(double amount) {
        balance = balance + amount;
    }

    public void withdraw(double amount) {
        if(amount > balance){
            throw new IllegalArgumentException ("Not enoungh funds");
        } else {
            balance = balance - amount;
        }
    }

    public void displayInfo() {
        System.out.println(owner+" this is your balance: "+balance);
    }

}

class SavingsAccount extends Account {
    private double interestRate ; //5 for 5%

    public SavingsAccount(String owner, double balance) {
        super(owner, balance);
        this.interestRate = 5;
    }

    public void addInterest() {
        balance = balance + (balance * (interestRate/ 100));
//        System.out.println("Interest rate is "+balance);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Interest rate is: "+interestRate+ "%");
    }

}

class CurrentAccount extends Account {
    private double overdraftLimit  = 500;

    public CurrentAccount (String owner, double balance) {
        super(owner, balance);
    }

    @Override
    public void withdraw(double amount) {
        if(balance - amount < -overdraftLimit) {
            throw new IllegalArgumentException("Your overall balance should be atleast "+overdraftLimit);
        } else {
            balance = balance - amount;
        }
    }

}
public class BankAccount_InheritancePrac {

    public static void main(String[] args) {

        Account basis = new Account("Basic", 500);
        try {
            basis.withdraw(1000);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        SavingsAccount acc1 = new SavingsAccount("Hardi", 1000);
        acc1.addInterest();
        acc1.displayInfo();
        acc1.withdraw(10);
        acc1.displayInfo();

        Account acc2 = new CurrentAccount("Hardi Current account", 1000);
        acc2.withdraw(1300);
        acc2.displayInfo();



    }

}
