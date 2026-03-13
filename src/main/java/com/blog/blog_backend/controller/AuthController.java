package com.blog.blog_backend.controller;

import com.blog.blog_backend.model.User;
import com.blog.blog_backend.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${custom.cors.allowed-origins}")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }
        
        // 密码加密
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        
        User savedUser = userRepository.save(user);
        savedUser.setPassword(null); // 返回时不显示密码
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent() && BCrypt.checkpw(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("nickname", user.getNickname());
            // 简单登录方案：返回一个模拟 Token
            response.put("token", "simulated-jwt-token-for-" + user.getUsername());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("用户名或密码错误");
    }
}
