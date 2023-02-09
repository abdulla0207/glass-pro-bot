package uz.glasspro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import uz.glasspro.service.UserStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy="userEntity", cascade = CascadeType.REMOVE)
    private List<OrderEntity> orderEntities;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_status")
    private UserStatusEnum userStatusEnum;
}
