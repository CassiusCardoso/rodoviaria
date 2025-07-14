package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;

import java.util.UUID;

public class BuscarInformacoesOnibusUseCase {
    private final OnibusRepository onibusRepository;
    private final OnibusMapper onibusMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento
    public BuscarInformacoesOnibusUseCase(OnibusRepository onibusRepository, OnibusMapper onibusMapper) {
        this.onibusRepository = onibusRepository;
        this.onibusMapper = onibusMapper;
    }

    public OnibusResponse execute(UUID onibusId){
        // Verificar se o ônibus existe
        Onibus onibusBuscado = onibusRepository.buscarOnibusPorId(onibusId)
                .orElseThrow(() -> new OnibusInvalidoException("Ônibus com o identificador informado não existe no sistema."));

        return this.onibusMapper.toResponse(onibusBuscado);

    }
}
