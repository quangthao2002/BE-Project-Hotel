package org.example.qthotelbe.security.user;

import lombok.*;
import org.example.qthotelbe.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HotelUserDetails implements UserDetails {
    private Long id;
    private  String email;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public static HotelUserDetails buildUserDetails(User user){
        List<GrantedAuthority> authorities = user.getRoles()// lay ra danh sach cac role cua user
                .stream() // chuyen sang dang stream
                .map(role -> new SimpleGrantedAuthority(role.getName())) // chuyen sang dang SimpleGrantedAuthority de tao ra danh sach cac quyen
                .collect(Collectors.toList());// chuyen sang dang list
    // tra ve doi tuong HotelUserDetails chua thong tin cua user va danh sach cac quyen
        return new HotelUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities);

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
