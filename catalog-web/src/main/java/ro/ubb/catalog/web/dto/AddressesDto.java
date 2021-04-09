package ro.ubb.catalog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.ubb.catalog.core.model.Address;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddressesDto {
    private List<AddressDto> addresses;
}
