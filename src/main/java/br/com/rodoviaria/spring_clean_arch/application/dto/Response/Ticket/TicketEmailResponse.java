package br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TicketEmailResponse(
        UUID id,
        String nomePassageiroTicket,
        String email,
        String FormaPagamento,
        BigDecimal preco,
        LocalDate dataCompra
) implements Serializable {}