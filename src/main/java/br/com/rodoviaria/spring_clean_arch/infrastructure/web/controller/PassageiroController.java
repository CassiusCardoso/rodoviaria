package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AutenticarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AutenticarPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroPorViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.*;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.UsuarioAutenticado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {

    private final ListarPassageirosDeUmaViagemUseCase listarPassageirosDeUmaViagemUseCase;
    private final DesativarPassageiroUseCase desativarPassageiroUseCase;
    private final AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;
    private final AutenticarPassageiroUseCase autenticarPassageiroUseCase;
    private final CadastrarPassageiroUseCase cadastrarPassageiroUseCase;
    private final BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;

    public PassageiroController(ListarPassageirosDeUmaViagemUseCase listarPassageirosDeUmaViagemUseCase, DesativarPassageiroUseCase desativarPassageiroUseCase, AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase, AutenticarPassageiroUseCase autenticarPassageiroUseCase, CadastrarPassageiroUseCase cadastrarPassageiroUseCase, BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase){
        this.listarPassageirosDeUmaViagemUseCase = listarPassageirosDeUmaViagemUseCase; // S
        this.desativarPassageiroUseCase = desativarPassageiroUseCase; // S
        this.atualizarInformacoesPassageiroUseCase = atualizarInformacoesPassageiroUseCase; // S
        this.autenticarPassageiroUseCase = autenticarPassageiroUseCase; // S
        this.cadastrarPassageiroUseCase = cadastrarPassageiroUseCase; // S
        this.buscarInformacoesPassageiroUseCase = buscarInformacoesPassageiroUseCase; // S
    }

    @PostMapping
    public ResponseEntity<PassageiroResponse> cadastrar(@RequestBody CadastrarPassageiroRequest request){
        //O Controller apenas orquestra: recebe o DTO de request e passa para o caso de uso.
        PassageiroResponse passageiroResponse = cadastrarPassageiroUseCase.execute(request);

        // Retorna um ResponseEntity com o DTO de response.
        return ResponseEntity.status(HttpStatus.CREATED).body(passageiroResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassageiroResponse> buscar(@PathVariable UUID usuarioId){
        // Supondo que o caso de uso 'execute' receba o ID
        // (Nota: Você precisará ajustar o caso de uso para talvez receber o ID do usuário logado para segurança)
        PassageiroResponse passageiroResponse = buscarInformacoesPassageiroUseCase.execute(usuarioId);
        return ResponseEntity.ok(passageiroResponse);
    }

    @GetMapping("/viagem/{viagemId}")
    public ResponseEntity<List<PassageiroPorViagemResponse>> listarPorViagem(@PathVariable UUID viagemId){
        List<PassageiroPorViagemResponse> passageiros = listarPassageirosDeUmaViagemUseCase.execute(viagemId);
        return ResponseEntity.ok(passageiros);
    }

    @PutMapping("/{passageiroId}")
    public ResponseEntity<AtualizarInformacoesPassageiroResponse> atualizar(
            @PathVariable UUID passageiroId,
            @RequestBody AtualizarInformacoesPassageiroRequest request,
            @AuthenticationPrincipal UsuarioAutenticado usuarioLogado
            ){

        // Extrair informações de forma segura do principal autenticado
        UUID usuarioLogadoId = usuarioLogado.getId();
        Role usuarioRole = usuarioLogado.getRole();

        // Chama o caso de uso com todos os parâmetros
        AtualizarInformacoesPassageiroResponse response = atualizarInformacoesPassageiroUseCase.execute(request, passageiroId, usuarioLogadoId, usuarioRole);
        return ResponseEntity.ok(response);
    }

    // --- ENDPOINT DE DESATIVAÇÃO ---
    /**
     * Desativa a conta de um passageiro (Soft Delete).
     * Um usuário só pode desativar a própria conta.
     * (Futuramente, um ADMIN poderia ter permissão para desativar outras).
     */
    @DeleteMapping("/{passageiroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Define o status de sucesso padrão para 204 No Content
    public void desativar(
            @PathVariable UUID passageiroId,
            @AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        // Extrair informações de forma segura do principal autenticado
        UUID usuarioLogadoId = usuarioLogado.getId();
        Role usuarioRole = usuarioLogado.getRole();

        // Chama o caso de uso, que contém toda a lógica de negócio
        // incluindo a verificação de permissão (ex: usuarioLogadoId deve ser igual a passageiroId)
        desativarPassageiroUseCase.execute(usuarioLogadoId, passageiroId);

        // Para um DELETE bem-sucedido, a convenção é não retornar corpo.
        // A anotação @ResponseStatus(HttpStatus.NO_CONTENT) já cuida do status HTTP.
    }

    // --- ENDPOINT DE AUTENTICAÇÃO CORRIGIDO ---

    /**
     * Autentica um passageiro e retorna um token JWT se as credenciais forem válidas.
     * Usa o método POST, que é o padrão para login/autenticação.
     */
    @PostMapping("/login")
    public ResponseEntity<AutenticarPassageiroResponse> autenticar(@RequestBody AutenticarPassageiroRequest request){
        AutenticarPassageiroResponse response = autenticarPassageiroUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    // MELHORIA: Endpoint mais seguro para buscar informações do próprio usuário.
    @GetMapping("/me")
    public ResponseEntity<PassageiroResponse> buscarMinhasInformacoes(@AuthenticationPrincipal UsuarioAutenticado usuarioLogado){
        PassageiroResponse response = buscarInformacoesPassageiroUseCase.execute(usuarioLogado.getId());
        return ResponseEntity.ok(response);
    }
}
