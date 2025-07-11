package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ListarTodasLinhasUseCase {
    private final LinhaRepository linhaRepository;
    private final LinhaMapper linhaMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public ListarTodasLinhasUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaMapper = linhaMapper;
    }

    public List<LinhaResponse> execute(){
        // Pegando a entidade linha
        List<Linha> todasAsLinhas = linhaRepository.listarTodasLinhas();

        // Mapear o LinhaResponse para linha
        return todasAsLinhas.stream()
                .map(linhaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
