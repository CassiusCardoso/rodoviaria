package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.onibus.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;

import java.util.UUID;

public class BuscarInformacoesOnibusUseCase {
    private OnibusRepository onibusRepository;
    public BuscarInformacoesOnibusUseCase(OnibusRepository onibusRepository) {
        this.onibusRepository = onibusRepository;
    }

    public OnibusResponse execute(UUID onibusId){
        // Verificar se o ônibus existe
        Onibus onibusBuscado = onibusRepository.buscarOnibusPorId(onibusId)
                .orElseThrow(() -> new OnibusInvalidoException("Ônibus com o identificador informado não existe no sistema."));

        return OnibusMapper.INSTANCE.toResponse(onibusBuscado);

    }
}
