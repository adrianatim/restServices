package ro.ubb.catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class AddressDto extends BaseDto {

    private String city;
    private String street;
    private Integer number;
    private String zipCode;
}
