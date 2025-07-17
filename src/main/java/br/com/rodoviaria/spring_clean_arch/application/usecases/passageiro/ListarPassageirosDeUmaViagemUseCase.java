package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroPorViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarPassageirosDeUmaViagemUseCase {
    private final TicketRepository ticketRepository;
    private final ViagemRepository viagemRepository;
    private final TicketMapper ticketMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public  ListarPassageirosDeUmaViagemUseCase(TicketRepository ticketRepository, ViagemRepository viagemRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.viagemRepository = viagemRepository;
        this.ticketMapper = ticketMapper;
    }

    public List<PassageiroPorViagemResponse> execute(UUID viagemId){
        // Primeiro verificar se a viagem existe
        viagemRepository.buscarViagemPorId(viagemId)
                .orElseThrow(() -> new ViagemInvalidaException("A viagem com identificador" + viagemId + " não foi encontrada no sistema."));

        // Verificando se o ticket existe
        // É a partir dos tickets que temos a relação entre passageiro e viagem.
        List<Ticket> ticketsDaViagem = ticketRepository.listarTicketsPorViagem(viagemId);
        // Se a lista estiver vazia, você pode optar por retornar uma lista vazia
        // ou lançar uma exceção, dependendo da sua regra de negócio.
        // Lançar exceção pode ser mais informativo para o usuário.
        // Se a lista for vazia, o stream vai retornar uma lista vazia, o que é o comportamento correto.

        // 3. Converter a lista de Tickets para a lista de DTOs de resposta.
        return ticketsDaViagem.stream()
                .map(ticketMapper::toPassageiroPorViagemResponse)
                .toList();
    }
}
