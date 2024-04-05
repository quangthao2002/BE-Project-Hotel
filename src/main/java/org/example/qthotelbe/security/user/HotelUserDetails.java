package org.example.qthotelbe.security.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.qthotelbe.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HotelUserDetails implements UserDetails {

    private Long id;
    private String email;

    private String password;

    private Collection<GrantedAuthority> authorities;

    public static  HotelUserDetails buiHotelUserDetails(User user){
        List<GrantedAuthority> authorities = user.getRoles() // lay roles cua user va chuyen thanh list authorities voi cac role, authority la cac quyen cua user
                .stream()
//  Mỗi GrantedAuthority được tạo từ một role của user thông qua việc sử dụng new SimpleGrantedAuthority(role.getName()).
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        // tra ve doi tuong moi HotelUserDetails voi cac thong tin cua user va authorities
        return  new HotelUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
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
        return email ;
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
