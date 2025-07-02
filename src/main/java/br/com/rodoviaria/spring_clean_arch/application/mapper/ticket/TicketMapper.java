// Em: br/com/rodoviaria/spring_clean_arch/application/mapper/ticket/TicketMapper.java

package br.com.rodoviaria.spring_clean_arch.application.mapper.ticket;

// Imports do MapStruct
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// Imports dos seus DTOs e Entidades
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;

@Mapper
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    // --- MÉTODO EXISTENTE ---
    @Mapping(source = "passageiro.id", target = "compradorId")
    @Mapping(source = "viagem.id", target = "viagemId")
    @Mapping(source = "formaPagamento", target = "formaPagamento")
    @Mapping(source = "status", target = "status")
    TicketResponse toResponse(Ticket ticket);

    // --- NOVO MÉTODO ADICIONADO ---
    /**
     * Converte uma entidade Ticket para o DTO específico para envio de e-mail.
     * @param ticket A entidade de domínio completa.
     * @return um DTO com os dados formatados para o e-mail.
     */
    @Mapping(source = "passageiro.email.email", target = "email")
    @Mapping(source = "formaPagamento", target = "FormaPagamento") // O MapStruct converte o Enum para String
    TicketEmailResponse toTicketEmailResponse(Ticket ticket);
}