package com.vsoftexcel.vsoftexcel.controller;
import com.vsoftexcel.vsoftexcel.entity.Auth;
import com.vsoftexcel.vsoftexcel.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService userService;

    @PostMapping("/register")
    public Map<String,Object> register(@RequestBody Auth user) {
        Map<String,Object> result = userService.register(user);
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Auth user, HttpSession session) {
        String email = user.getEmail();
        String password = user.getPassword();

        Optional<Auth> userinfo = userService.login(email, password);

        Map<String, Object> response = new HashMap<>();

        if (userinfo.isPresent()) {
            session.setAttribute("user", userinfo.get());
            response.put("status", 200);
            response.put("message", "Login successful");
            response.put("sessionId", session.getId());
            response.put("user", userinfo.get().getFullName());
        } else {
            response.put("status", 401);
            response.put("message", "Invalid credentials");
        }
        return response;
    }


    // Logout
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        System.out.println(session);
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logged out successfully");
        return response;
    }
}