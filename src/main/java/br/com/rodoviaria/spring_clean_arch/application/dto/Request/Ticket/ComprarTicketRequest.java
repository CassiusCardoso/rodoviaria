package br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket;

import java.math.BigDecimal;
import java.util.UUID;

public record ComprarTicketRequest(
        String nomePassageiroTicket,
        String documentoPassageiroTicket,
        int numeroAssento,
        BigDecimal preco,
        String formaPagamento, // Use String, a conversão para Enum é no UseCase
        UUID viagemId,         // Correção: Apenas o ID da viagem
        UUID compradorId       // Correção: Apenas o ID de quem está comprando
) {
}
