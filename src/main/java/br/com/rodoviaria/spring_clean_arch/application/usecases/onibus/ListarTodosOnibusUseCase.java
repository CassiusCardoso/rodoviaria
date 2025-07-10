package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;

import java.util.List;

public class ListarTodosOnibusUseCase {

    private final OnibusRepository onibusRepository;
    public ListarTodosOnibusUseCase(OnibusRepository onibusRepository) {
        this.onibusRepository = onibusRepository;
    }

    public List<OnibusResponse> execute(){
        // A verificação de autorização foi removida.
        // A segurança agora é responsabilidade da camada de infraestrutura (SecurityConfig).

        // A lógica de negócio principal permanece a mesma.
        List<Onibus> todosOsOnibus = onibusRepository.listarTodosOnibus();

        // 3. MAPEIA A LISTA DE ENTIDADES PARA UMA LISTA DE DTOS
        return todosOsOnibus.stream()
                .map(OnibusMapper.INSTANCE::toResponse)
                .toList();
    }
}
