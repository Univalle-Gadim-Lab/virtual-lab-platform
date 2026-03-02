package edu.univalle.gadim.virtual_lab_platform.users.api.service;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole;
import java.util.List;

public interface UserRoleService {

  UserRole createUserRole(String userId, Role role);

  List<UserRole> createUserRoles(String userId, List<Role> roles);

  List<UserRole> getRoleByUserId(String userId);
}
