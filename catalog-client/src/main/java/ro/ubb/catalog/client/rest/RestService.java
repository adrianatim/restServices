package ro.ubb.catalog.client.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.web.dto.AddressDto;
import ro.ubb.catalog.web.dto.AddressesDto;

import java.util.Optional;

@Component
public class RestService {

    @Autowired
    RestTemplate restTemplate;

    public AddressesDto getAddresses() {
        return restTemplate.getForObject("http://localhost:8080/api/addresses", AddressesDto.class);
    }

    public Optional<AddressDto> addAddress(AddressDto newAddress) {
        return Optional.ofNullable(restTemplate.postForObject("http://localhost:8080/api/addresses", newAddress, AddressDto.class));
    }

    public void deleteAddress(Integer id) {
        restTemplate.delete("http://localhost:8080/api/addresses/{id}", id);
    }

    public void updateAddress(AddressDto newAddress, Integer id) {
        restTemplate.put("http://localhost:8080/api/addresses/{id}", newAddress, id);
    }

    public AddressesDto filterCity(String city) {
        return restTemplate.getForObject("http://localhost:8080/api/addresses/filter?city={city}", AddressesDto.class, city);
    }

    public AddressesDto sortAddressCity() {
        return restTemplate.getForObject("http://localhost:8080/api/addresses/sort", AddressesDto.class);
    }
}
