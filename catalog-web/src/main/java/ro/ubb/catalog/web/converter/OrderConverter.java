package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.web.dto.OrderDto;

@Component
public class OrderConverter extends BaseConverter<Orders, OrderDto> {

    @Override
    public Orders convertDtoToModel(OrderDto dto) {
        Orders order = Orders.builder().restaurantID(dto.getRestaurantID()).clientID(dto.getClientID())
                .dishID(dto.getDishID()).paymentType(dto.getPaymentType()).date(dto.getDate()).build();
        order.setId(dto.getId());
        return order;
    }

    @Override
    public OrderDto convertModelToDto(Orders order) {
        OrderDto orderDto = OrderDto.builder().restaurantID(order.getRestaurantID()).clientID(order.getClientID())
                .dishID(order.getDishID()).paymentType(order.getPaymentType()).date(order.getDate()).build();
        orderDto.setId(order.getId());
        return orderDto;
    }
}
