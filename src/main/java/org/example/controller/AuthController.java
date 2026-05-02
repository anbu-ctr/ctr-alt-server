package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ApiResponse;
import org.example.dto.SignInRequest;
import org.example.dto.SignInResponse;
import org.example.dto.SignUpRequest;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody final SignUpRequest request, final HttpServletResponse
            httpServletResponse) {
        try {
           final SignInResponse response = userService.signUp(request);

            if (response.isSuccess()) {
                final Cookie cookie = new Cookie("jwt_token", response.getToken());
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 60 * 60);
                httpServletResponse.addCookie(cookie);

               final Map<String, String> data = new HashMap<>();
                data.put("message", "Sign up successful");

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(data));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(response.getMessage()));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signIn(
            @RequestBody final SignInRequest request,
            final HttpServletResponse httpResponse) {
        try {
            System.out.println("signin");
            final SignInResponse response = userService.signIn(request);

            if (response.isSuccess()) {
                final Cookie cookie = new Cookie("jwt_token", response.getToken());
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 60 * 60);
                httpResponse.addCookie(cookie);

                final Map<String, String> data = new HashMap<>();
                data.put("message", "Sign in successful");

                return ResponseEntity.ok(ApiResponse.success(data));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error(response.getMessage()));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(final HttpServletResponse httpResponse) {
        final Cookie cookie = new Cookie("jwt_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // This will cause the browser to delete the cookie
        httpResponse.addCookie(cookie);

        final Map<String, String> data = new HashMap<>();
        data.put("message", "Logout successful");

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}