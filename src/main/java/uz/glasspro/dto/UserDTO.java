package uz.glasspro.dto;

import lombok.Getter;
import lombok.Setter;
import uz.glasspro.enums.RoleEnum;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;

    private RoleEnum roleEnum;
}
