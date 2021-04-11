package ro.ubb.catalog.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.ubb.catalog.client.rest.RestService;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.dto.OrderDto;
import ro.ubb.catalog.web.dto.OrdersDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderConsole {
    private final RestService restService;
    private final ServiceI<Integer, Orders> orderServiceI;
    private final Console console;
    private static final Logger LOG = LoggerFactory.getLogger(OrderConsole.class);

    /**
     * OrderConsole constructor
     *
     * @param orderServiceI the service for the OrderService
     * @param console       the main console
     */
    public OrderConsole(ServiceI<Integer, Orders> orderServiceI, Console console, RestService restService) {
        this.orderServiceI = orderServiceI;
        this.restService = restService;
        this.console = console;
    }

    /**
     * The menu for Order.
     */
    protected void printOrderMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new order. \n";
            str += "\t 2. Get all orders. \n";
            str += "\t 3. Delete an order. \n";
            str += "\t 4. Update an order. \n";
            str += "\t 5. Sort orders after their paymentType. \n";
            str += "\t 6. Filter orders after the paymentType. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getOrderCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the Order's commands
     */
    private Map<String, Runnable> getOrderCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addOrder);
        commands.put("2", this::getOrders);
        commands.put("3", this::deleteOrder);
        commands.put("4", this::updateOrder);
        commands.put("5", this::sortOrders);
        commands.put("6", this::filterOrders);
        return commands;
    }

    /**
     * Adds a Order.
     *
     * @throws Exception if the Order is null
     */
    private void addOrder() {
        Orders order = readOrder();
        LOG.info("saveOrder: Order={}", order);
        try {
            Optional<Orders> a = Optional.ofNullable(order);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null order!");
            return;
        }
        try {
            OrderDto orderDto = OrderDto.builder().restaurantID(order.getRestaurantID()).clientID(order.getClientID())
                    .dishID(order.getDishID()).paymentType(order.getPaymentType()).date(order.getDate()).build();
            orderDto.setId(order.getId());
            Optional<OrderDto> a = restService.addOrder(orderDto);
            a.ifPresent(x -> LOG.info("Order already exists"));
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception ex) {
            LOG.info("Validation exception!");
        }

    }

    /**
     * The function reads an Order from keyboard.
     *
     * @return null in case of exception,otherwise the Order read
     * @throws IOException if the input from keyboard is not ok
     */
    private Orders readOrder() {
        System.out.println("Read Order {restaurantId,clientId,dishId,paymentType,orderDate}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("RestaurantId:");
            Integer restaurantId = Integer.valueOf(bufferedReader.readLine());
            System.out.println("ClientId:");
            int clientId = Integer.parseInt(bufferedReader.readLine());
            System.out.println("DishId:");
            int dishId = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Payment Type:");
            String paymentType = bufferedReader.readLine();
            System.out.println("orderDate");
            String dateOrderStr = bufferedReader.readLine();
            Date orderDate = new SimpleDateFormat("yyyy/MM/dd").parse(dateOrderStr);
            Orders order = new Orders(restaurantId, clientId, dishId, paymentType, orderDate);
            order.setId(id);
            return order;
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the Orders.
     */
    private void getOrders() {
        LOG.info("getAllOrders --- method entered");
        OrdersDto Orders = restService.getOrders();
        Optional<Integer> op = Optional.of(Orders.getOrders().size());
        op.filter(x -> x == 0).ifPresent(s -> LOG.info("There are no Orders!"));
        Orders.getOrders().stream().map(OrderDto::toString).forEach(LOG::info);
    }

    /**
     * Deletes an Order.
     *
     * @throws Exception   if the id of the Order doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteOrder() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.parseInt(bufferRead.readLine());
            LOG.info("deleteOrder: id={}", id);
            restService.deleteOrder(id);
            LOG.info("deleteOrder --- method finished");
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOG.info("Please input an integer value!");
        } catch (Exception e) {
            LOG.info("No Order with this ID was found!");
        }
    }

    /**
     * Updates an Order
     *
     * @throws IllegalArgumentException if the Order is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateOrder() {
        Orders order = readOrder();
        LOG.info("updateOrder: Order={}", order);
        try {
            Optional<Orders> a = Optional.ofNullable(order);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null order!");
            return;
        }
        try {
            OrderDto orderDto = OrderDto.builder().restaurantID(order.getRestaurantID()).clientID(order.getClientID())
                    .dishID(order.getDishID()).paymentType(order.getPaymentType()).date(order.getDate()).build();
            orderDto.setId(order.getId());
            restService.updateOrder(orderDto, order.getId());
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception e) {
            LOG.info("Order with this ID doesn't exist/Validation Error!");
        }
    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortOrders() {
        LOG.info("sortOrder--- method entered");
        OrdersDto Orders = restService.sortOrderPaymentType();
        Optional<Integer> op = Optional.of(Orders.getOrders().size());
        op.filter(x -> x == 0).ifPresent(s -> System.out.println("No Order added yet!"));
        Orders.getOrders().stream().map(OrderDto::toString).forEach(LOG::info);
    }

    /**
     * Filters the Orders after the city name
     * The functions gets as input from the keyboard a city name
     * returns the list of Orders in that paymentType
     */
    private void filterOrders() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("PaymentType:");
            String paymentType = String.valueOf(bufferedReader.readLine());
            OrdersDto OrdersDto = restService.filterOrderPaymentType(paymentType);
            Optional<Integer> op = Optional.of(OrdersDto.getOrders().size());
            op.filter(x -> x == 0).ifPresent(s -> System.out.println("No order with this paymentType!"));
            OrdersDto.getOrders().stream().map(OrderDto::toString).forEach(LOG::info);
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        }
    }
}
