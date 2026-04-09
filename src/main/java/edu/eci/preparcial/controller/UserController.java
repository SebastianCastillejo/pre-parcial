package edu.eci.preparcial.controller;

import edu.eci.preparcial.controller.DTO.LoginDTO;
import edu.eci.preparcial.controller.DTO.RegisterDTO;
import edu.eci.preparcial.core.model.User;
import edu.eci.preparcial.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "type", "Bearer"
        ));
    }
}
