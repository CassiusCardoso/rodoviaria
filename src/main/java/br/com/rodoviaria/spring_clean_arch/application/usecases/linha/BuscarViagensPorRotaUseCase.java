package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarViagensPorRotaUseCase {
    private final LinhaRepository linhaRepository;
    private final ViagemRepository viagemRepository;
    private final ViagemMapper viagemMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public BuscarViagensPorRotaUseCase(LinhaRepository linhaRepository, ViagemRepository viagemRepository, ViagemMapper viagemMapper) {
        this.linhaRepository = linhaRepository;
        this.viagemRepository = viagemRepository;
        this.viagemMapper = viagemMapper;
    }

    public List<ViagemResponse> execute(UUID linhaId){
        // Primeiro, garanta que a linha que o usuário pediu realmente existe
        linhaRepository.buscarLinhaPorId(linhaId)
                .orElseThrow(() -> new LinhaInvalidaException("Linha com o identificador informado não existe."));

        List<Viagem> viagensDaLinha = viagemRepository.buscarViagensPorLinha(linhaId);

        // Mapeie cada Viagem encontrada para um ViagemResponse.
        // O ViagemMapper já sabe como fazer isso, incluindo a conversão
        // da Linha para LinhaResponse e do Onibus para OnibusResponse.

        return viagensDaLinha.stream()
                .map(viagemMapper::toResponse)
                .toList();

    }
}
