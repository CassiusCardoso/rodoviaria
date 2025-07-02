package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;

import java.util.List;
import java.util.UUID;

public class ListarMeusTicketsUseCase {

    private final TicketRepository ticketRepository;

    public ListarMeusTicketsUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Executa o caso de uso para listar todos os tickets de um passageiro.
     * @param passageiroId O ID do passageiro cujos tickets serão listados.
     * @return Uma lista de TicketResponse contendo os dados dos tickets.
     */
    public List<TicketResponse> execute(UUID passageiroId) {

        // 1. Busca a lista de entidades de domínio diretamente pelo repositório.
        // O repositório já garante que só virão tickets do passageiro correto.
        List<Ticket> tickets = ticketRepository.listarTicketsPorId(passageiroId);

        // 2. Validação (Opcional, mas boa prática):
        // Se a lista estiver vazia, você pode optar por retornar uma lista vazia
        // ou lançar uma exceção, dependendo da sua regra de negócio.
        // Lançar exceção pode ser mais informativo para o usuário.
        if (tickets.isEmpty()) {
            throw new TicketInvalidoException("Nenhum ticket encontrado para o passageiro com ID " + passageiroId);
        }

        // 3. Converte a lista de Entidades para uma lista de DTOs usando Streams e o Mapper.
        return tickets.stream()
                .map(TicketMapper.INSTANCE::toResponse)
                .toList();
    }
}