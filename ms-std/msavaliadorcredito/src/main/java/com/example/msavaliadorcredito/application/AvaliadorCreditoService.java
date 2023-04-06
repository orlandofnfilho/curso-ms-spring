package com.example.msavaliadorcredito.application;

import com.example.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import com.example.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import com.example.msavaliadorcredito.domain.CartaoCliente;
import com.example.msavaliadorcredito.domain.DadosCliente;
import com.example.msavaliadorcredito.domain.RetornoAvaliacaoCliente;
import com.example.msavaliadorcredito.domain.SituacaoCliente;
import com.example.msavaliadorcredito.infra.clients.CartoesControllerClient;
import com.example.msavaliadorcredito.infra.clients.ClienteControllerClient;
import feign.FeignException;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteControllerClient clienteControllerClient;
    private final CartoesControllerClient cartoesControllerClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> clienteReponse = clienteControllerClient.getByCpf(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesReponse = cartoesControllerClient.getCartoesByCpf(cpf);
            return SituacaoCliente
                    .builder()
                    .cliente(clienteReponse.getBody())
                    .cartoes(cartoesReponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), e.status());
        }

    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException
            , ErroComunicacaoMicroservicesException{
        try {
            ResponseEntity<DadosCliente> clienteReponse = clienteControllerClient.getByCpf(cpf);
        }


    }
}
