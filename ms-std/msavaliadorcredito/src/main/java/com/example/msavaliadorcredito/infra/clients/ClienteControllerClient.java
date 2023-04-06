package com.example.msavaliadorcredito.infra.clients;

import com.example.msavaliadorcredito.domain.Cliente;
import com.example.msavaliadorcredito.domain.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "msclientes", path = "/clientes")
public interface ClienteControllerClient {

    @GetMapping("/cpf")
    ResponseEntity<DadosCliente> getByCpf(@RequestParam String cpf);
}
