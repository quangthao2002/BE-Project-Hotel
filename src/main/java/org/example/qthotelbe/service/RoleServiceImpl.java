package org.example.qthotelbe.service;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.exception.RoleAlreadyExistsException;
import org.example.qthotelbe.exception.UserAlreadyExistsException;
import org.example.qthotelbe.model.Role;
import org.example.qthotelbe.model.User;
import org.example.qthotelbe.repository.RoleRepository;
import org.example.qthotelbe.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();

        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)) {
            throw new RoleAlreadyExistsException("Role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);// xoa tat ca nguoi dung trong role truoc khi xoa role
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findRoleByName(name).orElseThrow(() -> new RoleAlreadyExistsException("Role not found"));

    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if (role.isPresent() && role.get().getUsers().contains((user.get()))) {
            role.get().removeRoleFromUser(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }


    // gan role cho nguoi dung
    @Override
    public User assignRoleToUser(Long userId, Long roleId) { //
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

//        if (user.isEmpty()){
//            throw new UsernameNotFoundException("User not found");
//        }
//        if (role.isEmpty()){
//            throw new RoleAlreadyExistsException("Role not found");
//        }
        if (user.isPresent() && user.get().getRoles().contains(role.get())){
            throw  new UserAlreadyExistsException(user.get().getFirstName()+"is already assigned to the"+ role.get().getName());
        }
        if (role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role>  role = roleRepository.findById(roleId);
        role.ifPresent(Role :: removeAllUsersFromRole); // neu role ton tai thi xoa tat ca nguoi dung trong role
        return  roleRepository.save(role.get());
    }
}
