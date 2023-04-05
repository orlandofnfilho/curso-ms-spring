package com.example.msclientes.application;

import com.example.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String status() {
        log.info("Obtendo o status do msclientes");
        return "ok";
    }

    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody Cliente request) {
        Cliente clienteSalvo = clienteService.save(request);
        URI headerlocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .query("cpf={cpf}").buildAndExpand(clienteSalvo.getCpf()).toUri();
        return ResponseEntity.created(headerlocation).body(clienteSalvo);
    }

    @GetMapping("/cpf")
    public ResponseEntity<Cliente> getByCpf(@RequestParam String cpf) {
        Optional<Cliente> clienteSalvo = clienteService.getByCpf(cpf);
        if(clienteSalvo.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(clienteSalvo.get());
    }
}
