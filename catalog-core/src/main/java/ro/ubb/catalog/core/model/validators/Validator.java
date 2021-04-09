package ro.ubb.catalog.core.model.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
