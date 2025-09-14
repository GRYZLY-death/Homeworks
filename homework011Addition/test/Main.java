package test;

import model.Car;
import repository.CarsRepository;
import repository.CarsRepositoryImpl;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    // Константы с путями к файлам
    private static final String DATA_DIR = "homework011Addition/data/";
    private static final String INPUT_FILE = DATA_DIR + "cars.txt";
    private static final String OUTPUT_FILE = DATA_DIR + "cars_output.txt";
    private static final String RESULTS_FILE = DATA_DIR + "analysis_results.txt";

    public static void main(String[] args) {
        CarsRepository repository = new CarsRepositoryImpl();
        Scanner scanner = new Scanner(System.in);

        // Загрузка данных из файла
        System.out.println("Загрузка данных из файла: " + INPUT_FILE);
        List<Car> cars = repository.loadCarsFromFile(INPUT_FILE);

        if (cars.isEmpty()) {
            System.out.println("Нет данных для обработки. Проверьте файл: " + INPUT_FILE);
            System.out.println("Создан пример файла с тестовыми данными.");
            createSampleData(repository);
            cars = repository.loadCarsFromFile(INPUT_FILE);

            if (cars.isEmpty()) {
                System.out.println("Не удалось загрузить данные. Завершение работы.");
                return;
            }
        }

        // Вывод всех автомобилей в консоль
        System.out.println("\nАвтомобили в базе:");
        System.out.println("Number Model Color Mileage Cost");
        for (Car car : cars) {
            System.out.println(car);
        }

        // Сохранение автомобилей в файл
        repository.saveCarsToFile(cars, OUTPUT_FILE);

        // Получаем уникальные значения для выбора
        Set<String> availableColors = cars.stream()
                .map(Car::getColor)
                .collect(Collectors.toSet());

        Set<String> availableModels = cars.stream()
                .map(Car::getModel)
                .collect(Collectors.toSet());

        long minPrice = cars.stream()
                .mapToLong(Car::getCost)
                .min()
                .orElse(0);

        long maxPrice = cars.stream()
                .mapToLong(Car::getCost)
                .max()
                .orElse(0);

        // Меню выбора запросов
        System.out.println("\n=== ВЫБОР ЗАПРОСОВ ===");
        System.out.println("Доступные цвета: " + String.join(", ", availableColors));
        System.out.println("Доступные модели: " + String.join(", ", availableModels));
        System.out.println("Диапазон цен: от " + minPrice + " до " + maxPrice);

        System.out.println("\nВыберите запросы для выполнения (введите номера через запятую):");
        System.out.println("1) Номера автомобилей по цвету или пробегу");
        System.out.println("2) Количество уникальных моделей в ценовом диапазоне");
        System.out.println("3) Цвет автомобиля с минимальной стоимостью");
        System.out.println("4) Средняя стоимость модели");
        System.out.print("Ваш выбор: ");

        String choice = scanner.nextLine();
        String[] choices = choice.split(",");

        StringBuilder results = new StringBuilder();
        results.append("Результаты анализа автомобилей\n");
        results.append("=".repeat(50)).append("\n");

        // Обработка выбранных запросов
        for (String ch : choices) {
            switch (ch.trim()) {
                case "1":
                    executeQuery1(cars, scanner, availableColors, results);
                    break;
                case "2":
                    executeQuery2(cars, scanner, minPrice, maxPrice, results);
                    break;
                case "3":
                    executeQuery3(cars, results);
                    break;
                case "4":
                    executeQuery4(cars, scanner, availableModels, results);
                    break;
                default:
                    System.out.println("Неизвестный запрос: " + ch);
            }
            results.append("\n");
        }

        // Сохранение всех результатов в файл
        if (results.length() > 0) {
            repository.saveResultsToFile(results.toString(), RESULTS_FILE);
            System.out.println("\nРезультаты анализа сохранены в файл: " + RESULTS_FILE);
        }

        scanner.close();
    }

    // Создание примерных данных если файл не существует
    private static void createSampleData(CarsRepository repository) {
        List<Car> sampleCars = List.of(
                new Car("a123me", "Mercedes", "White", 0, 8300000),
                new Car("b873of", "Volga", "Black", 0, 673000),
                new Car("w487mn", "Lexus", "Grey", 76000, 900000),
                new Car("p987hj", "Volga", "Red", 610, 704340),
                new Car("c987ss", "Toyota", "White", 254000, 761000),
                new Car("o983op", "Toyota", "Black", 698000, 740000),
                new Car("p146op", "BMW", "White", 271000, 850000),
                new Car("u893ii", "Toyota", "Purple", 210900, 440000),
                new Car("l097df", "Toyota", "Black", 108000, 780000),
                new Car("y876wd", "Toyota", "Black", 160000, 1000000)
        );

        repository.saveCarsToFile(sampleCars, INPUT_FILE);
        System.out.println("Создан файл с примерными данными: " + INPUT_FILE);
    }

    // ... методы executeQuery1, executeQuery2, executeQuery3, executeQuery4 без изменений ...
    // (они остаются такими же как в предыдущем ответе)

    // Запрос 1: Номера автомобилей по цвету или пробегу
    private static void executeQuery1(List<Car> cars, Scanner scanner,
                                      Set<String> availableColors, StringBuilder results) {
        System.out.println("\n=== ЗАПРОС 1: Поиск по цвету или пробегу ===");

        // Выбор цвета
        System.out.println("Доступные цвета: " + String.join(", ", availableColors));
        System.out.print("Введите цвет для поиска (или Enter для пропуска): ");
        String colorToFind = scanner.nextLine().trim();
        if (colorToFind.isEmpty()) {
            colorToFind = null;
        }

        // Выбор пробега
        System.out.print("Введите пробег для поиска (или 0 для пропуска): ");
        long mileageToFind = 0;
        try {
            mileageToFind = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Используется пробег по умолчанию: 0");
        }

        // Выполнение запроса
        String finalColorToFind = colorToFind;
        long finalMileageToFind = mileageToFind;
        List<String> numbers = cars.stream()
                .filter(car ->
                        (finalColorToFind == null || car.getColor().equalsIgnoreCase(finalColorToFind)) ||
                                car.getMileage() == finalMileageToFind)
                .map(Car::getNumber)
                .collect(Collectors.toList());

        String result1 = "Номера автомобилей";
        if (colorToFind != null) {
            result1 += " по цвету '" + colorToFind + "'";
        }
        if (mileageToFind != 0) {
            result1 += (colorToFind != null ? " или" : " по") + " пробегу " + mileageToFind;
        }
        result1 += ": " + String.join(" ", numbers);

        System.out.println(result1);
        results.append("1. ").append(result1).append("\n");
    }

    // Запрос 2: Количество уникальных моделей в ценовом диапазоне
    private static void executeQuery2(List<Car> cars, Scanner scanner,
                                      long minPrice, long maxPrice, StringBuilder results) {
        System.out.println("\n=== ЗАПРОС 2: Уникальные модели в ценовом диапазоне ===");
        System.out.println("Доступный диапазон цен: от " + minPrice + " до " + maxPrice);

        // Ввод минимальной цены
        System.out.print("Введите минимальную цену: ");
        long min = minPrice;
        try {
            min = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Используется минимальная цена: " + minPrice);
        }

        // Ввод максимальной цены
        System.out.print("Введите максимальную цену: ");
        long max = maxPrice;
        try {
            max = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Используется максимальная цена: " + maxPrice);
        }

        // Выполнение запроса
        long finalMin = min;
        long finalMax = max;
        long uniqueCount = cars.stream()
                .filter(car -> car.getCost() >= finalMin && car.getCost() <= finalMax)
                .map(Car::getModel)
                .distinct()
                .count();

        String result2 = "Уникальные модели в диапазоне цен " + min + " - " + max + ": " + uniqueCount + " шт.";
        System.out.println(result2);
        results.append("2. ").append(result2).append("\n");
    }

    // Запрос 3: Цвет автомобиля с минимальной стоимостью
    private static void executeQuery3(List<Car> cars, StringBuilder results) {
        System.out.println("\n=== ЗАПРОС 3: Цвет самой дешевой машины ===");

        String minCostColor = cars.stream()
                .min(java.util.Comparator.comparingLong(Car::getCost))
                .map(Car::getColor)
                .orElse("Не найдено");

        String result3 = "Цвет автомобиля с минимальной стоимостью: " + minCostColor;
        System.out.println(result3);
        results.append("3. ").append(result3).append("\n");
    }

    // Запрос 4: Средняя стоимость модели
    private static void executeQuery4(List<Car> cars, Scanner scanner,
                                      Set<String> availableModels, StringBuilder results) {
        System.out.println("\n=== ЗАПРОС 4: Средняя стоимость модели ===");
        System.out.println("Доступные модели: " + String.join(", ", availableModels));

        System.out.print("Введите модель для анализа: ");
        String modelToFind = scanner.nextLine().trim();

        double avgCost = cars.stream()
                .filter(car -> car.getModel().equalsIgnoreCase(modelToFind))
                .mapToLong(Car::getCost)
                .average()
                .orElse(0.0);

        String result4 = String.format("Средняя стоимость модели %s: %.2f", modelToFind, avgCost);
        System.out.println(result4);
        results.append("4. ").append(result4).append("\n");
    }
}