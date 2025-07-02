package br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String nomePassageiroTicket,
        String documentoPassageiroTicket,
        int numeroAssento,
        BigDecimal preco,
        String formaPagamento, // Não utilizar ENUM aqui, porque no UseCase ele vai ser convertido
        String statusViagem, // Não utilizar ENUM aqui, porque no UseCase ele vai ser convertido
        LocalDate dataCompra,
        PassageiroResponse passageiro,
        ViagemResponse viagem
) {
}
