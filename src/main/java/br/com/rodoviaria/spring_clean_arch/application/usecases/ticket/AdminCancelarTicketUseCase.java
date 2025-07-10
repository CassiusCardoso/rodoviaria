package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

// Caso de uso somente para os ADMINS (QUE PODEM CANCELAR QUALQUER TICKET)

import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class AdminCancelarTicketUseCase {
    private final TicketRepository ticketRepository;

    public AdminCancelarTicketUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void execute(UUID ticketId){
        Ticket ticket = ticketRepository.buscarTicketPorId(ticketId)
                .orElseThrow(() -> new TicketInvalidoException("Ticket não encontrado"));

        // A AUTORIZAÇÃO É GARANTIDA POR ESTAR NA ROTA (/admin/**)

        // REGRAS DE NEGÓCIO: para uma viagem que já ocorreu, ticket já cancelado)
        if(ticket.getViagem().getDataPartida().isBefore(LocalDateTime.now())){
            throw new TicketInvalidoException("Não é possível cancelar um ticket de uma viagem que já ocorreu.");
        }

        if(ticket.getStatus() == StatusTicket.CANCELADO){
            throw new TicketInvalidoException("Este ticket já se encontra cancelado.");
        }
        Ticket ticketCancelado = ticket.cancelar();
        ticketRepository.salvar(ticketCancelado);
    }
}
