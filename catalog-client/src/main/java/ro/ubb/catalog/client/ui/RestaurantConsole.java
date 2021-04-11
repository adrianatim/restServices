package ro.ubb.catalog.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.ubb.catalog.client.rest.RestService;
import ro.ubb.catalog.core.model.Restaurant;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.dto.RestaurantDto;
import ro.ubb.catalog.web.dto.RestaurantsDto;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class RestaurantConsole {
    private final RestService restService;
    private final ServiceI<Integer, Restaurant> restaurantServiceI;
    private final Console console;
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantConsole.class);

    /**
     * restaurantConsole constructor
     *
     * @param restaurantServiceI the service for the restaurantService
     * @param console            the main console
     */
    public RestaurantConsole(ServiceI<Integer, Restaurant> restaurantServiceI, Console console, RestService restService) {
        this.restaurantServiceI = restaurantServiceI;
        this.restService = restService;
        this.console = console;
    }

    /**
     * The menu for restaurant.
     */
    protected void printRestaurantMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new restaurant. \n";
            str += "\t 2. Get all restaurants. \n";
            str += "\t 3. Delete an restaurant. \n";
            str += "\t 4. Update an restaurant. \n";
            str += "\t 5. Sort restaurants after their name. \n";
            str += "\t 6. Filter restaurants after the name. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getRestaurantCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the restaurant's commands
     */
    private Map<String, Runnable> getRestaurantCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addRestaurant);
        commands.put("2", this::getRestaurants);
        commands.put("3", this::deleteRestaurant);
        commands.put("4", this::updateRestaurant);
        commands.put("5", this::sortRestaurants);
        commands.put("6", this::filterRestaurants);
        return commands;
    }

    /**
     * Adds a restaurant.
     *
     * @throws Exception if the restaurant is null
     */
    private void addRestaurant() {
        Restaurant restaurant = readRestaurant();
        LOG.info("saveRestaurant: restaurant={}", restaurant);
        try {
            Optional<Restaurant> a = Optional.ofNullable(restaurant);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null restaurant!");
            return;
        }
        try {
            RestaurantDto restaurantDto = RestaurantDto.builder().name(restaurant.getName()).rating(restaurant.getRating()).capacity(restaurant.getCapacity()).delivery(restaurant.getDelivery()).build();
            restaurantDto.setId(restaurant.getId());
            Optional<RestaurantDto> a = restService.addRestaurant(restaurantDto);
            a.ifPresent(x -> LOG.info("restaurant already exists"));
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception ex) {
            LOG.info("Validation exception!");
        }

    }

    /**
     * The function reads an restaurant from keyboard.
     *
     * @return null in case of exception,otherwise the restaurant read
     * @throws IOException if the input from keyboard is not ok
     */
    private Restaurant readRestaurant() {
        System.out.println("Read restaurant {name,rating,capacity,delivery}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Name:");
            String name = bufferedReader.readLine();
            System.out.println("Rating");
            Integer rating = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Capacity:");
            Integer capacity = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Delivery:");
            Boolean delivery = Boolean.valueOf(bufferedReader.readLine());
            Restaurant restaurant = new Restaurant(name, rating, capacity, delivery);
            restaurant.setId(id);
            return restaurant;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the restaurants.
     */
    private void getRestaurants() {
        LOG.info("getAllRestaurants --- method entered");
        RestaurantsDto restaurants = restService.getRestaurants();
        Optional<Integer> op = Optional.of(restaurants.getRestaurants().size());
        op.filter(x -> x == 0).ifPresent(s -> LOG.info("There are no restaurants!"));
        restaurants.getRestaurants().stream().map(RestaurantDto::toString).forEach(LOG::info);
    }

    /**
     * Deletes an restaurant.
     *
     * @throws Exception   if the id of the restaurant doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteRestaurant() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.parseInt(bufferRead.readLine());
            LOG.info("deleteRestaurant: id={}", id);
            restService.deleteRestaurant(id);
            LOG.info("deleteRestaurant --- method finished");
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOG.info("Please input an integer value!");
        } catch (Exception e) {
            LOG.info("No restaurant with this ID was found!");
        }
    }

    /**
     * Updates an restaurant
     *
     * @throws IllegalArgumentException if the restaurant is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateRestaurant() {
        Restaurant restaurant = readRestaurant();
        LOG.info("updateRestaurant: Restaurant={}", restaurant);
        try {
            Optional<Restaurant> a = Optional.ofNullable(restaurant);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null restaurant!");
            return;
        }
        try {
            RestaurantDto restaurantDto = RestaurantDto.builder().name(restaurant.getName())
                    .rating(restaurant.getRating()).capacity(restaurant.getCapacity())
                    .delivery(restaurant.getDelivery()).build();
            restaurantDto.setId(restaurant.getId());
            restService.updateRestaurant(restaurantDto, restaurant.getId());
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception e) {
            LOG.info("restaurant with this ID doesn't exist/Validation Error!");
        }
    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortRestaurants() {
        LOG.info("sortRestaurant--- method entered");
        RestaurantsDto restaurants = restService.sortRestaurantName();
        Optional<Integer> op = Optional.of(restaurants.getRestaurants().size());
        op.filter(x -> x == 0).ifPresent(s -> System.out.println("No restaurant added yet!"));
        restaurants.getRestaurants().stream().map(RestaurantDto::toString).forEach(LOG::info);
    }

    /**
     * Filters the restaurants after the city name
     * The functions gets as input from the keyboard a city name
     * returns the list of restaurants in that city
     */
    private void filterRestaurants() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Name:");
            String name = String.valueOf(bufferedReader.readLine());
            RestaurantsDto restaurantsDto = restService.filterRestaurantName(name);
            Optional<Integer> op = Optional.of(restaurantsDto.getRestaurants().size());
            op.filter(x -> x == 0).ifPresent(s -> System.out.println("No restaurant with this name!"));
            restaurantsDto.getRestaurants().stream().map(RestaurantDto::toString).forEach(LOG::info);
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        }
    }
}
