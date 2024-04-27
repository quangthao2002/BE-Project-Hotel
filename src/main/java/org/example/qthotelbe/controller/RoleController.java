package org.example.qthotelbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.exception.RoleAlreadyExistsException;
import org.example.qthotelbe.model.Role;
import org.example.qthotelbe.model.User;
import org.example.qthotelbe.service.IRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;

@RestController

@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @GetMapping("/all-role")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new  ResponseEntity<>(roleService.getRoles(),FOUND);
    }


    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role theRole){
        try {
            roleService.createRole(theRole);
            return new ResponseEntity<>("Role created successfully", HttpStatus.CREATED);
        }catch (RoleAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }
    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    public User removeUserFromRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-role-to-user")
    public User assignUserRoleToRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
