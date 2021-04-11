package ro.ubb.catalog.core.model.validators;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Orders;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class OrderValidator implements Validator<Orders> {
    @Override
    public void validate(Orders entity) throws ValidatorException {
        Optional<Integer> id = Optional.of(entity.getId());
        id.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Id must be a positive integer value!\n");
        });

        Optional<Integer> restaurantID = Optional.of(entity.getRestaurantID());
        restaurantID.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Restaurant ID must be a positive integer!\n");
        });

        Optional<Integer> clientID = Optional.of(entity.getClientID());
        clientID.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Client ID must be a positive integer!\n");
        });

        Optional<Integer> dishID = Optional.of(entity.getDishID());
        dishID.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Dish ID must be a positive integer!\n");
        });

        Optional<String> paymentType = Optional.of(entity.getPaymentType());
        paymentType.filter(x -> x.equals("")).ifPresent(s -> {
            throw new ValidatorException("Payment type field cannot be empty!\n");
        });

        Optional<Date> date = Optional.of(entity.getDate());
        //Create calendar for getting the date the restaurant opened
        Calendar c = Calendar.getInstance();
        // set Month
        c.set(Calendar.MONTH, 1);
        // set Date
        c.set(Calendar.DATE, 1);
        // set Year
        c.set(Calendar.YEAR, 2019);
        // creating a date object with specified time.
        Date dateOne = c.getTime();
        // creating a date of object

        date.filter(x -> x.before(dateOne)).ifPresent(s -> {
            throw new ValidatorException("Date field cannot be empty and should contain a date after 01/01/2019!\n");
        });
    }
}
