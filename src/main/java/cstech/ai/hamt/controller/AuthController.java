package cstech.ai.hamt.controller;

import cstech.ai.hamt.config.JwtUtil;
import cstech.ai.hamt.dto.AuthResponse;
import cstech.ai.hamt.entity.AuthRequest;
import cstech.ai.hamt.entity.User;
import cstech.ai.hamt.repository.UserRepository;
import cstech.ai.hamt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            if(authenticate.isAuthenticated()) {

                final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

                User user = userDetailsService.findUserbyEmail(authRequest.getEmail());
                //final String jwt = jwtUtil.generateToken(userDetails.getUsername(), userRepository.findByEmail(authRequest.getEmail()).get().getRole());
                final String jwt = jwtUtil.generateToken(authRequest.getEmail(), user.getRole());
                //  User user = userDetailsService.findUserbyEmail(authRequest.getEmail());

                String role = user.getRole().toString();

                AuthResponse authResponse = new AuthResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        role,
                        jwt
                );
                return ResponseEntity.ok(authResponse);


            } else {
                // If authentication fails
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid user request");
            }
        } catch (AuthenticationException e) {
            // Handle authentication exception
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid user request");
        }


    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //  user.setPassword(user.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }



    //Function to forget password
    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestParam String email, @RequestParam String password) {
        // Trim the email to remove any leading/trailing whitespace
        email = email.trim();

        // Log the email being searched
        System.out.println("Searching for user with email: " + email);

        Optional<User> userInfo = userRepository.findByEmailId(email);

        if (userInfo.isPresent()) {
            User user = userInfo.get();
            try {
                // Encode the new password before saving
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return ResponseEntity.ok("Password changed successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected error: " + e.getMessage());
            }
        } else {
            // Log that the user was not found
            System.out.println("User not found for email: " + email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }

}
