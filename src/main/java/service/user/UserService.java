package service.user;

import model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    boolean save(User user);
    boolean existsByUsername(String username);

    boolean delete(User user);

    String generateReport();
}
