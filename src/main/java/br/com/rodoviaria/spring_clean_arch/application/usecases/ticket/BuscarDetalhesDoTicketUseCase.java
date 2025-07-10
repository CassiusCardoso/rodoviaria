// Em: br/com/rodoviaria/spring_clean_arch/Application/UseCases/Ticket/BuscarDetalhesDoTicketUseCase.java

package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

// Importe o seu Mapper
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;

import java.util.UUID;

public class BuscarDetalhesDoTicketUseCase {
    private final TicketRepository ticketRepository;

    public BuscarDetalhesDoTicketUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketResponse execute(UUID ticketId, UUID usuarioLogadoId) {

        // 1. BUSCANDO A ENTIDADE TICKET
        Ticket ticketBuscado = ticketRepository.buscarTicketPorId(ticketId)
                .orElseThrow(() -> new TicketInvalidoException("O ticket com ID " + ticketId + " não existe."));

        // 2. VERIFICANDO A AUTORIZAÇÃO
        if (!ticketBuscado.getPassageiro().getId().equals(usuarioLogadoId)) {
            // Futuramente, você pode adicionar uma verificação de Role de ADMIN aqui também
            throw new AutorizacaoInvalidaException("O usuário não tem permissão para visualizar este ticket.");
        }

        // 3. CONVERTENDO PARA RESPONSE E RETORNANDO (O passo que faltava)
        // Não há etapa de "salvar". Apenas convertemos o que já buscamos.
        return TicketMapper.INSTANCE.toResponse(ticketBuscado);
    }
}