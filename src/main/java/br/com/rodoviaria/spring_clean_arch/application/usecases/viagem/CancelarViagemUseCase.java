package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.StatusViagemInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CancelarViagemUseCase {
    private final ViagemRepository viagemRepository;

    public CancelarViagemUseCase(ViagemRepository viagemRepository) {
        this.viagemRepository = viagemRepository;
    }

    public void execute(UUID viagemId){

        // Buscar a entidade viagem para ser cancelada
        Viagem viagem = viagemRepository.buscarViagemPorId(viagemId)
                .orElseThrow(() -> new ViagemInvalidaException("Não encontramos nenhuma viagem com o identificador " + viagemId));

        // Validação de status corrigida e mais completa
        StatusViagem statusAtual = viagem.getStatusViagem();

        if (statusAtual == StatusViagem.CANCELADA ||
                statusAtual == StatusViagem.CONCLUIDA ||
                statusAtual == StatusViagem.EM_TRANSITO) {

            throw new StatusViagemInvalidoException("A viagem não pode ser cancelada, pois seu status atual é: " + statusAtual);
        }

        // Modifica o estado da entidade
        // Como a entidade Viagem é imutável, precisa criar um novo objeto com o estado atualizado.
        // É necessário ter o método 'cancelar()' na entidade Ticket.
        Viagem viagemCancelada = viagem.cancelar();

        // Salva o estado atualizado
        // O mesmo método "salvar" serve para criar e para atualizar.
        viagemRepository.salvar(viagemCancelada);
    }
}
