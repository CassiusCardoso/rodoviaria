package br.com.rodoviaria.spring_clean_arch.Application.Dto.Response.Ticket;

import br.com.rodoviaria.spring_clean_arch.Application.Dto.Response.Passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.Application.Dto.Response.Viagem.ViagemResponse;

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
