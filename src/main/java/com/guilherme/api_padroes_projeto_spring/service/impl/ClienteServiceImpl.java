package com.guilherme.api_padroes_projeto_spring.service.impl;

import com.guilherme.api_padroes_projeto_spring.exceptions.ClienteNotFoundException;
import com.guilherme.api_padroes_projeto_spring.model.Cliente;
import com.guilherme.api_padroes_projeto_spring.model.ClienteRepository;
import com.guilherme.api_padroes_projeto_spring.model.Endereco;
import com.guilherme.api_padroes_projeto_spring.model.EnderecoRepository;
import com.guilherme.api_padroes_projeto_spring.service.ClienteService;
import com.guilherme.api_padroes_projeto_spring.service.ViaCepService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
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

    public Resource exportarDados(String formato) {
        Iterable<Cliente> clientes = clienteRepository.findAll();
        if ("csv".equalsIgnoreCase(formato)) {
            return exportarDadosCsv(clientes);
        }
        throw new UnsupportedOperationException("Formato não suportado: " + formato);
    }

    private Resource exportarDadosCsv(Iterable<Cliente> clientes) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT.withHeader("ID", "Nome", "CEP", "Endereco"))) {

            for (Cliente cliente : clientes) {
                printer.printRecord(cliente.getId(), cliente.getNome(), cliente.getEndereco().getCep(), cliente.getEndereco().getLogradouro());
            }
            printer.flush();
            return new ByteArrayResource(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exportar dados em CSV", e);
        }

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



