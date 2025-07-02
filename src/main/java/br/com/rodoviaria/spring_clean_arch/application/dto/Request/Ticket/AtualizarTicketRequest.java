// Em: application/dto/request/ticket/AtualizarTicketRequest.java

package br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket;

public record AtualizarTicketRequest(
        String nomePassageiroTicket,
        String documentoPassageiroTicket
) {}