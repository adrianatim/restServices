package ro.ubb.catalog.core.repository.dbRepository;

import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.repository.RepoI;

import java.util.List;

public interface AddressDbRepoI extends RepoI<Address, Integer> {
    List<Address> findByCity(String city);
    List<Address> findByOrderByCity();
}
