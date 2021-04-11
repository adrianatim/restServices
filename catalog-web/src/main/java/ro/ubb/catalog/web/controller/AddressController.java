package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.converter.AddressConverter;
import ro.ubb.catalog.web.dto.AddressDto;
import ro.ubb.catalog.web.dto.AddressesDto;

import java.util.List;
import java.util.Optional;

@RestController
public class AddressController {

    private static final Logger Log = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    @Qualifier("addressService")
    private ServiceI<Integer, Address> addressService;

    @Autowired
    private AddressConverter addressConverter;

    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    AddressesDto getAllAddresses() {
        Log.trace("getAllAddresses --- method entered");
        List<Address> addresses = addressService.get();
        List<AddressDto> addressDtos = addressConverter.convertModelsToDtos(addresses);
        AddressesDto result = new AddressesDto(addressDtos);
        Log.trace("getAllAddresses: result={}", result);
        return result;
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.POST)
    AddressDto saveAddress(@RequestBody AddressDto addressDto) {
        Log.trace("saveAddress: dto={}", addressDto);
        Optional<Address> op = this.addressService.add(addressConverter.convertDtoToModel(addressDto));
        try {
            op.orElseThrow(Exception::new);
        } catch (Exception e) {
            return null;
        }
        AddressDto result = addressConverter.convertModelToDto(op.get());
        Log.trace("saveAddress: result={}", result);
        return result;
    }

    @RequestMapping(value = "/addresses/{id}", method = RequestMethod.PUT)
    AddressDto updateAddress(@PathVariable Integer id, @RequestBody AddressDto addressDto) {
        Log.trace("updateAddress: id={}, dto={}", id, addressDto);
        Optional<Address> op = addressService.update(addressConverter.convertDtoToModel(addressDto));
        AddressDto result = addressConverter.convertModelToDto(op.get());
        Log.trace("updateAddress: result={}", result);
        return result;
    }

    @RequestMapping(value = "/addresses/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteAddress(@PathVariable Integer id) {
        Log.trace("deleteAddress: id={}", id);
        addressService.delete(id);
        Log.trace("deleteAddress --- method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/addresses/filter", method = RequestMethod.GET)
    AddressesDto filterFunction(@RequestParam String city) {
        Log.trace("filterAddressCity --- method entered");
        List<Address> addresses = addressService.filterFunction(city);
        List<AddressDto> addressDtos = addressConverter.convertModelsToDtos(addresses);
        AddressesDto result = new AddressesDto(addressDtos);
        Log.trace("filterAddressCity: result={}", result);
        return result;
    }

    @RequestMapping(value = "/addresses/sort", method = RequestMethod.GET)
    AddressesDto sortFunction() {
        Log.trace("sortAddress--- method entered");
        List<Address> addresses = addressService.sortFunction();
        List<AddressDto> dtos = addressConverter.convertModelsToDtos(addresses);
        AddressesDto result = new AddressesDto(dtos);
        Log.trace("sortAddress: result={}", result);
        return result;
    }
}
