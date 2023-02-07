package uz.glasspro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.glasspro.dto.UserDTO;
import uz.glasspro.entity.UserEntity;
import uz.glasspro.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserDTO createUser(UserDTO userDTO) {
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

        return userEntity;
    }
}
