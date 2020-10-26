package reminderserver.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reminderserver.models.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    List<User> findAll();
    Optional<User> findByEmail(String email);
    List<User> findByActive(Boolean active);
}
