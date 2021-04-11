package ro.ubb.catalog.core.model.validators;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Dish;

import java.util.Optional;

/**
 * Class for a DishValidator, validates an object of type Dish.
 * A dish should have non-empty name, category; its price and quantity must be positive; its Id must be positive.
 */
@Component
public class DishValidator implements Validator<Dish> {
    @Override
    public void validate(Dish entity) throws ValidatorException {
        Optional<Integer> id = Optional.of(entity.getId());
        id.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Id must be a positive integer value!\n");
        });

        Optional<String> name = Optional.of(entity.getName());
        name.filter(x -> x.equals("")).ifPresent(s -> {
            throw new ValidatorException("Dish name field cannot be empty!\n");
        });

        Optional<String> category = Optional.of(entity.getCategory());
        category.filter(x -> x.equals("")).ifPresent(s -> {
            throw new ValidatorException("Category field cannot be empty!\n");
        });

        Optional<Float> price = Optional.of(entity.getPrice());
        price.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Price must be a greater than 0!\n");
        });

        Optional<Integer> quantity = Optional.of(entity.getQuantity());
        quantity.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Quantity must be a positive integer!\n");
        });
    }
}