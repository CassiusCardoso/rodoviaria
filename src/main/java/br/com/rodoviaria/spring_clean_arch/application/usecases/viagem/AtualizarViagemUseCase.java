package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.AtualizarViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.viagem.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.StatusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.DataHoraChegadaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class AtualizarViagemUseCase {
    private final ViagemRepository viagemRepository;
    private final OnibusRepository onibusRepository;

    public  AtualizarViagemUseCase(ViagemRepository viagemRepository, OnibusRepository onibusRepository) {
        this.viagemRepository = viagemRepository;
        this.onibusRepository = onibusRepository;
    }

    public ViagemResponse execute(UUID viagemId, AtualizarViagemRequest request){
        // Buscando a Viagem
        Viagem viagemAtual = viagemRepository.buscarViagemPorId(viagemId)
                .orElseThrow(() -> new ViagemInvalidaException("Não encontramos nenhuma viagem com esse identificador:" + viagemId));

        // 2. Validações de Negócio CRUCIAIS:
        // Não se pode alterar uma viagem que já foi concluída ou cancelada.
        if (viagemAtual.getStatusViagem() == StatusViagem.CONCLUIDA || viagemAtual.getStatusViagem() == StatusViagem.CANCELADA) {
            throw new RuntimeException("Não é possível alterar uma viagem que já foi concluída ou cancelada.");
        }
        if(viagemAtual.getStatusViagem().equals(StatusTicket.UTILIZADO)){
            throw new StatusInvalidoException("A viagem já teve o ticket UTILIZADO. Portanto não podemos alterar nenhuma informação!");
        }

        // Buscando o novo ônibus
        Onibus novoOnibus = onibusRepository.buscarOnibusPorId(request.novoOnibusId())
                .orElseThrow(() -> new OnibusInvalidoException("O novo ônibus selecionado não foi encontrado."));

        // 3. Cria a nova entidade Viagem com os dados atualizados
        // (Lembre-se que a data de chegada também precisa ser recalculada se a partida mudar)
        LocalDateTime novaDataChegada = request.novaDataPartida().plusMinutes(viagemAtual.getLinha().getDuracaoPrevistaMinutos());

        // Atualizando a entidade viagem com as alterações feitas
        Viagem novaViagem = new Viagem(
                viagemAtual.getId(),
                request.novaDataPartida(), // Novos valores que serão passados pelo DTO
                novaDataChegada,           // <-- Novo valor calculado
                viagemAtual.getStatusViagem(), // Status não muda aqui
                viagemAtual.getLinha(), // Linha não muda
                novoOnibus              // <-- Novo ônibus
        );

        // Salvar e retornar (persistência)
        Viagem viagemSalva = viagemRepository.salvar(viagemAtual);
        return ViagemMapper.INSTANCE.toResponse(viagemSalva);
    }

}
