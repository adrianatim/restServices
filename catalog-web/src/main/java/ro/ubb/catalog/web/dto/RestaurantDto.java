package ro.ubb.catalog.web.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class RestaurantDto extends BaseDto {

    private String name;
    private Integer rating;
    private Integer capacity;
    private Boolean delivery;
}
