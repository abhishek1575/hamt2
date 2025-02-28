package cstech.ai.hamt.controller;

import cstech.ai.hamt.dto.PasswordChangeDTO;
import cstech.ai.hamt.dto.UserDto;
import cstech.ai.hamt.entity.User;
import cstech.ai.hamt.repository.UserRepository;
import cstech.ai.hamt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userDetailsService;



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Add new user
    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> register(@RequestBody User user) {
        String Password = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String EmailMassage = "Dear" + user.getName() + "\n\n" +"Welcome to Hardware Asset Management Tool.\n"
                +"Please login to the portal"+
                "Portal Link : "+"\n\n"
                +"Best Regard,\n\n"
                +"Hardware Management Tool";

        return ResponseEntity.ok("User registered successfully");
    }

    @PutMapping("/modifyUser")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> modifyUser(@RequestBody UserDto user) {

        Optional<User> existingUser = userRepository.findById(user.getId());
        try {
            if(existingUser.isPresent()){
                User user1 = existingUser.get();
                user1.setEmail(user.getEmail());
                user1.setName(user.getName());
                user1.setRole(user.getRole());
                userRepository.save(user1);
            }
            return ResponseEntity.ok("User Modified Successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }

    }


    @PostMapping("/changePassword")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {

        Optional<User> userInfo = userRepository.findById(passwordChangeDTO.getId());
        if (userInfo.isPresent()) {
            User user = userInfo.get();
            try {
                if (passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
                    userRepository.save(user);
                    return ResponseEntity.ok("Password Changed Successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Old Password is Wrong");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }



    //Get all user
    @GetMapping("/getUserById")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getUserById(@RequestParam  Long  Id) {
        try {
            User users = userDetailsService.getUserById(Id);

            UserDto user =  new UserDto();
            user.setName(users.getName());
            user.setId(Id);
            user.setEmail(users.getEmail());
            user.setRole(users.getRole());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    //Function to delete User
    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> deleteUserById(@RequestParam Long id) {
        Optional<User> userInfo = userRepository.findById(id);

        if (userInfo.isPresent()) {
            try {
                userRepository.deleteById(id);
                return ResponseEntity.ok("User deleted successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    //Get all user
    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAll() {
        try {
            List<User> users = userRepository.getAll();
            List<UserDto> userDtos = new ArrayList<>();

            for (User user : users) {
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setName(user.getName());
                userDto.setEmail(user.getEmail());
                userDto.setRole(user.getRole());
                userDtos.add(userDto);
            }
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
