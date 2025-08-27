package com.vsoftexcel.vsoftexcel.service;

import com.vsoftexcel.vsoftexcel.entity.Auth;
import com.vsoftexcel.vsoftexcel.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthRepository repo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Map<String,Object> register(Auth user) {
        String email = user.getEmail();
        String password = user.getPassword();

        Map<String,Object> response = new HashMap<>();

        if (repo.findByEmail(email).isPresent()) {
            response.put("message", "Email already exists");
            response.put("status", 409);
            return response;
        }
        user.setPassword(encoder.encode(password)); // store encrypted password

        repo.save(user);
        response.put("message", "User registered successfully");
        response.put("status", 201);
        return response;
    }

    public Optional<Auth> login(String email, String password) {
        Optional<Auth> user = repo.findByEmail(email);
        if (user.isEmpty()) {
            return Optional.empty(); // user not found
        }

        if (encoder.matches(password, user.get().getPassword())) {
            return user; // success
        } else {
            return Optional.empty(); // password mismatch
        }
    }

}

