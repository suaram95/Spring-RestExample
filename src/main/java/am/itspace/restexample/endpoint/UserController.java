package am.itspace.restexample.endpoint;

import am.itspace.restexample.dto.AuthRequest;
import am.itspace.restexample.dto.AuthResponse;
import am.itspace.restexample.dto.UserDto;
import am.itspace.restexample.exception.DuplicateEntityException;
import am.itspace.restexample.exception.ResourceNotFoundException;
import am.itspace.restexample.model.User;
import am.itspace.restexample.service.UserService;
import am.itspace.restexample.util.JWTTokenUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenUtil tokenUtil;
    private final ModelMapper modelMapper;

    @PostMapping("/user/auth")
    public ResponseEntity auth(@RequestBody AuthRequest authRequest){
        Optional<User> byEmail = userService.findByEmail(authRequest.getEmail());
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
    public User save(@RequestBody UserDto userDto){
        if (!userService.findByEmail(userDto.getEmail()).isPresent()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = modelMapper.map(userDto, User.class);
            return userService.save(user);
        } else {
            throw new DuplicateEntityException("Username already exists");
        }
    }

    @GetMapping("/user")
    public List<User> getUsers(){
        return userService.findAll();
    }

    @PutMapping("/user/{userId}/image")
    public void  uploadImage(@PathVariable("userId") int userId, @RequestParam("image")MultipartFile file){
        Optional<User> byId = userService.findById(userId);
        if (!byId.isPresent()){
            throw new ResourceNotFoundException();
        }
        System.out.println(file.getOriginalFilename());
    }


}

