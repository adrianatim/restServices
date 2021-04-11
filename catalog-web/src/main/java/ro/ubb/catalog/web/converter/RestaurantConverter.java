package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Restaurant;
import ro.ubb.catalog.web.dto.RestaurantDto;

@Component
public class RestaurantConverter extends BaseConverter<Restaurant, RestaurantDto> {

    @Override
    public Restaurant convertDtoToModel(RestaurantDto dto) {
        Restaurant restaurant = Restaurant.builder().name(dto.getName()).rating(dto.getRating()).capacity(dto.getCapacity()).delivery(dto.getDelivery()).build();
        restaurant.setId(dto.getId());
        return restaurant;
    }

    @Override
    public RestaurantDto convertModelToDto(Restaurant restaurant) {
        RestaurantDto restaurantDto = RestaurantDto.builder().name(restaurant.getName()).rating(restaurant.getRating()).capacity(restaurant.getCapacity()).delivery(restaurant.getDelivery()).build();
        restaurantDto.setId(restaurant.getId());
        return restaurantDto;
    }
}
