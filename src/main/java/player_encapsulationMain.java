public class player_encapsulationMain {

    public static void main(String[] args) {
        Player_EncapsulationPrac P = new Player_EncapsulationPrac("Hardi");
        P.takeDamage(50);
        System.out.println(P.getHealth());
        P.heal(20);
        System.out.println(P.getHealth());
        P.takeDamage(700);
        System.out.println(P.getHealth());
    }
}