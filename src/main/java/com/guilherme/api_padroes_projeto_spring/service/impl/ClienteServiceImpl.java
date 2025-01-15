package com.guilherme.api_padroes_projeto_spring.service.impl;

import com.guilherme.api_padroes_projeto_spring.exceptions.ClienteNotFoundException;
import com.guilherme.api_padroes_projeto_spring.model.Cliente;
import com.guilherme.api_padroes_projeto_spring.model.ClienteRepository;
import com.guilherme.api_padroes_projeto_spring.model.Endereco;
import com.guilherme.api_padroes_projeto_spring.model.EnderecoRepository;
import com.guilherme.api_padroes_projeto_spring.service.ClienteService;
import com.guilherme.api_padroes_projeto_spring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 * @author gumeeee
 */
@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

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
        salvarClienteComCep(cliente);
    }


    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteById = clienteRepository.findById(id);

        if (clienteById.isEmpty()) {
            throw new ClienteNotFoundException(id);
        }

        salvarClienteComCep(cliente);
    }

    @Override
    public void deletar(Long id) {
        Optional<Cliente> clienteById = clienteRepository.findById(id);

        if (clienteById.isEmpty()) {
            throw new ClienteNotFoundException(id);
        }

        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco enderecoApiViaCep = viaCepService.consultarCep(cep);
            enderecoRepository.save(enderecoApiViaCep);
            return enderecoApiViaCep;
        });
        cliente.setEndereco(endereco);

        clienteRepository.save(cliente);
    }
}


