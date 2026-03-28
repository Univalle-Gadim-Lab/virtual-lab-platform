package edu.univalle.gadim.virtual_lab_platform.users.api.service;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

  User createUser(User user);

  Optional<User> getUserById(String id);

  Optional<User> getUserByUsername(String username);

  List<User> getAllUsers();
}
