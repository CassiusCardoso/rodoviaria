package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;

import java.util.UUID;

public class DesativarPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;

    public DesativarPassageiroUseCase(PassageiroRepository passageiroRepository) {
        this.passageiroRepository = passageiroRepository;
    }

    public void execute(UUID passageiroId){
        // Buscar a instância do objeto Passageiro para cancelar.
        Passageiro passageiro = passageiroRepository.buscarPassageiroPorId(passageiroId)
                .orElseThrow(() -> new PassageiroInvalidoException("Passageiro com o identificador informado não existe."));


        // Validar se o passageiro já está inativo
        if(passageiro.getAtivo() ==  false) {
            throw new PassageiroInvalidoException("Passageiro inválido para desativar");
        }

        // Modifica o estado da entidade
        // Como Passageiro é imutável, pois não tem set, É PRECISO criar um novo objeto com o estado atualizado
        // Precisa criar o método cancelar() na entidade Passageiro
        Passageiro passageiroDesativado = passageiro.desativar();

        // Salvar o estado atualizado
        // O mesmo método "salvar" serve para criar e para atualizar.
        passageiroRepository.salvar(passageiroDesativado);


    }
}
