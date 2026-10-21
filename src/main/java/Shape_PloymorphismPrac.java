import java.util.List;
import java.sql.SQLOutput;

abstract class Shape {
    protected String name;

    public Shape(String name) {
        this.name = name;
    }


    abstract double area();

    public void describe() {
        System.out.println("This is "+name+ " and the area is "+area());
    }
}


class Circle extends Shape {

    public double radius;

    @Override
    double area() {
        return Math.PI * radius * radius;
    }

    Circle(double radius) {
        super("Circle");
        this.radius = radius;
    }


}

class Rectangle extends Shape {

    double height;
    double width;

    @Override
    double area() {
        return height * width;
    }

    Rectangle(double height, double width) {
        super("Rectangle");
        this.height = height;
        this.width = width;
    }

    //For Square (Method Overloading)
    Rectangle(double side) {
        super("Square");
        this.height = side;
        this.width = side;
    }




}

class Triangle extends Shape {

    double height;
    double base;

    Triangle(double height, double base) {
        super("Triangle");
        this.height = height;
        this.base = base;
    }

    @Override
    double area() {
        return 0.5 * height * base;
    }
}



public class Shape_PloymorphismPrac {

    public static void main(String[] args) {
        List<Shape> shapeList = List.of (
                new Circle(3),
                new Rectangle(2,4),
                new Rectangle(2),
                new Triangle(3,5)
        );


        for (Shape x : shapeList) {
            x.describe();
        }

        double total_area = 0.0;
        for(Shape x : shapeList) {
            total_area = total_area + x.area();
        }

        System.out.println("The total area is " + total_area);

    }

}
