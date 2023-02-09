package uz.glasspro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.glasspro.dto.UserDTO;
import uz.glasspro.entity.UserEntity;
import uz.glasspro.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        if(userRepository.findById(userDTO.getId()).isEmpty()){
            if(!userDTO.getPhoneNumber().matches("[+]?9989[0-9]{8}"))
                return null;
            UserEntity userEntity = getEntity(userDTO);
            userRepository.save(userEntity);
            userDTO.setId(userEntity.getId());
        }
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

}
