package ro.ubb.catalog.web.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseDto implements Serializable {
    private Integer id;

    @Override
    public String toString() {
        return "ID: " + id + " - ";
    }
}
