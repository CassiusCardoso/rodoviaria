package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DesativarLinhaUseCase {
    private final LinhaRepository linhaRepository;
    public DesativarLinhaUseCase(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }

    public void execute(UUID linhaId){
        // Buscando se a linha com o id informado existe
        Linha linha = linhaRepository.buscarLinhaPorId(linhaId)
                .orElseThrow(() -> new LinhaInvalidaException("A linha com o número identificador não existe no sistema."));

        // Alterando o estado da entidade
        Linha linhaAlterada = linha.desativar();

        // Salvando a nova linha
        Linha linhaDesativada = linhaRepository.salvar(linhaAlterada);
    }
}
