package ro.ubb.catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class DishDto extends BaseDto{

    private String name;
    private float price;
    private int quantity;
    private String category;
}
