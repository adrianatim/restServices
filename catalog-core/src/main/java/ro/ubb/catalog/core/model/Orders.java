package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Orders extends BaseEntity<Integer> {

    private Integer restaurantID;
    private Integer clientID;
    private Integer dishID;
    private String paymentType;
    private Date date;
}
