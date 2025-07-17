package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarLinhaPorIdUseCase {
    private final LinhaRepository linhaRepository;
    private final LinhaMapper linhaMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public BuscarLinhaPorIdUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaMapper = linhaMapper;
    }

    public LinhaResponse execute(UUID linhaId) {
        Linha linha = linhaRepository.buscarLinhaPorId(linhaId)
                .orElseThrow(() -> new LinhaInvalidaException("Linha não encontrada."));
        return linhaMapper.toResponse(linha);
    }
}
