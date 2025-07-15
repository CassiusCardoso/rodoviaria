package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.admin.AutenticarAdminRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.AtualizarInformacoesDaLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.CadastrarLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.AtualizarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.CadastrarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.AtualizarViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.ViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.admin.AutenticarAdminResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.admin.AutenticarAdminUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.AtualizarInformacoesDaLinhaUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.CadastrarLinhaUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.DesativarLinhaUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.AtualizarOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.CadastrarOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.DesativarOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.ListarTodosOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.AtualizarInformacoesPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.BuscarInformacoesPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.DesativarPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.ticket.AdminCancelarTicketUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.AtualizarViagemUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.CancelarViagemUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.CriarViagemUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.ListarViagensPorPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticado;
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
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints para gerenciamento pelo Administrador")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AutenticarAdminUseCase autenticarAdminUseCase;
    // ONIBUS USE CASE
    private final CadastrarOnibusUseCase cadastrarOnibusUseCase;
    private final ListarTodosOnibusUseCase listarTodosOnibusUseCase;
    private final DesativarOnibusUseCase desativarOnibusUseCase;
    private final AtualizarOnibusUseCase atualizarOnibusUseCase;
    // PASSAGEIROS USE CASE
    private final AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;
    private final DesativarPassageiroUseCase desativarPassageiroUseCase;
    private final BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;

    // LINHAS USE CASE
    private final DesativarLinhaUseCase desativarLinhaUseCase;
    private final AtualizarInformacoesDaLinhaUseCase atualizarInformacoesDaLinhaUseCase;
    private final CadastrarLinhaUseCase cadastrarLinhaUseCase;

    // TICKETS USE CASE
    private final AdminCancelarTicketUseCase adminCancelarTicketUseCase;

    // VIAGEM USE CASE
    private final CriarViagemUseCase criarViagemUseCase;
    private final AtualizarViagemUseCase atualizarViagemUseCase;
    private final CancelarViagemUseCase cancelarViagemUseCase;
    private final ListarViagensPorPassageiroUseCase listarViagensPorPassageiro;

    public AdminController(AutenticarAdminUseCase autenticarAdminUseCase, CadastrarOnibusUseCase cadastrarOnibusUseCase, ListarTodosOnibusUseCase listarTodosOnibusUseCase, DesativarOnibusUseCase desativarOnibusUseCase, AtualizarOnibusUseCase atualizarOnibusUseCase, AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase, DesativarPassageiroUseCase desativarPassageiroUseCase, BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase, DesativarLinhaUseCase desativarLinhaUseCase, AtualizarInformacoesDaLinhaUseCase atualizarInformacoesDaLinhaUseCase, CadastrarLinhaUseCase cadastrarLinhaUseCase, AdminCancelarTicketUseCase adminCancelarTicketUseCase, CriarViagemUseCase criarViagemUseCase, AtualizarViagemUseCase atualizarViagemUseCase, CancelarViagemUseCase cancelarViagemUseCase, ListarViagensPorPassageiroUseCase listarViagensPorPassageiro){
        this.autenticarAdminUseCase = autenticarAdminUseCase;
        // ONIBUS
        this.cadastrarOnibusUseCase = cadastrarOnibusUseCase;
        this.listarTodosOnibusUseCase = listarTodosOnibusUseCase;
        this.desativarOnibusUseCase = desativarOnibusUseCase;
        this.atualizarOnibusUseCase = atualizarOnibusUseCase;
        // PASSAGEIRO
        this.atualizarInformacoesPassageiroUseCase = atualizarInformacoesPassageiroUseCase;
        this.desativarPassageiroUseCase = desativarPassageiroUseCase;
        this.buscarInformacoesPassageiroUseCase = buscarInformacoesPassageiroUseCase;

        // LINHAS
        this.desativarLinhaUseCase = desativarLinhaUseCase;
        this.atualizarInformacoesDaLinhaUseCase = atualizarInformacoesDaLinhaUseCase;
        this.cadastrarLinhaUseCase = cadastrarLinhaUseCase;

        // TICKET
        this.adminCancelarTicketUseCase = adminCancelarTicketUseCase;

        // VIAGEM
        this.criarViagemUseCase = criarViagemUseCase;
        this.atualizarViagemUseCase = atualizarViagemUseCase;
        this.cancelarViagemUseCase = cancelarViagemUseCase;
        this.listarViagensPorPassageiro = listarViagensPorPassageiro;
    }

    // ▼▼▼ ADICIONE O NOVO MÉTODO AQUI ▼▼▼
    @PostMapping("/login")
    @Operation(summary = "Autentica um administrador", description = "Autentica um administrador com email e senha, retornando um token JWT. Este endpoint é público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou conta desativada")
    })
    public ResponseEntity<AutenticarAdminResponse> autenticarAdmin(@RequestBody AutenticarAdminRequest request) {
        AutenticarAdminResponse response = autenticarAdminUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    // ========== GERENCIAMENTO DE ÔNIBUS ==========

    @PostMapping("/onibus")
    @Operation(summary = "Cadastra um novo ônibus", description = "Registra um novo ônibus no sistema. A placa deve ser única.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ônibus cadastrado com sucesso", content = @Content(schema = @Schema(implementation = OnibusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou placa já existente", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<OnibusResponse> cadastrarOnibus(@RequestBody CadastrarOnibusRequest request, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado) {
        OnibusResponse onibusResponse = cadastrarOnibusUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(onibusResponse);
    }

    @GetMapping("/onibus")
    @Operation(summary = "Lista todos os ônibus", description = "Retorna uma lista de todos os ônibus cadastrados no sistema, incluindo ativos e inativos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ônibus retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<List<OnibusResponse>> listarTodosOnibus(@Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        List<OnibusResponse> onibusResponses = listarTodosOnibusUseCase.execute();
        return ResponseEntity.ok(onibusResponses);
    }

    @DeleteMapping("/onibus/{onibusId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa um ônibus", description = "Realiza a desativação lógica (soft delete) de um ônibus. O ônibus não poderá ser alocado para novas viagens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ônibus desativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ônibus não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public void desativarOnibus(@Parameter(description = "ID do ônibus a ser desativado") @PathVariable UUID onibusId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        desativarOnibusUseCase.execute(onibusId);
    }

    @PutMapping("/onibus/{onibusId}")
    @Operation(summary = "Atualiza dados de um ônibus", description = "Altera informações como modelo e capacidade de um ônibus existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ônibus atualizado com sucesso", content = @Content(schema = @Schema(implementation = OnibusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ônibus não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<OnibusResponse> atualizarOnibus(@Parameter(description = "ID do ônibus a ser atualizado") @PathVariable UUID onibusId, @RequestBody AtualizarOnibusRequest request, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminLogado){
        OnibusResponse onibusResponse = atualizarOnibusUseCase.execute(request, onibusId);
        return ResponseEntity.ok(onibusResponse);
    }

    // ========== GERENCIAMENTO DE PASSAGEIROS ==========

    @GetMapping("/passageiros/{passageiroId}")
    @Operation(summary = "Busca um passageiro por ID", description = "Retorna os detalhes de um passageiro específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passageiro encontrado", content = @Content(schema = @Schema(implementation = PassageiroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Passageiro não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<PassageiroResponse> buscarPassageiroPorId(@Parameter(description = "ID do passageiro a ser buscado") @PathVariable UUID passageiroId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        PassageiroResponse passageiroResponse = buscarInformacoesPassageiroUseCase.execute(passageiroId);
        return ResponseEntity.ok(passageiroResponse);
    }

    @PutMapping("/passageiros/{passageiroId}")
    @Operation(summary = "Atualiza dados de um passageiro", description = "Permite que um administrador altere os dados cadastrais de um passageiro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passageiro atualizado com sucesso", content = @Content(schema = @Schema(implementation = AtualizarInformacoesPassageiroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Passageiro não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<AtualizarInformacoesPassageiroResponse> atualizarPassageiro(@RequestBody AtualizarInformacoesPassageiroRequest request, @Parameter(description = "ID do passageiro a ser atualizado") @PathVariable UUID passageiroId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        AtualizarInformacoesPassageiroResponse atualizarInformacoesPassageiroResponse = atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        return ResponseEntity.ok(atualizarInformacoesPassageiroResponse);
    }

    @DeleteMapping("/passageiros/{passageiroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa um passageiro", description = "Realiza a desativação lógica (soft delete) da conta de um passageiro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passageiro desativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Passageiro não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public void desativarPassageiro(@Parameter(description = "ID do passageiro a ser desativado") @PathVariable UUID passageiroId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        desativarPassageiroUseCase.execute(passageiroId);
    }

    // ========== GERENCIAMENTO DE LINHAS ==========

    @PostMapping("/linhas")
    @Operation(summary = "Cadastra uma nova linha", description = "Registra uma nova rota (origem e destino) no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Linha cadastrada com sucesso", content = @Content(schema = @Schema(implementation = LinhaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou linha já existente", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<LinhaResponse> cadastrarLinha(@RequestBody CadastrarLinhaRequest request, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado) {
        LinhaResponse linhaResponse = cadastrarLinhaUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(linhaResponse);
    }

    @PutMapping("/linhas/{linhaId}")
    @Operation(summary = "Atualiza dados de uma linha", description = "Altera informações de uma linha existente, como origem, destino ou duração.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Linha atualizada com sucesso", content = @Content(schema = @Schema(implementation = LinhaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Linha não encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<LinhaResponse> atualizarLinha(@Parameter(description = "ID da linha a ser atualizada") @PathVariable UUID linhaId, @RequestBody AtualizarInformacoesDaLinhaRequest request, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        LinhaResponse linhaResponse = atualizarInformacoesDaLinhaUseCase.execute(linhaId, request);
        return ResponseEntity.ok(linhaResponse);
    }

    @DeleteMapping("/linhas/{linhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa uma linha", description = "Realiza a desativação lógica (soft delete) de uma linha. Novas viagens não poderão ser criadas para esta linha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Linha desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Linha não encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public void desativarLinha(@Parameter(description = "ID da linha a ser desativada") @PathVariable UUID linhaId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        desativarLinhaUseCase.execute(linhaId);
    }

    // ========== GERENCIAMENTO DE TICKETS ==========

    @DeleteMapping("/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancela um ticket", description = "Permite que um administrador cancele o ticket de qualquer passageiro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket cancelado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public void cancelarTicket(@Parameter(description = "ID do ticket a ser cancelado") @PathVariable UUID ticketId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        adminCancelarTicketUseCase.execute(ticketId);
    }

    // ========== GERENCIAMENTO DE VIAGENS ==========

    @PostMapping("/viagens")
    @Operation(summary = "Cria uma nova viagem", description = "Agenda uma nova viagem, associando uma linha e um ônibus.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Viagem criada com sucesso", content = @Content(schema = @Schema(implementation = ViagemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<ViagemResponse> criarViagem(@RequestBody ViagemRequest request, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        ViagemResponse viagemResponse = criarViagemUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(viagemResponse);
    }

    @PutMapping("/viagens/{viagemId}")
    @Operation(summary = "Atualiza uma viagem", description = "Permite alterar a data de partida ou o ônibus de uma viagem agendada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viagem atualizada com sucesso", content = @Content(schema = @Schema(implementation = ViagemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Viagem não encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<ViagemResponse> atualizarViagem(@Parameter(description = "ID da viagem a ser atualizada") @PathVariable UUID viagemId, @RequestBody AtualizarViagemRequest request, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        ViagemResponse viagemResponse = atualizarViagemUseCase.execute(viagemId, request);
        return ResponseEntity.ok(viagemResponse);
    }

    @DeleteMapping("/viagens/{viagemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancela uma viagem", description = "Cancela uma viagem agendada. Isso não cancela os tickets associados automaticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Viagem cancelada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Viagem não encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public void cancelarViagem(@Parameter(description = "ID da viagem a ser cancelada") @PathVariable UUID viagemId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        cancelarViagemUseCase.execute(viagemId);
    }

    @GetMapping("/viagens/passageiros/{passageiroId}")
    @Operation(summary = "Lista as viagens de um passageiro", description = "Retorna todas as viagens para as quais um passageiro específico comprou um ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de viagens retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Passageiro não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<List<ViagemPorPassageiroResponse>> listarViagensPorPassageiro(@Parameter(description = "ID do passageiro") @PathVariable UUID passageiroId, @Parameter(hidden = true) @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        List<ViagemPorPassageiroResponse> viagensPorPassageiro = listarViagensPorPassageiro.execute(passageiroId);
        return ResponseEntity.ok(viagensPorPassageiro);
    }
}