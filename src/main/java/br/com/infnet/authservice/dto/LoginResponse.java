package br.com.infnet.authservice.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresAt ) {
}