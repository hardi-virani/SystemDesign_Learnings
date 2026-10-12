public class BankAccount_forPracOfClassAndObj {

    String name;
    double balance;

    public BankAccount_forPracOfClassAndObj(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance = balance + amount;
    }

    public void withdraw(double amount) {

        if(amount > balance) {
            System.out.println("Insufficient funds");
        }
        balance = balance - amount;
    }

    public void showBalance() {
        System.out.println(balance);
    }

    public static void main(String[]args) {
        BankAccount_forPracOfClassAndObj acc1 = new BankAccount_forPracOfClassAndObj("Acc1", 100);
        acc1.withdraw(1000);
        acc1.showBalance();
    }

}

