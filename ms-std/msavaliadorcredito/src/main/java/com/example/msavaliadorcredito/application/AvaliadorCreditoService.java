package com.example.msavaliadorcredito.application;

import com.example.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import com.example.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import com.example.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import com.example.msavaliadorcredito.domain.*;
import com.example.msavaliadorcredito.infra.clients.CartoesControllerClient;
import com.example.msavaliadorcredito.infra.clients.ClienteControllerClient;
import com.example.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteControllerClient clienteControllerClient;
    private final CartoesControllerClient cartoesControllerClient;
    private final SolicitacaoEmissaoCartaoPublisher solicitacaoEmissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteControllerClient.getByCpf(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesControllerClient.getCartoesByCpf(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteControllerClient.getByCpf(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesControllerClient.getCartoesRendaMenorIgual(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBigDecimal = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBigDecimal.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado cartaoAprovado = new CartaoAprovado();
                cartaoAprovado.setCartao(cartao.getNome());
                cartaoAprovado.setBandeira(cartao.getBandeira());
                cartaoAprovado.setLimiteAprovado(limiteAprovado);

                return cartaoAprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try{
            solicitacaoEmissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
