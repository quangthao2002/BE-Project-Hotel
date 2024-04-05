package org.example.qthotelbe.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.exception.RoleNotFoundException;
import org.example.qthotelbe.exception.UserAlreadyExistsException;
import org.example.qthotelbe.model.Role;
import org.example.qthotelbe.model.User;
import org.example.qthotelbe.repository.RoleRepository;
import org.example.qthotelbe.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final  UserRepository userRepository;
    private  final  PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    //Dang ky nguoi dung moi
    @Override
    public User registerUser(User user) {

        if (userRepository.existsByEmail((user.getEmail() ))){
            throw  new UserAlreadyExistsException("User with email: " + user.getEmail() + " already exists");
        }
        user.setPassword((passwordEncoder.encode(user.getPassword())));
        Role userRole = roleRepository.findRoleByName("ROLE_USER").get();// tim vai tro cua nguoi dung la ROLE_USER
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found")) ;
    }


    @Transactional
    @Override
    public void deleteUserByEmail(String email) {
        User theUser = getUserByEmail(email);
        if (theUser != null) {
            userRepository.deleteByEmail(email);
        }
    }
}
