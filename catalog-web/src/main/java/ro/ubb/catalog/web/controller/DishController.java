package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Dish;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.converter.DishConverter;
import ro.ubb.catalog.web.dto.DishDto;
import ro.ubb.catalog.web.dto.DishesDto;

import java.util.List;
import java.util.Optional;

@RestController
public class DishController {

    private static final Logger Log = LoggerFactory.getLogger(DishController.class);

    @Autowired
    @Qualifier("dishService")
    private ServiceI<Integer, Dish> dishService;

    @Autowired
    private DishConverter dishConverter;

    @RequestMapping(value = "/dishes", method = RequestMethod.GET)
    DishesDto getAllDishes() {
        Log.trace("getAllDishes --- method entered");
        List<Dish> dishes = dishService.get();
        List<DishDto> dishDtos = dishConverter.convertModelsToDtos(dishes);
        DishesDto result = new DishesDto(dishDtos);
        Log.trace("getAllDishes: result={}", result);
        return result;
    }

    @RequestMapping(value = "/dishes", method = RequestMethod.POST)
    DishDto saveDish(@RequestBody DishDto dishDto) {
        Log.trace("saveDish: dto={}", dishDto);
        Optional<Dish> op = this.dishService.add(dishConverter.convertDtoToModel(dishDto));
        try {
            op.orElseThrow(Exception::new);
        } catch (Exception e) {
            return null;
        }
        DishDto result = dishConverter.convertModelToDto(op.get());
        Log.trace("saveDish: result={}", result);
        return result;
    }

    @RequestMapping(value = "/dishes/{id}", method = RequestMethod.PUT)
    DishDto updateDish(@PathVariable Integer id, @RequestBody DishDto dishDto) {
        Log.trace("updateDish: id={}, dto={}", id, dishDto);
        Optional<Dish> op = dishService.update(dishConverter.convertDtoToModel(dishDto));
        DishDto result = dishConverter.convertModelToDto(op.get());
        Log.trace("updateDish: result={}", result);
        return result;
    }

    @RequestMapping(value = "/dishes/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteDish(@PathVariable Integer id) {
        Log.trace("deleteDish: id={}", id);
        dishService.delete(id);
        Log.trace("deleteDish --- method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/dishes/filter", method = RequestMethod.GET)
    DishesDto filterFunction(@RequestParam String name) {
        Log.trace("filterDishName --- method entered");
        List<Dish> dishes = dishService.filterFunction(name);
        List<DishDto> dishDtos = dishConverter.convertModelsToDtos(dishes);
        DishesDto result = new DishesDto(dishDtos);
        Log.trace("filterDishName: result={}", result);
        return result;
    }

    @RequestMapping(value = "/dishes/sort", method = RequestMethod.GET)
    DishesDto sortFunction() {
        Log.trace("sortDish -- method entered");
        List<Dish> dishes = dishService.sortFunction();
        List<DishDto> dtos = dishConverter.convertModelsToDtos(dishes);
        DishesDto result = new DishesDto(dtos);
        Log.trace("sortDish: result={}", result);
        return result;
    }
}