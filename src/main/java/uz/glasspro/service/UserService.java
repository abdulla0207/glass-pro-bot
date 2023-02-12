package uz.glasspro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.glasspro.dto.UserDTO;
import uz.glasspro.entity.UserEntity;
import uz.glasspro.enums.RoleEnum;
import uz.glasspro.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        if(!userDTO.getPhoneNumber().matches("[+]?9989[0-9]{8}"))
            return null;

        UserEntity userEntity = getEntity(userDTO);
        userRepository.save(userEntity);
        userDTO.setId(userEntity.getId());
        return userDTO;
    }

    private UserEntity getEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setUsername(userDTO.getUserName());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        userEntity.setCreatedDate(LocalDateTime.now());
        userEntity.setUserStatusEnum(UserStatusEnum.ACTIVE);
        userEntity.setRoleEnum(RoleEnum.USER);
        return userEntity;
    }

    public String removeUser(String phoneNum) {
        Optional<UserEntity> userEntity = userRepository.getUserEntityByPhoneNumber(phoneNum);
        if(userEntity.isEmpty()){
            return "Аккаунт с таким номером не существует в базе данных\n" +
                    "<b>Пожалуйста, повторите попытку</b>\n\n" +
                    "Попробуйте написать номер в формате:" +
                    "<b>+9989xxxxxxxx</b> или <b>9998xxxxxxxx</b>";
        }

        userRepository.removeUserEntityByPhoneNumber(phoneNum);
        return "Пользователь с номером <b>"+phoneNum+"</b> был удален с базы данных";
    }

    public UserDTO getUserById(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if(userEntity.isEmpty())
            return null;
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userEntity.get().getUsername());
        userDTO.setFirstName(userEntity.get().getFirstName());
        userDTO.setPhoneNumber(userEntity.get().getPhoneNumber());
        userDTO.setRoleEnum(userEntity.get().getRoleEnum());
        userDTO.setLastName(userEntity.get().getLastName());
        userDTO.setId(userId);

        return userDTO;
    }
}
