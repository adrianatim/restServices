package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.model.Restaurant;
import ro.ubb.catalog.core.model.validators.RestaurantValidator;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.repository.dbRepository.OrderDbRepoI;
import ro.ubb.catalog.core.repository.dbRepository.RestaurantDbRepoI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantService implements ServiceI<Integer, Restaurant> {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantService.class);

    @Autowired
    private RestaurantDbRepoI restaurantDbRepoI;

    @Autowired
    private OrderDbRepoI orderDbRepoI;

    @Autowired
    private RestaurantValidator restaurantValidator;

    @Override
    public Optional<Restaurant> add(Restaurant restaurant) throws ValidatorException {
        restaurantValidator.validate(restaurant);
        LOG.trace("add: Restaurant={}", restaurant);
        Optional<Restaurant> a = restaurantDbRepoI.findById(restaurant.getId());

        try {
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            restaurantDbRepoI.save(restaurant);
            return a;
        }
        LOG.trace("add --- method finished");
        return a;
    }

    @Override
    public List<Restaurant> get() {
        LOG.trace("get --- method entered");
        List<Restaurant> result = new ArrayList<>(restaurantDbRepoI.findAll());
        LOG.trace("getAllRestaurants: result={}", result);
        return result;
    }

    @Override
    public Optional<Restaurant> delete(Integer integer) {
        LOG.trace("delete: id={}", integer);
        Optional<Restaurant> a = restaurantDbRepoI.findById(integer);
        restaurantDbRepoI.deleteById(integer);
        Set<Orders> allOrders = this.orderDbRepoI.findAll().stream().filter(x -> x.getRestaurantID().equals(integer)).collect(Collectors.toSet());
        allOrders.forEach(x -> this.orderDbRepoI.delete(x));
        LOG.trace("delete --- method finished");
        return a;
    }

    @Override
    @Transactional
    public Optional<Restaurant> update(Restaurant restaurant) throws ValidatorException {
        restaurantValidator.validate(restaurant);
        LOG.trace("update: restaurant={}", restaurant);
        Optional<Restaurant> restaurant1 = restaurantDbRepoI.findById(restaurant.getId());

        restaurantDbRepoI.findById(restaurant.getId())
                .ifPresent(a -> {
                    a.setName(restaurant.getName());
                    a.setCapacity(restaurant.getCapacity());
                    a.setDelivery(restaurant.getDelivery());
                    a.setRating(restaurant.getRating());
                    LOG.debug("updatedRestaurant: restaurant={}", a);
                });

        LOG.trace("upd --- method finished");
        return restaurant1;
    }

    @Override
    public List<Restaurant> filterFunction(String string) {
        return this.restaurantDbRepoI.findByName(string);
    }

    @Override
    public List<Restaurant> sortFunction() {
        return this.restaurantDbRepoI.findByOrderByName();
    }

    public static Logger getLOG() {
        return LOG;
    }
}
