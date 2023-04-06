package com.example.msavaliadorcredito.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    private Long id;
    private String cpf;
    private String nome;
    private Integer idade;
}
