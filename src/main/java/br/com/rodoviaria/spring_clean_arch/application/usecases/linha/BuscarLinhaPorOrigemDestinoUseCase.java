package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.linha.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

public class BuscarLinhaPorOrigemDestinoUseCase {
    private final LinhaRepository linhaRepository;
    public BuscarLinhaPorOrigemDestinoUseCase(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }

    public LinhaResponse execute (String origem, String destino){
        // Verificando a linha
        Linha linha = linhaRepository.buscarPorOrigemEDestino(origem, destino)
                .orElseThrow(() -> new LinhaInvalidaException("Não existe nenhuma linha com origem e destino citados."));

        return LinhaMapper.INSTANCE.toResponse(linha);
    }
}
