package ro.ubb.catalog.client.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.web.dto.*;

import java.util.Optional;

@Component
public class RestService {

    @Autowired
    RestTemplate restTemplate;

    //ADDRESS
    public AddressesDto getAddresses() {
        return restTemplate.getForObject("http://localhost:8080/api/addresses", AddressesDto.class);
    }

    public Optional<AddressDto> addAddress(AddressDto newAddress) {
        return Optional.ofNullable(restTemplate.postForObject("http://localhost:8080/api/addresses", newAddress, AddressDto.class));
    }

    public void deleteAddress(Integer id) {
        restTemplate.delete("http://localhost:8080/api/addresses/{id}", id);
    }

    public void updateAddress(AddressDto newAddress, Integer id) {
        restTemplate.put("http://localhost:8080/api/addresses/{id}", newAddress, id);
    }

    public AddressesDto filterCity(String city) {
        return restTemplate.getForObject("http://localhost:8080/api/addresses/filter?city={city}", AddressesDto.class, city);
    }

    public AddressesDto sortAddressCity() {
        return restTemplate.getForObject("http://localhost:8080/api/addresses/sort", AddressesDto.class);
    }

    //DISH
    public DishesDto getDishes() {
        return restTemplate.getForObject("http://localhost:8080/api/dishes", DishesDto.class);
    }

    public Optional<DishDto> addDish(DishDto newDish) {
        return Optional.ofNullable(restTemplate.postForObject("http://localhost:8080/api/dishes", newDish, DishDto.class));
    }

    public void deleteDish(Integer id) {
        restTemplate.delete("http://localhost:8080/api/dishes/{id}", id);
    }

    public void updateDish(DishDto newDish, Integer id) {
        restTemplate.put("http://localhost:8080/api/dishes/{id}", newDish, id);
    }

    public DishesDto filterName(String name) {
        return restTemplate.getForObject("http://localhost:8080/api/dishes/filter?name={name}", DishesDto.class, name);
    }

    public DishesDto sortDishName() {
        return restTemplate.getForObject("http://localhost:8080/api/dishes/sort", DishesDto.class);
    }

    //RESTAURANT
    public RestaurantsDto getRestaurants() {
        return restTemplate.getForObject("http://localhost:8080/api/restaurants", RestaurantsDto.class);
    }

    public Optional<RestaurantDto> addRestaurant(RestaurantDto newRestaurant) {
        return Optional.ofNullable(restTemplate.postForObject("http://localhost:8080/api/restaurants", newRestaurant, RestaurantDto.class));
    }

    public void deleteRestaurant(Integer id) {
        restTemplate.delete("http://localhost:8080/api/restaurants/{id}", id);
    }

    public void updateRestaurant(RestaurantDto newRestaurant, Integer id) {
        restTemplate.put("http://localhost:8080/api/restaurants/{id}", newRestaurant, id);
    }

    public RestaurantsDto filterRestaurantName(String name) {
        return restTemplate.getForObject("http://localhost:8080/api/restaurants/filter?name={name}", RestaurantsDto.class, name);
    }

    public RestaurantsDto sortRestaurantName() {
        return restTemplate.getForObject("http://localhost:8080/api/restaurants/sort", RestaurantsDto.class);
    }

    //ORDER
    public OrdersDto getOrders() {
        return restTemplate.getForObject("http://localhost:8080/api/orders", OrdersDto.class);
    }

    public Optional<OrderDto> addOrder(OrderDto newOrder) {
        return Optional.ofNullable(restTemplate.postForObject("http://localhost:8080/api/orders", newOrder, OrderDto.class));
    }

    public void deleteOrder(Integer id) {
        restTemplate.delete("http://localhost:8080/api/orders/{id}", id);
    }

    public void updateOrder(OrderDto newOrder, Integer id) {
        restTemplate.put("http://localhost:8080/api/orders/{id}", newOrder, id);
    }

    public OrdersDto filterOrderPaymentType(String paymentType) {
        return restTemplate.getForObject("http://localhost:8080/api/orders/filter?paymentType={paymentType}", OrdersDto.class, paymentType);
    }

    public OrdersDto sortOrderPaymentType() {
        return restTemplate.getForObject("http://localhost:8080/api/orders/sort", OrdersDto.class);
    }
}
