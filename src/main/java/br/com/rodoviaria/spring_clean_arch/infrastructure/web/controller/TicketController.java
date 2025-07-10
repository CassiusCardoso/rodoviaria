package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.AtualizarTicketRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.ComprarTicketRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.ticket.*;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.UsuarioAutenticado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final ListarMeusTicketsUseCase listarMeusTicketsUseCase;
    private final ComprarTicketUseCase comprarTicketUseCase;
    private final AtualizarTicketUseCase atualizarTicketUseCase;
    private final PassageiroCancelarTicketUseCase passageiroCancelarTicketUseCase;
    private final BuscarDetalhesDoTicketUseCase buscarDetalhesDoTicketUseCase;

    public TicketController(ListarMeusTicketsUseCase listarMeusTicketsUseCase, ComprarTicketUseCase comprarTicketUseCase, AtualizarTicketUseCase atualizarTicketUseCase, PassageiroCancelarTicketUseCase passageiroCancelarTicketUseCase, BuscarDetalhesDoTicketUseCase buscarDetalhesDoTicketUseCase){
        this.listarMeusTicketsUseCase = listarMeusTicketsUseCase;
        this.comprarTicketUseCase = comprarTicketUseCase;
        this.atualizarTicketUseCase = atualizarTicketUseCase;
        this.passageiroCancelarTicketUseCase = passageiroCancelarTicketUseCase;
        this.buscarDetalhesDoTicketUseCase = buscarDetalhesDoTicketUseCase;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> comprarTicket(@RequestBody ComprarTicketRequest request){
        TicketResponse ticketResponse = comprarTicketUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketResponse);
    }

    @GetMapping("/meus-tickets")
    public ResponseEntity<List<TicketResponse>> listarMeusTickets(@AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        // Usamos o ID do usuário que vem do token, e não da URL.
        List<TicketResponse> ticketsResponse = listarMeusTicketsUseCase.execute(usuarioLogado.getId());
        return ResponseEntity.ok(ticketsResponse);

    }

    // CORREÇÃO AQUI: O método precisa receber quem está logado para passar ao UseCase.
    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void passageiroCancelarTicket(
            @PathVariable UUID ticketId,
            @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        // Passamos os dois IDs para o caso de uso fazer a validação de propriedade.
        passageiroCancelarTicketUseCase.execute(ticketId, usuarioLogado.getId());
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> buscarDetalhesTicket(@PathVariable UUID ticketId, @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        TicketResponse ticketResponse = buscarDetalhesDoTicketUseCase.execute(ticketId, usuarioLogado.getId());
        return ResponseEntity.ok(ticketResponse);
    }
    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> atualizarTicket(@PathVariable UUID ticketId, @RequestBody AtualizarTicketRequest request, @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        TicketResponse ticketResponse = atualizarTicketUseCase.execute(ticketId, request, usuarioLogado.getId());
        return ResponseEntity.ok(ticketResponse);
    }

}
