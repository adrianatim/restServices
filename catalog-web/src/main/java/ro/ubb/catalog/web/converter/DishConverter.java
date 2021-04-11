package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Dish;
import ro.ubb.catalog.web.dto.DishDto;

@Component
public class DishConverter extends BaseConverter<Dish, DishDto> {

    @Override
    public Dish convertDtoToModel(DishDto dto) {
        Dish dish = Dish.builder().name(dto.getName()).price(dto.getPrice()).quantity(dto.getQuantity())
                .category(dto.getCategory()).build();
        dish.setId(dto.getId());
        return dish;
    }

    @Override
    public DishDto convertModelToDto(Dish dish) {
        DishDto dishDto = DishDto.builder().name(dish.getName()).price(dish.getPrice()).quantity(dish.getQuantity())
                .category(dish.getCategory()).build();
        dishDto.setId(dish.getId());
        return dishDto;
    }
}
