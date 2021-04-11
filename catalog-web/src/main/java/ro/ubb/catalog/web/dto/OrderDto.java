package ro.ubb.catalog.web.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class OrderDto extends BaseDto {
    private Integer restaurantID;
    private Integer clientID;
    private Integer dishID;
    private String paymentType;
    private Date date;

}
