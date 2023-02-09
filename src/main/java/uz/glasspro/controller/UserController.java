package uz.glasspro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.glasspro.dto.UserDTO;
import uz.glasspro.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        UserDTO response = userService.createUser(userDTO);
        return ResponseEntity.ok(response);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{phoneNum}")
    public String removeUser(@PathVariable String phoneNum){
        String res = userService.removeUser(phoneNum);

        return res;
    }

}
