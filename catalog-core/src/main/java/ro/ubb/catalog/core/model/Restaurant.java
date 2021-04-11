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
public class Restaurant extends BaseEntity<Integer> {

    private String name;
    private Integer rating;
    private Integer capacity;
    private Boolean delivery;
}
