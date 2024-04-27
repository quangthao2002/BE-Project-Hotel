package org.example.qthotelbe.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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


    @Override
    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findRoleByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));// dung de set role cho user colelctions.singletonList tra ve 1 list chua 1 phan tu duy nhat
        userRepository.save(user);
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
