package ro.ubb.catalog.core.repository.dbRepository;

import ro.ubb.catalog.core.model.Dish;
import ro.ubb.catalog.core.repository.RepoI;

import java.util.List;

public interface DishDbRepoI extends RepoI<Dish, Integer> {
    List<Dish> findByName(String name);
    List<Dish> findByOrderByName();
}