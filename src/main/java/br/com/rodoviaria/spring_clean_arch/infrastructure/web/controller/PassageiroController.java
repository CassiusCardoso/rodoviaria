package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AutenticarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AutenticarPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.*;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.ListarViagensPorPassageiroUseCase;
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

@RestController
@RequestMapping("/passageiros")
@Tag(name = "Passageiro", description = "Endpoints para autenticação e gerenciamento da conta do passageiro")
public class PassageiroController {

    // Casos de uso relevantes para o passageiro
    private final AutenticarPassageiroUseCase autenticarPassageiroUseCase;
    private final CadastrarPassageiroUseCase cadastrarPassageiroUseCase;
    private final BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;
    private final AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;
    private final DesativarPassageiroUseCase desativarPassageiroUseCase;
    private final ListarViagensPorPassageiroUseCase listarViagensPorPassageiro;

    public PassageiroController(
            AutenticarPassageiroUseCase autenticarPassageiroUseCase,
            CadastrarPassageiroUseCase cadastrarPassageiroUseCase,
            BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase,
            AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase,
            DesativarPassageiroUseCase desativarPassageiroUseCase,
            ListarViagensPorPassageiroUseCase listarViagensPorPassageiro
    ) {
        this.autenticarPassageiroUseCase = autenticarPassageiroUseCase;
        this.cadastrarPassageiroUseCase = cadastrarPassageiroUseCase;
        this.buscarInformacoesPassageiroUseCase = buscarInformacoesPassageiroUseCase;
        this.atualizarInformacoesPassageiroUseCase = atualizarInformacoesPassageiroUseCase;
        this.desativarPassageiroUseCase = desativarPassageiroUseCase;
        this.listarViagensPorPassageiro = listarViagensPorPassageiro;
    }

    // --- Endpoints Públicos ---

    @PostMapping("/login")
    @Operation(summary = "Autentica um passageiro", description = "Autentica um passageiro com email e senha, retornando um token JWT em caso de sucesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida", content = @Content(schema = @Schema(implementation = AutenticarPassageiroResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    public ResponseEntity<AutenticarPassageiroResponse> autenticar(@RequestBody AutenticarPassageiroRequest request) {
        return ResponseEntity.ok(autenticarPassageiroUseCase.execute(request));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo passageiro", description = "Cria uma nova conta de passageiro no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Passageiro cadastrado com sucesso", content = @Content(schema = @Schema(implementation = PassageiroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF/Email já existente", content = @Content)
    })
    public ResponseEntity<PassageiroResponse> cadastrar(@RequestBody CadastrarPassageiroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarPassageiroUseCase.execute(request));
    }

    // --- Endpoints Protegidos para o Passageiro Logado ---

    @GetMapping("/me")
    @Operation(summary = "Busca informações do passageiro logado", description = "Retorna os dados cadastrais do passageiro autenticado.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do passageiro retornados com sucesso", content = @Content(schema = @Schema(implementation = PassageiroResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<PassageiroResponse> buscarMinhasInformacoes(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
        return ResponseEntity.ok(buscarInformacoesPassageiroUseCase.execute(usuarioLogado.getId()));
    }

    @PutMapping("/me")
    @Operation(summary = "Atualiza informações do passageiro logado", description = "Permite que o passageiro autenticado atualize seus próprios dados cadastrais.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso", content = @Content(schema = @Schema(implementation = AtualizarInformacoesPassageiroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<AtualizarInformacoesPassageiroResponse> atualizarMinhasInformacoes(
            @RequestBody AtualizarInformacoesPassageiroRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado
    ) {
        return ResponseEntity.ok(atualizarInformacoesPassageiroUseCase.execute(request, usuarioLogado.getId()));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa a conta do passageiro logado", description = "Realiza a desativação lógica (soft delete) da conta do passageiro autenticado.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public void desativarMinhaConta(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
        desativarPassageiroUseCase.execute(usuarioLogado.getId());
    }

    @GetMapping("/me/viagens")
    @Operation(summary = "Lista as viagens do passageiro logado", description = "Retorna uma lista de todas as viagens para as quais o passageiro autenticado comprou tickets.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de viagens retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    public ResponseEntity<List<ViagemPorPassageiroResponse>> listarMinhasViagens(
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuarioLogado
    ) {
        List<ViagemPorPassageiroResponse> response = listarViagensPorPassageiro.execute(usuarioLogado.getId());
        return ResponseEntity.ok(response);
    }
}