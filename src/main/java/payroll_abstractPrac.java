abstract class Employee{

    //field
    protected String name;

    //constructor
    public Employee(String name) {
        this.name = name;
    }

    //abstract method
    abstract double calculatePay();

    //Concrete method
    void printPayslip() {
        System.out.println(name + " Your Salary is: "+calculatePay());
    }

}

class FulltimeEmployee extends Employee {

    private double monthlySalary;

    public FulltimeEmployee(String name, int monthlySalary) {
        // super(level) = calling parent's constructor
        super(name); //parent class field
        this.monthlySalary = monthlySalary;
    }

    //parent class method
    @Override
    double calculatePay() {
        return monthlySalary;
    }

}

class Intern extends Employee{
    private double hourlyrate;
    private double hoursworked;

    public Intern(String name, int hourlyrate, int hoursworked) {
        super(name);
        this.hourlyrate = hourlyrate;
        this.hoursworked = hoursworked;

        if(hourlyrate < 0 || hoursworked < 0) {
            throw  new IllegalArgumentException("Hourly rate OR Hoursworked cannot be negative");
        }
    }

    @Override
    double calculatePay() {
        return hourlyrate * hoursworked;
    }
}

public class payroll_abstractPrac {

    private Employee emp;

    public payroll_abstractPrac(Employee emp) {
        this.emp = emp;
    }

    public void run() {
        emp.printPayslip();
    }

}
