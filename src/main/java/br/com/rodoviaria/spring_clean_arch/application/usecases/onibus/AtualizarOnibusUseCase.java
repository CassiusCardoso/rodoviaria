package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.AtualizarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.util.UUID;

public class AtualizarOnibusUseCase {
    private final OnibusRepository onibusRepository;
    private final ViagemRepository viagemRepository;
    private final OnibusMapper onibusMapper;

    public AtualizarOnibusUseCase(OnibusRepository onibusRepository, ViagemRepository viagemRepository, OnibusMapper onibusMapper) {
        this.onibusRepository = onibusRepository;
        this.viagemRepository = viagemRepository;
        this.onibusMapper = onibusMapper;
    }

    public OnibusResponse execute(AtualizarOnibusRequest request, UUID onibusId){
        // Verificar se já existe ou não um ônibus com o id
        Onibus onibus = onibusRepository.buscarOnibusPorId(onibusId)
                .orElseThrow(() -> new OnibusInvalidoException("Não há nenhum registro de ônibus com esse identificador"));

        // SUGESTÃO DE NOVA VALIDAÇÃO
        if (!onibus.getAtivo()) {
            throw new OnibusInvalidoException("Não é possível alterar um ônibus que já está desativado.");
        }

        if (viagemRepository.existeViagemEmTransitoParaOnibus(onibusId)) {
            throw new OnibusInvalidoException("Não é possível alterar um ônibus que está atualmente em trânsito.");
        }

        // Atualizar a entidade
        Onibus onibusAtualizado = new Onibus(
                onibus.getId(),
                onibus.getPlaca(),
                request.modelo(),
                request.capacidade(),
                onibus.getAtivo()
        );

        Onibus onibusSalvo =  onibusRepository.salvar(onibusAtualizado);
        // RETORNANDO O RESPONSE
        // Usamos o Mapper para converter a entidade atualizada para o DTO de resposta.
        return onibusMapper.toResponse(onibusSalvo);
    }
}
