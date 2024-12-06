package repository.user;

import model.User;
import model.validator.Notification;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();
    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean save(User user);

    boolean delete(User user);
    void removeAll();

    boolean existsByUsername(String username);

    Optional<Long> findIdByUsername(String username);


}
