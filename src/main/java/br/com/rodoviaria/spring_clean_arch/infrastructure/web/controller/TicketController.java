package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.AtualizarTicketRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.ComprarTicketRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.ticket.*;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Tickets", description = "Endpoints para compra e gerenciamento de tickets de passagem")
@SecurityRequirement(name = "bearerAuth")
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
    @Operation(summary = "Comprar um novo ticket", description = "Realiza a compra de um ticket para uma viagem específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket comprado com sucesso", content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, assento ocupado ou viagem/passageiro não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<TicketResponse> comprarTicket(@RequestBody ComprarTicketRequest request){
        TicketResponse ticketResponse = comprarTicketUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketResponse);
    }

    @GetMapping("/meus-tickets")
    @Operation(summary = "Lista os tickets do passageiro logado", description = "Retorna uma lista de todos os tickets comprados pelo passageiro autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tickets retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<List<TicketResponse>> listarMeusTickets(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        List<TicketResponse> ticketsResponse = listarMeusTicketsUseCase.execute(usuarioLogado.getId());
        return ResponseEntity.ok(ticketsResponse);
    }

    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancela um ticket", description = "Permite que o passageiro autenticado cancele um de seus próprios tickets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket cancelado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário não é o dono do ticket", content = @Content)
    })
    public void passageiroCancelarTicket(
            @Parameter(description = "ID do ticket a ser cancelado") @PathVariable UUID ticketId,
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        passageiroCancelarTicketUseCase.execute(ticketId, usuarioLogado.getId());
    }

    @GetMapping("/{ticketId}")
    @Operation(summary = "Busca detalhes de um ticket", description = "Retorna os detalhes de um ticket específico, se o usuário logado for o dono.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes do ticket retornados com sucesso", content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<TicketResponse> buscarDetalhesTicket(@Parameter(description = "ID do ticket a ser buscado") @PathVariable UUID ticketId, @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        TicketResponse ticketResponse = buscarDetalhesDoTicketUseCase.execute(ticketId, usuarioLogado.getId());
        return ResponseEntity.ok(ticketResponse);
    }

    @PutMapping("/{ticketId}")
    @Operation(summary = "Atualiza informações de um ticket", description = "Permite que o passageiro altere o nome e documento associados a um ticket que ainda não foi utilizado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket atualizado com sucesso", content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<TicketResponse> atualizarTicket(@Parameter(description = "ID do ticket a ser atualizado") @PathVariable UUID ticketId, @RequestBody AtualizarTicketRequest request, @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        TicketResponse ticketResponse = atualizarTicketUseCase.execute(ticketId, request, usuarioLogado.getId());
        return ResponseEntity.ok(ticketResponse);
    }
}