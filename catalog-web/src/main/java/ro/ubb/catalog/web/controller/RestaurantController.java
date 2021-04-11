package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Restaurant;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.converter.RestaurantConverter;
import ro.ubb.catalog.web.dto.RestaurantDto;
import ro.ubb.catalog.web.dto.RestaurantsDto;

import java.util.List;
import java.util.Optional;

@RestController
public class RestaurantController {

    private static final Logger Log = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    @Qualifier("restaurantService")
    private ServiceI<Integer, Restaurant> restaurantService;

    @Autowired
    private RestaurantConverter restaurantConverter;

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    RestaurantsDto getAllRestaurants() {
        Log.trace("getAllRestaurants --- method entered");
        List<Restaurant> restaurant = restaurantService.get();
        List<RestaurantDto> restaurantDtos = restaurantConverter.convertModelsToDtos(restaurant);
        RestaurantsDto result = new RestaurantsDto(restaurantDtos);
        Log.trace("getAllRestaurant: result={}", result);
        return result;
    }

    @RequestMapping(value = "/restaurants", method = RequestMethod.POST)
    RestaurantDto saveRestaurant(@RequestBody RestaurantDto RestaurantDto) {
        Log.trace("saveRestaurant: dto={}", RestaurantDto);
        Optional<Restaurant> op = this.restaurantService.add(restaurantConverter.convertDtoToModel(RestaurantDto));
        try {
            op.orElseThrow(Exception::new);
        } catch (Exception e) {
            return null;
        }
        RestaurantDto result = restaurantConverter.convertModelToDto(op.get());
        Log.trace("saveRestaurant: result={}", result);
        return result;
    }

    @RequestMapping(value = "/restaurants/{id}", method = RequestMethod.PUT)
    RestaurantDto updateRestaurant(@PathVariable Integer id, @RequestBody RestaurantDto RestaurantDto) {
        Log.trace("updateRestaurant: id={}, dto={}", id, RestaurantDto);
        Optional<Restaurant> op = restaurantService.update(restaurantConverter.convertDtoToModel(RestaurantDto));
        RestaurantDto result = restaurantConverter.convertModelToDto(op.get());
        Log.trace("updateRestaurant: result={}", result);
        return result;
    }

    @RequestMapping(value = "/restaurants/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteRestaurant(@PathVariable Integer id) {
        Log.trace("deleteRestaurant: id={}", id);
        restaurantService.delete(id);
        Log.trace("deleteRestaurant --- method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurants/filter", method = RequestMethod.GET)
    RestaurantsDto filterFunction(@RequestParam String name) {
        Log.trace("filterRestaurantName --- method entered");
        List<Restaurant> restaurant = restaurantService.filterFunction(name);
        List<RestaurantDto> restaurantDtos = restaurantConverter.convertModelsToDtos(restaurant);
        RestaurantsDto result = new RestaurantsDto(restaurantDtos);
        Log.trace("filterRestaurantName: result={}", result);
        return result;
    }

    @RequestMapping(value = "/restaurants/sort", method = RequestMethod.GET)
    RestaurantsDto sortFunction() {
        Log.trace("sortRestaurant--- method entered");
        List<Restaurant> restaurant = restaurantService.sortFunction();
        List<RestaurantDto> restaurantDtos = restaurantConverter.convertModelsToDtos(restaurant);
        RestaurantsDto result = new RestaurantsDto(restaurantDtos);
        Log.trace("sortRestaurant: result={}", result);
        return result;
    }
}
