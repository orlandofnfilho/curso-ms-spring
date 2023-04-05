package com.example.mscartoes.application;

import com.example.mscartoes.domain.Cartao;
import com.example.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<List<ClienteCartao>> getCartoesByCpf(@RequestParam String cpf){
        List<ClienteCartao> cartoesDisponiveis = clienteCartaoService.listCartoesByCpf(cpf);
        return ResponseEntity.ok().body(cartoesDisponiveis);
    }
}
