package br.com.infnet.authservice.dto;

public record UsuarioAutenticadoResponse(
        String id,
        String nome,
        String email) {
}
