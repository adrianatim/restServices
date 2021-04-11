package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Dish;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.model.Restaurant;
import ro.ubb.catalog.core.model.validators.OrderValidator;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.repository.dbRepository.DishDbRepoI;
import ro.ubb.catalog.core.repository.dbRepository.OrderDbRepoI;
import ro.ubb.catalog.core.repository.dbRepository.RestaurantDbRepoI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements ServiceI<Integer, Orders> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    @Qualifier("orderDbRepoI")
    private OrderDbRepoI orderDbRepoI;

    @Autowired
    @Qualifier("restaurantDbRepoI")
    private RestaurantDbRepoI restaurantDbRepoI;

    @Autowired
    @Qualifier("dishDbRepoI")
    private DishDbRepoI dishDbRepoI;

    @Autowired
    private OrderValidator orderValidator;

    @Override
    public Optional<Orders> add(Orders order) throws ValidatorException {
        orderValidator.validate(order);
        LOG.trace("add: order={}", order);
        Optional<Orders> a = orderDbRepoI.findById(order.getId());
        Optional<Restaurant> restaurant = restaurantDbRepoI.findById(order.getRestaurantID());
        Optional<Dish> dish = dishDbRepoI.findById(order.getDishID());

        try {
            a.orElseThrow(Exception::new);
            restaurant.orElseThrow(Exception::new);
            dish.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can not save this order! Nonexistent id!");
            return a;
        }
        orderDbRepoI.save(order);
        LOG.trace("add --- method finished");
        return a;
    }

    @Override
    public List<Orders> get() {
        LOG.trace("get --- method entered");
        List<Orders> result = new ArrayList<>(orderDbRepoI.findAll());
        LOG.trace("getAllOrders: result={}", result);
        return result;
    }

    @Override
    public Optional<Orders> delete(Integer integer) {
        LOG.trace("delete: id={}", integer);
        Optional<Orders> a = orderDbRepoI.findById(integer);
        orderDbRepoI.deleteById(integer);
        LOG.trace("delete --- method finished");
        return a;
    }

    @Override
    @Transactional
    public Optional<Orders> update(Orders order) throws ValidatorException {
        orderValidator.validate(order);
        LOG.trace("update: order={}", order);
        Optional<Orders> order1 = orderDbRepoI.findById(order.getId());

        orderDbRepoI.findById(order.getId())
                .ifPresent(a -> {
                    a.setDate(order.getDate());
                    a.setClientID(order.getClientID());
                    a.setDishID(order.getDishID());
                    a.setPaymentType(order.getPaymentType());
                    a.setRestaurantID(order.getRestaurantID());
                    LOG.debug("updatedOrder: Order={}", a);
                });

        LOG.trace("upd --- method finished");
        return order1;
    }

    @Override
    public List<Orders> filterFunction(String string) {
        return this.orderDbRepoI.findByPaymentType(string);
    }

    @Override
    public List<Orders> sortFunction() {
        return this.orderDbRepoI.findByOrderByPaymentType();
    }

//    public void deleteClientId(Integer id) {
//        Set<Orders> allOrders = this.orderDbRepoI.findAll().stream().filter(x -> x.getClientID().equals(id)).collect(Collectors.toSet());
//        allOrders.forEach(x -> this.orderDbRepoI.delete(x));
//    }


    public static Logger getLOG() {
        return LOG;
    }
}