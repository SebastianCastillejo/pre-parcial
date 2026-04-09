package edu.eci.preparcial.controller.mapper;

import edu.eci.preparcial.core.model.User;
import edu.eci.preparcial.persistence.nonrelational.document.UserDocument;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toModel(UserDocument document) {
        User user = new User();
        user.setId(document.getId());
        user.setName(document.getName());
        user.setEmail(document.getEmail());
        user.setRole(document.getRole());
        user.setCreatedAt(document.getCreatedAt());
        return user;
    }
}
