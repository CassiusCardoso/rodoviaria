// Em: br/com/rodoviaria/spring_clean_arch/application/mapper/ticket/TicketMapper.java

package br.com.rodoviaria.spring_clean_arch.application.mapper.ticket;

// Imports do MapStruct
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroPorViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro.PassageiroMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// Imports dos seus DTOs e Entidades
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;

@Mapper(uses = {TicketMapper.class, PassageiroMapper.class})
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

    // --- NOVO MÉTODO PARA O NOSSO CASO DE USO ---
    // Ele converte a entidade Ticket para o DTO PassageiroPorViagemResponse.
    @Mapping(source = "passageiro.id", target = "passageiroId")
    @Mapping(source = "passageiro.nome", target = "nome")
    @Mapping(source = "passageiro.telefone.telefone", target = "telefone")
    @Mapping(source = "viagem.id", target = "viagemId")
    @Mapping(source = "viagem.linha.origem", target = "origem")
    @Mapping(source = "viagem.linha.destino", target = "destino")
    PassageiroPorViagemResponse toPassageiroPorViagemResponse(Ticket ticket);
}
