package uz.glasspro.dto;

import lombok.Getter;
import lombok.Setter;
import uz.glasspro.enums.OrderStatusEnum;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    private Integer id;
    private String name;
    private String color;
    private Double height;
    private Double width;
    private Integer amount;
    private Double price;

    private LocalDateTime createdDate;
    private OrderStatusEnum orderStatusEnum;
    private Long userId;
    private UserDTO userDTO;
}
