package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

public class BuscarLinhaPorOrigemDestinoUseCase {
    private final LinhaRepository linhaRepository;
    private final LinhaMapper linhaMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public BuscarLinhaPorOrigemDestinoUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaMapper = linhaMapper;
    }

    public LinhaResponse execute (String origem, String destino){
        // Verificando a linha
        Linha linha = linhaRepository.buscarPorOrigemEDestino(origem, destino)
                .orElseThrow(() -> new LinhaInvalidaException("Não existe nenhuma linha com origem e destino citados."));

        return linhaMapper.toResponse(linha);
    }
}
