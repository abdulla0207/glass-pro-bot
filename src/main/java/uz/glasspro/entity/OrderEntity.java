package uz.glasspro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.glasspro.enums.OrderStatusEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;
    @Column
    private String color;
    @Column
    private Double height;
    @Column
    private Double width;
    @Column
    private Integer amount;
    @Column
    private Double price;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatusEnum orderStatusEnum;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private UserEntity userEntity;
}
