package test;

import model.Car;

public class CarTest {
    public static void main(String[] args) {
        // Тестирование класса Car
        Car car1 = new Car("a123me", "Mercedes", "White", 0, 8300000);
        Car car2 = new Car("a123me", "Mercedes", "White", 0, 8300000);

        System.out.println("Тестирование класса Car:");
        System.out.println("Car 1: " + car1);
        System.out.println("Car 2: " + car2);
        System.out.println("Equals: " + car1.equals(car2));
        System.out.println("HashCode car1: " + car1.hashCode());
        System.out.println("HashCode car2: " + car2.hashCode());

        // Тестирование геттеров
        System.out.println("Номер: " + car1.getNumber());
        System.out.println("Модель: " + car1.getModel());
        System.out.println("Цвет: " + car1.getColor());
        System.out.println("Пробег: " + car1.getMileage());
        System.out.println("Стоимость: " + car1.getCost());
    }
}
