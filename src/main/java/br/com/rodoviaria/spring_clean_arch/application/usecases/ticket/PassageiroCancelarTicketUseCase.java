package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class CancelarTicketUseCase {

    private final TicketRepository ticketRepository;

    public CancelarTicketUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Executa o caso de uso para cancelar um ticket.
     * @param ticketId O ID do ticket a ser cancelado.
     * @param usuarioLogadoId O ID do usuário que está realizando a operação (para autorização).
     */
    public void execute(UUID ticketId, UUID usuarioLogadoId) {

        // 1. Busca o ticket que será cancelado.
        Ticket ticket = ticketRepository.buscarTicketPorId(ticketId)
                .orElseThrow(() -> new TicketInvalidoException("Ticket com ID " + ticketId + " não encontrado."));

        // 2. Validação de Autorização: O usuário logado é o dono do ticket?
        if (!ticket.getPassageiro().getId().equals(usuarioLogadoId)) {
            // Futuramente, aqui poderia ter uma lógica para permitir que um ADMIN também cancele.
            throw new AutorizacaoInvalidaException("O usuário não tem permissão para cancelar este ticket.");
        }

        // 3. Validações de Regra de Negócio:
        // Não se pode cancelar um ticket de uma viagem que já aconteceu.
        if (ticket.getViagem().getDataPartida().isBefore(LocalDateTime.now())) {
            throw new TicketInvalidoException("Não é possível cancelar um ticket de uma viagem que já ocorreu.");
        }
        // O ticket já não está cancelado?
        if (ticket.getStatus() == StatusTicket.CANCELADO) {
            throw new TicketInvalidoException("Este ticket já se encontra cancelado.");
        }
        // O ticket já foi utilizado?
        if (ticket.getStatus() == StatusTicket.UTILIZADO) {
            throw new TicketInvalidoException("Não é possível cancelar um ticket que já foi utilizado.");
        }

        // 4. Modifica o estado da entidade.
        // Como a entidade Ticket é imutável, criamos um NOVO objeto com o estado atualizado.
        // É necessário ter o método 'cancelar()' na entidade Ticket.
        Ticket ticketCancelado = ticket.cancelar();

        // 5. Salva o estado atualizado.
        // O mesmo método "salvar" serve para criar e para atualizar.
        ticketRepository.salvar(ticketCancelado);
    }
}