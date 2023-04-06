package com.example.msavaliadorcredito.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cartao {

    private Long id;
    private String nome;
    private String bandeira;
    private BigDecimal limiteBasico;
}
