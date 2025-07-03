package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.linha.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ListarTodasLinhasUseCase {
    private final LinhaRepository linhaRepository;
    public ListarTodasLinhasUseCase(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }

    public List<LinhaResponse> execute(){
        // Pegando a entidade linha
        List<Linha> todasAsLinhas = linhaRepository.listarTodasLinhas();

        // Mapear o LinhaResponse para linha
        return todasAsLinhas.stream()
                .map(LinhaMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }
}
