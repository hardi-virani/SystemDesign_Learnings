public class abstractPrac_main {

    public static void main(String[] args) {
        payroll_abstractPrac Fulltimer = new payroll_abstractPrac(new FulltimeEmployee("Hardi", 10000)) ;
        Fulltimer.run();


        payroll_abstractPrac intern = new payroll_abstractPrac(new Intern("HardiIntern", 100, 30)) ;
        intern.run();

        payroll_abstractPrac internTest = new payroll_abstractPrac(new Intern("Test", -29, 2));
        internTest.run();

    }
}
