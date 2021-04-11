package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Dish;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.model.validators.DishValidator;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.repository.dbRepository.DishDbRepoI;
import ro.ubb.catalog.core.repository.dbRepository.OrderDbRepoI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DishService implements ServiceI<Integer, Dish> {

    private static final Logger LOG = LoggerFactory.getLogger(DishService.class);

    @Autowired
    private DishDbRepoI dishDbRepoI;

    @Autowired
    private OrderDbRepoI orderDbRepoI;

    @Autowired
    private DishValidator dishValidator;

    @Override
    public Optional<Dish> add(Dish dish) throws ValidatorException {
        dishValidator.validate(dish);
        LOG.trace("add: dish={}", dish);
        Optional<Dish> a = dishDbRepoI.findById(dish.getId());

        try {
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            dishDbRepoI.save(dish);
            return a;
        }
        LOG.trace("add --- method finished");
        return a;
    }

    @Override
    public List<Dish> get() {
        LOG.trace("get --- method entered");
        List<Dish> result = new ArrayList<>(dishDbRepoI.findAll());
        LOG.trace("getAllDishes: result={}", result);
        return result;
    }

    @Override
    public Optional<Dish> delete(Integer integer) {
        LOG.trace("delete: id={}", integer);
        Optional<Dish> a = dishDbRepoI.findById(integer);
        dishDbRepoI.deleteById(integer);
        Set<Orders> allOrders = this.orderDbRepoI.findAll().stream().filter(x -> x.getDishID().equals(integer)).collect(Collectors.toSet());
        allOrders.forEach(x -> this.orderDbRepoI.delete(x));
        LOG.trace("delete --- method finished");
        return a;
    }

    @Override
    @Transactional
    public Optional<Dish> update(Dish dish) throws ValidatorException {
        dishValidator.validate(dish);
        LOG.trace("update: dish={}", dish);
        Optional<Dish> dish1 = dishDbRepoI.findById(dish.getId());

        dishDbRepoI.findById(dish.getId())
                .ifPresent(a -> {
                    a.setName(dish.getName());
                    a.setCategory(dish.getCategory());
                    a.setPrice(dish.getPrice());
                    a.setQuantity(dish.getQuantity());
                    LOG.debug("updatedDish: dish={}", a);
                });

        LOG.trace("upd --- method finished");
        return dish1;
    }

    @Override
    public List<Dish> filterFunction(String string) {
        return this.dishDbRepoI.findByName(string);
    }

    @Override
    public List<Dish> sortFunction() {
        return this.dishDbRepoI.findByOrderByName();
    }

    public static Logger getLOG() {
        return LOG;
    }
}
