package ro.ubb.catalog.core.repository.dbRepository;

import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.repository.RepoI;

import java.util.List;

public interface OrderDbRepoI extends RepoI<Orders, Integer> {
    List<Orders> findByPaymentType(String paymentType);
    List<Orders> findByOrderByPaymentType();
}
