package reminderserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reminderserver.Repositories.UserRepository;
import reminderserver.models.User;

import java.util.*;


@Service
public class UserService {


    @Autowired
    UserRepository userRepository;

    public void create(User user){
        userRepository.save(user);
    }

    public void update(User user) {
        for (User me : userRepository.findAll()) {
            if (user.getId().equals(me.getId())) {
                me.setActive(user.isActive());
                userRepository.save(me);
            }
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findByActive(Boolean active){
        return userRepository.findByActive(active);
    }
}
