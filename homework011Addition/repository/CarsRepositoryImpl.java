package repository;

import model.Car;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CarsRepositoryImpl implements CarsRepository {
    private List<Car> cars = new ArrayList<>();

    @Override
    public List<Car> loadCarsFromFile(String filename) {
        List<Car> loadedCars = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String number = parts[0];
                    String model = parts[1];
                    String color = parts[2];
                    long mileage = Long.parseLong(parts[3]);
                    long cost = Long.parseLong(parts[4]);
                    loadedCars.add(new Car(number, model, color, mileage, cost));
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка формата числа: " + e.getMessage());
        }
        cars = loadedCars;
        return loadedCars;
    }

    @Override
    public void saveCarsToFile(List<Car> cars, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Number|Model|Color|Mileage|Cost");
            for (Car car : cars) {
                writer.println(car.getNumber() + "|" + car.getModel() + "|" +
                        car.getColor() + "|" + car.getMileage() + "|" + car.getCost());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    @Override
    public void saveResultsToFile(String results, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println(results);
            writer.println("=".repeat(50));
        } catch (IOException e) {
            System.err.println("Ошибка при записи результатов: " + e.getMessage());
        }
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public List<String> findNumbersByColorOrMileage(String color, long mileage) {
        return cars.stream()
                .filter(car -> car.getColor().equals(color) || car.getMileage() == mileage)
                .map(Car::getNumber)
                .collect(Collectors.toList());
    }

    @Override
    public long countUniqueModelsInPriceRange(long minPrice, long maxPrice) {
        return cars.stream()
                .filter(car -> car.getCost() >= minPrice && car.getCost() <= maxPrice)
                .map(Car::getModel)
                .distinct()
                .count();
    }

    @Override
    public String findColorOfMinCostCar() {
        return cars.stream()
                .min(Comparator.comparingLong(Car::getCost))
                .map(Car::getColor)
                .orElse("Не найдено");
    }

    @Override
    public double findAverageCostByModel(String model) {
        return cars.stream()
                .filter(car -> car.getModel().equals(model))
                .mapToLong(Car::getCost)
                .average()
                .orElse(0.0);
    }
}
