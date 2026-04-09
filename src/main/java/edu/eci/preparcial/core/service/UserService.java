package edu.eci.preparcial.core.service;

import edu.eci.preparcial.controller.DTO.LoginDTO;
import edu.eci.preparcial.controller.DTO.RegisterDTO;
import edu.eci.preparcial.controller.mapper.UserMapper;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.User;
import edu.eci.preparcial.persistence.nonrelational.document.UserDocument;
import edu.eci.preparcial.persistence.nonrelational.repository.UserRepository;
import edu.eci.preparcial.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public User register(RegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new SportLifeException("El email ya está registrado", HttpStatus.CONFLICT);
        }
        UserDocument document = new UserDocument();
        document.setId(UUID.randomUUID());
        document.setName(dto.getName());
        document.setEmail(dto.getEmail());
        document.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userMapper.toModel(userRepository.save(document));
    }

    public String login(LoginDTO dto) {
        UserDocument user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new SportLifeException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new SportLifeException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
