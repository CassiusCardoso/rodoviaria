package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AutenticarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AutenticarPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.*;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.ListarViagensPorPassageiro;
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

    // Casos de uso relevantes para o passageiro
    private final AutenticarPassageiroUseCase autenticarPassageiroUseCase;
    private final CadastrarPassageiroUseCase cadastrarPassageiroUseCase;
    private final BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;
    private final AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;
    private final DesativarPassageiroUseCase desativarPassageiroUseCase;
    private final ListarViagensPorPassageiro listarViagensPorPassageiro;

    public PassageiroController(
            AutenticarPassageiroUseCase autenticarPassageiroUseCase,
            CadastrarPassageiroUseCase cadastrarPassageiroUseCase,
            BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase,
            AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase,
            DesativarPassageiroUseCase desativarPassageiroUseCase,
            ListarViagensPorPassageiro listarViagensPorPassageiro
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
    public ResponseEntity<AutenticarPassageiroResponse> autenticar(@RequestBody AutenticarPassageiroRequest request) {
        return ResponseEntity.ok(autenticarPassageiroUseCase.execute(request));
    }

    @PostMapping
    public ResponseEntity<PassageiroResponse> cadastrar(@RequestBody CadastrarPassageiroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarPassageiroUseCase.execute(request));
    }

    // --- Endpoints Protegidos para o Passageiro Logado ---

    @GetMapping("/me")
    public ResponseEntity<PassageiroResponse> buscarMinhasInformacoes(@AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
        return ResponseEntity.ok(buscarInformacoesPassageiroUseCase.execute(usuarioLogado.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<AtualizarInformacoesPassageiroResponse> atualizarMinhasInformacoes(
            @RequestBody AtualizarInformacoesPassageiroRequest request,
            @AuthenticationPrincipal UsuarioAutenticado usuarioLogado
    ) {
        return ResponseEntity.ok(atualizarInformacoesPassageiroUseCase.execute(request, usuarioLogado.getId()));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarMinhaConta(@AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
        desativarPassageiroUseCase.execute(usuarioLogado.getId());
    }

    @GetMapping("/me/viagens")
    public ResponseEntity<List<ViagemPorPassageiroResponse>> listarMinhasViagens(
            @AuthenticationPrincipal UsuarioAutenticado usuarioLogado
    ) {
        List<ViagemPorPassageiroResponse> response = listarViagensPorPassageiro.execute(usuarioLogado.getId());
        return ResponseEntity.ok(response);
    }
}