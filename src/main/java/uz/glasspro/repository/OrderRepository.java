package uz.glasspro.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.glasspro.entity.OrderEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query(value = "select * from orders", nativeQuery = true)
    List<OrderEntity> getOrderList();

    @Modifying
    @Transactional
    @Query(value = "select * from orders where user_id = 65474440", nativeQuery = true)
    List<OrderEntity> getOrderById(long userId);




}
