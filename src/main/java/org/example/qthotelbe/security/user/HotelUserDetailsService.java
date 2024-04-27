package org.example.qthotelbe.security.user;


import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.model.User;
import org.example.qthotelbe.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return HotelUserDetails.buildUserDetails(user); // tra ve doi tuong HotelUserDetails chua thong tin cua user va danh sach cac quyen
    }
}
