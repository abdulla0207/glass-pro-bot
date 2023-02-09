package uz.glasspro.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.glasspro.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {



    Optional<UserEntity> getUserEntityByPhoneNumber(String phoneNumber);

    void removeUserEntityByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query("update UserEntity set userStatusEnum = ?2 where phoneNumber = ?1")
    Optional<UserEntity> banUserByPhoneNumber(String phoneNumber, String userStatus);

}
