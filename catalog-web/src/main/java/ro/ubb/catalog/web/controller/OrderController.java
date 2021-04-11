package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.converter.OrderConverter;
import ro.ubb.catalog.web.dto.OrderDto;
import ro.ubb.catalog.web.dto.OrdersDto;

import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    private static final Logger Log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    @Qualifier("orderService")
    private ServiceI<Integer, Orders> orderService;

    @Autowired
    private OrderConverter orderConverter;

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    OrdersDto getAllOrders() {
        Log.trace("getAllOrders --- method entered");
        List<Orders> orders = orderService.get();
        List<OrderDto> orderDtos = orderConverter.convertModelsToDtos(orders);
        OrdersDto result = new OrdersDto(orderDtos);
        Log.trace("getAllOrders: result={}", result);
        return result;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    OrderDto saveOrder(@RequestBody OrderDto orderDto) {
        Log.trace("saveOrder: dto={}", orderDto);
        Optional<Orders> op = this.orderService.add(orderConverter.convertDtoToModel(orderDto));
        try {
            op.orElseThrow(Exception::new);
        } catch (Exception e) {
            return null;
        }
        OrderDto result = orderConverter.convertModelToDto(op.get());
        Log.trace("saveOrder: result={}", result);
        return result;
    }

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.PUT)
    OrderDto updateOrder(@PathVariable Integer id, @RequestBody OrderDto OrderDto) {
        Log.trace("updateOrder: id={}, dto={}", id, OrderDto);
        Optional<Orders> op = orderService.update(orderConverter.convertDtoToModel(OrderDto));
        OrderDto result = orderConverter.convertModelToDto(op.get());
        Log.trace("updateOrder: result={}", result);
        return result;
    }

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        Log.trace("deleteOrder: id={}", id);
        orderService.delete(id);
        Log.trace("deleteOrder --- method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/filter", method = RequestMethod.GET)
    OrdersDto filterFunction(@RequestParam String paymentType) {
        Log.trace("filterOrderPaymentType --- method entered");
        List<Orders> orders = orderService.filterFunction(paymentType);
        List<OrderDto> orderDtos = orderConverter.convertModelsToDtos(orders);
        OrdersDto result = new OrdersDto(orderDtos);
        Log.trace("filterOrderPaymentType: result={}", result);
        return result;
    }

    @RequestMapping(value = "/orders/sort", method = RequestMethod.GET)
    OrdersDto sortFunction() {
        Log.trace("sortOrder--- method entered");
        List<Orders> orders = orderService.sortFunction();
        List<OrderDto> dtos = orderConverter.convertModelsToDtos(orders);
        OrdersDto result = new OrdersDto(dtos);
        Log.trace("sortOrder: result={}", result);
        return result;
    }
}
