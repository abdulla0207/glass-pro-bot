package uz.glasspro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;
    private String location;
}
