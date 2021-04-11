package ro.ubb.catalog.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.ubb.catalog.client.rest.RestService;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.dto.AddressDto;
import ro.ubb.catalog.web.dto.AddressesDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class AddressConsole {
    private RestService restService;
    private ServiceI<Integer, Address> addressServiceI;
    private Console console;
    private static final Logger LOG = LoggerFactory.getLogger(
            AddressConsole.class);

    /**
     * AddressConsole constructor
     *
     * @param addressService the service for the addressService
     * @param console        the main console
     */
    public AddressConsole(ServiceI<Integer,Address> addressService,Console console,RestService restService) {
        this.addressServiceI = addressService;
        this.restService=restService;
        this.console = console;
    }

    /**
     * The menu for address.
     */
    protected void printAddressMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new address. \n";
            str += "\t 2. Get all addresses. \n";
            str += "\t 3. Delete an address. \n";
            str += "\t 4. Update an address. \n";
            str += "\t 5. Sort addresses after their city. \n";
            str += "\t 6. Filter addresses after the city. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getAddressCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the address's commands
     */
    private Map<String, Runnable> getAddressCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addAddress);
        commands.put("2", this::getAddresses);
        commands.put("3", this::deleteAddress);
        commands.put("4", this::updateAddress);
        commands.put("5", this::sortAddresses);
        commands.put("6", this::filterAddresses);
        return commands;
    }

    /**
     * Adds an address.
     *
     * @throws Exception if the address is null
     */
    private void addAddress() {
        Address address = readAddress();
        LOG.info("saveAddress: address={}", address);
        try {
            Optional<Address> a = Optional.ofNullable(address);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null address!");
            return;
        }
        try {
            AddressDto newAddress = AddressDto.builder().city(address.getCity()).street(address.getStreet()).number(address.getNumber()).zipCode(address.getZipCode()).build();
            newAddress.setId(address.getId());
            Optional<AddressDto> a = restService.addAddress(newAddress);
            a.ifPresent(x -> {
                LOG.info("Address already exists");
            });
        }catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception ex) {
            LOG.info("Validation exception!");
        }

    }

    /**
     * The function reads an address from keyboard.
     *
     * @return null in case of exception,otherwise the dish read
     * @throws IOException if the input from keyboard is not ok
     */
    private Address readAddress() {
        System.out.println("Read Address {city,street,number,zipcode}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("City:");
            String city = bufferedReader.readLine();
            System.out.println("Street");
            String street = bufferedReader.readLine();
            System.out.println("Number");
            int number = Integer.parseInt(bufferedReader.readLine());
            System.out.println("ZipCode:");
            String zipcode = bufferedReader.readLine();
            Address address = new Address(city, street, number, zipcode);
            address.setId(id);
            return address;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the addressees.
     */
    private void getAddresses() {
        LOG.info("getAllAddresses --- method entered");
        AddressesDto addresses = restService.getAddresses();
        Optional<Integer> op = Optional.of(addresses.getAddresses().size());
        op.filter(x -> x == 0).ifPresent( s -> {LOG.info("There are no addresses!");});
        addresses.getAddresses().stream().map(AddressDto::toString).forEach(LOG::info);
    }

    /**
     * Deletes an address.
     *
     * @throws Exception   if the id of the address doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteAddress() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.valueOf(bufferRead.readLine());
            LOG.info("deleteAddress: id={}", id);
            restService.deleteAddress(id);
            LOG.info("deleteAddress --- method finished");
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOG.info("Please input an integer value!");
        } catch (Exception e) {
            LOG.info("No address with this ID was found!");
            return;
        }
    }

    /**
     * Updates an address
     *
     * @throws IllegalArgumentException if the address is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateAddress() {
        Address address = readAddress();
        LOG.info("updateAddress: address={}",address);
        try {
            Optional<Address> a = Optional.ofNullable(address);
            a.orElseThrow(Exception::new);
        }
        catch (Exception e) {
            LOG.info("Can't have a null address!");
            return;
        }
        try {
            AddressDto newAddress = AddressDto.builder().city(address.getCity()).street(address.getStreet()).number(address.getNumber()).zipCode(address.getZipCode()).build();
            newAddress.setId(address.getId());
            restService.updateAddress(newAddress,address.getId());
        }
        catch (ValidatorException e) {
            LOG.info(e.getMessage());
        }
        catch (Exception e) {
            LOG.info("Address with this ID doesn't exist/Validation Error!");
        }
    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortAddresses () {
        LOG.info("sortAddress--- method entered");
        AddressesDto addresses = restService.sortAddressCity();
        Optional<Integer> op = Optional.of(addresses.getAddresses().size());
        op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No address added yet!");});
        addresses.getAddresses().stream().map(AddressDto::toString).forEach(LOG::info);
    }

    /**
     * Filters the addresses after the city name
     * The functions gets as input from the keyboard a city name
     * returns the list of addresses in that city
     */
    private void filterAddresses () {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("City:");
            String city = String.valueOf(bufferedReader.readLine());
            AddressesDto addressesDto=restService.filterCity(city);
            Optional<Integer> op = Optional.of(addressesDto.getAddresses().size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No address with this city!");});
            addressesDto.getAddresses().stream().map(AddressDto::toString).forEach(LOG::info);
        } catch (IOException ex) {
            LOG.info(ex.getMessage());
        }
    }
}
