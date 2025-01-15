package com.guilherme.api_padroes_projeto_spring.service.impl;

import com.guilherme.api_padroes_projeto_spring.exceptions.ClienteNotFoundException;
import com.guilherme.api_padroes_projeto_spring.model.Cliente;
import com.guilherme.api_padroes_projeto_spring.model.ClienteRepository;
import com.guilherme.api_padroes_projeto_spring.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 * @author gumeeee
 */
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {

        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    public void inserir(Cliente cliente) {

    }

    @Override
    public void atualizar(Long id, Cliente cliente) {

    }

    @Override
    public void deletar(Long id) {

    }
}
