package com.guilherme.api_padroes_projeto_spring.exceptions;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(Long id) {
        super("Cliente com ID " + id + " não encontrado.");
    }
}

