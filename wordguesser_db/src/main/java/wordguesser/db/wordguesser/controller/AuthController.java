package wordguesser.db.wordguesser.controller;

import wordguesser.db.wordguesser.model.User;
import wordguesser.db.wordguesser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "El usuario ya existe"));
        }
        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Registro exitoso", "userId", newUser.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        return userRepository.findByUsername(loginUser.getUsername())
                .map(user -> {
                    if (user.getPassword().equals(loginUser.getPassword())) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("message", "Login exitoso");
                        response.put("userId", user.getId());
                        response.put("username", user.getUsername());
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.status(401).body(Map.of("message", "Contraseña incorrecta"));
                    }
                })
                .orElse(ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado")));
    }
    
    @GetMapping("/ping")
    public String ping() {
        return "Pong! Servidor WordGuesser Activo";
    }
}