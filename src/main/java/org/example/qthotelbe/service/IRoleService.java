package org.example.qthotelbe.service;

import org.example.qthotelbe.model.Role;
import org.example.qthotelbe.model.User;

import java.util.List;

public interface IRoleService {

    List<Role> getRoles();

    Role createRole(Role role);

    void deleteRole(Long id);

    Role findByName(String name);

    User removeUserFromRole(Long userId, Long roleId);

    User assignRoleToUser(Long userId, Long roleId);

    Role removeAllUsersFromRole(Long roleId);

}
