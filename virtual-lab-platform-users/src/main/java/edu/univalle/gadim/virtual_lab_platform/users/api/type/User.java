package edu.univalle.gadim.virtual_lab_platform.users.api.type;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.NonNull;

public interface User {
  String id();
  String name();
  String lastName();
  Optional<String> externalCode();
  String password();
  UserStatus status();
  LocalDateTime createdDate();
}
