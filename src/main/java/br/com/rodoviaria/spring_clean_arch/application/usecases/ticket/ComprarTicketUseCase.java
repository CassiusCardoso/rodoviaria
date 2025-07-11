package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.ComprarTicketRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.AssentoInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.util.UUID;

public class ComprarTicketUseCase {
    // Declarar as dependências (contratos de domínio)
    private final ViagemRepository viagemRepository;
    private final TicketRepository ticketRepository;
    private final PassageiroRepository passageiroRepository;
    private final TicketMapper ticketMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento
    // Pode ter um mapper aqui também

    // Injentar as dependências (o mundo exterior nos dará a implementação)
    public ComprarTicketUseCase(ViagemRepository viagemRepository,  TicketRepository ticketRepository, PassageiroRepository passageiroRepository, TicketMapper ticketMapper) {
        this.viagemRepository = viagemRepository;
        this.ticketRepository = ticketRepository;
        this.passageiroRepository = passageiroRepository;
        this.ticketMapper = ticketMapper;
    }
    // Método de execução (a lógica do caso de uso)
    public TicketResponse execute(ComprarTicketRequest request) {

        // BUSCAR AS ENTIDADES DE DOMÍNIO
        // Os UseCases precisam buscar as entidades de domínio para trabalhar com eles
        Viagem viagem = viagemRepository.buscarViagemPorId(request.viagemId())
                .orElseThrow(() -> new ViagemInvalidaException("Viagem com o ID: " + request.viagemId() + " não encontrada."));

        Passageiro passageiroComprador = passageiroRepository.buscarPassageiroPorId(request.compradorId())
                .orElseThrow(() -> new PassageiroInvalidoException("Passageiro com o ID: " + request.compradorId() + " não foi encontrado."));

        // EXECUTAR REGRAS DE NEGÓCIO DA APLICAÇÃO
        // Esta é uma regra que envolve múltiplos repositórios, por isso vive aqui
        boolean assentoOcupado = ticketRepository.assentoOcupado(viagem, request.numeroAssento());
        if(assentoOcupado) {
            throw new AssentoInvalidoException("Esse assento já está ocupado para a viagem selecionada.");
        }
        // CRIAR A ENTIDADE DE DOMÍNIO
        // O Use Case é responsável por orquestrar a criação da entidade.
        // Note que as validações de formato e estado (preço negativo, etc.)
        // estão protegidas dentro do construtor da própria entidade Ticket.
        Ticket novoTicket = new Ticket(
                UUID.randomUUID(),
                request.nomePassageiroTicket(),
                request.documentoPassageiroTicket(),
                request.numeroAssento(),
                request.preco(),
                FormaPagamento.valueOf(request.formaPagamento().toUpperCase()), // Converte String para Enum
                StatusTicket.CONFIRMADO, // Define um status inicial padrão
                passageiroComprador,
                viagem);

        // PERSISR A NOVA ENTIDADE
        Ticket ticketSalvo = ticketRepository.salvar(novoTicket);
        // Mapper para converter Entity em DTO
        return ticketMapper.toResponse(ticketSalvo);
    }
}
