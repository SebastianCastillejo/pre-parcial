package edu.eci.preparcial.core;

import edu.eci.preparcial.controller.DTO.LoginDTO;
import edu.eci.preparcial.controller.DTO.RegisterDTO;
import edu.eci.preparcial.controller.mapper.UserMapper;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.User;
import edu.eci.preparcial.core.service.UserService;
import edu.eci.preparcial.persistence.nonrelational.document.UserDocument;
import edu.eci.preparcial.persistence.nonrelational.repository.UserRepository;
import edu.eci.preparcial.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock BCryptPasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;
    @Mock UserMapper userMapper;
    @InjectMocks UserService userService;

    @Test
    void register_EmailYaExiste_LanzaConflict() {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("test@mail.com");
        dto.setName("Test");
        dto.setPassword("Pass1234!");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> userService.register(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("El email ya está registrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_EmailNuevo_GuardaCorrectamente() {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("nuevo@mail.com");
        dto.setName("Nuevo");
        dto.setPassword("Pass1234!");

        when(userRepository.existsByEmail("nuevo@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("Pass1234!")).thenReturn("hashedPass");
        when(userRepository.save(any())).thenReturn(new UserDocument());
        when(userMapper.toModel(any())).thenReturn(new User());

        assertDoesNotThrow(() -> userService.register(dto));
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void login_UsuarioNoExiste_LanzaUnauthorized() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("noexiste@mail.com");
        dto.setPassword("Pass1234!");

        when(userRepository.findByEmail("noexiste@mail.com")).thenReturn(Optional.empty());

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> userService.login(dto));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }

    @Test
    void login_PasswordIncorrecta_LanzaUnauthorized() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("user@mail.com");
        dto.setPassword("wrongPass");

        UserDocument doc = new UserDocument();
        doc.setEmail("user@mail.com");
        doc.setPassword("hashedCorrect");
        doc.setRole("CUSTOMER");

        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(doc));
        when(passwordEncoder.matches("wrongPass", "hashedCorrect")).thenReturn(false);

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> userService.login(dto));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }

    @Test
    void login_CredencialesCorrectas_RetornaToken() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("user@mail.com");
        dto.setPassword("Pass1234!");

        UserDocument doc = new UserDocument();
        doc.setId(UUID.randomUUID());
        doc.setEmail("user@mail.com");
        doc.setPassword("hashed");
        doc.setRole("CUSTOMER");

        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(doc));
        when(passwordEncoder.matches("Pass1234!", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("user@mail.com", "CUSTOMER")).thenReturn("jwt-token");

        String token = userService.login(dto);

        assertEquals("jwt-token", token);
    }
}
