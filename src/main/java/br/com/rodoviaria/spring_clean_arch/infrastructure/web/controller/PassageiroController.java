package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {

    private final ListarPassageirosDeUmaViagemUseCase listarPassageirosDeUmaViagemUseCase;
    private final DesativarPassageiroUseCase desativarPassageiroUseCase;
    private final AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;
    private final AutenticarPassageiroUseCase autenticarPassageiroUseCase;
    private final CadastrarPassageiroUseCase cadastrarPassageiroUseCase;
    private final BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;

    public PassageiroController(ListarPassageirosDeUmaViagemUseCase l, DesativarPassageiroUseCase d, AtualizarInformacoesPassageiroUseCase atualizar, AutenticarPassageiroUseCase autenticar, CadastrarPassageiroUseCase cadastrar, BuscarInformacoesPassageiroUseCase buscar){
        this.listarPassageirosDeUmaViagemUseCase = l;
        this.desativarPassageiroUseCase = d;
        this.atualizarInformacoesPassageiroUseCase = atualizar;
        this.autenticarPassageiroUseCase = autenticar;
        this.cadastrarPassageiroUseCase = cadastrar;
        this.buscarInformacoesPassageiroUseCase = buscar;
    }

}
