package br.com.infnet.authservice.dto;

public record RegisterRequest(
        String nome,
        String email,
        String senha
) {}