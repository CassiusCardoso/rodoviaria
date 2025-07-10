package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

import java.util.UUID;

public class BuscarLinhaPorIdUseCase {
    private final LinhaRepository linhaRepository;
    public BuscarLinhaPorIdUseCase(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }

    public LinhaResponse execute(UUID linhaId) {
        Linha linha = linhaRepository.buscarLinhaPorId(linhaId)
                .orElseThrow(() -> new LinhaInvalidaException("Linha não encontrada."));
        return LinhaMapper.INSTANCE.toResponse(linha);
    }
}
