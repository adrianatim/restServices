package ro.ubb.catalog.core.model.validators;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Restaurant;

import java.util.Optional;

/**
 * Class for Restaurant Validator
 * The address should have a valid ID, non empty name field,a positive integer between 0 and 5 for rating, a positive integer for the capacity, boolean type for delivery
 */
@Component
public class RestaurantValidator implements Validator<Restaurant> {
    @Override
    public void validate(Restaurant entity) throws ValidatorException {
        Optional<Integer> id = Optional.of(entity.getId());
        id.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Id must be a positive integer value!\n");
        });

        Optional<String> name = Optional.of(entity.getName());
        name.filter(x -> x.equals("")).ifPresent(s -> {
            throw new ValidatorException("Name field cannot be empty!\n");
        });

        Optional<Integer> rating = Optional.of(entity.getRating());
        rating.filter(x -> x < 0 || x > 5).ifPresent(s -> {
            throw new ValidatorException("Rating should be an integer from 0 to 5!\n");
        });

        Optional<Integer> capacity = Optional.of(entity.getCapacity());
        capacity.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Capacity must be a positive integer value!\n");
        });

    }
}