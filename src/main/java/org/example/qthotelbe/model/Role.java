package org.example.qthotelbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    //  them nguoi dung vao role hien tai
    public void assignRoleToUser(User user) {
        user.getRoles().add(this);  // add role hien tai vao user
        this.getUsers().add(user);// add nguoi dung vao tap hop cac nguoi dung cua vaii tro hien tai
    }

    // phuong thuc nay se giup chung ta xoa vai tro cua nguoi dung
    public void removeRoleFromUser(User user) {
        user.getRoles().remove(this);// remove role hien tai khoi user
        this.getUsers().remove(user);
    }

    public void removeAllUsersFromRole(){
        if (this.getUsers() != null){
            List<User> roleUsers =  this.getUsers().stream().toList();
            //gọi phương thức removeRoleFromUser cho mỗi người dùng trong danh sách roleUsers
         roleUsers.forEach(this :: removeRoleFromUser);
//            for (User user : roleUsers) {
//                this.removeRoleFromUser(user);
//            }
        }
    }
    public String getName(){
        return  name != null ? name :  "";
    }
}
