package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;// Em: br/com/rodoviaria/spring_clean_arch/Application/UseCases/Ticket/AtualizarTicketUseCase.java

// ... outros imports
import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.AtualizarTicketRequest; // Importe o novo DTO
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.StatusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.DataHoraChegadaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class AtualizarTicketUseCase {
    private final TicketRepository ticketRepository;

    public AtualizarTicketUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // O método agora recebe o ID e o DTO com os novos dados
    public TicketResponse execute(UUID id, AtualizarTicketRequest request) {

        // 1. Buscando o Ticket que já existe (seu código original, está perfeito!)
        Ticket ticketAtual = ticketRepository.buscarTicketPorId(id)
                .orElseThrow(() -> new TicketInvalidoException("Ticket com ID " + id + " não existe."));

        // 2. Verificando regras de negócio (seu código original, perfeito!)
        if(ticketAtual.getViagem().getDataPartida().isBefore(LocalDateTime.now())){ // Usei getData_partida() que é mais preciso
            throw new DataHoraChegadaInvalidaException("Você não pode alterar informações de um ticket para uma viagem que já ocorreu.");
        }
        if(ticketAtual.getStatus() == StatusTicket.UTILIZADO){
            throw new StatusInvalidoException("Ticket já foi utilizado e não pode ser alterado.");
        }

        // 3. ATUALIZANDO A ENTIDADE IMUTÁVEL
        // Como a entidade Ticket é imutável, criamos uma NOVA instância com os dados atualizados.
        // Usamos os dados do 'request' para os campos que mudaram e os dados do 'ticketAtual' para os que não mudaram.
        Ticket ticketAtualizado = new Ticket(
                ticketAtual.getId(),
                request.nomePassageiroTicket(),      // <-- Novo valor do request
                request.documentoPassageiroTicket(), // <-- Novo valor do request
                ticketAtual.getNumeroAssento(),      // <-- Valor antigo, não muda
                ticketAtual.getPreco(),              // <-- Valor antigo, não muda
                ticketAtual.getFormaPagamento(),     // <-- Valor antigo, não muda
                ticketAtual.getStatus(),             // <-- Valor antigo, não muda
                ticketAtual.getPassageiro(),         // <-- Valor antigo, não muda
                ticketAtual.getViagem()              // <-- Valor antigo, não muda
        );

        // 4. PERSISTINDO O NOVO ESTADO
        // O método salvar irá atualizar o registro existente no banco de dados.
        Ticket ticketSalvo = ticketRepository.salvar(ticketAtualizado);

        // 5. RETORNANDO O RESPONSE
        // Usamos o Mapper para converter a entidade atualizada para o DTO de resposta.
        return TicketMapper.INSTANCE.toResponse(ticketSalvo);
    }
}