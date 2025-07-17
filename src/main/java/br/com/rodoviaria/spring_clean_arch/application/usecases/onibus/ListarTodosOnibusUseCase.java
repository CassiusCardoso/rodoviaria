package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarTodosOnibusUseCase {

    private final OnibusRepository onibusRepository;
    private final OnibusMapper onibusMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public ListarTodosOnibusUseCase(OnibusRepository onibusRepository, OnibusMapper onibusMapper) {
        this.onibusRepository = onibusRepository;
        this.onibusMapper = onibusMapper;
    }

    public List<OnibusResponse> execute(){
        // A verificação de autorização foi removida.
        // A segurança agora é responsabilidade da camada de infraestrutura (SecurityConfig).

        // A lógica de negócio principal permanece a mesma.
        List<Onibus> todosOsOnibus = onibusRepository.listarTodosOnibus();

        // 3. MAPEIA A LISTA DE ENTIDADES PARA UMA LISTA DE DTOS
        return todosOsOnibus.stream()
                .map(onibusMapper::toResponse)
                .toList();
    }
}
