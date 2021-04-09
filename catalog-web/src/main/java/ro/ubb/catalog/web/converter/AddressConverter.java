package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.web.dto.AddressDto;

@Component
public class AddressConverter extends BaseConverter<Address, AddressDto> {

    @Override
    public Address convertDtoToModel(AddressDto dto) {
        Address address = Address.builder().city(dto.getCity()).street(dto.getStreet()).number(dto.getNumber()).zipCode(dto.getZipCode()).build();
        address.setId(dto.getId());
        return address;
    }

    @Override
    public AddressDto convertModelToDto(Address address) {
        AddressDto addressDto = AddressDto.builder().city(address.getCity()).street(address.getStreet()).number(address.getNumber()).zipCode(address.getZipCode()).build();
        addressDto.setId(address.getId());
        return addressDto;
    }
}
