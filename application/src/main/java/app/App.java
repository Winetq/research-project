package app;

public class App {

    public static void main(String[] args) {
        Calculator calculator = new Calculator(8, 4);
        System.out.println(calculator.add());
        System.out.println(calculator.subtract());
    }

}
