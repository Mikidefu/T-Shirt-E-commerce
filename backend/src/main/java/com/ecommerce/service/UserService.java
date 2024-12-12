package com.ecommerce.service;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil; // Iniettato

    @Autowired
    @Lazy
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil; // Inizializzazione
    }

    // 1. Registrazione Utente
    /**
     * Metodo per registrare un nuovo utente con ruolo specificato.
     *
     * @param userDTO il DTO dell'utente con i dati da registrare
     * @return l'utente creato
     */
    public User registerUser(UserDTO userDTO) {
        // Controlla se l'utente esiste già
        if (userRepository.findByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Utente con email o username già esistente");
        }

        // Cifra la password prima di salvarla
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Crea l'utente
        User user = new User(userDTO.getUsername(), userDTO.getEmail(), encodedPassword, userDTO.getRole());

        // Salva l'utente nel database
        return userRepository.save(user);
    }


    //2 Autenticazione Utente(verifica i dati)
    public String authenticateUser(String emailOrUsername, String password) {
        User user = userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenUtil.generateToken(user.getUsername());  // Genera e restituisci il token JWT
        }
        return null;
    }



    // 4. Aggiornamento Password Utente
    @Transactional
    public User updateUserPassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato.");
        }

        User user = userOptional.get();

        // Verifica se la vecchia password è corretta
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("La vecchia password è errata.");
        }

        // Cifra la nuova password
        String encryptedPassword = passwordEncoder.encode(newPassword);

        // Aggiorna la password dell'utente
        user.setPassword(encryptedPassword);

        // Salva l'utente con la nuova password
        return userRepository.save(user);
    }

    public User getUserByUsername(String user) {
        return userRepository.findByUsername(user)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con Username: " + user));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + userId));
    }
}
