package ro.ubb.catalog.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.client.rest.RestService;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.model.Dish;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.model.Restaurant;
import ro.ubb.catalog.core.service.ServiceI;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class Console {
    private static final Logger LOG = LoggerFactory.getLogger(
            Console.class);

    private ServiceI<Integer, Address> addressService;
    private ServiceI<Integer, Dish> dishServiceI;
    private ServiceI<Integer, Restaurant> restaurantServiceI;
    private ServiceI<Integer, Orders> orderServiceI;
    private RestTemplate restTemplate;

    @Autowired
    private RestService restService;

    /**
     * The main menu.
     */
    protected void printOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(this.restService);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Exit \n";
            str += "\t 1. Client Menu \n";
            str += "\t 2. Dish Menu \n";
            str += "\t 3. Order Menu \n";
            str += "\t 4. Address Menu \n";
            str += "\t 5. Restaurant Menu \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getCommands().getOrDefault(key, () -> System.out.println("Invalid command")).run();
        }
    }

    /**
     * @return the main commands
     */
    private Map<String, Runnable> getCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", () -> System.exit(0));
//        commands.put("1", this::callClientMenu);
        commands.put("2", this::callDishMenu);
        commands.put("3", this::callOrderMenu);
        commands.put("4", this::callAddressMenu);
        commands.put("5", this::callRestaurantMenu);
        return commands;
    }

    //    private void callClientMenu() {
//        ClientConsole clientConsole = new ClientConsole(this.clientServiceI, this.orderServiceI, this);
//        clientConsole.printClientMenu();
//    }
//
    private void callDishMenu() {
        DishConsole dishConsole = new DishConsole(this.dishServiceI, this, restService);
        dishConsole.printDishMenu();
    }

    private void callOrderMenu() {
        OrderConsole orderConsole = new OrderConsole(this.orderServiceI, this, restService);
        orderConsole.printOrderMenu();
    }

    private void callAddressMenu() {
        AddressConsole addressConsole = new AddressConsole(addressService, this, restService);
        addressConsole.printAddressMenu();
    }

    private void callRestaurantMenu() {
        RestaurantConsole restaurantConsole = new RestaurantConsole(this.restaurantServiceI, this, restService);
        restaurantConsole.printRestaurantMenu();
    }

    /**
     * The function runs the console.
     */
    public void runConsole() {
        while (true)
            this.printOptions();
    }
}
