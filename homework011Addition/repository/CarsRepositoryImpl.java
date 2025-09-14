package repository;

import model.Car;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CarsRepositoryImpl implements repository.CarsRepository {
    private List<Car> cars = new ArrayList<>();

    private void ensureDirectoryExists(String filename) {
        File file = new File(filename);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                System.out.println("Создана папка: " + parentDir.getAbsolutePath());
            }
        }
    }

    @Override
    public List<Car> loadCarsFromFile(String filename) {
        List<Car> loadedCars = new ArrayList<>();
        ensureDirectoryExists(filename);

        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Файл не существует: " + filename);
            return loadedCars;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Пропускаем пустые строки и комментарии
                }

                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    try {
                        String number = parts[0].trim();
                        String model = parts[1].trim();
                        String color = parts[2].trim();
                        long mileage = Long.parseLong(parts[3].trim());
                        long cost = Long.parseLong(parts[4].trim());
                        loadedCars.add(new Car(number, model, color, mileage, cost));
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка в строке " + lineNumber + ": неверный формат числа");
                    }
                } else {
                    System.err.println("Ошибка в строке " + lineNumber + ": ожидается 5 полей, получено " + parts.length);
                }
            }
            System.out.println("Загружено " + loadedCars.size() + " автомобилей из файла: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        cars = loadedCars;
        return loadedCars;
    }

    @Override
    public void saveCarsToFile(List<Car> cars, String filename) {
        ensureDirectoryExists(filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("# Number|Model|Color|Mileage|Cost");
            for (Car car : cars) {
                writer.println(car.getNumber() + "|" + car.getModel() + "|" +
                        car.getColor() + "|" + car.getMileage() + "|" + car.getCost());
            }
            System.out.println("Данные сохранены в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    @Override
    public void saveResultsToFile(String results, String filename) {
        ensureDirectoryExists(filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(results);
            System.out.println("Результаты сохранены в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при записи результатов: " + e.getMessage());
        }
    }

    @Override
    public List<String> findNumbersByColorOrMileage(String color, long mileage) {
        return List.of();
    }

    @Override
    public long countUniqueModelsInPriceRange(long minPrice, long maxPrice) {
        return 0;
    }

    @Override
    public String findColorOfMinCostCar() {
        return "";
    }

    @Override
    public double findAverageCostByModel(String model) {
        return 0;
    }

    // ... остальные методы без изменений ...
}