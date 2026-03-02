package edu.univalle.gadim.virtual_lab_platform.users.api.type;

import java.time.LocalDateTime;

public interface User {
  String userId();
  String name();
  String lastName();
  String studentCode();
  String password();
  UserStatus status();
  LocalDateTime createdDate();
}
