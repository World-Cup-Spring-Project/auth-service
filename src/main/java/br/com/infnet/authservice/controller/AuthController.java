package br.com.infnet.authservice.controller;

import br.com.infnet.authservice.dto.LoginRequest;
import br.com.infnet.authservice.dto.LoginResponse;
import br.com.infnet.authservice.dto.UsuarioAutenticadoResponse;
import br.com.infnet.authservice.service.AuthService;
import br.com.infnet.authservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse login = authService.login(request);
        return ResponseEntity.ok(login);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioAutenticadoResponse> me(
            @RequestHeader("Authorization") String authHeader) {
        String token = extrairToken(authHeader);
        UsuarioAutenticadoResponse autenticado = jwtService.verificarToken(token);
        return ResponseEntity.ok(autenticado);
    }

    private String extrairToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token não informado ou mal formatado");
        }
        return authHeader.substring(7);
    }
}
