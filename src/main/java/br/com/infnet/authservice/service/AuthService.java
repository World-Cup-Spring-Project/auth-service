package br.com.infnet.authservice.service;

import br.com.infnet.authservice.dto.LoginRequest;
import br.com.infnet.authservice.dto.LoginResponse;
import br.com.infnet.authservice.dto.RegisterRequest;
import br.com.infnet.authservice.model.Usuario;
import br.com.infnet.authservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request){
        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciais invalidas"));

        if(!usuario.isAtivo()){
            throw new RuntimeException("Usuario Inativo");
        }
        if(!usuario.getSenhaHash().equals(request.senha())){
            throw new RuntimeException("Credenciais Invalidas");
        }
        String accessToken = jwtService.gerarAccessToken(usuario);
        return new LoginResponse(accessToken, "Bearer", jwtService.getExpireTime());
    }

    public void register(RegisterRequest request) {

        if (usuarioRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado no sistema.");
        }

        Usuario novoUsuario = new Usuario(
                UUID.randomUUID(),
                request.nome(),
                request.email(),
                request.senha(),
                true,
                LocalDateTime.now()
        );

        usuarioRepository.save(novoUsuario);
    }
}