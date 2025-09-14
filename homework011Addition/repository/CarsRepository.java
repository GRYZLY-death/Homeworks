package repository;

import model.Car;
import java.util.List;

public interface CarsRepository {
    List<Car> loadCarsFromFile(String filename);
    void saveCarsToFile(List<Car> cars, String filename);
    void saveResultsToFile(String results, String filename);

    default List<Car> findAll() {
        return null;
    }