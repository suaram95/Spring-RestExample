package am.itspace.restexample.endpoint;

import am.itspace.restexample.dto.AuthRequest;
import am.itspace.restexample.dto.AuthResponse;
import am.itspace.restexample.exception.DuplicateEntityException;
import am.itspace.restexample.model.User;
import am.itspace.restexample.repository.UserRepository;
import am.itspace.restexample.util.JWTTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenUtil tokenUtil;


    @PostMapping("/user/auth")
    public ResponseEntity auth(@RequestBody AuthRequest authRequest){
        Optional<User> byEmail = userRepository.findByEmail(authRequest.getEmail());
        if (byEmail.isPresent()){
            User user = byEmail.get();
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())){
                String token = tokenUtil.generateToken(user.getEmail());
                return ResponseEntity.ok(AuthResponse.builder()
                        .token(token)
                        .name(user.getName())
                        .surname(user.getSurname())
                        .build());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }


    @PostMapping("/user")
    public void save(@RequestBody User user){
        if (!userRepository.findByEmail(user.getEmail()).isPresent()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            throw new DuplicateEntityException("Username already exists");
        }
    }

    @GetMapping("/user")
    public List<User> getUsers(){
        return userRepository.findAll();
    }


}

