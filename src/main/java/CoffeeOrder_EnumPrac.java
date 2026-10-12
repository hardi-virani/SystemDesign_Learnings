enum CoffeeSize {
    SMALL(3),
    MEDIUM(4),
    LARGE(5);

    private final int price;

    CoffeeSize(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}


enum CoffeeStatus {
    ORDERED,
    BREWING,
    READY,
    PICKED_UP
}

public class CoffeeOrder_EnumPrac {

    String name;
    CoffeeSize size;
    CoffeeStatus status;

    CoffeeOrder_EnumPrac(String name, CoffeeSize size, CoffeeStatus status) {
        this.name = name;
        this.size = size;
        this.status = CoffeeStatus.ORDERED;
    }


    public boolean advanceStatus() {
        switch (status) {
            case ORDERED:
                status = CoffeeStatus.BREWING;
                return true;

            case BREWING:
                status = CoffeeStatus.READY;
                return true;

            case READY:
                status = CoffeeStatus.PICKED_UP;
                return true;

            default:
                return false;
        }
    }

    public void showOrder() {
        System.out.println(name+ " your order of " +size+ " for $" +size.getPrice()+ " is " + status);
    }

    public static void main(String[] args) {
        CoffeeOrder_EnumPrac order1 = new CoffeeOrder_EnumPrac("Hardi", CoffeeSize.LARGE, CoffeeStatus.ORDERED);
        order1.advanceStatus();
        order1.advanceStatus();
        order1.showOrder();
    }

}
