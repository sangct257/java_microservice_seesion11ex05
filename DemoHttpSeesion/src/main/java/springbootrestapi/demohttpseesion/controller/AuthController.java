package springbootrestapi.demohttpseesion.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        String username = loginRequest.get("username");

        // Lưu thông tin người dùng vào HttpSession
        session.setAttribute("user", username);

        return ResponseEntity.ok("Đăng nhập thành công! Session ID: " + session.getId());
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(HttpSession session) {
        // Lấy thông tin user từ Session
        String user = (String) session.getAttribute("user");

        if (user != null) {
            return ResponseEntity.ok("Xin chào : " + user);
        } else {
            return ResponseEntity.ok("Bạn chưa đăng nhập");
        }
    }
}