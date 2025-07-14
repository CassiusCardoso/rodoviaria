package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;

import java.util.List;
import java.util.UUID;

public class ListarMeusTicketsUseCase {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento


    public ListarMeusTicketsUseCase(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    /**
     * Executa o caso de uso para listar todos os tickets de um passageiro.
     * @param passageiroId O ID do passageiro cujos tickets serão listados.
     * @return Uma lista de TicketResponse contendo os dados dos tickets.
     */
    public List<TicketResponse> execute(UUID passageiroId) {

        // EDIT 14/07 - 09:26 AVALIDAÇÃO AQUI
        if (passageiroId == null) {
            throw new IllegalArgumentException("O ID do passageiro não pode ser nulo.");
        }

        // 1. Busca a lista de entidades de domínio diretamente pelo repositório.
        // O repositório já garante que só virão tickets do passageiro correto.
        List<Ticket> tickets = ticketRepository.listarTicketsPorPassageiroId(passageiroId);


        // 3. Converte a lista de Entidades para uma lista de DTOs usando Streams e o Mapper.
        return tickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }
}