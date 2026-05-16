package edu.univalle.gadim.virtual_lab_platform.users.api.type;

import javax.annotation.Nonnull;

public interface UserRole {
  @Nonnull
  String id();

  @Nonnull
  String userId();

  @Nonnull
  Role role();
}
