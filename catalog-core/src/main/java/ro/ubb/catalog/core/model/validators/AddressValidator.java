package ro.ubb.catalog.core.model.validators;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Address;

import java.util.Optional;

/**
 * Class for Address Validator
 * The address should have a valid ID, non empty city and street fields, a positive integer for the number, a valid zip code (6 digits)
 */
@Component
public class AddressValidator implements Validator<Address> {
    @Override
    public void validate(Address entity) throws ValidatorException {
        Optional<Integer> id = Optional.of(entity.getId());
        id.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Id must be a positive integer value!\n");
        });

        Optional<String> city = Optional.of(entity.getCity());
        city.filter(x -> x.equals("")).ifPresent(s -> {
            throw new ValidatorException("City field cannot be empty!\n");
        });

        Optional<String> street = Optional.of(entity.getStreet());
        street.filter(x -> x.equals("")).ifPresent(s -> {
            throw new ValidatorException("Street field cannot be empty!\n");
        });

        Optional<Integer> number = Optional.of(entity.getNumber());
        number.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Street number must be a positive integer value!\n");
        });

        Optional<String> zipCode = Optional.of(entity.getZipCode());
        zipCode.filter(x -> x.length() != 6 || !ExtraFunctions.OnlyDigits(x)).ifPresent(s -> {
            throw new ValidatorException("The zip code should have the length 6 and contain only digits!\n");
        });

    }
}
