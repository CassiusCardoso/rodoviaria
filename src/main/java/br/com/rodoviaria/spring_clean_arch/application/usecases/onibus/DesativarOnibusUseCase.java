package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.util.UUID;

public class DesativarOnibusUseCase {
    private final OnibusRepository onibusRepository;
    private final ViagemRepository viagemRepository;
    public DesativarOnibusUseCase(OnibusRepository onibusRepository, ViagemRepository viagemRepository) {
        this.onibusRepository = onibusRepository;
        this.viagemRepository = viagemRepository;
    }

    public void execute(UUID onibusId){
        // Buscar a instância de um ônibus
        Onibus onibus = onibusRepository.buscarOnibusPorId(onibusId)
                .orElseThrow(() -> new OnibusInvalidoException("Ônibus com o identificador informado não existe."));


        // Verificando se o ônibus já está inativo
        if(!onibus.getAtivo()){
            throw new OnibusInvalidoException("Ônibus inválido para desativar.");
        }

        // 4. NOVA VALIDAÇÃO DE NEGÓCIO CRÍTICA
        // Usa o novo contrato do ViagemRepository para verificar se o ônibus tem "compromissos futuros".
        if (viagemRepository.existeViagemFuturaNaoCanceladaParaOnibus(onibusId)) {
            throw new OnibusInvalidoException("Não é possível desativar um ônibus que está alocado em viagens futuras. Cancele ou realoque as viagens primeiro.");
        }

        // Modificando o estado da entidade
        // Alterando a nova entidade do onibus com status ativo = false
        Onibus onibusDesativado = onibus.desativar();

        // Salvando o estado atualizado
        // O mesmo método "salvar" serve para criar e para atualizar
        onibusRepository.salvar(onibusDesativado);
    }
}
