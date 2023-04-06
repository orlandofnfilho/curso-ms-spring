package com.example.mscartoes.application;

import com.example.mscartoes.domain.Cartao;
import com.example.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
@Slf4j
public class CartaoController {

    private final CartaoService cartaoService;

    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status() {
        return "cartões ok";
    }

    @PostMapping
    public ResponseEntity<Cartao> save(@RequestBody Cartao request) {
        Cartao cartaoSalvo = cartaoService.save(request);
        return new ResponseEntity<Cartao>(cartaoSalvo, HttpStatus.CREATED);
    }

    @GetMapping("renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaMenorIgual(@RequestParam Long renda){
        List<Cartao> cartoesSalvos = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok().body(cartoesSalvos);
    }

    @GetMapping("cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(
            @RequestParam("cpf") String cpf){
        List<ClienteCartao> lista = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = lista.stream()
                .map(CartoesPorClienteResponse::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }
}
