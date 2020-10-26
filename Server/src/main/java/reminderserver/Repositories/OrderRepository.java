package reminderserver.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reminderserver.models.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order,Long> {
    List<Order> findAll();
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findByCustomerId(Long customerId);
}
