package uz.glasspro.controller;

import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.glasspro.dto.OrderDTO;
import uz.glasspro.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getOrderList(){
        List<OrderDTO> orderDTO = orderService.getOrderList();
        return ResponseEntity.ok(orderDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO){
        OrderDTO response = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id")
    public ResponseEntity<List<OrderDTO>> getUserOrderList(long userId) {
        List<OrderDTO> response = orderService.getUserOrderList(userId);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
