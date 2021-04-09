package ro.ubb.catalog.core.model.validators;

/**
 * @author radu.
 */

public class ValidatorException extends RestaurantException {
    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }
}
