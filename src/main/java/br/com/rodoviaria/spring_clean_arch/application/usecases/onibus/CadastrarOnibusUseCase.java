package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.CadastrarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Placa;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class CadastrarOnibusUseCase {
    private final OnibusRepository onibusRepository;
    private final OnibusMapper onibusMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public CadastrarOnibusUseCase(OnibusRepository onibusRepository, OnibusMapper onibusMapper) {
        this.onibusRepository = onibusRepository;
        this.onibusMapper = onibusMapper;
    }

    public OnibusResponse execute(CadastrarOnibusRequest request) {
        // Verificando se já existe um ônibus com a placa inserida pelo usuário
        onibusRepository.buscarPelaPlaca(request.placa()).ifPresent(onibusExistente -> {
            throw new OnibusInvalidoException("Já existe um ônibus cadastrado com a placa: " + request.placa());
        });

        Placa novaPlaca = new Placa(request.placa());
        // 3. CRIAR A ENTIDADE DE DOMÍNIO
        // Agora com o Value Object, não com a String.
        Onibus novoOnibus = new Onibus(
                UUID.randomUUID(),
                novaPlaca,
                request.modelo(),
                request.capacidade(),
                true // Assim que um ônibus é instanciado ele já está como ativo

        );

        // PERSISTIR A NOVA ENTIDADE
        Onibus onibusCadastrado = onibusRepository.salvar(novoOnibus);

        // MAPEAR PARA O DTO DE RESPOSTA E RETORNAR
        return onibusMapper.toResponse(onibusCadastrado);

    }
}
