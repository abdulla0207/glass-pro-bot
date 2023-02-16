package uz.glasspro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.glasspro.dto.OrderDTO;
import uz.glasspro.entity.OrderEntity;
import uz.glasspro.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderDTO> getOrderList() {
        List<OrderEntity> orderEntities = orderRepository.getOrderList();

        List<OrderDTO> orderDTOS = new ArrayList<>();

        for(OrderEntity order : orderEntities){
            OrderDTO orderDTO = toDTO(order);
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    private OrderDTO toDTO(OrderEntity order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAmount(order.getAmount());
        orderDTO.setColor(order.getColor());
        orderDTO.setHeight(order.getHeight());
        orderDTO.setOrderStatusEnum(order.getOrderStatusEnum());
        orderDTO.setWidth(order.getWidth());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setName(order.getName());
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUserEntity().getId());
        orderDTO.setCreatedDate(order.getCreatedDate());
        return orderDTO;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        OrderEntity order = getEntity(orderDTO);

        orderRepository.save(order);
        orderDTO.setUserId(order.getUserId());
        return orderDTO;
    }

    private OrderEntity getEntity(OrderDTO orderDTO) {
        OrderEntity order = new OrderEntity();
        order.setAmount(orderDTO.getAmount());
        order.setUserId(orderDTO.getUserId());
        order.setOrderStatusEnum(orderDTO.getOrderStatusEnum());
        order.setPrice(orderDTO.getPrice());
        order.setWidth(orderDTO.getWidth());
        order.setColor(orderDTO.getColor());
        order.setName(orderDTO.getName());
        order.setHeight(orderDTO.getHeight());
        return order;
    }

    public List<OrderDTO> getUserOrderList(long userId) {
        List<OrderEntity> orderEntities = orderRepository.getOrderById(userId);

        if(orderEntities.isEmpty()){
            return null;
        }
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for(OrderEntity order : orderEntities){
            OrderDTO orderDTO = toDTO(order);
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }
}
