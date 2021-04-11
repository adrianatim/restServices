package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Dish extends BaseEntity<Integer> {

    private String name;
    private float price;
    private int quantity;
    private String category;
}
