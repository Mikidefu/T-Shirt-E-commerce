package com.ecommerce.controller;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/updateInfo")
    public ResponseEntity<User> updateInfo(@RequestParam long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        User user = userService.updateUserPassword(userId, oldPassword, newPassword);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Endpoint per registrare un nuovo utente con ruolo specificato.
     *
     * @param userDTO il DTO con i dati dell'utente
     * @return la risposta con lo stato dell'operazione
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registrazione avvenuta con successo");
        response.put("user", user); // aggiungi anche i dettagli dell'utente, se necessario
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(
            @RequestParam String emailOrUsername,
            @RequestParam String password) {

        String jwtToken = String.valueOf(userService.authenticateUser(emailOrUsername, password));
        if (jwtToken != null) {
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
