package com.example.msclientes.application;

import com.example.msclientes.domain.Cliente;
import com.example.msclientes.infra.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> getByCpf(String cpf){
        return clienteRepository.findByCpf(cpf);
    }

}
