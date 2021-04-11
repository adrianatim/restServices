package ro.ubb.catalog.core.repository.dbRepository;

import ro.ubb.catalog.core.model.Restaurant;
import ro.ubb.catalog.core.repository.RepoI;

import java.util.List;

public interface RestaurantDbRepoI extends RepoI<Restaurant, Integer> {
    List<Restaurant> findByName(String name);
    List<Restaurant> findByOrderByName();

}
