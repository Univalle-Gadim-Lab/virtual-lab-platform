package edu.univalle.gadim.virtual_lab_platform.users.api.type;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface User {
  @Nonnull
  String id();

  @Nonnull
  String name();

  @Nonnull
  String lastName();

  @Nonnull
  Optional<String> externalCode();

  @Nonnull
  String password();

  @Nonnull
  UserStatus status();

  @Nonnull
  LocalDateTime createdDate();
}
