package br.com.rodoviaria.spring_clean_arch.app_temp.dto.Request.Viagem;

import java.time.LocalDateTime;
import java.util.UUID;

public record CriarTicketRequest(
        LocalDateTime dataPartida,
        LocalDateTime dataHoraChegada,
        String formaPagamento, // Use String, a conversão para Enum é no UseCase
        UUID viagemId,         // Correção: Apenas o ID da viagem
        UUID compradorId       // Correção: Apenas o ID de quem está comprando
) {
}
