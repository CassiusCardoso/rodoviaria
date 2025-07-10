// Em: br/com/rodoviaria/spring_clean_arch/application/mapper/ticket/TicketMapper.java

package br.com.rodoviaria.spring_clean_arch.application.mapper;

// Imports do MapStruct
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroPorViagemResponse;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Placa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// Imports dos seus DTOs e Entidades
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;

@Mapper(uses = {PassageiroMapper.class}) // Removido TicketMapper.class do uses
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    // Mapeamento para TicketResponse
    @Mapping(source = "status", target = "statusViagem")
    @Mapping(source = "formaPagamento", target = "formaPagamento")
    TicketResponse toResponse(Ticket ticket);

    // Mapeamento para TicketEmailResponse
    @Mapping(source = "passageiro.email.email", target = "email")
    @Mapping(source = "formaPagamento", target = "FormaPagamento")
    TicketEmailResponse toTicketEmailResponse(Ticket ticket);

    // Mapeamento para PassageiroPorViagemResponse
    @Mapping(source = "passageiro.id", target = "passageiroId")
    @Mapping(source = "passageiro.nome", target = "nome")
    @Mapping(source = "passageiro.telefone.telefone", target = "telefone")
    @Mapping(source = "viagem.id", target = "viagemId")
    @Mapping(source = "viagem.linha.origem", target = "origem")
    @Mapping(source = "viagem.linha.destino", target = "destino")
    @Mapping(source = "viagem.onibus.placa", target = "placaOnibus")
    // Adicionado mapeamento explícito
    PassageiroPorViagemResponse toPassageiroPorViagemResponse(Ticket ticket);

    // Método para mapear Placa para String
    default String map(Placa placa) {
        return placa != null ? placa.getValorFormatado() : null;
    }
}