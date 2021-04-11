package ro.ubb.catalog.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.ubb.catalog.client.rest.RestService;
import ro.ubb.catalog.core.model.Dish;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.dto.DishDto;
import ro.ubb.catalog.web.dto.DishesDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class DishConsole {
    private final RestService restService;
    private final ServiceI<Integer, Dish> dishServiceI;
    private final Console console;
    private static final Logger LOG = LoggerFactory.getLogger(
            DishConsole.class);

    /**
     * DishConsole constructor
     *
     * @param dishServiceI the service for the DishService
     * @param console      the main console
     */
    public DishConsole(ServiceI<Integer, Dish> dishServiceI, Console console, RestService restService) {
        this.dishServiceI = dishServiceI;
        this.restService = restService;
        this.console = console;
    }

    /**
     * The menu for Dish.
     */
    protected void printDishMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new dish. \n";
            str += "\t 2. Get all dishes. \n";
            str += "\t 3. Delete an dish. \n";
            str += "\t 4. Update an dish. \n";
            str += "\t 5. Sort dishes after their name. \n";
            str += "\t 6. Filter dishes after the name. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getDishCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the dish's commands
     */
    private Map<String, Runnable> getDishCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addDish);
        commands.put("2", this::getDishes);
        commands.put("3", this::deleteDish);
        commands.put("4", this::updateDish);
        commands.put("5", this::sortDishes);
        commands.put("6", this::filterDishes);
        return commands;
    }

    /**
     * Adds a dish.
     *
     * @throws Exception if the dish is null
     */
    private void addDish() {
        Dish dish = readDish();
        LOG.info("saveDish: Dish={}", dish);
        try {
            Optional<Dish> a = Optional.ofNullable(dish);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null Dish!");
            return;
        }
        try {
            DishDto newDish = DishDto.builder().name(dish.getName()).price(dish.getPrice()).quantity(dish.getQuantity())
                    .category(dish.getCategory()).build();
            newDish.setId(dish.getId());
            Optional<DishDto> a = restService.addDish(newDish);
            a.ifPresent(x -> LOG.info("Dish already exists"));
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception ex) {
            LOG.info("Validation exception!");
        }

    }

    /**
     * The function reads an Dish from keyboard.
     *
     * @return null in case of exception,otherwise the dish read
     * @throws IOException if the input from keyboard is not ok
     */
    private Dish readDish() {
        System.out.println("Read Dish {name,price,quantity,category}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Name:");
            String name = bufferedReader.readLine();
            System.out.println("Price");
            float price = Float.parseFloat(bufferedReader.readLine());
            System.out.println("Quantity");
            int quantity = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Category:");
            String category = bufferedReader.readLine();
            Dish dish = new Dish(name, price, quantity, category);
            dish.setId(id);
            return dish;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the Dishes.
     */
    private void getDishes() {
        LOG.info("getAllDishes --- method entered");
        DishesDto Dishes = restService.getDishes();
        Optional<Integer> op = Optional.of(Dishes.getDishes().size());
        op.filter(x -> x == 0).ifPresent(s -> LOG.info("There are no Dishes!"));
        Dishes.getDishes().stream().map(DishDto::toString).forEach(LOG::info);
    }

    /**
     * Deletes an Dish.
     *
     * @throws Exception   if the id of the Dish doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteDish() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.parseInt(bufferRead.readLine());
            LOG.info("deleteDish: id={}", id);
            restService.deleteDish(id);
            LOG.info("deleteDish --- method finished");
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOG.info("Please input an integer value!");
        } catch (Exception e) {
            LOG.info("No Dish with this ID was found!");
        }
    }

    /**
     * Updates an Dish
     *
     * @throws IllegalArgumentException if the Dish is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateDish() {
        Dish dish = readDish();
        LOG.info("updateDish: Dish={}", dish);
        try {
            Optional<Dish> a = Optional.ofNullable(dish);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null dish!");
            return;
        }
        try {
            DishDto newDish = DishDto.builder().name(dish.getName()).price(dish.getPrice()).quantity(dish.getQuantity())
                    .category(dish.getCategory()).build();
            newDish.setId(dish.getId());
            restService.updateDish(newDish, dish.getId());
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception e) {
            LOG.info("Dish with this ID doesn't exist/Validation Error!");
        }
    }

    /**
     * Prints all the sorted dishes from the list received
     */
    private void sortDishes() {
        LOG.info("sortDish--- method entered");
        DishesDto dishes = restService.sortDishName();
        Optional<Integer> op = Optional.of(dishes.getDishes().size());
        op.filter(x -> x == 0).ifPresent(s -> System.out.println("No Dish added yet!"));
        dishes.getDishes().stream().map(DishDto::toString).forEach(LOG::info);
    }

    /**
     * Filters the Dishes after the name
     * The functions gets as input from the keyboard a name
     * returns the list of Dishes in that name
     */
    private void filterDishes() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Name:");
            String name = String.valueOf(bufferedReader.readLine());
            DishesDto dishesDto = restService.filterName(name);
            Optional<Integer> op = Optional.of(dishesDto.getDishes().size());
            op.filter(x -> x == 0).ifPresent(s -> System.out.println("No dish with this name!"));
            dishesDto.getDishes().stream().map(DishDto::toString).forEach(LOG::info);
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        }
    }
}
