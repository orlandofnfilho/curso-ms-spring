package com.example.msavaliadorcredito.infra.clients;

import com.example.msavaliadorcredito.domain.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscartoes", path = "/cartoes")
public interface CartoesControllerClient {

    @GetMapping("cpf")
    ResponseEntity<List<CartaoCliente>> getCartoesByCpf(@RequestParam String cpf);
}
