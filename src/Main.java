import Test.Benz;
import Test.Byd;
import Test.Car;

public class Main {
    public static void test(Object object) {
        Car car = (Car) object;
        car.run();
    }

    public static void main(String[] args) {
        Byd benz = new Byd();
        test(benz);
    }
}
