package test;

import model.Car;
import repository.CarsRepository;
import repository.CarsRepositoryImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CarsRepository repository = new CarsRepositoryImpl();

        // Загрузка данных из файла
        System.out.println("Загрузка данных из файла...");
        List<Car> cars = repository.loadCarsFromFile("repository/cars_input.txt");

        // Вывод всех автомобилей в консоль
        System.out.println("Автомобили в базе:");
        System.out.println("Number Model Color Mileage Cost");
        for (Car car : cars) {
            System.out.println(car);
        }

        // Сохранение автомобилей в файл
        repository.saveCarsToFile(cars, "cars_output.txt");

        // Выполнение запросов и сохранение результатов
        StringBuilder results = new StringBuilder();

        // 1) Номера автомобилей с заданным цветом или нулевым пробегом
        String colorToFind = "Black";
        long mileageToFind = 0L;

        List<String> numbers = repository.findNumbersByColorOrMileage(colorToFind, mileageToFind);
        String result1 = "Номера автомобилей по цвету '" + colorToFind + "' или пробегу " + mileageToFind + ": " +
                String.join(" ", numbers);
        System.out.println("\n" + result1);
        results.append(result1).append("\n");

        // 2) Количество уникальных моделей в ценовом диапазоне
        long minPrice = 700000L;
        long maxPrice = 800000L;
        long uniqueCount = repository.countUniqueModelsInPriceRange(minPrice, maxPrice);
        String result2 = "Уникальные модели в диапазоне цен " + minPrice + " - " + maxPrice + ": " + uniqueCount + " шт.";
        System.out.println(result2);
        results.append(result2).append("\n");

        // 3) Цвет автомобиля с минимальной стоимостью
        String minCostColor = repository.findColorOfMinCostCar();
        String result3 = "Цвет автомобиля с минимальной стоимостью: " + minCostColor;
        System.out.println(result3);
        results.append(result3).append("\n");

        // 4) Средняя стоимость искомой модели
        String modelToFind1 = "Toyota";
        String modelToFind2 = "Volvo";

        double avgCost1 = repository.findAverageCostByModel(modelToFind1);
        double avgCost2 = repository.findAverageCostByModel(modelToFind2);

        String result4a = String.format("Средняя стоимость модели %s: %.2f", modelToFind1, avgCost1);
        String result4b = String.format("Средняя стоимость модели %s: %.2f", modelToFind2, avgCost2);

        System.out.println(result4a);
        System.out.println(result4b);

        results.append(result4a).append("\n");
        results.append(result4b).append("\n");

        // Сохранение всех результатов в файл
        repository.saveResultsToFile(results.toString(), "analysis_results.txt");

        System.out.println("\nРезультаты анализа сохранены в файл 'analysis_results.txt'");
    }
}