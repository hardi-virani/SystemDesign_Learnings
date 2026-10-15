class Player_EncapsulationPrac {

    private String name;
    private int health;

    public Player_EncapsulationPrac(String name) {
        this.name = name;
        this.health = 100;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException();
        }

        health = health - amount;

        if(health < 0){
            health = 0;

        }
        if (!isAlive()) {
            System.out.println("Game over for " + name);
        }
    }

    public void heal (int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException();
        }
        health = health + amount;
        if(health > 100) {
            health = 100;
        }
    }

    private boolean isAlive() {
        return health > 0;
    }

}

